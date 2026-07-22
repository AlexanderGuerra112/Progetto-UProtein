document.addEventListener('DOMContentLoaded', function() {
    const loginForm = document.getElementById('loginForm');
    
    if (loginForm) {
        loginForm.addEventListener('submit', function(event) {
            const emailInput = document.getElementById('email');
            const passwordInput = document.getElementById('password');
            const jsErrore = document.getElementById('js-errore-msg');

            jsErrore.textContent = '';

            if (emailInput.value.trim() === '' || passwordInput.value.trim() === '') {
                event.preventDefault(); 
                jsErrore.textContent = 'Compilare tutti i campi senza inserire solo spazi vuoti.';
            }
        });
    }
});