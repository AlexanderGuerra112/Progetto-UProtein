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

    // Aggiunge un prodotto al carrello. Se c'è già lo stesso prodotto, aumenta solo la quantità
    public void aggiungiProdotto(Prodotto prodotto, int quantita) {
        for (ElementoCarrello elemento : elementi) {
            // CORRETTO: Adesso usiamo il metodo esatto della tua classe Prodotto
            if (elemento.getProdotto().getIdProdotto() == prodotto.getIdProdotto()) {
                elemento.setQuantita(elemento.getQuantita() + quantita);
                return;
            }
        }
        // Se il prodotto non era nel carrello, lo aggiunge come nuovo elemento
        elementi.add(new ElementoCarrello(prodotto, quantita));
    }

    // Rimuove completamente un prodotto dal carrello in base al suo ID
    public void rimuoviProdotto(int idProdotto) {
        // CORRETTO: Anche qui usiamo il metodo esatto della tua classe Prodotto
        elementi.removeIf(elemento -> elemento.getProdotto().getIdProdotto() == idProdotto);
    }

    // Svuota completamente il carrello (es. dopo aver acquistato)
    public void svuota() {
        elementi.clear();
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