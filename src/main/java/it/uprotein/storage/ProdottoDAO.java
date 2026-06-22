package it.uprotein.storage;

import java.sql.SQLException;
import java.util.List;
import it.uprotein.model.Prodotto;

public interface ProdottoDAO {

    // Salva un nuovo prodotto nel DB
    void doSave(Prodotto prodotto) throws SQLException;

    // Cerca un prodotto tramite il suo ID
    Prodotto doRetrieveByKey(int idProdotto) throws SQLException;

    // Aggiorna un prodotto esistente
    void doUpdate(Prodotto prodotto) throws SQLException;

    // Elimina un prodotto dal DB tramite ID
    boolean doDelete(int idProdotto) throws SQLException;

    // Restituisce tutti i prodotti, con ordinamento sicuro
    List<Prodotto> doRetrieveAll(String order) throws SQLException;

    // Restituisce i prodotti filtrati per categoria
    List<Prodotto> doRetrieveByCategoria(String categoria) throws SQLException;
    
    public List<Prodotto> getProdottiPerRuota(double prezzoMin, double prezzoMax, int stockMinimo) throws SQLException;
}