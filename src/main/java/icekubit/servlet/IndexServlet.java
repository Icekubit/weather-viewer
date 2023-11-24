package icekubit.servlet;

import icekubit.entity.User;
import icekubit.service.AuthorisationService;
import icekubit.service.SessionService;
import icekubit.util.ThymeleafUtil;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

@WebServlet("")
public class IndexServlet extends HttpServlet {


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Cookie[] cookies = req.getCookies();
        String username = null;
        if (cookies != null) {
            Optional<Cookie> cookieOptional = Arrays.stream(cookies)
                    .filter(cookie -> cookie.getName().equals("user_session"))
                    .findFirst();
            if (cookieOptional.isPresent()) {
                Optional<User> userOptional = AuthorisationService.getInstance().getUserForThisSession(cookieOptional.get().getValue());
                if (userOptional.isPresent()) {
                    username = userOptional.get().getLogin();
                }
            }
        }
        TemplateEngine templateEngine = (TemplateEngine) req.getServletContext().getAttribute("templateEngine");
        WebContext context = ThymeleafUtil.buildWebContext(req, resp, req.getServletContext());
        System.out.println(username);
        context.setVariable("username", username);
        templateEngine.process("index", context, resp.getWriter());

    }
}
