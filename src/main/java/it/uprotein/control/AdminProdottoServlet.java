package it.uprotein.control;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.List;
import javax.sql.DataSource;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;

import it.uprotein.model.Prodotto;
import it.uprotein.model.Utente;
import it.uprotein.storage.ProdottoDAOImpl;

@WebServlet("/adminProdotto")
@MultipartConfig(
    fileSizeThreshold = 1024 * 1024 * 2,  // 2 MB
    maxFileSize = 1024 * 1024 * 10,       // 10 MB
    maxRequestSize = 1024 * 1024 * 50     // 50 MB
)
public class AdminProdottoServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Controllo admin
        HttpSession session = request.getSession(false);
        Utente utente = (session != null) ? (Utente) session.getAttribute("utente") : null;
        if (utente == null || !utente.getRuolo().equalsIgnoreCase("admin")) {
            request.setAttribute("errore", "Accesso negato: area riservata agli amministratori.");
            request.getRequestDispatcher("/WEB-INF/views/common/login.jsp")
                   .forward(request, response);
            return;
        }

        // Recupera DataSource con fallback JNDI
        DataSource ds = (DataSource) getServletContext().getAttribute("DataSource");
        if (ds == null) {
            try {
                javax.naming.Context initContext = new javax.naming.InitialContext();
                javax.naming.Context envContext =
                    (javax.naming.Context) initContext.lookup("java:/comp/env");
                ds = (DataSource) envContext.lookup("jdbc/uprotein_db");
            } catch (Exception e) {
                log("[UProtein - ERROR] DataSource nullo in AdminProdottoServlet doGet: "
                    + e.getMessage());
            }
        }
        if (ds == null) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Errore interno.");
            return;
        }

        ProdottoDAOImpl dao = new ProdottoDAOImpl(ds);
        String azione = request.getParameter("azione");

        try {

            // azione=mostra -> mostra la lista prodotti
            if (azione == null || azione.equalsIgnoreCase("mostra")) {
                List<Prodotto> prodotti = dao.doRetrieveAll(null);
                request.setAttribute("prodotti", prodotti);
                request.setAttribute("prodottoDaModificare", null);
                request.getRequestDispatcher("/WEB-INF/views/admin/gestioneProdotto.jsp")
                       .forward(request, response);

            // azione=mostraModifica -> carica il prodotto e precompila il form
            } else if (azione.equalsIgnoreCase("mostraModifica")) {
                String idStr = request.getParameter("id");
                if (idStr == null || idStr.trim().isEmpty()) {
                    response.sendRedirect(request.getContextPath() + "/adminProdotto?azione=mostra");
                    return;
                }
                int id = Integer.parseInt(idStr.trim());
                Prodotto prodottoDaModificare = dao.doRetrieveByKey(id);

                if (prodottoDaModificare == null) {
                    response.sendRedirect(request.getContextPath() + "/adminProdotto?azione=mostra");
                    return;
                }

                List<Prodotto> prodotti = dao.doRetrieveAll(null);
                request.setAttribute("prodotti", prodotti);
                request.setAttribute("prodottoDaModificare", prodottoDaModificare);
                request.getRequestDispatcher("/WEB-INF/views/admin/gestioneProdotto.jsp")
                       .forward(request, response);

            } else {
                response.sendRedirect(request.getContextPath() + "/adminProdotto?azione=mostra");
            }

        } catch (SQLException e) {
            log("[UProtein - ERROR] Errore SQL AdminProdottoServlet doGet: " + e.getMessage());
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                               "Errore nel caricamento del catalogo prodotti.");
        } catch (NumberFormatException e) {
            log("[UProtein - ERROR] ID prodotto non valido in AdminProdottoServlet.");
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Richiesta non valida.");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Controllo admin
        HttpSession session = request.getSession(false);
        Utente utente = (session != null) ? (Utente) session.getAttribute("utente") : null;
        if (utente == null || !utente.getRuolo().equalsIgnoreCase("admin")) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        // Recupera DataSource con fallback JNDI
        DataSource ds = (DataSource) getServletContext().getAttribute("DataSource");
        if (ds == null) {
            try {
                javax.naming.Context initContext = new javax.naming.InitialContext();
                javax.naming.Context envContext =
                    (javax.naming.Context) initContext.lookup("java:/comp/env");
                ds = (DataSource) envContext.lookup("jdbc/uprotein_db");
            } catch (Exception e) {
                log("[UProtein - ERROR] DataSource nullo in AdminProdottoServlet doPost: "
                    + e.getMessage());
            }
        }
        if (ds == null) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Errore interno.");
            return;
        }

        ProdottoDAOImpl dao = new ProdottoDAOImpl(ds);
        String azione = request.getParameter("azione");

        try {

            // azione=elimina
            if (azione != null && azione.equalsIgnoreCase("elimina")) {
                String idStr = request.getParameter("id");
                if (idStr == null || idStr.trim().isEmpty()) {
                    response.sendRedirect(request.getContextPath() + "/adminProdotto?azione=mostra");
                    return;
                }
                int id = Integer.parseInt(idStr.trim());
                dao.doDelete(id);
                response.sendRedirect(request.getContextPath() + "/adminProdotto?azione=mostra");

            // azione=salva
            } else if (azione != null && azione.equalsIgnoreCase("salva")) {

                String idStr = request.getParameter("id");
                String nome = request.getParameter("nome");
                String categoria = request.getParameter("categoria");
                String descrizione = request.getParameter("descrizione");
                String prezzoStr = request.getParameter("prezzo");
                String disponibilitaStr = request.getParameter("disponibilita");

                if (nome == null || nome.trim().isEmpty()
                        || categoria == null || categoria.trim().isEmpty()
                        || prezzoStr == null || prezzoStr.trim().isEmpty()
                        || disponibilitaStr == null || disponibilitaStr.trim().isEmpty()) {
                    response.sendRedirect(request.getContextPath() + "/adminProdotto?azione=mostra");
                    return;
                }

                // --- GESTIONE UPLOAD IMMAGINE ---
                Part filePart = request.getPart("foto");
                String fileName = null;

                if (filePart != null && filePart.getSize() > 0) {
                    // Estrae solo il nome originale del file (es: "nuovacreatina.png")
                    fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();

                    // Individua il percorso della cartella "images" dentro la Web App
                    String uploadPath = getServletContext().getRealPath("") + File.separator + "images";
                    File uploadDir = new File(uploadPath);
                    if (!uploadDir.exists()) {
                        uploadDir.mkdir();
                    }

                    // Salva il file caricato nella cartella /images/ del server
                    filePart.write(uploadPath + File.separator + fileName);
                } else {
                    // Se non è stato caricato nessun nuovo file, recupera l'immagine precedente (in modifica)
                    fileName = request.getParameter("immagine_url_esistente");
                }

                Prodotto p = new Prodotto();
                p.setNome(nome.trim());
                p.setCategoria(categoria.trim());
                p.setDescrizione(descrizione != null ? descrizione.trim() : "");
                p.setPrezzo(Double.parseDouble(prezzoStr.trim()));
                p.setDisponibilitaMagazzino(Integer.parseInt(disponibilitaStr.trim()));
                p.setImmagineUrl(fileName != null ? fileName : "");

                if (idStr == null || idStr.trim().isEmpty()) {
                    dao.doSave(p);
                } else {
                    p.setIdProdotto(Integer.parseInt(idStr.trim()));
                    dao.doUpdate(p);
                }

                response.sendRedirect(request.getContextPath() + "/adminProdotto?azione=mostra");

            } else {
                response.sendRedirect(request.getContextPath() + "/adminProdotto?azione=mostra");
            }

        } catch (SQLException e) {
            log("[UProtein - ERROR] Errore SQL AdminProdottoServlet doPost: " + e.getMessage());
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                               "Errore nell'operazione sul prodotto.");
        } catch (NumberFormatException e) {
            log("[UProtein - ERROR] Prezzo o disponibilità non validi.");
            response.sendError(HttpServletResponse.SC_BAD_REQUEST,
                               "Prezzo o quantità inseriti non validi.");
        }
    }
}