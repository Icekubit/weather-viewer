package icekubit.servlet;

import icekubit.service.AuthorisationService;
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
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            String userSessionId = Arrays.stream(req.getCookies())
                    .filter(cookie -> cookie.getName().equals("user_session"))
                    .findFirst().get().getValue();
            System.out.println(userSessionId);
            AuthorisationService.getInstance().logout(userSessionId);
        } catch (Exception e) {
            resp.sendError(500);
        }
        Cookie sessionCookie = new Cookie("user_session", "");
        sessionCookie.setMaxAge(0);
        resp.addCookie(sessionCookie);
        resp.sendRedirect("/");
    }
}
