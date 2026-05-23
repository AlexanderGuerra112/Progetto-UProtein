<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <title>UProtein - Level UP Your Gym Game</title>
    <!-- Collegamento al CSS nella stessa cartella -->
    <link rel="stylesheet" href="style.css">
</head>
<body>
<h1>Benvenuto su UProtein!</h1>
<p>Il tuo portale per gli integratori da palestra.</p>
    <!-- Navbar [4] -->
    <nav class="navbar">
        <a href="home.jsp" class="logo">UProtein</a>
        <div class="nav-links">
            <a href="prodotti.jsp?cat=Proteine">Proteine</a>
            <a href="prodotti.jsp?cat=Creatina">Creatina</a>
            <a href="prodotti.jsp?cat=Vitamine">Vitamine</a>
            <a href="prodotti.jsp?cat=Barrette">Barrette</a>
        </div>
        <div class="nav-icons">
            <a href="login.jsp" style="color:white; margin-right:20px; text-decoration:none;">👤 Profilo</a>
            <a href="carrello.jsp" style="color:white; text-decoration:none;">🛒 Carrello</a>
        </div>
    </nav>

    <!-- Hero Banner (Ispirato a Bulk [1, 7]) -->
    <header class="hero">
        <h1>FINO AL 75% DI SCONTO</h1>
        <p style="color: #6B6B6B; margin-bottom: 25px;">I migliori supplementi per il tuo level UP.</p>
        <button class="btn-primary" onclick="location.href='prodotti.jsp'">COMPRA ORA</button>
    </header>

    <!-- Sezione Bestseller [4] -->
    <section>
        <h2 style="text-align: center; margin-top: 40px; font-weight: bold;">I NOSTRI BESTSELLER</h2>
        <div class="bestseller-grid">
            
            <!-- Esempio Prodotto 1 -->
            <div class="product-card">
                <div style="background:#eee; height:180px; border-radius:5px; margin-bottom:15px;"></div>
                <h3>Whey Protein 1kg</h3>
                <p class="price-old">€39,90</p>
                <p class="price">€29,90</p>
                <button class="btn-primary" style="width:100%;">Aggiungi al carrello</button>
            </div>

            <!-- Esempio Prodotto 2 -->
            <div class="product-card">
                <div style="background:#eee; height:180px; border-radius:5px; margin-bottom:15px;"></div>
                <h3>Creatina 300g</h3>
                <p class="price-old">€24,90</p>
                <p class="price">€19,90</p>
                <button class="btn-primary" style="width:100%;">Aggiungi al carrello</button>
            </div>

            <!-- Esempio Prodotto 3 -->
            <div class="product-card">
                <div style="background:#eee; height:180px; border-radius:5px; margin-bottom:15px;"></div>
                <h3>Vitamina D3</h3>
                <p class="price">€12,90</p>
                <button class="btn-primary" style="width:100%;">Aggiungi al carrello</button>
            </div>

        </div>
    </section>

    <!-- Footer informativo [4] -->
    <footer style="background: #1A1A1A; color: white; padding: 40px 5%; margin-top: 50px;">
        <div style="display: flex; justify-content: space-between;">
            <div>
                <h4>UProtein</h4>
                <p style="font-size: 12px; color: #6B6B6B;">© 2025 UProtein. All rights reserved.</p>
            </div>
            <div style="font-size: 14px;">
                <a href="#" style="color:white; display:block; margin-bottom:10px;">Chi siamo</a>
                <a href="#" style="color:white; display:block;">Spedizioni e Resi</a>
            </div>
        </div>
    </footer>

</body>
</html>