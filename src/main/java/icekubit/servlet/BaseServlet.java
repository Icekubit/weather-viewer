package icekubit.servlet;

import icekubit.entity.User;
import icekubit.service.AuthorizationService;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Stream;

public class BaseServlet extends HttpServlet {

    protected AuthorizationService authorizationService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        ServletContext servletContext = config.getServletContext();
        authorizationService = (AuthorizationService) servletContext.getAttribute("authorizationService");
    }

    protected Optional<User> getUserIfCookieSessionExist(HttpServletRequest req) {
        return Stream.ofNullable(req.getCookies())
                .flatMap(Arrays::stream)
                .filter(cook -> cook.getName().equals("user_session"))
                .findFirst()
                .flatMap(cook -> authorizationService.getUserForThisSession(cook.getValue()));
    }
}
