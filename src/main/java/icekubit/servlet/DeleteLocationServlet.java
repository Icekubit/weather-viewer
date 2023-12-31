package icekubit.servlet;

import icekubit.entity.User;
import icekubit.exception.ForbiddenActionException;
import icekubit.service.UserWeatherService;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Optional;

@WebServlet("/delete_location")
public class DeleteLocationServlet extends BaseServlet {
    private UserWeatherService userWeatherService;
    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        ServletContext servletContext = config.getServletContext();
        userWeatherService = (UserWeatherService) servletContext.getAttribute("userWeatherService");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Optional<User> userOptional = getUserIfCookieSessionExist(req);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            int locationId = Integer.parseInt(req.getParameter("locationId"));
            try {
                userWeatherService.deleteLocation(locationId, user);
                resp.sendRedirect(req.getContextPath() + "/");
            } catch (ForbiddenActionException e) {
                resp.sendRedirect(req.getContextPath() + "/forbidden");
            }
        } else {
            resp.sendRedirect(req.getContextPath() + "/");
        }
    }
}
