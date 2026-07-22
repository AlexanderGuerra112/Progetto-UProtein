package it.uprotein.storage;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;
import it.uprotein.model.*;
import it.uprotein.model.Prodotto;
import it.uprotein.model.Ordine;

public class OrdineDAOImpl implements OrdineDAO {
    private DataSource ds;

    public OrdineDAOImpl(DataSource ds) {
        this.ds = ds;
    }

    @Override
    public synchronized void doSave(Utente utente, Carrello carrello, String metodoPagamento, String indirizzoConsegna) throws SQLException {
        Connection con = null;
        PreparedStatement psOrdine = null;
        PreparedStatement psDettaglio = null;
        PreparedStatement psSelectProdotto = null; 
        PreparedStatement psUpdateProdotto = null; 

        String insertOrdine = "INSERT INTO ORDINE (id_utente, data_ordine, totale_ordine, indirizzo_consegna, stato_ordine, metodo_pagamento) VALUES (?, ?, ?, ?, ?, ?)";
        String insertDettaglio = "INSERT INTO DETTAGLIO_ORDINE (id_ordine, id_prodotto, quantita, prezzo_acquistato, nome_prodotto, categoria_prodotto) VALUES (?, ?, ?, ?, ?, ?)";        
        String selectProdotto = "SELECT disponibilita_magazzino, nome FROM PRODOTTO WHERE id_prodotto = ?";
        String updateProdotto = "UPDATE PRODOTTO SET disponibilita_magazzino = disponibilita_magazzino - ? WHERE id_prodotto = ?";

        try {
            con = ds.getConnection();
            con.setAutoCommit(false); 

            psSelectProdotto = con.prepareStatement(selectProdotto);
            for (ElementoCarrello elemento : carrello.getElementi()) {
                psSelectProdotto.setInt(1, elemento.getProdotto().getIdProdotto());
                
                try (ResultSet rsProdotto = psSelectProdotto.executeQuery()) {
                    if (rsProdotto.next()) {
                        int qtaDisponibile = rsProdotto.getInt("disponibilita_magazzino");
                        String nomeProdotto = rsProdotto.getString("nome");
                        
                        if (qtaDisponibile < elemento.getQuantita()) {
                            throw new SQLException("Attenzione: scorte insufficienti per " + nomeProdotto + ". Pezzi disponibili: " + qtaDisponibile);
                        }
                    }
                }
            }

            psOrdine = con.prepareStatement(insertOrdine, Statement.RETURN_GENERATED_KEYS);
            psOrdine.setInt(1, utente.getIdUtente());
            psOrdine.setDate(2, new Date(System.currentTimeMillis()));
            psOrdine.setDouble(3, carrello.getTotale()); 
            psOrdine.setString(4, indirizzoConsegna);
            psOrdine.setString(5, "Confermato");
            psOrdine.setString(6, metodoPagamento);
            psOrdine.executeUpdate();

            ResultSet rs = psOrdine.getGeneratedKeys();
            int idOrdine = -1;
            if (rs.next()) {
                idOrdine = rs.getInt(1);
            }

            psDettaglio = con.prepareStatement(insertDettaglio);
            psUpdateProdotto = con.prepareStatement(updateProdotto);
            
            for (ElementoCarrello elemento : carrello.getElementi()) {
                psDettaglio.setInt(1, idOrdine);
                psDettaglio.setInt(2, elemento.getProdotto().getIdProdotto());
                psDettaglio.setInt(3, elemento.getQuantita());
                psDettaglio.setDouble(4, elemento.getProdotto().getPrezzo());
                psDettaglio.setString(5, elemento.getProdotto().getNome());
                psDettaglio.setString(6, elemento.getProdotto().getCategoria());
                psDettaglio.addBatch();

                psUpdateProdotto.setInt(1, elemento.getQuantita());
                psUpdateProdotto.setInt(2, elemento.getProdotto().getIdProdotto());
                psUpdateProdotto.addBatch();
            }
            
            psDettaglio.executeBatch();
            psUpdateProdotto.executeBatch();

            con.commit(); 
            carrello.svuota(); 

        } catch (SQLException e) {
            if (con != null) con.rollback();  
            throw e;
        } finally {
            if (psOrdine != null) psOrdine.close();
            if (psDettaglio != null) psDettaglio.close();
            if (psSelectProdotto != null) psSelectProdotto.close();
            if (psUpdateProdotto != null) psUpdateProdotto.close();
            if (con != null) con.close();
        }
    }

