<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page isELIgnored="false" %>
<%@ page import="java.util.List, it.uprotein.model.Prodotto" %>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <title>UProtein - Level UP Your Gym Game</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css?v=3">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/toast.css">
</head>
<body>
<%
    Boolean mostraToast = (Boolean) session.getAttribute("mostraToastCarrello");
    if (mostraToast != null && mostraToast) {
        
        session.removeAttribute("mostraToastCarrello");
%>
    <div id="toast-notifica-carrello" class="toast-uprotein">
        🛒 Prodotto aggiunto al carrello!
    </div>
<%
    }
%>





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
                
                <%-- CONTROLLO RUOLO --%>
                <% if (utenteLoggato.getRuolo() != null && utenteLoggato.getRuolo().equalsIgnoreCase("admin")) { %>
                    <a href="${pageContext.request.contextPath}/adminProdotto?azione=mostra" class="nav-btn nav-admin">[Area Admin]</a>
                <% } else { %>
                    <a href="${pageContext.request.contextPath}/storico-ordini" class="nav-btn nav-orders">📦 I Miei Ordini</a>
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
        <h1>LEVEL UP YOUR GYM GAME</h1>
        <p>Proteine, creatina e barrette della massima qualità per i tuoi workout.</p>
        <button class="btn-primary" onclick="location.href='${pageContext.request.contextPath}/prodotti?azione=mostra'">
            COMPRA ORA
        </button>
    </header>
 
    <%-- SEZIONE RUOTA DELLA FORTUNA --%>
    <section class="ruota-banner-section">
        <div class="ruota-banner-content">
            <h3>🎡 Tenta la fortuna ogni giorno!</h3>
            <p>Gira la nostra ruota e vinci proteine, barrette o integratori in omaggio in base alla disponibilità.</p>
            <% if (utenteLoggato != null) { %>
                <a href="${pageContext.request.contextPath}/ruota" class="btn-ruota">Gira la Ruota</a>
            <% } else { %>
                <a href="${pageContext.request.contextPath}/login?azione=mostra" class="btn-ruota">Accedi per Giocare</a>
            <% } %>
        </div>
    </section>

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
                        <img src="<%= request.getContextPath() %>/images/<%= p.getImmagineUrl() %>" alt="<%= p.getNome() %>" class="product-img">
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
<script src="<%= request.getContextPath() %>/js/pop-up.js"></script>
</body>
</html>