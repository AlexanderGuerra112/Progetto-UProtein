package it.uprotein.storage;

import java.sql.SQLException;
import java.util.List;
import it.uprotein.model.Utente;

public interface UtenteDAO {

    // Salva un nuovo utente (Registrazione)
    void doSave(Utente utente) throws SQLException;

    // Recupera un utente tramite email e password (Login)
    Utente doRetrieveByLogin(String email, String password) throws SQLException;

    // Cerca un utente tramite il suo ID (PK)
    Utente doRetrieveByKey(int idUtente) throws SQLException;

    // Aggiorna i dati di un utente (Profilo)
    void doUpdate(Utente utente) throws SQLException;

    // Elimina un utente dal sistema
    boolean doDelete(int idUtente) throws SQLException;

    // Restituisce tutti gli utenti con un ordinamento specifico
    List<Utente> doRetrieveAll(String order) throws SQLException;
    
    void doUpdatePassword(int idUtente, String nuovaPassword) throws SQLException;
    
    public void doUpdateDataUltimoGiro(String email, java.sql.Date data) throws SQLException;
}

