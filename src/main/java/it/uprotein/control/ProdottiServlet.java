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

import it.uprotein.model.Prodotto;
import it.uprotein.storage.ProdottoDAOImpl;

@WebServlet("/prodotti")
public class ProdottiServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String azione = request.getParameter("azione");
        if (azione == null) azione = "mostra";

        if (azione.equalsIgnoreCase("mostra")) {
        	  DataSource ds = (DataSource) getServletContext().getAttribute("DataSource");
              if (ds == null) {
                  try {
                      javax.naming.Context initContext = new javax.naming.InitialContext();
                      javax.naming.Context envContext = (javax.naming.Context) initContext.lookup("java:/comp/env");
                      ds = (DataSource) envContext.lookup("jdbc/uprotein_db");
                  } catch (Exception e) {
                      log("[UProtein - ERROR] DataSource nullo nella ProdottiServlet: " + e.getMessage());
                  }
              }

              if (ds == null) {
                  response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                                     "Errore interno del server.");
                  return;
              }

              ProdottoDAOImpl dao = new ProdottoDAOImpl(ds);

            try {
                String categoria = request.getParameter("categoria");
                List<Prodotto> listaProdotti;

                if (categoria != null && !categoria.trim().isEmpty()) {
                    // Mostra prodotti di una categoria specifica
                    listaProdotti = dao.doRetrieveByCategoria(categoria.trim());
                    request.setAttribute("categoriaAttiva", categoria.trim());
                } else {
                    // Mostra tutti i prodotti
                    listaProdotti = dao.doRetrieveAll("categoria");
                    request.setAttribute("categoriaAttiva", "Tutti");
                }

                request.setAttribute("listaProdotti", listaProdotti);
                request.getRequestDispatcher("/WEB-INF/views/common/prodotti.jsp")
                       .forward(request, response);

            } catch (SQLException e) {
                log("[UProtein - ERROR] Errore SQL ProdottiServlet: " + e.getMessage());
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                                   "Errore nel caricamento dei prodotti.");
            }

        } else {
            response.sendRedirect(request.getContextPath() + "/prodotti?azione=mostra");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}