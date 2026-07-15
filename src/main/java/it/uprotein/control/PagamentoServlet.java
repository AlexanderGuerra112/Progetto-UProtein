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

@WebServlet("/pagamento")
public class PagamentoServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
            
        HttpSession session = request.getSession();

        // 1. Controllo Sessione Utente (Ottimo lavoro del collega)
        if (session.getAttribute("utente") == null) {
            response.sendRedirect(request.getContextPath() + "/login?azione=mostra");
            return;
        }

        // 2. PROTEZIONE EXTRA: Controllo Carrello Vuoto
        // Impedisce l'accesso diretto alla pagina di pagamento se non si ha nulla in carrello
        Carrello carrello = (Carrello) session.getAttribute("carrello");
        
        // NOTA: Se il tuo metodo per verificare se il carrello è vuoto ha un nome diverso 
        // (es. carrello.getProdotti().isEmpty()), adattalo qui sotto.
        if (carrello == null || carrello.getElementi().isEmpty()) { 
            response.sendRedirect(request.getContextPath() + "/carrello?azione=mostra");
            return;
        }

        // 3. Instradamento sicuro alla vista (WEB-INF protegge la JSP da accessi diretti)
        request.getRequestDispatcher("/WEB-INF/views/cliente/pagamento.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}