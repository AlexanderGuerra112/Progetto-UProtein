<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page isELIgnored="false" %>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <title>UProtein - Pagamento Sicuro</title>
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

    
    <div class="carrello-container" style="max-width: 600px; margin-top: 40px; margin-bottom: 40px;">
        <h2 class="section-title" style="text-align: center;">Pagamento Sicuro</h2>
        <p style="text-align: center; margin-bottom: 30px; color: #555;">Inserisci i dati della tua Carta di Credito per completare l'ordine.</p>

        <div class="carrello-inner" style="padding: 30px;">
            <form action="<%= request.getContextPath() %>/checkout" method="post">
                
                <input type="hidden" name="metodoPagamento" value="Carta di Credito">

                <div style="margin-bottom: 20px;">
                    <label for="titolare" style="display: block; margin-bottom: 8px; font-weight: bold;">Titolare della Carta</label>
                    <input type="text" id="titolare" name="titolare" required placeholder="Mario Rossi" style="width: 100%; padding: 12px; border: 1px solid #ddd; border-radius: 4px; font-family: inherit;">
                </div>

                <div style="margin-bottom: 20px;">
                    <label for="numero_carta" style="display: block; margin-bottom: 8px; font-weight: bold;">Numero della Carta</label>
                    <input type="text" id="numero_carta" name="numero_carta" required pattern="[0-9]{16}" title="Inserisci esattamente le 16 cifre della carta, senza spazi" placeholder="1234 5678 1234 5678" style="width: 100%; padding: 12px; border: 1px solid #ddd; border-radius: 4px; font-family: inherit;">
                </div>

                <div style="display: flex; gap: 20px; margin-bottom: 30px;">
                    <div style="flex: 1;">
                        <label for="scadenza" style="display: block; margin-bottom: 8px; font-weight: bold;">Scadenza (MM/AA)</label>
                        <input type="text" id="scadenza" name="scadenza" required pattern="(0[1-9]|1[0-2])\/[0-9]{2}" title="Formato richiesto: MM/AA (es. 12/26)" placeholder="12/26" style="width: 100%; padding: 12px; border: 1px solid #ddd; border-radius: 4px; font-family: inherit;">
                    </div>
                    <div style="flex: 1;">
                        <label for="cvv" style="display: block; margin-bottom: 8px; font-weight: bold;">CVV</label>
                        <input type="text" id="cvv" name="cvv" required pattern="[0-9]{3}" title="Inserisci il codice di sicurezza a 3 cifre posto sul retro" placeholder="123" style="width: 100%; padding: 12px; border: 1px solid #ddd; border-radius: 4px; font-family: inherit;">
                    </div>
                </div>

                <button type="submit" class="btn-checkout" style="width: 100%; border: none; cursor: pointer; padding: 15px; font-size: 16px; font-weight: bold; text-align: center;">
                    Paga e Conferma Ordine
                </button>
                
                <div style="text-align: center; margin-top: 20px;">
                    <a href="<%= request.getContextPath() %>/carrello?azione=mostra" style="color: #666; text-decoration: underline;">&larr; Annulla e torna al carrello</a>
                </div>
            </form>
        </div>
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