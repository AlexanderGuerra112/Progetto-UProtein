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
        
        // 1. RECUPERO ROBUSTO DEL DATA SOURCE (identico alla tua HomeServlet)
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

        // 2. CONTROLLO UTENTE E CARRELLO
        Utente utente = (Utente) session.getAttribute("utente");
        Carrello carrello = (Carrello) session.getAttribute("carrello");
        
        if (utente == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        if (carrello == null || carrello.getElementi().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/carrello.jsp");
            return;
        }

        // 3. PREPARAZIONE DATI PER LA FATTURA
        // Salviamo i prodotti in un attributo di request prima che il DAO svuoti il carrello
        request.setAttribute("prodottiFattura", new ArrayList<>(carrello.getElementi()));
        request.setAttribute("totaleFattura", carrello.getTotale());

        // 4. SALVATAGGIO ORDINE
        String metodoPagamento = request.getParameter("metodoPagamento");
        OrdineDAO ordineDao = new OrdineDAOImpl(ds);

        try {
            // doSave scriverà nel DB e chiamerà carrello.svuota() come richiesto [3]
            ordineDao.doSave(utente, carrello, metodoPagamento);
            
            // 5. FORWARD ALLA FATTURA (Percorso corretto con 'views' e cartella 'client')
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