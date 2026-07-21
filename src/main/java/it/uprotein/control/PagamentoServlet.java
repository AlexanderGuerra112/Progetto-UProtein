package it.uprotein.control;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

// Importa il modello del carrello del tuo progetto
import it.uprotein.model.Carrello; 

@WebServlet("/common/pagamento")
public class PagamentoServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
            
        HttpSession session = request.getSession();

        if (session.getAttribute("utente") == null) {
            response.sendRedirect(request.getContextPath() + "/login?azione=mostra");
            return;
        }

        Carrello carrello = (Carrello) session.getAttribute("carrello");
      
        if (carrello == null || carrello.getElementi().isEmpty()) { 
            response.sendRedirect(request.getContextPath() + "/carrello?azione=mostra");
            return;
        }

      
        request.getRequestDispatcher("/WEB-INF/views/cliente/pagamento.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}