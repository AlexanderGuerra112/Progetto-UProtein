<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page isELIgnored="false" %>
<%@ page import="java.util.List, it.uprotein.model.Ordine" %>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <title>UProtein - I Miei Ordini</title>
    <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/css/style.css?v=1">
</head>
<body>

    <%-- NAVBAR INTEGRATA --%>
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
                
                <%-- CONTROLLO RUOLO --%>
                <% if (utenteLoggato.getRuolo() != null && utenteLoggato.getRuolo().equalsIgnoreCase("admin")) { %>
                    <a href="<%= request.getContextPath() %>/adminProdotto?azione=mostra" class="nav-btn nav-admin">[Area Admin]</a>
                <% } else { %>
                    <a href="<%= request.getContextPath() %>/storico-ordini" class="nav-btn nav-orders">📦 I Miei Ordini</a>
                <% } %>
                
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

    <%-- INTESTAZIONE PAGINA (Stile coerente con pagina prodotti) --%>
    <div class="prodotti-header">
        <h2 class="section-title">📦 Il tuo storico ordini</h2>
    </div>

    <%-- CONTENUTO STORICO ORDINI --%>
    <main class="container-storico">
        <p class="sottotitolo-pagina">Qui puoi visualizzare lo stato e i dettagli di tutti i tuoi acquisti.</p>

        <%
            List<Ordine> listaOrdini = (List<Ordine>) request.getAttribute("listaOrdini");
            String errore = (String) request.getAttribute("erroreStorico");

            if (errore != null) {
        %>
            <div class="messaggio-errore">
                <%= errore %>
            </div>
        <%
            } else if (listaOrdini == null || listaOrdini.isEmpty()) {
        %>
            <div class="messaggio-vuoto">
                <p>Non hai ancora effettuato nessun ordine su UProtein. Cosa aspetti? 💪</p>
                <a href="<%= request.getContextPath() %>/home" class="btn-shop">Inizia lo shopping</a>
            </div>
        <%
            } else {
        %>
            <div class="tabella-responsiva">
                <table class="tabella-ordini">
                    <thead>
                        <tr>
                            <th>ID Ordine</th>
                            <th>Data</th>
                            <th>Totale</th>
                            <th>Stato</th>
                            <th>Metodo Pagamento</th>
                            <th>Azioni</th>
                        </tr>
                    </thead>
                    <tbody>
                        <%
                            for (Ordine ord : listaOrdini) {
                        %>
                            <tr>
                                <td><strong>#<%= ord.getIdOrdine() %></strong></td>
                                <td><%= ord.getDataOrdine() %></td>
                                <td class="testo-evidenziato">€ <%= String.format("%.2f", ord.getTotale()) %></td>
                                <td>
                                    <span class="badge-stato stato-confermato">
                                        <%= ord.getStatoOrdine() %>
                                    </span>
                                </td>
                                <td><%= ord.getMetodoPagamento() %></td>
                                <td>
                                    <a href="<%= request.getContextPath() %>/dettaglio-ordine?id=<%= ord.getIdOrdine() %>" class="btn-dettaglio">
                                        🔍 Vedi Dettaglio
                                    </a>
                                </td>
                            </tr>
                        <%
                            }
                        %>
                    </tbody>
                </table>
            </div>
        <%
            }
        %>
    </main>

    <%-- FOOTER (Identico alla pagina prodotti) --%>
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