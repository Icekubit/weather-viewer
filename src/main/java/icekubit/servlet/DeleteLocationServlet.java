package icekubit.servlet;

import icekubit.entity.User;
import icekubit.service.AuthorizationService;
import icekubit.service.UserWeatherService;
import icekubit.service.WeatherApiService;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Arrays;
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
            userWeatherService.deleteLocation(user, locationId);
        }
        resp.sendRedirect("/");
    }
}
