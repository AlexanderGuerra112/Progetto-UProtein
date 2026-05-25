package it.uprotein.control;

import java.io.IOException;
import java.sql.SQLException;
import javax.sql.DataSource;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import it.uprotein.model.Utente;
import it.uprotein.storage.UtenteDAOImpl;

@WebServlet("/registrazione")
public class RegistrazioneServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.getRequestDispatcher("/WEB-INF/views/common/registrazione.jsp")
               .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String azione = request.getParameter("azione");

        if (azione == null || !azione.equalsIgnoreCase("salva")) {
            response.sendRedirect(request.getContextPath() + "/registrazione");
            return;
        }

        String nome      = request.getParameter("nome");
        String cognome   = request.getParameter("cognome");
        String email     = request.getParameter("email");
        String password  = request.getParameter("password");
        String telefono  = request.getParameter("telefono");
        String indirizzo = request.getParameter("indirizzo");

        if (nome == null || nome.trim().isEmpty()
                || cognome == null || cognome.trim().isEmpty()
                || email == null || email.trim().isEmpty()
                || password == null || password.trim().isEmpty()) {

            request.setAttribute("errore", "Tutti i campi sono obbligatori.");
            request.getRequestDispatcher("/WEB-INF/views/common/registrazione.jsp")
                   .forward(request, response);
            return;
        }

        Utente nuovoUtente = new Utente();
        nuovoUtente.setNome(nome.trim());
        nuovoUtente.setCognome(cognome.trim());
        nuovoUtente.setEmail(email.trim());
        nuovoUtente.setPassword(password);
        nuovoUtente.setTelefono(telefono);
        nuovoUtente.setIndirizzoSpedizione(indirizzo);
        nuovoUtente.setRuolo("cliente");

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
            log("[UProtein - ERROR] DataSource nullo nella RegistrazioneServlet.");
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Errore interno.");
            return;
        }

        UtenteDAOImpl dao = new UtenteDAOImpl(ds);

        try {
            dao.doSave(nuovoUtente);
            request.setAttribute("successo", "Registrazione completata! Effettua il login.");
            request.getRequestDispatcher("/WEB-INF/views/common/login.jsp")
                   .forward(request, response);

        } catch (SQLException e) {
            log("[UProtein - ERROR] Errore salvataggio utente " + email + ": " + e.getMessage());
            request.setAttribute("errore", "Email già registrata o errore di connessione.");
            request.getRequestDispatcher("/WEB-INF/views/common/registrazione.jsp")
                   .forward(request, response);
        }
    }
}