<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page isELIgnored="false" %>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <title>UProtein - Login</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/responsive.css">
</head>
<body>

    <main class="login-container">
        <h2>Accedi a UProtein</h2>

        <%-- Messaggio di errore inviato dalla Servlet --%>
        <%
            String errore = (String) request.getAttribute("errore");
            if (errore != null) {
        %>
            <p class="errore-msg"><%= errore %></p>
        <%
            }
        %>

        <%-- Messaggio di successo (es. dopo registrazione) --%>
        <%
            String successo = (String) request.getAttribute("successo");
            if (successo != null) {
        %>
            <p class="successo-msg"><%= successo %></p>
        <%
            }
        %>

        <form action="${pageContext.request.contextPath}/login" method="POST">
            <input type="hidden" name="azione" value="controlla">

            <div class="form-group">
                <label for="email">Email:</label>
                <input type="email" id="email" name="email" required placeholder="Inserisci la tua email">
            </div>

            <div class="form-group">
                <label for="password">Password:</label>
                <input type="password" id="password" name="password" required placeholder="Inserisci la password">
            </div>

            <button type="submit" class="btn-login">Entra</button>
        </form>

        <p>Non hai un account? <a href="${pageContext.request.contextPath}/registrazione?azione=mostra">Registrati qui</a></p>
    </main>

</body>
</html>
