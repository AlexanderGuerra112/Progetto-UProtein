<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ page import="it.uprotein.model.Carrello" %>
<%@ page import="it.uprotein.model.ElementoCarrello" %>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <title>UProtein - Il tuo Carrello</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css?v=1">
</head>
<body>

    <div class="navbar">
        <a href="${pageContext.request.contextPath}/" class="logo">UProtein</a>
        <div class="nav-links">
            <a href="${pageContext.request.contextPath}/">Home</a>
            <a href="${pageContext.request.contextPath}/carrello?azione=mostra">Carrello</a>
        </div>
    </div>

    <div class="carrello-container">
        <h2 class="section-title" style="text-align: left; margin-bottom: 30px;">Il tuo Carrello della Spesa</h2>

        <%
            // Recuperiamo il carrello dalla sessione
            Carrello carrello = (Carrello) session.getAttribute("carrello");
            if (carrello == null || carrello.getElementi().isEmpty()) {
        %>
            <div class="carrello-vuoto">
                <p>Il tuo carrello è attualmente vuoto.</p>
                <a href="${pageContext.request.contextPath}/" class="btn-primary">Torna allo Shopping</a>
            </div>
        <%
            } else {
        %>
            <div class="carrello-inner">
                
                <div class="carrello-lista">
                    <%
                        for (ElementoCarrello elemento : carrello.getElementi()) {
                    %>
                        <div class="carrello-item">
                            <% if(elemento.getProdotto().getImmagineUrl() != null && !elemento.getProdotto().getImmagineUrl().isEmpty()) { %>
                                <img src="${pageContext.request.contextPath}/<%= elemento.getProdotto().getImmagineUrl() %>" alt="<%= elemento.getProdotto().getNome() %>" class="carrello-item-img" style="object-fit: cover;">
                            <% } else { %>
                                <div class="carrello-item-img"></div>
                            <% } %>
                            
                            <div class="carrello-item-info">
                                <div class="carrello-item-nome"><%= elemento.getProdotto().getNome() %></div>
                                <div class="carrello-item-categoria"><%= elemento.getProdotto().getCategoria() %></div>
                            </div>
                            
                            <div class="carrello-item-azioni">
                                <span class="carrello-quantita">Quantità: <%= elemento.getQuantita() %></span>
                                <div class="carrello-subtotale"><%= String.format("%.2f", elemento.getSubtotale()) %> &euro;</div>
                                <a href="${pageContext.request.contextPath}/carrello?azione=rimuovi&idProdotto=<%= elemento.getProdotto().getIdProdotto() %>" class="btn-rimuovi">
                                    Rimuovi
                                </a>
                            </div>
                        </div>
                    <%
                        }
                    %>
                </div>

                <div class="carrello-riepilogo">
                    <h3>Riepilogo Ordine</h3>
                    
                    <div class="riepilogo-row">
                        <span>Prodotti:</span>
                        <span><%= carrello.getElementi().size() %></span>
                    </div>
                    
                    <div class="riepilogo-row">
                        <span>Spedizione:</span>
                        <span>Gratis</span>
                    </div>
                    
                    <div class="riepilogo-totale">
                        <span>Totale:</span>
                        <span><%= String.format("%.2f", carrello.getTotale()) %> &euro;</span>
                    </div>
                    
                    <a href="${pageContext.request.contextPath}/checkout" class="btn-primary btn-full" style="text-align: center; text-decoration: none; display: block; box-sizing: border-box;">
                        Procedi al Pagamento
                    </a>
                    
                    <a href="${pageContext.request.contextPath}/" style="display: block; text-align: center; margin-top: 15px; color: var(--quasi-nero); text-decoration: none; font-size: 13px;">
                        Continua lo Shopping
                    </a>
                </div>

            </div>
        <%
            }
        %>
    </div>

</body>
</html>