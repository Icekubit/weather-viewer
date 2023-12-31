package icekubit.servlet;

import icekubit.dto.LocationDto;
import icekubit.entity.User;
import icekubit.service.UserWeatherService;
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
import java.util.List;
import java.util.Optional;

@WebServlet("/search_location")
public class SearchingLocationsServlet extends BaseServlet {

    private UserWeatherService userWeatherService;
    private TemplateEngine templateEngine;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        ServletContext servletContext = config.getServletContext();
        userWeatherService = (UserWeatherService) servletContext.getAttribute("userWeatherService");
        templateEngine = (TemplateEngine) servletContext.getAttribute("templateEngine");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Optional<User> userOptional = getUserIfCookieSessionExist(req);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            WebContext context = ThymeleafUtil.buildWebContext(req, resp, req.getServletContext());
            try {
                List<LocationDto> locations = userWeatherService
                        .searchLocationsByNameAndExcludeSaved(user, req.getParameter("searchQuery"));
                context.setVariable("username", user.getLogin());
                context.setVariable("searchQuery", req.getParameter("searchQuery"));
                context.setVariable("locations", locations);
                templateEngine.process("searching-location", context, resp.getWriter());
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        } else {
            resp.sendRedirect(req.getContextPath() + "/");
        }
    }
}
