<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page isELIgnored="false" %>
<%@ page import="java.util.List, it.uprotein.model.Prodotto" %>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <title>UProtein - Prodotti</title>
    <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/css/style.css">
</head>
<body>

    <nav class="navbar">
        <a href="<%= request.getContextPath() %>/home" class="logo">UProtein</a>
        <div class="nav-links">
            <a href="<%= request.getContextPath() %>/prodotti?azione=mostra&categoria=Proteine">Proteine</a>
            <a href="<%= request.getContextPath() %>/prodotti?azione=mostra&categoria=Creatina">Creatina</a>
            <a href="<%= request.getContextPath() %>/prodotti?azione=mostra&categoria=Vitamine">Vitamine</a>
            <a href="<%= request.getContextPath() %>/prodotti?azione=mostra&categoria=Barrette">Barrette</a>
        </div>
        <div class="nav-icons">
            <%
                it.uprotein.model.Utente utenteLoggato =
                    (it.uprotein.model.Utente) session.getAttribute("utente");
                if (utenteLoggato != null) {
            %>
                <a href="<%= request.getContextPath() %>/home">&#128100; <%= utenteLoggato.getNome() %></a>
                <a href="<%= request.getContextPath() %>/login?azione=logout" class="btn-esci">(Esci)</a>
            <%
                } else {
            %>
                <a href="<%= request.getContextPath() %>/login?azione=mostra">&#128100; Profilo</a>
            <%
                }
            %>
            <a href="<%= request.getContextPath() %>/carrello?azione=mostra">&#128722; Carrello</a>
        </div>
    </nav>

    <%-- INTESTAZIONE PAGINA --%>
    <div class="prodotti-header">
        <%
            String categoriaAttiva = (String) request.getAttribute("categoriaAttiva");
        %>
        <h2 class="section-title"><%= categoriaAttiva != null ? categoriaAttiva : "Tutti i prodotti" %></h2>
    </div>

    <%-- GRIGLIA PRODOTTI --%>
    <div class="bestseller-grid">
        <%
            List<Prodotto> listaProdotti = (List<Prodotto>) request.getAttribute("listaProdotti");
            if (listaProdotti == null || listaProdotti.isEmpty()) {
        %>
            <p class="nessun-prodotto">Nessun prodotto disponibile in questa categoria.</p>
        <%
            } else {
                for (Prodotto p : listaProdotti) {
        %>
            <div class="product-card">
                <div class="product-img-placeholder"></div>
                <h3><%= p.getNome() %></h3>
                <p class="price">€ <%= String.format("%.2f", p.getPrezzo()) %></p>
                <p class="product-disponibilita">
                    <% if (p.getDisponibilitaMagazzino() > 0) { %>
                        Disponibile
                    <% } else { %>
                        Esaurito
                    <% } %>
                </p>
                <%-- Form POST per aggiungere al carrello --%>
                <% if (p.getDisponibilitaMagazzino() > 0) { %>
                <form action="<%= request.getContextPath() %>/carrello" method="GET">
                    <input type="hidden" name="azione" value="aggiungi">
                    <input type="hidden" name="idProdotto" value="<%= p.getIdProdotto() %>">
                    <input type="hidden" name="quantita" value="1">
                    <button type="submit" class="btn-primary btn-full">Aggiungi al carrello</button>
                </form>
                <% } else { %>
                    <button class="btn-primary btn-full btn-esaurito" disabled>Esaurito</button>
                <% } %>
            </div>
        <%
                }
            }
        %>
    </div>

    <footer class="footer">
        <div class="footer-inner">
            <div class="footer-brand">
                <h4>UProtein</h4>
                <p>&#169; 2026 UProtein. All rights reserved.</p>
            </div>
            <div class="footer-links">
                <a href="#">Chi siamo</a>
                <a href="#">Spedizioni e Resi</a>
            </div>
        </div>
    </footer>

</body>
</html>