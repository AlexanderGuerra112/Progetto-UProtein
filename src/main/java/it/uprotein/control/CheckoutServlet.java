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
        
        if (utente == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        if (carrello == null || carrello.getElementi().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/carrello.jsp");
            return;
        }

        
        request.setAttribute("prodottiFattura", new ArrayList<>(carrello.getElementi()));
        request.setAttribute("totaleFattura", carrello.getTotale());

        
        String metodoPagamento = request.getParameter("metodoPagamento");
        OrdineDAO ordineDao = new OrdineDAOImpl(ds);

        try {
           
            ordineDao.doSave(utente, carrello, metodoPagamento);
            String metodoScelto = request.getParameter("metodoPagamento");
            if (metodoScelto == null) metodoScelto = "Carta di Credito";

           request.setAttribute("metodoPagamentoStampato", metodoScelto);
                  
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