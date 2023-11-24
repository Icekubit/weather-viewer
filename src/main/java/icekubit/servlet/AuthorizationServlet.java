package icekubit.servlet;

import icekubit.exception.InvalidPasswordException;
import icekubit.exception.NoSuchUserException;
import icekubit.service.AuthorisationService;
import icekubit.util.ThymeleafUtil;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import java.io.IOException;

@WebServlet("/authorization")
public class AuthorizationServlet extends HttpServlet {
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
            String sessionId = AuthorisationService.getInstance().authoriseUser(username, password);
            Cookie sessionCookie = new Cookie("user_session", sessionId);
            sessionCookie.setMaxAge(60 * 60);
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