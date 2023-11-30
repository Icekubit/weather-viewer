package icekubit.servlet;

import icekubit.exception.InvalidPasswordException;
import icekubit.exception.NoSuchUserException;
import icekubit.service.AuthorizationService;
import icekubit.util.PropertiesUtil;
import icekubit.util.ThymeleafUtil;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import java.io.IOException;
import java.util.UUID;

@WebServlet("/authorization")
public class AuthorizationServlet extends HttpServlet {
    private AuthorizationService authorizationService;
    private static final Integer SESSION_DURATION = Integer.parseInt(PropertiesUtil.get("session.duration"));

    @Override
    public void init(ServletConfig config) {
        ServletContext servletContext = config.getServletContext();
        authorizationService = (AuthorizationService) servletContext.getAttribute("authorizationService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        TemplateEngine templateEngine = (TemplateEngine) req.getServletContext().getAttribute("templateEngine");
        WebContext context = ThymeleafUtil.buildWebContext(req, resp, req.getServletContext());
        templateEngine.process("authorization", context, resp.getWriter());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        TemplateEngine templateEngine = (TemplateEngine) req.getServletContext().getAttribute("templateEngine");
        WebContext context = ThymeleafUtil.buildWebContext(req, resp, req.getServletContext());
        try {
            UUID sessionId = authorizationService.authorizeUser(username, password);
            Cookie sessionCookie = new Cookie("user_session", sessionId.toString());
            sessionCookie.setMaxAge(SESSION_DURATION);
            resp.addCookie(sessionCookie);
            resp.sendRedirect("/");
        } catch (NoSuchUserException e1) {
            context.setVariable("invalidUser", true);
            templateEngine.process("authorization", context, resp.getWriter());
        } catch (InvalidPasswordException e2) {
            context.setVariable("invalidPassword", true);
            templateEngine.process("authorization", context, resp.getWriter());
        }
    }
}