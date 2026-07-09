<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="it.uprotein.model.*" %>
<%@ page import="java.util.List" %>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <title>UProtein - Fattura Ordine</title>
   
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

    <div class="carrello-container fattura-box">
        
        <div class="fattura-header">
            <h1 class="logo">UPROTEIN</h1>
            <p class="subtitle"> Level UP Your Style & Integration</p>
        </div>

        <div class="carrello-item-info">
            <h3 class="section-title-small">Dettagli Cliente</h3>
            <% 
                Utente utente = (Utente) session.getAttribute("utente");
                if (utente != null) {
            %>
                <p class="info-text"><strong>Nominativo:</strong> <%= utente.getNome() %> <%= utente.getCognome() %></p>
                <p class="info-text"><strong>Indirizzo:</strong> <%= utente.getIndirizzoSpedizione() %></p>
                <p class="info-text"><strong>Telefono:</strong> <%= utente.getTelefono() %></p>
            <% } %>
        </div>

        <table class="fattura-table">
            <thead>
                <tr>
                    <th>Prodotto</th>
                    <th>Quantità</th>
                    <th class="text-right">Prezzo Unitario</th>
                    <th class="text-right">Subtotale</th>
                </tr>
            </thead>
            <tbody>
                <% 
                    // Recuperiamo la copia dei prodotti salvata nella request prima dello svuotamento
                    List<ElementoCarrello> prodottiFattura = (List<ElementoCarrello>) request.getAttribute("prodottiFattura");
                    if (prodottiFattura != null && !prodottiFattura.isEmpty()) {
                        for (ElementoCarrello elemento : prodottiFattura) {
                %>
                <tr>
                    <td><%= elemento.getProdotto().getNome() %></td>
                    <td class="text-center"><%= elemento.getQuantita() %></td>
                    <td class="text-right">€<%= String.format("%.2f", elemento.getProdotto().getPrezzo()) %></td>
                    <td class="text-right font-bold">€<%= String.format("%.2f", elemento.getSubtotale()) %></td>
                </tr>
                <% 
                        }
                    } else {
                %>
                <tr>
                    <td colspan="4" class="text-center">Nessun prodotto trovato in questa fattura.</td>
                </tr>
                <% 
                    } 
                %>
            </tbody>
        </table>

        <div class="riepilogo-totale fattura-totale">
            <span>TOTALE PAGATO:</span>
            <%
                // Recuperiamo il totale salvato nella request
                Double totaleFattura = (Double) request.getAttribute("totaleFattura");
            %>
            <span class="price">€<%= (totaleFattura != null) ? String.format("%.2f", totaleFattura) : "0.00" %></span>
        </div>

        <div class="no-print actions-center">
            <button onclick="window.print()" class="btn-primary">
                Stampa Ricevuta PDF
            </button>
            <p>
                <a href="${pageContext.request.contextPath}/home" class="link-home">
                    &larr; Torna allo shopping
                </a>
            </p>
        </div>
    </div>

</body>
</html>