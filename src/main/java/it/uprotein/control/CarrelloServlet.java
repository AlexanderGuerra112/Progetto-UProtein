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

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(true);

        
        Carrello carrello = (Carrello) session.getAttribute("carrello");
        if (carrello == null) {
            carrello = new Carrello();
            session.setAttribute("carrello", carrello);
        }

        String azione = request.getParameter("azione");
        if (azione == null) azione = "mostra";

        try {

            if (azione.equalsIgnoreCase("mostra")) {

                
                request.setAttribute("elementiCarrello", carrello.getElementi());
                request.setAttribute("totale", carrello.getTotale());
                request.getRequestDispatcher("/WEB-INF/views/common/carrello.jsp")
                       .forward(request, response);

            } else if (azione.equalsIgnoreCase("aggiungi")) {

                int idProdotto = Integer.parseInt(request.getParameter("idProdotto"));
                int quantita = 1;
                String qtaParam = request.getParameter("quantita");
                if (qtaParam != null && !qtaParam.trim().isEmpty()) {
                    quantita = Integer.parseInt(qtaParam.trim());
                }
                if (quantita <= 0) quantita = 1;

                DataSource ds = (DataSource) getServletContext().getAttribute("DataSource");
                if (ds == null) {
                    try {
                        javax.naming.Context initContext = new javax.naming.InitialContext();
                        javax.naming.Context envContext = (javax.naming.Context) initContext.lookup("java:/comp/env");
                        ds = (DataSource) envContext.lookup("jdbc/uprotein_db");
                    } catch (Exception e) {
                        log("[UProtein - ERROR] DataSource nullo nella CarrelloServlet: " + e.getMessage());
                    }
                }
                if (ds == null) {
                    response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Errore interno del server.");
                    return;
                }
                ProdottoDAOImpl prodottoDAO = new ProdottoDAOImpl(ds);
                Prodotto prodotto = prodottoDAO.doRetrieveByKey(idProdotto);

                if (prodotto != null) {
                    carrello.aggiungiProdotto(prodotto, quantita);
                }

                // Torna alla pagina da cui l'utente ha cliccato
                String referer = request.getHeader("Referer");
                if (referer != null) {
                    response.sendRedirect(referer);
                } else {
                    response.sendRedirect(request.getContextPath() + "/carrello?azione=mostra");
                }

             } else if (azione.equalsIgnoreCase("rimuovi")) {

                int idProdotto = Integer.parseInt(request.getParameter("idProdotto"));
                String prezzoParam = request.getParameter("prezzo");

                // Se la JSP ci passa anche il prezzo, facciamo una rimozione mirata
                if (prezzoParam != null && !prezzoParam.trim().isEmpty()) {
                    try {
                        double prezzo = Double.parseDouble(prezzoParam.trim());
                        carrello.rimuoviProdotto(idProdotto, prezzo);
                    } catch (NumberFormatException e) {
                        log("[UProtein - WARNING] Prezzo non valido nella rimozione, uso rimozione generica per ID.");
                        carrello.rimuoviProdotto(idProdotto);
                    }
                } else {
                    // Altrimenti rimuove qualsiasi elemento con quell'ID (comportamento standard)
                    carrello.rimuoviProdotto(idProdotto);
                }
                
                response.sendRedirect(request.getContextPath() + "/carrello?azione=mostra");
            } else if (azione.equalsIgnoreCase("svuota")) {

                carrello.svuota();
                response.sendRedirect(request.getContextPath() + "/carrello?azione=mostra");

            } else {
                response.sendRedirect(request.getContextPath() + "/carrello?azione=mostra");
            }

        } catch (SQLException e) {
            log("[UProtein - ERROR] Errore SQL carrello: " + e.getMessage());
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                               "Errore durante il caricamento del prodotto.");
        } catch (NumberFormatException e) {
            log("[UProtein - ERROR] ID prodotto o quantità non validi.");
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Richiesta non valida.");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}