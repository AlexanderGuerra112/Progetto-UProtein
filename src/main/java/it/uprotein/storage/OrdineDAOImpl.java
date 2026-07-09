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

        String insertOrdine = "INSERT INTO ORDINE (id_utente, data_ordine, totale_ordine, indirizzo_consegna , stato_ordine, metodo_pagamento) VALUES (?, ?, ?, ?, ?, ?)";
        String insertDettaglio = "INSERT INTO DETTAGLIO_ORDINE (id_ordine, id_prodotto, quantita, prezzo_acquistato) VALUES (?, ?, ?, ?)";

        try {
            con = ds.getConnection();
            con.setAutoCommit(false); 

            //  Inserimento nella tabella 
            psOrdine = con.prepareStatement(insertOrdine, Statement.RETURN_GENERATED_KEYS);
            psOrdine.setInt(1, utente.getIdUtente());
            psOrdine.setDate(2, new Date(System.currentTimeMillis()));
            psOrdine.setDouble(3, carrello.getTotale()); // Usa il tuo metodo getTotale()
            psOrdine.setString(4, utente.getIndirizzoSpedizione());
            psOrdine.setString(5, "Confermato");
            psOrdine.setString(6, metodoPagamento);
            psOrdine.executeUpdate();

            // Recupero dell'ID ordine appena generato per collegare i dettagli
            ResultSet rs = psOrdine.getGeneratedKeys();
            int idOrdine = -1;
            if (rs.next()) {
                idOrdine = rs.getInt(1);
            }

           // Inserimento dei prodotti 
            psDettaglio = con.prepareStatement(insertDettaglio);
            
            for (ElementoCarrello elemento : carrello.getElementi()) {
                psDettaglio.setInt(1, idOrdine);
                psDettaglio.setInt(2, elemento.getProdotto().getIdProdotto());
                psDettaglio.setInt(3, elemento.getQuantita());
                psDettaglio.setDouble(4, elemento.getProdotto().getPrezzo());
                psDettaglio.addBatch();
            }
            psDettaglio.executeBatch();

            con.commit(); // Conferma finale dell operazione
            
            // Svuotiamo il carrello dopo l'acquisto 
            carrello.svuota(); 

        } catch (SQLException e) {
            if (con != null) con.rollback();  // Se qualcosa fallisce annulliammo tutto per sicurezza
            throw e;
        } finally {
            if (psOrdine != null) psOrdine.close();
            if (psDettaglio != null) psDettaglio.close();
            if (con != null) con.close();
        }
    }
}