document.addEventListener("DOMContentLoaded", function() {
    // Cerchiamo il pop-up nella pagina tramite il suo ID
    var toast = document.getElementById("toast-notifica-carrello");
    
    // Se il pop-up esiste (cioè se la Servlet ha attivato la sessione)
    if (toast) {
        // Aspetta 3000 millisecondi (3 secondi) e poi aggiunge la classe per nasconderlo
        setTimeout(function() {
            toast.classList.add("toast-uprotein-invisibile");
        }, 3000);
    }
});