package it.uprotein.control;

import java.io.IOException;
import java.sql.SQLException;
import javax.sql.DataSource;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import it.uprotein.model.Utente;
import it.uprotein.storage.UtenteDAOImpl;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String azione = request.getParameter("azione");

        // Controlliamo se esiste già un utente in sessione
        HttpSession session = request.getSession(false);
        boolean giaLoggato = (session != null && session.getAttribute("utente") != null);

        if (azione == null || azione.equalsIgnoreCase("mostra")) {
            if (giaLoggato) {
                // Se sei già loggato, ti rimando alla home senza chiederti di nuovo il login
                response.sendRedirect(request.getContextPath() + "/home");
            } else {
                // Se NON sei loggato, ti mostro la pagina di login normale
                request.getRequestDispatcher("/WEB-INF/views/common/login.jsp")
                       .forward(request, response);
            }

        } else if (azione.equalsIgnoreCase("logout")) {
            if (session != null) {
                session.invalidate();
            }
            response.sendRedirect(request.getContextPath() + "/home");

        } else {
            response.sendRedirect(request.getContextPath() + "/login?azione=mostra");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String azione = request.getParameter("azione");

        if (azione == null || !azione.equalsIgnoreCase("controlla")) {
            response.sendRedirect(request.getContextPath() + "/login?azione=mostra");
            return;
        }

        String email    = request.getParameter("email");
        String password = request.getParameter("password");

        if (email == null || email.trim().isEmpty()
                || password == null || password.trim().isEmpty()) {
            request.setAttribute("errore", "Email e password sono obbligatori.");
            request.getRequestDispatcher("/WEB-INF/views/common/login.jsp")
                   .forward(request, response);
            return;
        }

        // Blocco di connessione sicura al DB
        DataSource ds = (DataSource) getServletContext().getAttribute("DataSource");
        if (ds == null) {
            try {
                javax.naming.Context initContext = new javax.naming.InitialContext();
                javax.naming.Context envContext = (javax.naming.Context) initContext.lookup("java:/comp/env");
                ds = (DataSource) envContext.lookup("jdbc/uprotein_db");
            } catch (Exception e) {
                log("[UProtein - ERROR] Impossibile recuperare il DataSource: " + e.getMessage());
            }
        }

        if (ds == null) {
            log("[UProtein - ERROR] DataSource nullo definitivo nella LoginServlet.");
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Errore interno del server.");
            return;
        }

        UtenteDAOImpl dao = new UtenteDAOImpl(ds);

        try {
            Utente user = dao.doRetrieveByLogin(email.trim(), password);

            if (user != null) {
                HttpSession session = request.getSession();
                session.setAttribute("utente", user);
                session.setAttribute("ruolo",  user.getRuolo());
                
                // REINDIRIZZAMENTO AUTOMATICO SE SEI ADMIN
                if (user.getRuolo() != null && user.getRuolo().equalsIgnoreCase("admin")) {
                    response.sendRedirect(request.getContextPath() + "/adminProdotto?azione=mostra");
                } else {
                    response.sendRedirect(request.getContextPath() + "/home");
                }
            } else {
                request.setAttribute("errore", "Email o password non corretti. Riprova.");
                request.getRequestDispatcher("/WEB-INF/views/common/login.jsp")
                       .forward(request, response);
            }

        } catch (SQLException e) {
            log("[UProtein - ERROR] Eccezione SQL nel login di " + email + ": " + e.getMessage());
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                               "Errore di connessione al database.");
        }
    }
}


 