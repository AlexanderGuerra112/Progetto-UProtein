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
import it.uprotein.model.Prodotto;
import it.uprotein.storage.OrdineDAO;
import it.uprotein.storage.OrdineDAOImpl;

@WebServlet("/dettaglio-ordine")
public class DettaglioOrdineServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private OrdineDAO ordineDAO;

    @Override
    public void init() throws ServletException {
        // Connessione sicura standard di UProtein
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

        // 1. Controllo di sicurezza: se l'utente non è loggato, lo rimandiamo al login
        if (utenteLoggato == null) {
            response.sendRedirect(request.getContextPath() + "/login?azione=mostra");
            return;
        }

        // 2. Recuperiamo l'ID dell'ordine passato come parametro nell'URL (es: /dettaglio-ordine?id=5)
        String idOrdineStr = request.getParameter("id");
        if (idOrdineStr == null || idOrdineStr.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/storico-ordini");
            return;
        }

        try {
            int idOrdine = Integer.parseInt(idOrdineStr);

            // 3. Recuperiamo l'ordine dal Database usando il nuovo metodo del Passo 4
            Ordine ordine = ordineDAO.doRetrieveByKey(idOrdine);

            // Controllo anti-hacker: l'utente può visualizzare SOLO i propri ordini!
            if (ordine == null || ordine.getIdUtente() != utenteLoggato.getIdUtente()) {
                response.sendRedirect(request.getContextPath() + "/storico-ordini");
                return;
            }

            // 4. Recuperiamo tutti i prodotti acquistati in quell'ordine specifico
            List<Prodotto> prodottiAcquistati = ordineDAO.doRetrieveProdottiByOrdine(idOrdine);

            // 5. Impacchettiamo i dati e li spediamo alla JSP
            request.setAttribute("ordine", ordine);
            request.setAttribute("prodottiAcquistati", prodottiAcquistati);

        } catch (NumberFormatException | SQLException e) {
            e.printStackTrace();
            request.setAttribute("erroreDettaglio", "Impossibile recuperare i dettagli dell'ordine selezionato.");
        }

        // 6. Apriamo la pagina JSP (che si trova dentro WEB-INF per sicurezza)
        request.getRequestDispatcher("/WEB-INF/views/cliente/dettaglio-ordine.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}