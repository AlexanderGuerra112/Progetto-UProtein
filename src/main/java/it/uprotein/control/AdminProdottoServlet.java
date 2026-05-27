package it.uprotein.control;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import javax.sql.DataSource;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import it.uprotein.model.Prodotto;
import it.uprotein.model.Utente;
import it.uprotein.storage.ProdottoDAOImpl;

@WebServlet("/adminProdotto")
public class AdminProdottoServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        Utente utente = (session != null) ? (Utente) session.getAttribute("utente") : null;

        if (utente == null || !utente.getRuolo().equalsIgnoreCase("admin")) {
            request.setAttribute("errore", "Accesso negato: area riservata agli amministratori.");
            request.getRequestDispatcher("/WEB-INF/views/common/login.jsp").forward(request, response);
            return;
        }

        DataSource ds = (DataSource) getServletContext().getAttribute("DataSource");
        if (ds == null) {
            try {
                javax.naming.Context initContext = new javax.naming.InitialContext();
                javax.naming.Context envContext = (javax.naming.Context) initContext.lookup("java:/comp/env");
                ds = (DataSource) envContext.lookup("jdbc/uprotein_db");
            } catch (Exception e) {
                log("[UProtein - ERROR] Impossibile recuperare il DataSource in AdminProdottoServlet (doGet): " + e.getMessage());
            }
        }

        ProdottoDAOImpl dao = new ProdottoDAOImpl(ds);
        String azione = request.getParameter("azione");

        try {
            if (azione == null || azione.equalsIgnoreCase("mostra")) {
                List<Prodotto> prodotti = dao.doRetrieveAll(null);
                request.setAttribute("prodotti", prodotti);
                request.getRequestDispatcher("/WEB-INF/views/admin/gestioneProdotto.jsp").forward(request, response);
            } 
            else if (azione.equalsIgnoreCase("elimina")) {
                int id = Integer.parseInt(request.getParameter("id"));
                dao.doDelete(id);
                response.sendRedirect("adminProdotto?azione=mostra");
            }
        } catch (SQLException e) {
            // Sostituito printStackTrace con un log pulito sul server e messaggio personalizzato
            log("[UProtein - ERROR] Errore nel recupero o eliminazione dei prodotti: " + e.getMessage());
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Errore! Impossibile elaborare la richiesta sul catalogo prodotti.");
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        Utente utente = (session != null) ? (Utente) session.getAttribute("utente") : null;
        if (utente == null || !utente.getRuolo().equalsIgnoreCase("admin")) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        DataSource ds = (DataSource) getServletContext().getAttribute("DataSource");
        if (ds == null) {
            try {
                javax.naming.Context initContext = new javax.naming.InitialContext();
                javax.naming.Context envContext = (javax.naming.Context) initContext.lookup("java:/comp/env");
                ds = (DataSource) envContext.lookup("jdbc/uprotein_db");
            } catch (Exception e) {
                log("[UProtein - ERROR] Impossibile recuperare il DataSource in AdminProdottoServlet (doPost): " + e.getMessage());
            }
        }

        ProdottoDAOImpl dao = new ProdottoDAOImpl(ds);
        String azione = request.getParameter("azione");

        try {
            if (azione != null && azione.equalsIgnoreCase("salva")) {
                Prodotto p = new Prodotto();
                String idStr = request.getParameter("id");
                
                p.setNome(request.getParameter("nome"));
                p.setCategoria(request.getParameter("categoria")); 
                p.setDescrizione(request.getParameter("descrizione"));
                p.setPrezzo(Double.parseDouble(request.getParameter("prezzo")));
                p.setDisponibilitaMagazzino(Integer.parseInt(request.getParameter("disponibilita")));
                p.setImmagineUrl(request.getParameter("immagine_url"));

                if (idStr == null || idStr.isEmpty()) {
                    dao.doSave(p); 
                } else {
                    p.setIdProdotto(Integer.parseInt(idStr));
                    dao.doUpdate(p); 
                }
                response.sendRedirect("adminProdotto?azione=mostra");
            }
        } catch (SQLException e) {
            // Sostituito printStackTrace con un log pulito sul server e messaggio personalizzato
            log("[UProtein - ERROR] Errore durante il salvataggio del prodotto: " + e.getMessage());
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Errore! Impossibile completare il salvataggio del nuovo prodotto.");
        }
    }
}