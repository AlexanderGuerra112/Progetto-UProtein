package it.uprotein.model;

import java.io.Serializable;
import java.sql.Date;

public class Utente implements Serializable {
    private static final long serialVersionUID = 1L;

    private int idUtente;
    private String nome;
    private String cognome;
    private String email;
    private String password;
    private String ruolo; 
    private String indirizzoSpedizione;
    private String telefono;
    private Date dataUltimoGiro; 
    public Utente() {}

   
    public int getIdUtente() { return idUtente; }
    public void setIdUtente(int idUtente) { this.idUtente = idUtente; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getCognome() { return cognome; }
    public void setCognome(String cognome) { this.cognome = cognome; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getRuolo() { return ruolo; }
    public void setRuolo(String ruolo) { this.ruolo = ruolo; }

    public String getIndirizzoSpedizione() { return indirizzoSpedizione; }
    public void setIndirizzoSpedizione(String indirizzoSpedizione) { this.indirizzoSpedizione = indirizzoSpedizione; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public Date getDataUltimoGiro() { return dataUltimoGiro; }
    public void setDataUltimoGiro(Date dataUltimoGiro) { this.dataUltimoGiro = dataUltimoGiro; }
}