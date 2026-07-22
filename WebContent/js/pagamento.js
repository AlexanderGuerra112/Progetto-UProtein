document.addEventListener("DOMContentLoaded", function() {
    
    var cardInput = document.getElementById("numero_carta");
    var expiryInput = document.getElementById("scadenza");
    var cvvInput = document.getElementById("cvv");

    // Verifica che gli elementi esistano nella pagina
    if (cardInput && expiryInput && cvvInput) {
        
        // Formattazione Numero Carta (Spazio ogni 4 cifre, max 16 cifre)
        cardInput.addEventListener("input", function(e) {
            var value = e.target.value.replace(/\D/g, ""); // Solo numeri
            var matches = value.match(/.{1,4}/g);
            var formatted = matches ? matches.join(" ") : ""; // Sostituisce il ?. con un controllo classico
            e.target.value = formatted.substring(0, 19); // 16 cifre + 3 spazi
        });

        //  Formattazione Data di Scadenza (MM/AA con slash automatico)
        expiryInput.addEventListener("input", function(e) {
            var value = e.target.value.replace(/\D/g, ""); // Solo numeri
            
            if (value.length >= 2) {
                var month = parseInt(value.substring(0, 2), 10);
                if (month < 1) month = 1;
                if (month > 12) month = 12;
                
                // Formatta il mese con lo zero davanti senza usare padStart 
                var monthStr = (month < 10 ? "0" : "") + month;
                e.target.value = monthStr + "/" + value.substring(2, 4);
            } else {
                e.target.value = value;
            }
        });

        // Gestione del tasto "BackSpace" sulla scadenza per non rimanere bloccati sullo slash
        expiryInput.addEventListener("keydown", function(e) {
            if (e.key === "Backspace" && expiryInput.value.length === 3) {
                expiryInput.value = expiryInput.value.substring(0, 2);
            }
        });

        // 3. Controllo CVV (Solo numeri, max 3 cifre)
        cvvInput.addEventListener("input", function(e) {
            var value = e.target.value.replace(/\D/g, ""); // Solo numeri
            e.target.value = value.substring(0, 3);
        });
    }
});