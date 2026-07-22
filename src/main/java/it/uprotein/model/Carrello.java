package it.uprotein.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Carrello implements Serializable {
    
    private static final long serialVersionUID = 1L;

    // La lista che contiene fisicamente i prodotti e le loro quantità
    private List<ElementoCarrello> elementi;

    public Carrello() {
        this.elementi = new ArrayList<>();
    }

    // Recupera tutti gli elementi nel carrello
    public List<ElementoCarrello> getElementi() {
        return elementi;
    }

    
    public void aggiungiProdotto(Prodotto prodotto, int quantita) {
        for (ElementoCarrello elemento : elementi) {
            if (elemento.getProdotto().getIdProdotto() == prodotto.getIdProdotto() &&
                Double.compare(elemento.getProdotto().getPrezzo(), prodotto.getPrezzo()) == 0) {
                
                elemento.setQuantita(elemento.getQuantita() + quantita);
                return;
            }
        }
        // Se non c'è una riga con la stessa combinazione ID-Prezzo, aggiunge un nuovo elemento
        elementi.add(new ElementoCarrello(prodotto, quantita));
    }

    // NUOVO METODO: Rimuove in modo mirato un prodotto basandosi sia su ID che su Prezzo
    public void rimuoviProdotto(int idProdotto, double prezzo) {
        elementi.removeIf(elemento -> elemento.getProdotto().getIdProdotto() == idProdotto 
            && Double.compare(elemento.getProdotto().getPrezzo(), prezzo) == 0);
    }

    // Mantiene la compatibilità con la rimozione classica 
    public void rimuoviProdotto(int idProdotto) {
        elementi.removeIf(elemento -> elemento.getProdotto().getIdProdotto() == idProdotto);
    }

    // Svuota completamente il carrello
    public void svuota() {
        this.elementi = new ArrayList<>();
    }

    // Calcola il prezzo totale di tutto il carrello
    public double getTotale() {
        double totale = 0;
        for (ElementoCarrello elemento : elementi) {
            totale += elemento.getSubtotale();
        }
        return totale;
    }
}