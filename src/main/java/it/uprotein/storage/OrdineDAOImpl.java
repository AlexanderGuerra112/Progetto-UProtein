package it.uprotein.storage;

import java.sql.*;
import javax.sql.DataSource;
import it.uprotein.model.*;

public class OrdineDAOImpl implements OrdineDAO {
    private DataSource ds;

    public OrdineDAOImpl(DataSource ds) {
        this.ds = ds;
    }

    @Override
    public synchronized void doSave(Utente utente, Carrello carrello, String metodoPagamento) throws SQLException {
        Connection con = null;
        PreparedStatement psOrdine = null;
        PreparedStatement psDettaglio = null;
        PreparedStatement psSelectProdotto = null; 
        PreparedStatement psUpdateProdotto = null; 

        String insertOrdine = "INSERT INTO ORDINE (id_utente, data_ordine, totale_ordine, indirizzo_consegna , stato_ordine, metodo_pagamento) VALUES (?, ?, ?, ?, ?, ?)";
        String insertDettaglio = "INSERT INTO DETTAGLIO_ORDINE (id_ordine, id_prodotto, quantita, prezzo_acquistato) VALUES (?, ?, ?, ?)";
        
       
        String selectProdotto = "SELECT disponibilita_magazzino, nome FROM PRODOTTO WHERE id_prodotto = ?";
        String updateProdotto = "UPDATE PRODOTTO SET disponibilita_magazzino = disponibilita_magazzino - ? WHERE id_prodotto = ?";

        try {
            con = ds.getConnection();
            con.setAutoCommit(false); 

            //  CONTROLLO OUT OF STOCK
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

            //   NUOVO ORDINE
            psOrdine = con.prepareStatement(insertOrdine, Statement.RETURN_GENERATED_KEYS);
            psOrdine.setInt(1, utente.getIdUtente());
            psOrdine.setDate(2, new Date(System.currentTimeMillis()));
            psOrdine.setDouble(3, carrello.getTotale()); 
            psOrdine.setString(4, utente.getIndirizzoSpedizione());
            psOrdine.setString(5, "Confermato");
            psOrdine.setString(6, metodoPagamento);
            psOrdine.executeUpdate();

            
            ResultSet rs = psOrdine.getGeneratedKeys();
            int idOrdine = -1;
            if (rs.next()) {
                idOrdine = rs.getInt(1);
            }

            //  AGGIORNAMENTO DETTAGLI E DECREMENTO MAGAZZINO
            psDettaglio = con.prepareStatement(insertDettaglio);
            psUpdateProdotto = con.prepareStatement(updateProdotto);
            
            for (ElementoCarrello elemento : carrello.getElementi()) {
                
                psDettaglio.setInt(1, idOrdine);
                psDettaglio.setInt(2, elemento.getProdotto().getIdProdotto());
                psDettaglio.setInt(3, elemento.getQuantita());
                psDettaglio.setDouble(4, elemento.getProdotto().getPrezzo());
                psDettaglio.addBatch();

                //  scarico dal magazzino
                psUpdateProdotto.setInt(1, elemento.getQuantita());
                psUpdateProdotto.setInt(2, elemento.getProdotto().getIdProdotto());
                psUpdateProdotto.addBatch();
            }
            
            psDettaglio.executeBatch();
            psUpdateProdotto.executeBatch();

            // Se tutto è andato senza errori, salviamo sul db
            con.commit(); 
            
            // Svuotiamo il carrello
            carrello.svuota(); 

        } catch (SQLException e) {
            // Se scatta l Out of Stock o un errore annulliamo tutto 
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
}