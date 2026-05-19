package it.uprotein.model;

import java.io.Serializable;

public class Prodotto implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private int idProdotto;
    private String nome;
    private String categoria;
    private String descrizione;
    private double prezzo;
    private int disponibilitaMagazzino;
    private String immagineUrl;
    
    public Prodotto() {
    	
    }
    
    public Prodotto(int idProdotto, String nome, String categoria, String descrizione, double prezzo, 
                      int disponibilitaMagazzino, String immagineUrl) {
  this.idProdotto = idProdotto;
  this.nome = nome;
  this.categoria = categoria;
  this.descrizione = descrizione;
  this.prezzo = prezzo;
  this.disponibilitaMagazzino = disponibilitaMagazzino;
  this.immagineUrl = immagineUrl;
  }

	public int getIdProdotto() {
		return idProdotto;
	}

	public void setIdProdotto(int idProdotto) {
		this.idProdotto = idProdotto;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getCategoria() {
		return categoria;
	}

	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public double getPrezzo() {
		return prezzo;
	}

	public void setPrezzo(double prezzo) {
		this.prezzo = prezzo;
	}

	public int getDisponibilitaMagazzino() {
		return disponibilitaMagazzino;
	}

	public void setDisponibilitaMagazzino(int disponibilitaMagazzino) {
		this.disponibilitaMagazzino = disponibilitaMagazzino;
	}

	public String getImmagineUrl() {
		return immagineUrl;
	}

	public void setImmagineUrl(String immagineUrl) {
		this.immagineUrl = immagineUrl;
	}
	
    
    
    
}
