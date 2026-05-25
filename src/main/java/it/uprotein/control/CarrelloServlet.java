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

import it.uprotein.model.Carrello;
import it.uprotein.model.Prodotto;
import it.uprotein.storage.ProdottoDAOImpl; 

@WebServlet("/carrello")
public class CarrelloServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
       
        HttpSession session = request.getSession(true);
        
       
        Carrello carrello = (Carrello) session.getAttribute("carrello");
        
       
        if (carrello == null) {
            carrello = new Carrello();
            session.setAttribute("carrello", carrello);
        }

        String azione = request.getParameter("azione");
        if (azione == null) {
            azione = "mostra"; 
        }

        try {
            if (azione.equalsIgnoreCase("mostra")) {
                request.getRequestDispatcher("/WEB-INF/views/common/carrello.jsp").forward(request, response);
            } 
            
            else if (azione.equalsIgnoreCase("aggiungi")) {
                int idProdotto = Integer.parseInt(request.getParameter("idProdotto"));
                int quantita = 1; 
                
                String qtaParam = request.getParameter("quantita");
                if (qtaParam != null) {
                    quantita = Integer.parseInt(qtaParam);
                }

                DataSource ds = (DataSource) getServletContext().getAttribute("DataSource");
                ProdottoDAOImpl prodottoDAO = new ProdottoDAOImpl(ds);
                Prodotto prodotto = prodottoDAO.doRetrieveByKey(idProdotto); 

                if (prodotto != null) {
                    carrello.aggiungiProdotto(prodotto, quantita);
                }

                response.sendRedirect(request.getContextPath() + "/carrello?azione=mostra");
            } 
            
            else if (azione.equalsIgnoreCase("rimuovi")) {
                int idProdotto = Integer.parseInt(request.getParameter("idProdotto"));
                
                carrello.rimuoviProdotto(idProdotto);
                
                response.sendRedirect(request.getContextPath() + "/carrello?azione=mostra");
            }
            
        } catch (SQLException e) {
            log("[UProtein - ERROR] Errore SQL nella gestione del carrello: " + e.getMessage());
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Errore durante il caricamento del prodotto.");
        } catch (NumberFormatException e) {
            log("[UProtein - ERROR] ID Prodotto o quantità non validi nel carrello.");
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Richiesta non valida.");
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        doGet(request, response);
    }
}