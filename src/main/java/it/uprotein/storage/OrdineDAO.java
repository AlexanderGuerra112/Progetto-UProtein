package it.uprotein.storage;

import java.sql.SQLException;
import java.util.List;
import it.uprotein.model.Utente;
import it.uprotein.model.Carrello;
import it.uprotein.model.Ordine; 
import it.uprotein.model.Prodotto; // 1. Aggiungiamo questo import

public interface OrdineDAO {

    // Salva un nuovo ordine (già presente)
    void doSave(Utente utente, Carrello carrello, String metodoPagamento) throws SQLException;

    // Recupera la lista degli ordini effettuati da un utente (già presente)
    List<Ordine> doRetrieveByUtente(int idUtente) throws SQLException;

    // 2. NUOVO: Recupera un singolo ordine dal suo ID
    Ordine doRetrieveByKey(int idOrdine) throws SQLException;

    // 3. NUOVO: Recupera i prodotti acquistati in un determinato ordine
    List<Prodotto> doRetrieveProdottiByOrdine(int idOrdine) throws SQLException;
}