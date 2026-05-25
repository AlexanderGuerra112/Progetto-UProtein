package it.uprotein.model;

import java.io.Serializable;

public class ElementoCarrello implements Serializable {
    
   
    private static final long serialVersionUID = 1L;

    private Prodotto prodotto; 
    private int quantita;

    public ElementoCarrello() {
    }

    public ElementoCarrello(Prodotto prodotto, int quantita) {
        this.prodotto = prodotto;
        this.quantita = quantita;
    }

    public double getSubtotale() {
        return this.prodotto.getPrezzo() * this.quantita;
    }

    public Prodotto getProdotto() { 
        return prodotto; 
    }
    
    public void setProdotto(Prodotto prodotto) { 
        this.prodotto = prodotto; 
    }

    public int getQuantita() { 
        return quantita; 
    }
    
    public void setQuantita(int quantita) { 
        this.quantita = quantita; 
    }
}