<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page isELIgnored="false" %>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <title>UProtein - Registrazione</title>
  <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

    <div class="login-container">
        <h2>Crea il tuo Account UProtein</h2>

        <%-- Messaggio di errore inviato dalla Servlet --%>
        <%
            String errore = (String) request.getAttribute("errore");
            if (errore != null) {
        %>
            <p class="errore-msg"><%= errore %></p>
        <%
            }
        %>

        <form action="${pageContext.request.contextPath}/registrazione" method="POST">
            <input type="hidden" name="azione" value="salva">

            <div class="form-group">
                <label for="nome">Nome:</label>
                <input type="text" id="nome" name="nome" required placeholder="Inserisci il tuo nome">
            </div>

            <div class="form-group">
                <label for="cognome">Cognome:</label>
                <input type="text" id="cognome" name="cognome" required placeholder="Inserisci il tuo cognome">
            </div>

            <div class="form-group">
                <label for="email">Email:</label>
                <input type="email" id="email" name="email" required placeholder="Inserisci la tua email">
            </div>

            <div class="form-group">
                <label for="password">Password:</label>
                <input type="password" id="password" name="password" required placeholder="Crea una password">
            </div>

            <div class="form-group">
                <label for="telefono">Telefono:</label>
                <input type="text" id="telefono" name="telefono" required placeholder="Inserisci il tuo telefono">
            </div>

            <div class="form-group">
                <label for="indirizzo">Indirizzo di Spedizione:</label>
                <input type="text" id="indirizzo" name="indirizzo" required placeholder="Via, Numero, Città, CAP">
            </div>

            <button type="submit" class="btn-login">Registrati</button>
        </form>

        <p>Hai già un account? <a href="${pageContext.request.contextPath}/login?azione=mostra">Accedi qui</a></p>
    </div>

</body>
</html>
