<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>UProtein - Login</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/style.css">
</head>
<body>

    <div class="login-container">
        <h2>Accedi a UProtein</h2>
        
        <% 
            // Se la Servlet ci rimanda qui con un errore, lo stampiamo a schermo
            String errore = (String) request.getAttribute("errore");
            if (errore != null) {
        %>
            <div style="color: red; margin-bottom: 15px;">
                <%= errore %>
            </div>
        <% 
            } 
        %>

        <form action="${pageContext.request.contextPath}/login?azione=controlla" method="POST">
            
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
    </div>

</body>
</html>