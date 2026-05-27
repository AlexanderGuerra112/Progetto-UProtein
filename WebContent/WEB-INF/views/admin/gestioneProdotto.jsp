<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List, it.uprotein.model.Prodotto" %>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <title>Dashboard Amministratore - UProtein</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css">
    <style>
        body {
            font-family: 'Verdana', sans-serif;
            color: #1A1A1A;
            margin: 30px;
            background-color: #FFFFFF;
        }
        
        /* Titoli Principali */
        h1, h2 {
            color: #1A1A1A;
            border-bottom: 2px solid #2ECC71;
            padding-bottom: 10px;
        }
        
        .uprotein-green {
            color: #2ECC71;
            font-weight: bold;
        }
        
        /* Stili della Tabella Catalogo */
        .admin-table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 20px;
            margin-bottom: 40px;
        }
        
        .admin-table th, .admin-table td {
            border: 1px solid #E0E0E0;
            padding: 12px;
            text-align: left;
        }
        
        .admin-table th {
            background-color: #1A1A1A;
            color: #FFFFFF;
            font-weight: bold;
        }
        
        .admin-table tr:nth-child(even) {
            background-color: #F9F9F9;
        }
        
        .btn-elimina {
            background-color: #E74C3C;
            color: white;
            border: none;
            padding: 6px 12px;
            border-radius: 4px;
            cursor: pointer;
            text-decoration: none;
            font-size: 0.9em;
            display: inline-block;
        }
        
        .btn-elimina:hover {
            background-color: #C0392B;
        }
        
        /* Stili del Form di Inserimento */
        .form-container {
            background-color: #F5F5F5;
            padding: 25px;
            border-radius: 6px;
            max-width: 650px;
            margin-top: 20px;
            border: 1px solid #E0E0E0;
        }
        
        .form-group {
            margin-bottom: 15px;
        }
        
        .form-group label {
            display: block;
            margin-bottom: 6px;
            font-weight: bold;
            font-size: 0.95em;
        }
        
        .form-group input, .form-group textarea, .form-group select {
            width: 100%;
            padding: 10px;
            border: 1px solid #CCCCCC;
            border-radius: 4px;
            box-sizing: border-box;
            font-family: inherit;
        }
        
        .btn-salva {
            background-color: #2ECC71;
            color: white;
            border: none;
            padding: 12px 24px;
            border-radius: 4px;
            cursor: pointer;
            font-size: 1em;
            font-weight: bold;
            transition: background 0.2s;
        }
        
        .btn-salva:hover {
            background-color: #27AE60;
        }
    </style>
</head>
<body>

    <h1>Dashboard Amministratore - <span class="uprotein-green">UProtein</span></h1>
    
    <h2>Catalogo Prodotti</h2>
    
    <table class="admin-table">
        <thead>
            <tr>
                <th>ID</th>
                <th>Nome</th>
                <th>Categoria</th>
                <th>Prezzo</th>
                <th>Stock</th>
                <th>Azioni</th>
            </tr>
        </thead>
        <tbody>
            <%
                List<Prodotto> prodotti = (List<Prodotto>) request.getAttribute("prodotti");
                if (prodotti != null && !prodotti.isEmpty()) {
                    for (Prodotto p : prodotti) {
            %>
                    <tr>
                        <td><%= p.getIdProdotto() %></td>
                        <td><%= p.getNome() %></td>
                        <td><%= p.getCategoria() %></td>
                        <td>€ <%= String.format("%.2f", p.getPrezzo()) %></td>
                        <td><%= p.getDisponibilitaMagazzino() %></td>
                        <td>
                            <a href="${pageContext.request.contextPath}/adminProdotto?azione=elimina&id=<%= p.getIdProdotto() %>" 
                               class="btn-elimina" 
                               onclick="return confirm('Sei sicuro di voler eliminare questo prodotto definitivo?');">
                               Elimina
                            </a>
                        </td>
                    </tr>
            <%
                    }
                } else {
            %>
                <tr>
                    <td colspan="6" style="text-align: center; color: #7F8C8D;">Nessun prodotto disponibile nel catalogo.</td>
                </tr>
            <%
                }
            %>
        </tbody>
    </table>

    <h2>Aggiungi un Nuovo Prodotto</h2>
    <div class="form-container">
        <form action="${pageContext.request.contextPath}/adminProdotto" method="POST">
            <input type="hidden" name="azione" value="salva">
            <input type="hidden" name="id" value=""> <div class="form-group">
                <label for="nome">Nome Prodotto:</label>
                <input type="text" id="nome" name="nome" required placeholder="Es. Whey Protein 100% 1kg">
            </div>

            <div class="form-group">
                <label for="categoria">Categoria:</label>
                <select id="categoria" name="categoria" required>
                    <option value="Proteine">Proteine</option>
                    <option value="Creatina">Creatina</option>
                    <option value="Vitamine">Vitamine</option>
                    <option value="Barrette">Barrette</option>
                </select>
            </div>

            <div class="form-group">
                <label for="descrizione">Descrizione Prodotto:</label>
                <textarea id="descrizione" name="descrizione" rows="4" required placeholder="Inserisci le caratteristiche del prodotto..."></textarea>
            </div>

            <div class="form-group">
                <label for="prezzo">Prezzo di Vendita (€):</label>
                <input type="number" id="prezzo" name="prezzo" step="0.01" min="0.01" required placeholder="Es. 29.90">
            </div>

            <div class="form-group">
                <label for="disponibilita">Quantità in Magazzino (Stock):</label>
                <input type="number" id="disponibilita" name="disponibilita" min="0" required placeholder="Es. 50">
            </div>

            <div class="form-group">
                <label for="immagine_url">Percorso / URL Immagine:</label>
                <input type="text" id="immagine_url" name="immagine_url" placeholder="Es. immagini/prodotti/whey.jpg">
            </div>

            <button type="submit" class="btn-salva">Salva nel Catalogo</button>
        </form>
    </div>

</body>
</html>