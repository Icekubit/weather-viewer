package icekubit.servlet;

import icekubit.entity.Location;
import icekubit.entity.User;
import icekubit.exception.UnauthorizedActionException;
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
import java.util.NoSuchElementException;
import java.util.Optional;

@WebServlet("/delete_location")
public class DeleteLocationServlet extends BaseServlet {
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
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Optional<User> userOptional = getUserIfCookieSessionExist(req);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            int locationId = Integer.parseInt(req.getParameter("locationId"));
            Optional<Location> locationOptional = userWeatherService.findLocationById(locationId);

            if (locationOptional.isPresent() &&
                    locationOptional.get().getUser().getId() == user.getId()) {
                userWeatherService.deleteLocation(locationId);
                resp.sendRedirect(req.getContextPath() + "/");
            } else {
                resp.sendRedirect(req.getContextPath() + "/forbidden");
            }
        } else {
            resp.sendRedirect(req.getContextPath() + "/");
        }
    }
}
