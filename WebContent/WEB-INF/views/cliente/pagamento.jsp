<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page isELIgnored="false" %>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <title>UProtein - Pagamento Sicuro</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/css/style.css">
    <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/css/responsive.css">
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

    <main class="pagamento-container">
        <h2 class="pagamento-title">Pagamento Sicuro</h2>
        <p class="pagamento-subtitle">Inserisci i dati della tua carta per completare l'acquisto.</p>

        <div class="pagamento-card">
            
            <%-- Blocco errori dal server --%>
            <% if (request.getAttribute("errorePagamento") != null) { %>
                <div class="pagamento-error">
                    <%= request.getAttribute("errorePagamento") %>
                </div>
            <% } %>

            <form action="<%= request.getContextPath() %>/common/checkout" method="post" class="pagamento-form">
                
                <input type="hidden" name="metodoPagamento" value="Carta di Credito">

                <div class="form-group">
                    <label for="titolare">Titolare della Carta</label>
                    <input type="text" id="titolare" name="titolare" required 
                           placeholder="Mario Rossi" class="input-field" autocomplete="cc-name">
                </div>

                <div class="form-group">
                    <label for="numero_carta">Numero della Carta</label>
                    <input type="text" id="numero_carta" name="numero_carta" required 
                           placeholder="1234 5678 1234 5678" class="input-field" autocomplete="cc-number">
                </div>

                <div class="form-row-split">
                    <div class="form-group">
                        <label for="scadenza">Scadenza (MM/AA)</label>
                        <input type="text" id="scadenza" name="scadenza" required 
                               placeholder="12/26" class="input-field" autocomplete="cc-exp">
                    </div>
                    <div class="form-group">
                        <label for="cvv">CVV</label>
                        <input type="password" id="cvv" name="cvv" required 
                               placeholder="123" class="input-field" autocomplete="cc-csc">
                    </div>
                </div>

                <button type="submit" class="btn-checkout">
                    Paga e Conferma Ordine
                </button>
                
                <div class="pagamento-actions">
                    <a href="<%= request.getContextPath() %>/carrello?azione=mostra" class="link-back">&larr; Annulla e torna al carrello</a>
                </div>
            </form>
        </div>
    </main>

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

    <script src="<%= request.getContextPath() %>/js/pagamento.js"></script>

</body>
</html>