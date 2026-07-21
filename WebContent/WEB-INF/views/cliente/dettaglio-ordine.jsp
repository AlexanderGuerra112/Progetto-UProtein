<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page isELIgnored="false" %>
<%@ page import="java.util.List, it.uprotein.model.Ordine, it.uprotein.model.Prodotto" %>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <title>UProtein - Dettaglio Ordine</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/css/style.css?v=1">
    <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/css/responsive.css">
</head>
<body>

    <%-- NAVBAR UPROTEIN --%>
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
                
                <% if (utenteLoggato.getRuolo() != null && utenteLoggato.getRuolo().equalsIgnoreCase("admin")) { %>
                    <a href="<%= request.getContextPath() %>/admin/adminProdotto?azione=mostra" class="nav-btn nav-admin">[Area Admin]</a>
                <% } else { %>
                    <a href="<%= request.getContextPath() %>/common/storico-ordini" class="nav-btn nav-orders">📦 I Miei Ordini</a>
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

<main>
    <%-- INTESTAZIONE PAGINA --%>
    <div class="prodotti-header">
        <h2 class="section-title">🔍 Dettaglio Ordine</h2>
    </div>

    <%-- CONTENUTO PRINCIPALE --%>
    <div class="container-dettaglio">
        <%
            Ordine ordine = (Ordine) request.getAttribute("ordine");
            List<Prodotto> prodottiAcquistati = (List<Prodotto>) request.getAttribute("prodottiAcquistati");
            String errore = (String) request.getAttribute("erroreDettaglio");

            if (errore != null) {
        %>
            <div class="messaggio-errore">
                <%= errore %>
            </div>
        <%
            } else if (ordine != null) {
        %>
            <%-- INFO DATA ACQUISTO --%>
            <div class="info-ordine-card">
                <h3 class="info-ordine-titolo">Riepilogo Ordine #<%= ordine.getIdOrdine() %></h3>
                <p class="info-ordine-riga">📅 <strong>Data dell'acquisto:</strong> <%= ordine.getDataOrdine() %></p>
                <p class="info-ordine-riga">💰 <strong>Totale speso:</strong> € <%= String.format("%.2f", ordine.getTotale()) %></p>
            </div>

            <%-- LISTA DEI PRODOTTI ACQUISTATI --%>
            <h3 class="titolo-lista-prodotti">🛒 Articoli Acquistati:</h3>
            <div class="tabella-responsiva">
                <table class="tabella-ordini">
                    <thead>
                        <tr>
                            <th>Prodotto</th>
                            <th>Prezzo Unitario al momento dell'acquisto</th>
                            <th>Quantità</th>
                        </tr>
                    </thead>
                    <tbody>
                        <%
                            if (prodottiAcquistati != null && !prodottiAcquistati.isEmpty()) {
                                for (Prodotto p : prodottiAcquistati) {
                        %>
                                    <tr>
                                        <td>
                                            <div class="dettaglio-prodotto-nome">
                                                <strong><%= p.getNome() %></strong>
                                                <span class="dettaglio-categoria"><%= p.getCategoria() %></span>
                                            </div>
                                        </td>
                                        <td class="testo-evidenziato">€ <%= String.format("%.2f", p.getPrezzo()) %></td>
                                        <td><span class="badge-quantita">x <%= p.getQuantita() %></span></td>
                                    </tr>
                        <%
                                }
                            } else {
                        %>
                            <tr>
                                <td colspan="3" class="nessun-prodotto">Nessun dettaglio prodotto trovato per questo ordine.</td>
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
        
        <%-- BOTTONE DI RITORNO --%>
        <div class="azione-ritorno">
            <a href="<%= request.getContextPath() %>/common/storico-ordini" class="btn-indietro">⬅️ Torna allo storico</a>
        </div>
        </div>
    </main>

    <%-- FOOTER UPROTEIN --%>
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