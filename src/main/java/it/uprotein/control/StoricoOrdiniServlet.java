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

import it.uprotein.model.Utente;
import it.uprotein.model.Ordine;
import it.uprotein.storage.OrdineDAO;
import it.uprotein.storage.OrdineDAOImpl;

@WebServlet("/common/storico-ordini")
public class StoricoOrdiniServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private OrdineDAO ordineDAO;

    @Override
    public void init() throws ServletException {
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

        this.ordineDAO = new OrdineDAOImpl(ds);
    }
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        Utente utenteLoggato = (Utente) session.getAttribute("utente");

        if (utenteLoggato == null) {
            response.sendRedirect(request.getContextPath() + "/login?azione=mostra");
            return;
        }

        try {
            List<Ordine> listaOrdini = ordineDAO.doRetrieveByUtente(utenteLoggato.getIdUtente());
            
            request.setAttribute("listaOrdini", listaOrdini);
            
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("erroreStorico", "Impossibile recuperare lo storico degli ordini in questo momento.");
        }

        request.getRequestDispatcher("/WEB-INF/views/cliente/storico-ordini.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}