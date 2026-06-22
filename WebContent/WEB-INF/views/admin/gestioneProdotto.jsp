<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page isELIgnored="false" %>
<%@ page import="java.util.List, it.uprotein.model.Prodotto" %>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <title>Dashboard Amministratore - UProtein</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

<div class="admin-container">

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
                        <%-- Modifica: GET è corretto perché non modifica dati --%>
                        <a href="<%= request.getContextPath() %>/adminProdotto?azione=mostraModifica&id=<%= p.getIdProdotto() %>"
                           class="btn-modifica">Modifica</a>

                        <%-- Elimina: POST perché modifica dati nel DB --%>
                        <form action="<%= request.getContextPath() %>/adminProdotto"
                              method="POST"
                             class="form-elimina"
                              onsubmit="return confirm('Sei sicuro di voler eliminare questo prodotto?');">
                            <input type="hidden" name="azione" value="elimina">
                            <input type="hidden" name="id" value="<%= p.getIdProdotto() %>">
                            <button type="submit" class="btn-elimina">Elimina</button>
                        </form>
                    </td>
                </tr>
            <%
                    }
                } else {
            %>
                <tr>
                    <td colspan="6" class="nessun-prodotto-admin">
                        Nessun prodotto disponibile nel catalogo.
                    </td>
                </tr>
            <%
                }
            %>
        </tbody>
    </table>

    <%
        Prodotto prodottoDaModificare = (Prodotto) request.getAttribute("prodottoDaModificare");
        boolean isModifica = (prodottoDaModificare != null);
    %>

    <h2><%= isModifica ? "Modifica Prodotto" : "Aggiungi un Nuovo Prodotto" %></h2>

    <div class="admin-form-container">
        <form action="<%= request.getContextPath() %>/adminProdotto" method="POST">
            <input type="hidden" name="azione" value="salva">
            <input type="hidden" name="id"
                   value="<%= isModifica ? prodottoDaModificare.getIdProdotto() : "" %>">

            <div class="form-group">
                <label for="nome">Nome Prodotto:</label>
                <input type="text" id="nome" name="nome" required
                       placeholder="Es. Whey Protein 100% 1kg"
                       value="<%= isModifica ? prodottoDaModificare.getNome() : "" %>">
            </div>

            <div class="form-group">
                <label for="categoria">Categoria:</label>
                <select id="categoria" name="categoria" required>
                    <option value="Proteine" <%= isModifica && "Proteine".equals(prodottoDaModificare.getCategoria()) ? "selected" : "" %>>Proteine</option>
                    <option value="Creatina" <%= isModifica && "Creatina".equals(prodottoDaModificare.getCategoria()) ? "selected" : "" %>>Creatina</option>
                    <option value="Vitamine" <%= isModifica && "Vitamine".equals(prodottoDaModificare.getCategoria()) ? "selected" : "" %>>Vitamine</option>
                    <option value="Barrette" <%= isModifica && "Barrette".equals(prodottoDaModificare.getCategoria()) ? "selected" : "" %>>Barrette</option>
                </select>
            </div>

            <div class="form-group">
                <label for="descrizione">Descrizione Prodotto:</label>
                <textarea id="descrizione" name="descrizione" rows="4" required
                          placeholder="Inserisci le caratteristiche del prodotto..."><%= isModifica ? prodottoDaModificare.getDescrizione() : "" %></textarea>
            </div>

            <div class="form-group">
                <label for="prezzo">Prezzo di Vendita (€):</label>
                <input type="number" id="prezzo" name="prezzo"
                       step="1.00" min="0.90" required placeholder="Es. 29.90"
                       value="<%= isModifica ? prodottoDaModificare.getPrezzo() : "" %>">
            </div>

            <div class="form-group">
                <label for="disponibilita">Quantità in Magazzino:</label>
                <input type="number" id="disponibilita" name="disponibilita"
                       min="0" required placeholder="Es. 50"
                       value="<%= isModifica ? prodottoDaModificare.getDisponibilitaMagazzino() : "" %>">
            </div>

            <div class="form-group">
                <label for="immagine_url">URL Immagine:</label>
                <input type="text" id="immagine_url" name="immagine_url"
                       placeholder="Es. immagini/prodotti/whey.jpg"
                       value="<%= isModifica && prodottoDaModificare.getImmagineUrl() != null ? prodottoDaModificare.getImmagineUrl() : "" %>">
            </div>

            <button type="submit" class="btn-salva">
                <%= isModifica ? "Aggiorna Prodotto" : "Salva nel Catalogo" %>
            </button>

            <% if (isModifica) { %>
                <a href="<%= request.getContextPath() %>/adminProdotto?azione=mostra"
                   class="btn-annulla">Annulla</a>
            <% } %>
        </form>
    </div>

</div>

</body>
</html>