package it.uprotein.control;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.sql.DataSource;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import it.uprotein.model.Carrello;
import it.uprotein.model.Prodotto;
import it.uprotein.model.Utente;
import it.uprotein.storage.ProdottoDAOImpl;

@WebServlet("/ruota")
public class RuotaServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // GET: Gestisce il caricamento iniziale e mostra la pagina della ruota
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("utente") == null) {
            response.sendRedirect(request.getContextPath() + "/login?azione=mostra");
            return;
        }

        Utente utente = (Utente) session.getAttribute("utente");
        boolean giaGirato = false;

        // Verifica se l'utente ha già effettuato la giocata oggi
        if (utente.getDataUltimoGiro() != null && utente.getDataUltimoGiro().toLocalDate().equals(LocalDate.now())) {
            giaGirato = true;
        }

        DataSource ds = (DataSource) getServletContext().getAttribute("DataSource");
        if (ds == null) {
            try {
                javax.naming.Context initContext = new javax.naming.InitialContext();
                javax.naming.Context envContext = (javax.naming.Context) initContext.lookup("java:/comp/env");
                ds = (DataSource) envContext.lookup("jdbc/uprotein_db");
            } catch (Exception e) {
                log("[UProtein - ERROR] DataSource nullo nel doGet: " + e.getMessage());
            }
        }

        try {
            List<Prodotto> prodottiRuota = generaSpicchiRuota(ds);
            session.setAttribute("prodottiRuota", prodottiRuota);
            request.setAttribute("prodottiRuota", prodottiRuota);
            request.setAttribute("giaGirato", giaGirato);
            
            request.getRequestDispatcher("/WEB-INF/views/cliente/ruota.jsp").forward(request, response);
            
        } catch (SQLException e) {
            log("[UProtein - ERROR] Errore SQL RuotaServlet GET: " + e.getMessage());
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    // POST: Gestisce l'estrazione del premio, l'inserimento nel carrello a 0€ e risponde in JSON
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("utente") == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        Utente utente = (Utente) session.getAttribute("utente");

        // 1. Controllo di sicurezza: ha già giocato oggi?
        if (utente.getDataUltimoGiro() != null && utente.getDataUltimoGiro().toLocalDate().equals(LocalDate.now())) {
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write("{\"errore\": \"Hai già girato la ruota oggi!\"}");
            return;
        }

        // 2. Recupero degli spicchi generati per la sessione corrente
        @SuppressWarnings("unchecked")
        List<Prodotto> prodottiRuota = (List<Prodotto>) session.getAttribute("prodottiRuota");
        if (prodottiRuota == null || prodottiRuota.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        // 3. Estrazione casuale del prodotto vincente
        int indiceVincente = (int) (Math.random() * prodottiRuota.size());
        Prodotto prodottoVinto = prodottiRuota.get(indiceVincente);

        // 4. Aggiornamento dello stato dell'utente nella sessione corrente
        utente.setDataUltimoGiro(java.sql.Date.valueOf(LocalDate.now()));

        DataSource ds = (DataSource) getServletContext().getAttribute("DataSource");
        if (ds == null) {
            try {
                javax.naming.Context initContext = new javax.naming.InitialContext();
                javax.naming.Context envContext = (javax.naming.Context) initContext.lookup("java:/comp/env");
                ds = (DataSource) envContext.lookup("jdbc/uprotein_db");
            } catch (Exception e) {
                log("[UProtein - ERROR] DataSource nullo nel doPost: " + e.getMessage());
            }
        }

        // 5. SALVATAGGIO DATI (DATABASE E CARRELLO IN SESSIONE)
       try {
            // Aggiorna la data dell'ultimo giro nel Database (tramite il tuo DAO)
            it.uprotein.storage.UtenteDAO utenteDao = new it.uprotein.storage.UtenteDAOImpl(ds);
            utenteDao.doUpdateDataUltimoGiro(utente.getEmail(), java.sql.Date.valueOf(LocalDate.now()));
            
            // Se l'utente ha vinto un articolo reale (id diverso da 0), lo inseriamo nel carrello
            if (prodottoVinto.getIdProdotto() != 0) {
                
                // Recuperiamo o creiamo l'oggetto Carrello personalizzato dalla sessione
                Carrello carrello = (Carrello) session.getAttribute("carrello");
                if (carrello == null) {
                    carrello = new Carrello();
                    session.setAttribute("carrello", carrello);
                }
                
                // Cloniamo il prodotto impostando il prezzo regalo a 0.0€
                Prodotto prodottoRegalo = new Prodotto();
                prodottoRegalo.setIdProdotto(prodottoVinto.getIdProdotto());
                prodottoRegalo.setNome(prodottoVinto.getNome() + " 🎁 (Premio Ruota)");
                prodottoRegalo.setPrezzo(0.0);
                prodottoRegalo.setCategoria(prodottoVinto.getCategoria());
                prodottoRegalo.setImmagineUrl(prodottoVinto.getImmagineUrl());
                // Inserimento dell'omaggio nel carrello con quantità 1
                carrello.aggiungiProdotto(prodottoRegalo, 1);
                
                System.out.println("[RUOTA] Prodotto aggiunto con successo a 0€ nel carrello di: " + utente.getNome());
            }
            
        } catch (Exception e) {
            log("[UProtein - ERROR] Errore SQL persistenza/carrello nel doPost: " + e.getMessage());
        }
        

        // 6. RISPOSTA JSON per l'animazione lato Javascript
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        String jsonRisposta = String.format("{\"idProdotto\": %d, \"nome\": \"%s\"}", 
                prodottoVinto.getIdProdotto(), prodottoVinto.getNome().replace("\"", "\\\""));
        
        response.getWriter().write(jsonRisposta);
    }

    private List<Prodotto> generaSpicchiRuota(DataSource ds) throws SQLException {
        ProdottoDAOImpl prodottoDAO = new ProdottoDAOImpl(ds);
        List<Prodotto> economici = prodottoDAO.getProdottiPerRuota(0.0, 25.0, 15);
        List<Prodotto> premium = prodottoDAO.getProdottiPerRuota(25.0, 999.0, 15);

        List<Prodotto> spicchi = new ArrayList<>();
        Prodotto sconfitta = new Prodotto();
        sconfitta.setIdProdotto(0);
        sconfitta.setNome("Riprova Domani");
        spicchi.add(sconfitta);

        if (!premium.isEmpty()) {
            spicchi.add(premium.remove(0));
        } else if (!economici.isEmpty()) {
            spicchi.add(economici.remove(0));
        }

        for (int i = 0; i < 6; i++) {
            if (!economici.isEmpty()) {
                spicchi.add(economici.remove(0));
            } else {
                Prodotto diScorta = new Prodotto();
                diScorta.setIdProdotto(0);
                diScorta.setNome("Riprova Domani");
                spicchi.add(diScorta);
            }
        }

        Collections.shuffle(spicchi);
        return spicchi;
    }
}