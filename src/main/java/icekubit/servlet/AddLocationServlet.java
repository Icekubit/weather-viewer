package icekubit.servlet;

import icekubit.dao.LocationDao;
import icekubit.entity.Location;
import icekubit.entity.User;
import icekubit.service.AuthorizationService;
import icekubit.service.WeatherService;
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

@WebServlet("/add_location")
public class AddLocationServlet extends HttpServlet {

    private AuthorizationService authorizationService;
    private WeatherService weatherService;
    @Override
    public void init(ServletConfig config) throws ServletException {
        ServletContext servletContext = config.getServletContext();
        authorizationService = (AuthorizationService) servletContext.getAttribute("authorizationService");
        weatherService = (WeatherService) servletContext.getAttribute("weatherService");
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
                }
            }
        }
        Location location = Location
                .builder()
                .name(req.getParameter("name"))
                .latitude(Double.parseDouble(req.getParameter("latitude")))
                .longitude(Double.parseDouble(req.getParameter("longitude")))
                .user(user)
                .build();
        weatherService.save(location);
        resp.sendRedirect("/");
    }
}
