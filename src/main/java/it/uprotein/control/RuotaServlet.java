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

@WebServlet("/common/ruota")
public class RuotaServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

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

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("utente") == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        Utente utente = (Utente) session.getAttribute("utente");

        if (utente.getDataUltimoGiro() != null && utente.getDataUltimoGiro().toLocalDate().equals(LocalDate.now())) {
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write("{\"errore\": \"Hai già girato la ruota oggi!\"}");
            return;
        }

        @SuppressWarnings("unchecked")
        List<Prodotto> prodottiRuota = (List<Prodotto>) session.getAttribute("prodottiRuota");
        if (prodottiRuota == null || prodottiRuota.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        int indiceVincente = (int) (Math.random() * prodottiRuota.size());
        Prodotto prodottoVinto = prodottiRuota.get(indiceVincente);

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

       try {
            it.uprotein.storage.UtenteDAO utenteDao = new it.uprotein.storage.UtenteDAOImpl(ds);
            utenteDao.doUpdateDataUltimoGiro(utente.getEmail(), java.sql.Date.valueOf(LocalDate.now()));
            
            if (prodottoVinto.getIdProdotto() != 0) {
                
                Carrello carrello = (Carrello) session.getAttribute("carrello");
                if (carrello == null) {
                    carrello = new Carrello();
                    session.setAttribute("carrello", carrello);
                }
                
                Prodotto prodottoRegalo = new Prodotto();
                prodottoRegalo.setIdProdotto(prodottoVinto.getIdProdotto());
                prodottoRegalo.setNome(prodottoVinto.getNome() + " 🎁 (Premio Ruota)");
                prodottoRegalo.setPrezzo(0.0);
                prodottoRegalo.setCategoria(prodottoVinto.getCategoria());
                prodottoRegalo.setImmagineUrl(prodottoVinto.getImmagineUrl());
                carrello.aggiungiProdotto(prodottoRegalo, 1);
                
                System.out.println("[RUOTA] Prodotto aggiunto con successo a 0€ nel carrello di: " + utente.getNome());
            }
            
        } catch (Exception e) {
            log("[UProtein - ERROR] Errore SQL persistenza/carrello nel doPost: " + e.getMessage());
        }
        

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