package icekubit.servlet;

import icekubit.entity.User;
import icekubit.util.ThymeleafUtil;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import java.io.IOException;
import java.util.Optional;

@WebServlet("/forbidden")
public class ForbiddenServlet extends BaseServlet {
    private TemplateEngine templateEngine;
    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        ServletContext servletContext = config.getServletContext();
        templateEngine = (TemplateEngine) servletContext.getAttribute("templateEngine");

    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Optional<User> userOptional = getUserIfCookieSessionExist(req);
        WebContext context = ThymeleafUtil.buildWebContext(req, resp, req.getServletContext());
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            context.setVariable("username", user.getLogin());

            resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
            templateEngine.process("forbidden-action", context, resp.getWriter());
            } else {
            resp.sendRedirect(req.getContextPath() + "/");
        }
    }
}
