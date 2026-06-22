// Recuperiamo le variabili passate dalla JSP attraverso l'oggetto globale configRuota
let giaGiratoOggi = configRuota.giaGiratoOggi;
const premiTesto = configRuota.premiTesto;
const contextPath = configRuota.contextPath;

const bottone = document.getElementById("bottone-gira");
const timerDiv = document.getElementById("timer");
const countdownSpan = document.getElementById("countdown");
const canvas = document.getElementById("canvas-ruota");
const ctx = canvas.getContext("2d");

const colori = ["#34495e", "#2ecc71", "#3498db", "#9b59b6", "#f1c40f", "#e67e22", "#1abc9c", "#e74c3c"];
const numSpicchi = 8;
const angoloSpicchio = (2 * Math.PI) / numSpicchi;

function disegnaRuota() {
    for (let i = 0; i < numSpicchi; i++) {
        ctx.beginPath();
        ctx.fillStyle = colori[i];
        ctx.moveTo(200, 200);
        ctx.arc(200, 200, 200, i * angoloSpicchio, (i + 1) * angoloSpicchio);
        ctx.fill();
        
        ctx.save();
        ctx.translate(200, 200);
        ctx.rotate(i * angoloSpicchio + angoloSpicchio / 2);
        ctx.fillStyle = "white";
        ctx.font = "bold 11px Arial";
        ctx.textAlign = "right";
        
        let testoSpicchio = premiTesto[i] || "Riprova Domani";
        if(testoSpicchio.length > 18) testoSpicchio = testoSpicchio.substring(0,16) + "..";
        
        ctx.fillText(testoSpicchio, 185, 5);
        ctx.restore();
    }
}

// Inizializzazione della ruota al caricamento della pagina
disegnaRuota();

if (giaGiratoOggi) {
    attivaContoAllaRovescia();
}

function avviaGiocata() {
    bottone.disabled = true;

    // Chiamata FETCH alla servlet
    fetch(`${contextPath}/ruota`, { method: "POST" })
    .then(response => {
        if (!response.ok) { throw new Error(response.statusText); }
        // La servlet ora risponde in JSON, quindi leggiamo come .json() e non più come .text()
        return response.json(); 
    })
    .then(data => {
        // Gestione di eventuali errori mandati dal server sotto forma di JSON
        if (data.errore) {
            alert(data.errore);
            window.location.reload();
            return;
        }

        // Il server ci passa il nome del prodotto vinto (es. data.nome)
        let nomePremioVinto = data.nome;
        
        // Cerchiamo l'indice dello spicchio corrispondente nel nostro array dei premi della pagina
        let indiceVincente = premiTesto.indexOf(nomePremioVinto);
        
        // Se per qualche motivo il nome non combacia, usiamo un indice di riserva (0)
        if (indiceVincente === -1) {
            indiceVincente = 0;
        }

        // --- ANIMAZIONE DELLA RUOTA ---
        let gradiPerSpicchio = 360 / numSpicchi;
        // Calcolo per far fare 5 giri (1800 gradi) + posizionarsi sullo spicchio corretto
        let gradiFinali = 1800 + (360 - (indiceVincente * gradiPerSpicchio) - (gradiPerSpicchio / 2)); 
        
        // Attiviamo l'animazione CSS sul canvas
        canvas.style.transform = "rotate(" + gradiFinali + "deg)";
        
        // Attendiamo 4 secondi (la durata della transizione CSS) prima di mostrare il pop-up
        setTimeout(() => {
            if (nomePremioVinto === "Riprova Domani") {
                alert("Peccato! Non hai vinto nessun omaggio. Riprova domani!");
            } else {
                // Usiamo la variabile con il nome reale passata dal server!
                alert("🎉 Complimenti! Hai vinto l'articolo: " + nomePremioVinto + "!\nLo trovi a 0€ all'interno del tuo carrello!");
            }
            attivaContoAllaRovescia();
        }, 4000);
    })
    .catch(err => {
        alert("Errore di connessione durante la giocata.");
        console.error(err);
        bottone.disabled = false; // Riabilitiamo il bottone in caso di errore di rete
    });
}

function attivaContoAllaRovescia() {
    bottone.disabled = true;
    bottone.innerText = "Giro già effettuato";
    timerDiv.style.display = "inline-block";

    function aggiorna() {
        let adesso = new Date();
        let mezzanotte = new Date();
        mezzanotte.setHours(24, 0, 0, 0); 

        let diff = mezzanotte - adesso;

        if (diff <= 0) {
            clearInterval(intervallo);
            window.location.reload();
        }

        let ore = Math.floor(diff / (1000 * 60 * 60));
        let min = Math.floor((diff % (1000 * 60 * 60)) / (1000 * 60));
        let sec = Math.floor((diff % (1000 * 60)) / 1000);

        countdownSpan.innerText = 
            (ore < 10 ? "0" : "") + ore + ":" + 
            (min < 10 ? "0" : "") + min + ":" + 
            (sec < 10 ? "0" : "") + sec;
    }

    aggiorna();
    let intervallo = setInterval(aggiorna, 1000);
}