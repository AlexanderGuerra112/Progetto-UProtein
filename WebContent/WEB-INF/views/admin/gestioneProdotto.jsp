<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page isELIgnored="false" %>
<%@ page import="java.util.List, it.uprotein.model.Prodotto" %>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <title>Dashboard Amministratore - UProtein</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/responsive.css">
</head>
<body>

<main class="admin-container">
    <div class="admin-header">
        <h1>Dashboard Amministratore - <span class="uprotein-green">UProtein</span></h1>
        <a href="<%= request.getContextPath() %>/" class="btn-home-admin"> Torna alla Home</a>
    </div>

    <h2>Catalogo Prodotti</h2>
<div class="tabella-responsiva">
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
                        <a href="<%= request.getContextPath() %>/admin/adminProdotto?azione=mostraModifica&id=<%= p.getIdProdotto() %>"
                           class="btn-modifica">Modifica</a>

                        <%-- Elimina: POST perché modifica dati nel DB --%>
                        <form action="<%= request.getContextPath() %>/admin/adminProdotto"
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
    </div>

    <%
        Prodotto prodottoDaModificare = (Prodotto) request.getAttribute("prodottoDaModificare");
        boolean isModifica = (prodottoDaModificare != null);
    %>

    <h2><%= isModifica ? "Modifica Prodotto" : "Aggiungi un Nuovo Prodotto" %></h2>

    <div class="admin-form-container">
       <form action="<%= request.getContextPath() %>/admin/adminProdotto" method="POST" enctype="multipart/form-data">
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
           step="0.01" min="0" required placeholder="Es. 29.90"
           value="<%= isModifica ? prodottoDaModificare.getPrezzo() : "" %>">
</div>

            <div class="form-group">
                <label for="disponibilita">Quantità in Magazzino:</label>
                <input type="number" id="disponibilita" name="disponibilita"
                       min="0" required placeholder="Es. 50"
                       value="<%= isModifica ? prodottoDaModificare.getDisponibilitaMagazzino() : "" %>">
            </div>

            <div class="form-group">
    <label for="foto">Seleziona Immagine Prodotto:</label>
    <input type="file" id="foto" name="foto" accept="image/*" <%= isModifica ? "" : "required" %>>

    <input type="hidden" name="immagine_url_esistente"
           value="<%= isModifica && prodottoDaModificare.getImmagineUrl() != null ? prodottoDaModificare.getImmagineUrl() : "" %>">

    <% if (isModifica && prodottoDaModificare.getImmagineUrl() != null && !prodottoDaModificare.getImmagineUrl().isEmpty()) { %>
        <p class="info-immagine-attuale">
            Immagine attuale: <strong><%= prodottoDaModificare.getImmagineUrl() %></strong> (Lascia vuoto per non cambiarla)
        </p>
    <% } %>
</div>

            <button type="submit" class="btn-salva">
                <%= isModifica ? "Aggiorna Prodotto" : "Salva nel Catalogo" %>
            </button>

            <% if (isModifica) { %>
                <a href="<%= request.getContextPath() %>/admin/adminProdotto?azione=mostra"
                   class="btn-annulla">Annulla</a>
            <% } %>
        </form>
    </div>

</main>

</body>
</html>