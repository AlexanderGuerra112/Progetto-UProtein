package it.uprotein.control;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.sql.DataSource;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import it.uprotein.model.*;
import it.uprotein.storage.*;

@WebServlet("/checkout")
public class CheckoutServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        
        DataSource ds = (DataSource) getServletContext().getAttribute("DataSource");
        if (ds == null) {
            try {
                javax.naming.Context initContext = new javax.naming.InitialContext();
                javax.naming.Context envContext = (javax.naming.Context) initContext.lookup("java:/comp/env");
                ds = (DataSource) envContext.lookup("jdbc/uprotein_db");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        Utente utente = (Utente) session.getAttribute("utente");
        Carrello carrello = (Carrello) session.getAttribute("carrello");
        
        // CORRETTO: Reindirizzamento alle Servlet e non alle JSP dirette
        if (utente == null) {
            response.sendRedirect(request.getContextPath() + "/login?azione=mostra");
            return;
        }

        if (carrello == null || carrello.getElementi().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/carrello?azione=mostra");
            return;
        }

        // 1. VALIDAZIONE LATO SERVER (Il tocco di classe accademico)
        String titolare = request.getParameter("titolare");
        String numeroCarta = request.getParameter("numero_carta");
        String scadenza = request.getParameter("scadenza");
        String cvv = request.getParameter("cvv");

        // Puliamo il numero della carta da eventuali spazi inseriti dal formattatore automatico JS
        String numeroCartaPulito = (numeroCarta != null) ? numeroCarta.replaceAll("\\s+", "") : "";

        if (titolare == null || titolare.trim().isEmpty() ||
            numeroCartaPulito == null || !numeroCartaPulito.matches("\\d{16}") ||
            scadenza == null || !scadenza.matches("(0[1-9]|1[0-2])/\\d{2}") ||
            cvv == null || !cvv.matches("\\d{3}")) {
            
            request.setAttribute("errorePagamento", "I dati della carta inseriti non sono validi o sono incompleti.");
            request.getRequestDispatcher("/WEB-INF/views/cliente/pagamento.jsp").forward(request, response);
            return;
        }

        // 2. CREAZIONE FATTURA E SALVATAGGIO ORDINE
        // Copiamo gli elementi per la fattura *prima* di svuotare il carrello
        request.setAttribute("prodottiFattura", new ArrayList<>(carrello.getElementi()));
        request.setAttribute("totaleFattura", carrello.getTotale());

        String metodoPagamento = request.getParameter("metodoPagamento");
        if (metodoPagamento == null) metodoPagamento = "Carta di Credito";
        request.setAttribute("metodoPagamentoStampato", metodoPagamento);

        OrdineDAO ordineDao = new OrdineDAOImpl(ds);

        try {
            // Salva l'ordine nel Database
            ordineDao.doSave(utente, carrello, metodoPagamento);
            
            // CORRETTO: Svuotiamo il carrello! L'acquisto è completato con successo.
            carrello.svuota();
            session.setAttribute("carrello", carrello);
                  
            RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/cliente/fattura.jsp");
            dispatcher.forward(request, response);
            
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/carrello?azione=mostra");
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}