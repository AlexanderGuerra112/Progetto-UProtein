package it.uprotein.control;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/registrazione")
public class RegistrazioneServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String azione = request.getParameter("azione");

        // Azione "mostra": Mostra il form vuoto per registrarsi
        if (azione == null || azione.equalsIgnoreCase("mostra")) {
            request.getRequestDispatcher("/WEB-INF/views/common/registrazione.jsp").forward(request, response);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // Qui dentro in un secondo momento metterai il codice per prendere i parametri 
        // e fare il dao.doSave(utente) per salvarlo nel database!
        doGet(request, response);
    }
}