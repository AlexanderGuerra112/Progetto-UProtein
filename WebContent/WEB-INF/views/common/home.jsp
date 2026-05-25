<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page isELIgnored="false" %>
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
    <a href="${pageContext.request.contextPath}/login?azione=logout" style="margin-left: 15px; color: #ff4d4d; font-size: 0.9em;">(Esci)</a>
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

            <div class="product-card">
                <div class="product-img-placeholder"></div>
                <h3>Whey Protein 1kg</h3>
                <p class="price-old">€39,90</p>
                <p class="price">€29,90</p>
                <button class="btn-primary btn-full">Aggiungi al carrello</button>
            </div>

            <div class="product-card">
                <div class="product-img-placeholder"></div>
                <h3>Creatina 300g</h3>
                <p class="price-old">€24,90</p>
                <p class="price">€19,90</p>
                <button class="btn-primary btn-full">Aggiungi al carrello</button>
            </div>

            <div class="product-card">
                <div class="product-img-placeholder"></div>
                <h3>Vitamina D3</h3>
                <p class="price">€12,90</p>
                <button class="btn-primary btn-full">Aggiungi al carrello</button>
            </div>

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
