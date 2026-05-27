<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page isELIgnored="false" %>
<%@ page import="java.util.List, it.uprotein.model.Prodotto" %>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <title>UProtein - Level UP Your Gym Game</title>
   <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

    <%-- NAVBAR --%>
    <nav class="navbar">
        <a href="${pageContext.request.contextPath}/home" class="logo">UProtein</a>
        <div class="nav-links">
            <a href="${pageContext.request.contextPath}/prodotti?azione=mostra&categoria=Proteine">Proteine</a>
            <a href="${pageContext.request.contextPath}/prodotti?azione=mostra&categoria=Creatina">Creatina</a>
            <a href="${pageContext.request.contextPath}/prodotti?azione=mostra&categoria=Vitamine">Vitamine</a>
            <a href="${pageContext.request.contextPath}/prodotti?azione=mostra&categoria=Barrette">Barrette</a>
        </div>
        <div class="nav-icons">
         <%
    // Controlliamo se nella sessione c'è un utente loggato
    it.uprotein.model.Utente utenteLoggato = (it.uprotein.model.Utente) session.getAttribute("utente");
    if (utenteLoggato != null) {
%>
    <a href="${pageContext.request.contextPath}/home">&#128100; <%= utenteLoggato.getNome() %></a>
    
    <%-- CONTROLLO RUOLO: Mostra l'accesso alla dashboard se l'utente è un admin --%>
    <% if (utenteLoggato.getRuolo() != null && utenteLoggato.getRuolo().equalsIgnoreCase("admin")) { %>
        <a href="${pageContext.request.contextPath}/adminProdotto?azione=mostra" style="color: #2ECC71; font-weight: bold; margin: 0 5px;">[Area Admin]</a>
    <% } %>

    <a href="${pageContext.request.contextPath}/login?azione=logout" class="btn-esci">(Esci)</a>
<%
    } else {
%>
    <a href="${pageContext.request.contextPath}/login?azione=mostra">&#128100; Profilo</a>
<%
    }
%>
            <a href="${pageContext.request.contextPath}/carrello?azione=mostra">&#128722; Carrello</a>
        </div>
    </nav>

    <%-- HERO BANNER --%>
    <header class="hero">
        <h1>FINO AL 75% DI SCONTO</h1>
        <p>I migliori supplementi per il tuo level UP.</p>
        <button class="btn-primary" onclick="location.href='${pageContext.request.contextPath}/prodotti?azione=mostra'">
            COMPRA ORA
        </button>
    </header>

    <%-- BESTSELLER --%>
<section>
    <h2 class="section-title">I NOSTRI BESTSELLER</h2>
    <div class="bestseller-grid">
        <%
            List<it.uprotein.model.Prodotto> prodotti =
                (List<it.uprotein.model.Prodotto>) request.getAttribute("prodotti");

            if (prodotti != null && !prodotti.isEmpty()) {
                int count = 0;
                for (it.uprotein.model.Prodotto p : prodotti) {
                    if (count >= 4) break; // Mostriamo solo i primi 4 in home
        %>
                <div class="product-card">
                    <div class="product-img-placeholder"></div>
                    <h3><%= p.getNome() %></h3>
                    <p class="price">€ <%= String.format("%.2f", p.getPrezzo()) %></p>
                    <form action="<%= request.getContextPath() %>/carrello" method="GET">
                        <input type="hidden" name="azione" value="aggiungi">
                        <input type="hidden" name="idProdotto" value="<%= p.getIdProdotto() %>">
                        <input type="hidden" name="quantita" value="1">
                        <button type="submit" class="btn-primary btn-full">Aggiungi al carrello</button>
                    </form>
                </div>
        <%
                    count++;
                }
            } else {
        %>
            <p class="nessun-prodotto">Nessun prodotto disponibile al momento.</p>
        <%
            }
        %>
    </div>
</section>

    <%-- FOOTER --%>
    <footer class="footer">
        <div class="footer-inner">
            <div class="footer-brand">
                <h4>UProtein</h4>
                <p>&#169; 2026 UProtein. All rights reserved.</p>
            </div>
          
        </div>
    </footer>

</body>
</html>
