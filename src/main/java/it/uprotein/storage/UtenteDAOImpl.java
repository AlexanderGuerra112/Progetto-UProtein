package it.uprotein.storage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;
import it.uprotein.model.Utente;

public class UtenteDAOImpl implements UtenteDAO {

    private static final String TABLE_NAME = "UTENTE";

    private static final List<String> ORDINI_AMMESSI = List.of(
        "id_utente", "nome", "cognome", "email", "ruolo"
    );

    private DataSource ds = null;

   
    public UtenteDAOImpl(DataSource ds) {
        this.ds = ds;
    }

    @Override
    public synchronized void doSave(Utente utente) throws SQLException {
        String insertSQL = "INSERT INTO " + TABLE_NAME + " (nome, cognome, email, password, ruolo, indirizzo_spedizione, telefono) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = ds.getConnection(); 
             PreparedStatement ps = con.prepareStatement(insertSQL)) {
            
            ps.setString(1, utente.getNome());
            ps.setString(2, utente.getCognome());
            ps.setString(3, utente.getEmail());
            ps.setString(4, utente.getPassword());
            
            String ruoloDaSalvare = "cliente"; 
            if (utente.getRuolo() != null && !utente.getRuolo().trim().isEmpty()) {
                ruoloDaSalvare = utente.getRuolo();
            }
            ps.setString(5, ruoloDaSalvare);
            
            ps.setString(6, utente.getIndirizzoSpedizione());
            ps.setString(7, utente.getTelefono());

            ps.executeUpdate();
        }
    }

    public synchronized Utente doRetrieveByLogin(String email, String password) throws SQLException {
        String selectSQL = "SELECT * FROM " + TABLE_NAME + " WHERE email = ? AND password = ?";
        Utente utente = null;

        try (Connection con = ds.getConnection(); 
             PreparedStatement ps = con.prepareStatement(selectSQL)) {
            
            // Il .trim() previene errori di battitura eliminando spazi vuoti all'inizio/fine
            ps.setString(1, email != null ? email.trim() : "");
            ps.setString(2, password != null ? password.trim() : "");

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    utente = mappaUtente(rs);
                }
            }
        }
        return utente;
    }

    @Override
    public synchronized Utente doRetrieveByKey(int idUtente) throws SQLException {
        String selectSQL = "SELECT * FROM " + TABLE_NAME + " WHERE id_utente = ?";
        Utente utente = null;

        try (Connection con = ds.getConnection(); 
             PreparedStatement ps = con.prepareStatement(selectSQL)) {
            
            ps.setInt(1, idUtente);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    utente = mappaUtente(rs);
                }
            }
        }
        return utente;
    }

    @Override
    public synchronized List<Utente> doRetrieveAll(String order) throws SQLException {
        String selectSQL = "SELECT * FROM " + TABLE_NAME;
        
        
        if (order != null && ORDINI_AMMESSI.contains(order.toLowerCase())) {
            selectSQL += " ORDER BY " + order;
        }

        List<Utente> utenti = new ArrayList<>();
        try (Connection con = ds.getConnection(); 
             PreparedStatement ps = con.prepareStatement(selectSQL);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                utenti.add(mappaUtente(rs));
            }
        }
        return utenti;
    }

    @Override
    public synchronized void doUpdate(Utente utente) throws SQLException {
        String updateSQL = "UPDATE " + TABLE_NAME + " SET nome=?, cognome=?, indirizzo_spedizione=?, telefono=? WHERE id_utente=?";

        try (Connection con = ds.getConnection(); 
             PreparedStatement ps = con.prepareStatement(updateSQL)) {
            
            ps.setString(1, utente.getNome());
            ps.setString(2, utente.getCognome());
            ps.setString(3, utente.getIndirizzoSpedizione());
            ps.setString(4, utente.getTelefono());
            ps.setInt(5, utente.getIdUtente());

            ps.executeUpdate();
        }
    }

    @Override
    public synchronized boolean doDelete(int idUtente) throws SQLException {
        String deleteSQL = "DELETE FROM " + TABLE_NAME + " WHERE id_utente = ?";
        int result;

        try (Connection con = ds.getConnection(); 
             PreparedStatement ps = con.prepareStatement(deleteSQL)) {
            
            ps.setInt(1, idUtente);
            result = ps.executeUpdate();
        }
        return (result != 0);
    }

   
    private Utente mappaUtente(ResultSet rs) throws SQLException {
        Utente u = new Utente();
        u.setIdUtente(rs.getInt("id_utente"));
        u.setNome(rs.getString("nome"));
        u.setCognome(rs.getString("cognome"));
        u.setEmail(rs.getString("email"));
        u.setPassword(rs.getString("password"));
        u.setRuolo(rs.getString("ruolo"));
        u.setIndirizzoSpedizione(rs.getString("indirizzo_spedizione"));
        u.setTelefono(rs.getString("telefono"));
        u.setDataUltimoGiro(rs.getDate("data_ultimo_giro")); 
        return u;
    }
    
    public synchronized void doUpdatePassword(int idUtente, String nuovaPassword) throws SQLException {
        String updateSQL = "UPDATE " + TABLE_NAME + " SET password = ? WHERE id_utente = ?";

        try (Connection con = ds.getConnection(); 
             PreparedStatement ps = con.prepareStatement(updateSQL)) {
            
            ps.setString(1, nuovaPassword);
            ps.setInt(2, idUtente);
            ps.executeUpdate();
        }
    }
    
    
    @Override
    public synchronized void doUpdateDataGiro(int idUtente, java.sql.Date nuovaData) throws SQLException {
        String updateSQL = "UPDATE " + TABLE_NAME + " SET data_ultimo_giro = ? WHERE id_utente = ?";

        try (Connection con = ds.getConnection(); 
             PreparedStatement ps = con.prepareStatement(updateSQL)) {
            
            ps.setDate(1, nuovaData);
            ps.setInt(2, idUtente);
            ps.executeUpdate();
        }
    }
}