<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page isELIgnored="false" %>
<%@ page import="java.util.List, it.uprotein.model.ElementoCarrello" %>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <title>UProtein - Carrello</title>
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

    <div class="carrello-container">
        <h2 class="section-title">Il tuo Carrello</h2>

        <%
           
            List<ElementoCarrello> elementi =
                (List<ElementoCarrello>) request.getAttribute("elementiCarrello");
            Double totale = (Double) request.getAttribute("totale");

            if (elementi == null || elementi.isEmpty()) {
        %>
            <div class="carrello-vuoto">
                <p>Il tuo carrello è vuoto.</p>
                <a href="<%= request.getContextPath() %>/prodotti?azione=mostra" class="btn-primary">
                    Continua lo shopping
                </a>
            </div>
        <%
            } else {
        %>
            <div class="carrello-inner">

                <div class="carrello-lista">
                    <% for (ElementoCarrello elemento : elementi) { %>
                    <div class="carrello-item">
                        <img src="<%= request.getContextPath() %>/images/<%= elemento.getProdotto().getImmagineUrl() %>" alt="<%= elemento.getProdotto().getNome() %>" class="carrello-item-img">
                        <div class="carrello-item-info">
                            <h3 class="carrello-item-nome"><%= elemento.getProdotto().getNome() %></h3>
                            <p class="carrello-item-categoria"><%= elemento.getProdotto().getCategoria() %></p>
                            <p class="price">€ <%= String.format("%.2f", elemento.getProdotto().getPrezzo()) %></p>
                        </div>
                        <div class="carrello-item-azioni">
                            <span class="carrello-quantita">Qtà: <%= elemento.getQuantita() %></span>
                            <p class="carrello-subtotale">
                                Subtotale: € <%= String.format("%.2f", elemento.getSubtotale()) %>
                            </p>
                            <a href="<%= request.getContextPath() %>/carrello?azione=rimuovi&idProdotto=<%= elemento.getProdotto().getIdProdotto() %>&prezzo=<%= elemento.getProdotto().getPrezzo() %>"
               class="btn-rimuovi">Rimuovi</a>
                        </div>
                    </div>
                    <% } %>
                </div>

                <div class="carrello-riepilogo">
                    <h3>Riepilogo ordine</h3>
                    <div class="riepilogo-row">
                        <span>Subtotale</span>
                        <span>€ <%= String.format("%.2f", totale) %></span>
                    </div>
                    <div class="riepilogo-row">
                        <span>Spedizione</span>
                        <span>Gratuita</span>
                    </div>
                    <div class="riepilogo-totale">
                        <span>Totale</span>
                        <span>€ <%= String.format("%.2f", totale) %></span>
                    </div>
                    
                    <% if (session.getAttribute("utente") != null) { %>
                        <a href="<%= request.getContextPath() %>/pagamento" class="btn-checkout" style="display: block; text-align: center;">
                             Procedi al Pagamento
                            </a>
                    <% } else { %>
                        <a href="<%= request.getContextPath() %>/login?azione=mostra" class="btn-checkout">
                            Accedi per completare l'ordine
                        </a>
                    <% } %>
                </div>

            </div>
        <% } %>
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