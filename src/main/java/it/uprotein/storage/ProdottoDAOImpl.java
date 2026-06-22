package it.uprotein.storage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import javax.sql.DataSource;

import it.uprotein.model.Prodotto;

public class ProdottoDAOImpl implements ProdottoDAO {

    private static final String TABLE_NAME = "PRODOTTO";

    
    private static final List<String> ORDINI_AMMESSI = List.of(
        "nome", "prezzo", "categoria", "disponibilita_magazzino"
    );

    private DataSource ds = null;

    public ProdottoDAOImpl(DataSource ds) {
        this.ds = ds;
    }

    @Override
    public synchronized List<Prodotto> getProdottiPerRuota(double prezzoMin, double prezzoMax, int stockMinimo) throws SQLException {
        List<Prodotto> lista = new LinkedList<>();
        String selectSQL = "SELECT * FROM " + TABLE_NAME + " WHERE disponibilita_magazzino >= ? AND prezzo >= ? AND prezzo < ? ORDER BY RAND() LIMIT 10";
        
        try (Connection con = ds.getConnection(); 
             PreparedStatement ps = con.prepareStatement(selectSQL)) {
            
            ps.setInt(1, stockMinimo);
            ps.setDouble(2, prezzoMin);
            ps.setDouble(3, prezzoMax);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapRow(rs)); 
                }
            }
        }
        return lista;
    }
    
    
    
    
    
    
    @Override
    public synchronized void doSave(Prodotto prodotto) throws SQLException {
        String insertSQL = "INSERT INTO " + TABLE_NAME
                + " (nome, categoria, descrizione, prezzo, disponibilita_magazzino, immagine_url)"
                + " VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection con = ds.getConnection();
             PreparedStatement ps = con.prepareStatement(insertSQL)) {

            ps.setString(1, prodotto.getNome());
            ps.setString(2, prodotto.getCategoria());
            ps.setString(3, prodotto.getDescrizione());
            ps.setDouble(4, prodotto.getPrezzo());
            ps.setInt(5, prodotto.getDisponibilitaMagazzino());
            ps.setString(6, prodotto.getImmagineUrl());

            ps.executeUpdate();
        }
    }
    
    
    
    
    
    

    @Override
    public synchronized Prodotto doRetrieveByKey(int idProdotto) throws SQLException {
        Prodotto bean = null;
        String selectSQL = "SELECT * FROM " + TABLE_NAME + " WHERE id_prodotto = ?";

        try (Connection con = ds.getConnection();
             PreparedStatement ps = con.prepareStatement(selectSQL)) {

            ps.setInt(1, idProdotto);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    bean = mapRow(rs);
                }
            }
        }
        return bean;
    }

    
    
    
    
    
    
    
    
    
    @Override
    public synchronized void doUpdate(Prodotto prodotto) throws SQLException {
        String updateSQL = "UPDATE " + TABLE_NAME
                + " SET nome = ?, categoria = ?, descrizione = ?, prezzo = ?,"
                + " disponibilita_magazzino = ?, immagine_url = ?"
                + " WHERE id_prodotto = ?";

        try (Connection con = ds.getConnection();
             PreparedStatement ps = con.prepareStatement(updateSQL)) {

            ps.setString(1, prodotto.getNome());
            ps.setString(2, prodotto.getCategoria());
            ps.setString(3, prodotto.getDescrizione());
            ps.setDouble(4, prodotto.getPrezzo());
            ps.setInt(5, prodotto.getDisponibilitaMagazzino());
            ps.setString(6, prodotto.getImmagineUrl());
            ps.setInt(7, prodotto.getIdProdotto());

            ps.executeUpdate();
        }
    }

    
    
    
    
    
    
    
    
    @Override
    public synchronized boolean doDelete(int idProdotto) throws SQLException {
        String deleteSQL = "DELETE FROM " + TABLE_NAME + " WHERE id_prodotto = ?";

        try (Connection con = ds.getConnection();
             PreparedStatement ps = con.prepareStatement(deleteSQL)) {

            ps.setInt(1, idProdotto);
            int result = ps.executeUpdate();
            return result != 0;
        }
    }

    
    
    @Override
    public synchronized List<Prodotto> doRetrieveAll(String order) throws SQLException {
        List<Prodotto> prodotti = new LinkedList<>();

        String orderClause = "";
        if (order != null && ORDINI_AMMESSI.contains(order.toLowerCase())) {
            orderClause = " ORDER BY " + order;
        }

        String selectSQL = "SELECT * FROM " + TABLE_NAME + orderClause;

        try (Connection con = ds.getConnection();
             PreparedStatement ps = con.prepareStatement(selectSQL);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                prodotti.add(mapRow(rs));
            }
        }
        return prodotti;
    }

    
    
    
    
    
    
    
    
    @Override
    public synchronized List<Prodotto> doRetrieveByCategoria(String categoria) throws SQLException {
        List<Prodotto> prodotti = new LinkedList<>();
        String selectSQL = "SELECT * FROM " + TABLE_NAME + " WHERE categoria = ?";

        try (Connection con = ds.getConnection();
             PreparedStatement ps = con.prepareStatement(selectSQL)) {

            ps.setString(1, categoria);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    prodotti.add(mapRow(rs));
                }
            }
        }
        return prodotti;
    }

  
    
    
    
    
    
    private Prodotto mapRow(ResultSet rs) throws SQLException {
        Prodotto bean = new Prodotto();
        bean.setIdProdotto(rs.getInt("id_prodotto"));
        bean.setNome(rs.getString("nome"));
        bean.setCategoria(rs.getString("categoria"));
        bean.setDescrizione(rs.getString("descrizione"));
        bean.setPrezzo(rs.getDouble("prezzo"));
        bean.setDisponibilitaMagazzino(rs.getInt("disponibilita_magazzino"));
        bean.setImmagineUrl(rs.getString("immagine_url"));
        return bean;
    }
}