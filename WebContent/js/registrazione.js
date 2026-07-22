document.addEventListener('DOMContentLoaded', function() {
    const registrazioneForm = document.getElementById('registrazioneForm');

    if (registrazioneForm) {
        registrazioneForm.addEventListener('submit', function(event) {
            const nome = document.getElementById('nome').value.trim();
            const cognome = document.getElementById('cognome').value.trim();
            const email = document.getElementById('email').value.trim();
            const password = document.getElementById('password').value.trim();
            const telefono = document.getElementById('telefono').value.trim();
            const indirizzo = document.getElementById('indirizzo').value.trim();
            
            const jsErrore = document.getElementById('js-errore-msg');

            jsErrore.textContent = '';

            if (nome === '' || cognome === '' || email === '' || password === '' || telefono === '' || indirizzo === '') {
                event.preventDefault();
                jsErrore.textContent = 'Tutti i campi sono obbligatori e non possono contenere solo spazi vuoti.';
            }
        });
    }
});