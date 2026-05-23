package it.uprotein.control;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import it.uprotein.model.Prodotto;
import it.uprotein.storage.ProdottoDAOImpl;


@WebServlet("/home") 
public class HomeServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
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

 
        ProdottoDAOImpl prodottoDAO = new ProdottoDAOImpl(ds);
        try {
            List<Prodotto> listaProdotti = prodottoDAO.doRetrieveAll(null);
            request.setAttribute("prodotti", listaProdotti);
        } catch (SQLException e) {
            e.printStackTrace(); 
        }
        
        request.getRequestDispatcher("/WEB-INF/views/common/home.jsp").forward(request, response);    }
    

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}