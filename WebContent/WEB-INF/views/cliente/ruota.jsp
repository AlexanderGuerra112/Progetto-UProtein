<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="it.uprotein.model.Prodotto" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Ruota della Fortuna - UProtein</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/ruota.css?v=1.1">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/responsive.css">
</head>
<body>

<main class="container-ruota">
    <h1>🎡 Ruota della Fortuna</h1>
    <p>I premi cambiano in base alla disponibilità del nostro magazzino! Tenta la fortuna.</p>
    
    <div class="ruota-wrapper">
        <div class="freccia"></div>
        <canvas id="canvas-ruota" width="400" height="400"></canvas>
    </div>
    
    <br>
    <button id="bottone-gira" class="btn-gira" onclick="avviaGiocata()">Gira la Ruota</button>
    
    <div id="timer">Prossimo giro disponibile tra: <span id="countdown">00:00:00</span></div>

    <div class="navigation-container">
        <a href="${pageContext.request.contextPath}/prodotti?azione=mostra" class="btn-back-home">
            ← Torna alla Home
        </a>
    </div>
</main>

<script>
    const configRuota = {
        contextPath: "${pageContext.request.contextPath}",
        giaGiratoOggi: ${giaGirato},
        premiTesto: [
            <% 
                List<Prodotto> listaProdotti = (List<Prodotto>) request.getAttribute("prodottiRuota");
                if(listaProdotti != null) {
                    for(int i=0; i<listaProdotti.size(); i++) {
                        out.print("\"" + listaProdotti.get(i).getNome() + "\"");
                        if(i < listaProdotti.size() - 1) out.print(", ");
                    }
                }
            %>
        ]
    };
</script>

<script src="${pageContext.request.contextPath}/js/ruota.js?v=1.1"></script>

</body>
</html>