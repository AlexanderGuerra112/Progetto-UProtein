package it.uprotein.model;

import java.io.Serializable;
import java.sql.Date;

public class Ordine implements Serializable {
    
    private static final long serialVersionUID = 1L;

    private int idOrdine;
    private int idUtente;
    private Date dataOrdine;
    private double totale;
    private String indirizzoConsegna;
    private String statoOrdine;
    private String metodoPagamento;

    
    public Ordine() {}

    // Getter e Setter
    public int getIdOrdine() { return idOrdine; }
    public void setIdOrdine(int idOrdine) { this.idOrdine = idOrdine; }

    public int getIdUtente() { return idUtente; }
    public void setIdUtente(int idUtente) { this.idUtente = idUtente; }

    public Date getDataOrdine() { return dataOrdine; }
    public void setDataOrdine(Date dataOrdine) { this.dataOrdine = dataOrdine; }

    public double getTotale() { return totale; }
    public void setTotale(double totale) { this.totale = totale; }

    public String getIndirizzoConsegna() { return indirizzoConsegna; }
    public void setIndirizzoConsegna(String indirizzoConsegna) { this.indirizzoConsegna = indirizzoConsegna; }

    public String getStatoOrdine() { return statoOrdine; }
    public void setStatoOrdine(String statoOrdine) { this.statoOrdine = statoOrdine; }

    public String getMetodoPagamento() { return metodoPagamento; }
    public void setMetodoPagamento(String metodoPagamento) { this.metodoPagamento = metodoPagamento; }
}