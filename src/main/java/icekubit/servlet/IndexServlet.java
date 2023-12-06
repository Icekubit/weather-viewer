package icekubit.servlet;

import icekubit.dao.LocationDao;
import icekubit.dto.WeatherDto;
import icekubit.entity.Location;
import icekubit.entity.User;
import icekubit.service.AuthorizationService;
import icekubit.service.WeatherService;
import icekubit.util.ThymeleafUtil;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.hibernate.LazyInitializationException;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@WebServlet("")
public class IndexServlet extends HttpServlet {

    private AuthorizationService authorizationService;
    private WeatherService weatherService;

    @Override
    public void init(ServletConfig config) {
        ServletContext servletContext = config.getServletContext();
        authorizationService = (AuthorizationService) servletContext.getAttribute("authorizationService");
        weatherService = (WeatherService) servletContext.getAttribute("weatherService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Cookie[] cookies = req.getCookies();
        User user = null;
        String username = null;
        List<WeatherDto> userLocations = null;
        if (cookies != null) {
            Optional<Cookie> cookieOptional = Arrays.stream(cookies)
                    .filter(cookie -> cookie.getName().equals("user_session"))
                    .findFirst();
            if (cookieOptional.isPresent()) {
                Optional<User> userOptional = authorizationService.getUserForThisSession(cookieOptional.get().getValue());
                if (userOptional.isPresent()) {
                    user = userOptional.get();
                    username = user.getLogin();
                    try {
                        userLocations = weatherService.getUserLocations(user);
                        System.out.println(userLocations);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
        TemplateEngine templateEngine = (TemplateEngine) req.getServletContext().getAttribute("templateEngine");
        WebContext context = ThymeleafUtil.buildWebContext(req, resp, req.getServletContext());


        context.setVariable("username", username);
        context.setVariable("userLocations", userLocations);
        templateEngine.process("index", context, resp.getWriter());

    }
}
