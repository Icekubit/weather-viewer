package icekubit.servlet;

import icekubit.entity.User;
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
import java.util.Optional;



@WebServlet("/logout")
public class LogoutServlet extends BaseServlet {

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Optional<User> userOptional = getUserIfCookieSessionExist(req);
        if (userOptional.isPresent()) {
            authorizationService.logout(userOptional.get());
            Cookie sessionCookie = new Cookie("user_session", "");
            sessionCookie.setMaxAge(0);
            resp.addCookie(sessionCookie);
        }
        resp.sendRedirect("/");
    }
}
