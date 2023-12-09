package icekubit.servlet;

import icekubit.exception.UserAlreadyExistException;
import icekubit.service.AuthorizationService;
import icekubit.service.RegistrationService;
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
import java.util.Arrays;
import java.util.UUID;

@WebServlet("/registration")
public class RegistrationServlet extends BaseServlet {
    private RegistrationService registrationService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        ServletContext servletContext = config.getServletContext();
        registrationService = (RegistrationService) servletContext.getAttribute("registrationService");
    }
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
            registrationService.registerUser(username, password);
            UUID sessionId = authorizationService.authorizeUser(username, password);
            resp.addCookie(new Cookie("user_session", sessionId.toString()));
            resp.sendRedirect("/");
        } catch (UserAlreadyExistException e) {
            context.setVariable("isUserAlreadyExist", true);
            templateEngine.process("registration", context, resp.getWriter());
        }
    }
}