    @Override
    public List<Ordine> doRetrieveByUtente(int idUtente) throws SQLException {
        List<Ordine> ordini = new ArrayList<>();
        String sql = "SELECT * FROM ORDINE WHERE id_utente = ? ORDER BY data_ordine DESC";
        
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            con = ds.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, idUtente);
            rs = ps.executeQuery();
            
            while (rs.next()) {
                Ordine ordine = new Ordine();
                ordine.setIdOrdine(rs.getInt("id_ordine"));
                ordine.setIdUtente(rs.getInt("id_utente"));
                ordine.setDataOrdine(rs.getDate("data_ordine"));
                ordine.setTotale(rs.getDouble("totale_ordine"));
                ordine.setIndirizzoConsegna(rs.getString("indirizzo_consegna"));
                ordine.setStatoOrdine(rs.getString("stato_ordine"));
                ordine.setMetodoPagamento(rs.getString("metodo_pagamento"));
                
                ordini.add(ordine);
            }
        } finally {
            if (rs != null) rs.close();
            if (ps != null) ps.close();
            if (con != null) con.close();
        }
        
        return ordini;
    }
    
    @Override
    public Ordine doRetrieveByKey(int idOrdine) throws SQLException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Ordine ordine = null;

        String selectSQL = "SELECT id_ordine, data_ordine, totale_ordine, id_utente FROM ordine WHERE id_ordine = ?";

        try {
            connection = ds.getConnection();
            preparedStatement = connection.prepareStatement(selectSQL);
            preparedStatement.setInt(1, idOrdine);

            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                ordine = new Ordine();
                ordine.setIdOrdine(resultSet.getInt("id_ordine"));
                ordine.setDataOrdine(resultSet.getDate("data_ordine")); 
                ordine.setTotale(resultSet.getDouble("totale_ordine")); 
                ordine.setIdUtente(resultSet.getInt("id_utente"));
            }
        } finally {
            try {
                if (resultSet != null) resultSet.close();
                if (preparedStatement != null) preparedStatement.close();
            } finally {
                if (connection != null) connection.close();
            }
        }
        return ordine;
    }

    @Override
    public List<Prodotto> doRetrieveProdottiByOrdine(int idOrdine) throws SQLException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        List<Prodotto> prodotti = new ArrayList<>();

        String selectSQL = "SELECT id_prodotto, nome_prodotto, categoria_prodotto, quantita, prezzo_acquistato " +
                            "FROM dettaglio_ordine WHERE id_ordine = ?";

        try {
            connection = ds.getConnection();
            preparedStatement = connection.prepareStatement(selectSQL);
            preparedStatement.setInt(1, idOrdine);

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Prodotto p = new Prodotto();
                p.setIdProdotto(resultSet.getInt("id_prodotto"));
                p.setNome(resultSet.getString("nome_prodotto"));
                p.setCategoria(resultSet.getString("categoria_prodotto"));
                p.setPrezzo(resultSet.getDouble("prezzo_acquistato"));
                p.setQuantita(resultSet.getInt("quantita"));
                prodotti.add(p);
            }
        } finally {
            try {
                if (resultSet != null) resultSet.close();
                if (preparedStatement != null) preparedStatement.close();
            } finally {
                if (connection != null) connection.close();
            }
        }
        return prodotti;
    }
}