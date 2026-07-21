package it.uprotein.filter;

import java.io.IOException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebFilter("/*")
public class AuthFilter extends HttpFilter {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        String path = request.getServletPath();

        if (!path.startsWith("/admin/") && !path.startsWith("/common/")) {
            chain.doFilter(request, response);
            return;
        }

        HttpSession session = request.getSession(false);
        String ruolo = (session != null) ? (String) session.getAttribute("ruolo") : null;

        boolean autorizzato = false;

        if (ruolo != null) {
            if (path.startsWith("/admin/")) {
                // Solo chi ha ruolo "admin" entra qui
                autorizzato = "admin".equalsIgnoreCase(ruolo);
            } else if (path.startsWith("/common/")) {
                autorizzato = true;
            }
        }

        if (autorizzato) {
            chain.doFilter(request, response);
        } else {
            response.sendRedirect(request.getContextPath() + "/login?azione=mostra");
        }
    }
}