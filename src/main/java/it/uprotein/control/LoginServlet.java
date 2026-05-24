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

/**
 * Servlet per la gestione dell'autenticazione utente.
 * Mappa l'URL /login come indicato nei diagrammi di progetto.
 */
@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String azione = request.getParameter("azione");

        
        if (azione == null || azione.equalsIgnoreCase("mostra")) {
            request.getRequestDispatcher("/WEB-INF/views/common/login.jsp").forward(request, response);
        } 
        
        
        else if (azione.equalsIgnoreCase("controlla")) {
           
            String email = request.getParameter("email");
            String password = request.getParameter("password");

            
            DataSource ds = (DataSource) getServletContext().getAttribute("DataSource");
            
            UtenteDAOImpl dao = new UtenteDAOImpl(ds);
            
            try {
                Utente user = dao.doRetrieveByLogin(email, password);

                if (user != null) {
                    HttpSession session = request.getSession();
                    session.setAttribute("utente", user); 
                    session.setAttribute("ruolo", user.getRuolo()); 
                    
                    response.sendRedirect("home?azione=mostra");
                } else {
                    request.setAttribute("errore", "Credenziali non valide. Riprova.");
                    request.getRequestDispatcher("/WEB-INF/views/common/login.jsp").forward(request, response);
                }
            } catch (SQLException e) {
                e.printStackTrace();
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Errore database");
            }
        }
        else if (azione.equalsIgnoreCase("logout")) {
            HttpSession session = request.getSession(false);
            if (session != null) {
                session.invalidate();
            }
            response.sendRedirect("home?azione=mostra");
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        doGet(request, response);
    }
}