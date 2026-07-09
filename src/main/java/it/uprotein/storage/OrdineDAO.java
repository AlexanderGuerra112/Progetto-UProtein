package it.uprotein.storage;

import java.sql.SQLException;
import it.uprotein.model.Utente;
import it.uprotein.model.Carrello;

public interface OrdineDAO {
    
    void doSave(Utente utente, Carrello carrello, String metodoPagamento) throws SQLException;
}
