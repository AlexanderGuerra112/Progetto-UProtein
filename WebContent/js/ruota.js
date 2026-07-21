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

disegnaRuota();

if (giaGiratoOggi) {
    attivaContoAllaRovescia();
}

function avviaGiocata() {
    bottone.disabled = true;

    fetch(`${contextPath}/common/ruota`, { method: "POST" })
    .then(response => {
        if (!response.ok) { throw new Error(response.statusText); }
        return response.json(); 
    })
    .then(data => {
        if (data.errore) {
            alert(data.errore);
            window.location.reload();
            return;
        }

        let nomePremioVinto = data.nome;
        
        let indiceVincente = premiTesto.indexOf(nomePremioVinto);
        
        if (indiceVincente === -1) {
            indiceVincente = 0;
        }

        let gradiPerSpicchio = 360 / numSpicchi;
        let gradiFinali = 1800 + (360 - (indiceVincente * gradiPerSpicchio) - (gradiPerSpicchio / 2)); 
        
        canvas.style.transform = "rotate(" + gradiFinali + "deg)";
        
        setTimeout(() => {
            if (nomePremioVinto === "Riprova Domani") {
                alert("Peccato! Non hai vinto nessun omaggio. Riprova domani!");
            } else {
               
                alert("🎉 Complimenti! Hai vinto l'articolo: " + nomePremioVinto + "!\nLo trovi a 0€ all'interno del tuo carrello!");
            }
            attivaContoAllaRovescia();
        }, 4000);
    })
    .catch(err => {
        alert("Errore di connessione durante la giocata.");
        console.error(err);
        bottone.disabled = false; 
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