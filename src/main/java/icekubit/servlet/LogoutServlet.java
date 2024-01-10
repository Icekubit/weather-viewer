package icekubit.servlet;

import icekubit.entity.User;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;


@WebServlet("/logout")
public class LogoutServlet extends BaseServlet {

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Optional<User> userOptional = getUserIfCookieSessionExist(req);
        if (userOptional.isPresent()) {
            String sessionId = Arrays.stream(req.getCookies())
                    .filter(cookie -> cookie.getName().equals("user_session"))
                    .findFirst()
                    .get()
                    .getValue();
            authorizationService.logout(UUID.fromString(sessionId));
            Cookie sessionCookie = new Cookie("user_session", "");
            sessionCookie.setMaxAge(0);
            resp.addCookie(sessionCookie);
        }
        resp.sendRedirect(req.getContextPath() + "/");
    }
}
