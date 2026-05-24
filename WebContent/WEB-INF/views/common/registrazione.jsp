<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>UProtein - Registrazione</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

    <div class="login-container" style="max-width: 400px; margin: 50px auto; font-family: sans-serif;">
        <h2>Crea il tuo Account UProtein</h2>
        
        <form action="${pageContext.request.contextPath}/registrazione?azione=salva" method="POST">
            <div style="margin-bottom: 15px;">
                <label>Nome:</label><br>
                <input type="text" name="nome" required style="width: 100%; padding: 8px;">
            </div>
            <div style="margin-bottom: 15px;">
                <label>Cognome:</label><br>
                <input type="text" name="cognome" required style="width: 100%; padding: 8px;">
            </div>
            <div style="margin-bottom: 15px;">
                <label>Email:</label><br>
                <input type="email" name="email" required style="width: 100%; padding: 8px;">
            </div>
            <div style="margin-bottom: 15px;">
                <label>Password:</label><br>
                <input type="password" name="password" required style="width: 100%; padding: 8px;">
            </div>
            
            <button type="submit" class="btn-primary" style="width: 100%; padding: 10px; border: none; font-weight: bold; cursor: pointer;">REGISTRATI</button>
        </form>
        
        <p style="margin-top: 15px;">Hai già un account? <a href="${pageContext.request.contextPath}/login?azione=mostra">Accedi qui</a></p>
    </div>

</body>
</html>