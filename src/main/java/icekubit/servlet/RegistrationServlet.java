package icekubit.servlet;

import icekubit.exception.UserAlreadyExistException;
import icekubit.service.AuthorisationService;
import icekubit.service.UserService;
import icekubit.util.ThymeleafUtil;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import java.io.IOException;

@WebServlet("/registration")
public class RegistrationServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        TemplateEngine templateEngine = (TemplateEngine) req.getServletContext().getAttribute("templateEngine");
        WebContext context = ThymeleafUtil.buildWebContext(req, resp, req.getServletContext());
        context.setVariable("isUserAlreadyExist", false);
        templateEngine.process("registration", context, resp.getWriter());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        TemplateEngine templateEngine = (TemplateEngine) req.getServletContext().getAttribute("templateEngine");
        WebContext context = ThymeleafUtil.buildWebContext(req, resp, req.getServletContext());

        String username = req.getParameter("username");
        String password = req.getParameter("password");
        try {
            UserService.getInstance().save(username, password);
            String sessionId = AuthorisationService.getInstance().authoriseUser(username, password);
            resp.addCookie(new Cookie("user_session", sessionId));
            resp.sendRedirect("/");
        } catch (UserAlreadyExistException e) {
            context.setVariable("isUserAlreadyExist", true);
            templateEngine.process("registration", context, resp.getWriter());
        }
    }
}
