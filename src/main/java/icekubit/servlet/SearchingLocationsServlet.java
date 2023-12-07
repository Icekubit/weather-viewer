package icekubit.servlet;

import icekubit.dto.LocationDto;
import icekubit.entity.User;
import icekubit.service.AuthorizationService;
import icekubit.service.UserWeatherService;
import icekubit.util.ThymeleafUtil;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@WebServlet("/search_location")
public class SearchingLocationsServlet extends HttpServlet {

    private AuthorizationService authorizationService;
    private UserWeatherService userWeatherService;

    @Override
    public void init(ServletConfig config)  {
        ServletContext servletContext = config.getServletContext();
        authorizationService = (AuthorizationService) servletContext.getAttribute("authorizationService");
        userWeatherService = (UserWeatherService) servletContext.getAttribute("userWeatherService");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Cookie[] cookies = req.getCookies();
        User user = null;
        if (cookies != null) {
            Optional<Cookie> cookieOptional = Arrays.stream(cookies)
                    .filter(cookie -> cookie.getName().equals("user_session"))
                    .findFirst();
            if (cookieOptional.isPresent()) {
                Optional<User> userOptional = authorizationService.getUserForThisSession(cookieOptional.get().getValue());
                if (userOptional.isPresent()) {
                    user = userOptional.get();

                    TemplateEngine templateEngine = (TemplateEngine) req.getServletContext().getAttribute("templateEngine");
                    WebContext context = ThymeleafUtil.buildWebContext(req, resp, req.getServletContext());
                    List<LocationDto> locations = null;
                    try {
                        locations = userWeatherService
                                .searchLocationsByNameAndExcludeSaved(user, req.getParameter("location"));
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    context.setVariable("locations", locations);
                    templateEngine.process("searching-location", context, resp.getWriter());
                } else {
                    resp.sendRedirect("/");
                }
            } else {
                resp.sendRedirect("/");
            }
        } else {
            resp.sendRedirect("/");
        }

    }
}
