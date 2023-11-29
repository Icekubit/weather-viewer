package icekubit.servlet;

import icekubit.service.AuthorizationService;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


import java.io.IOException;
import java.util.Arrays;

// TODO переделать кэтч


@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {
    private AuthorizationService authorizationService;

    @Override
    public void init(ServletConfig config) {
        ServletContext servletContext = config.getServletContext();
        authorizationService = (AuthorizationService) servletContext.getAttribute("authorizationService");
    }
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            String userSessionId = Arrays.stream(req.getCookies())
                    .filter(cookie -> cookie.getName().equals("user_session"))
                    .findFirst().get().getValue();
            System.out.println(userSessionId);
            authorizationService.logout(userSessionId);
        } catch (Exception e) {
            resp.sendError(500);
        }
        Cookie sessionCookie = new Cookie("user_session", "");
        sessionCookie.setMaxAge(0);
        resp.addCookie(sessionCookie);
        resp.sendRedirect("/");
    }
}
