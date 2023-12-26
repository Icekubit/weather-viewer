package icekubit.servlet;

import icekubit.entity.Location;
import icekubit.entity.User;
import icekubit.exception.LocationAlreadyExistsException;
import icekubit.service.UserWeatherService;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Optional;

@WebServlet("/add_location")
public class AddLocationServlet extends BaseServlet {

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
            Location location = Location.builder()
                    .name(req.getParameter("name"))
                    .latitude(new BigDecimal(req.getParameter("latitude")))
                    .longitude(new BigDecimal(req.getParameter("longitude")))
                    .user(userOptional.get())
                    .build();
            try {
                userWeatherService.save(location);
            } catch (LocationAlreadyExistsException e) {
                throw new RuntimeException();
            }
        }
        resp.sendRedirect(req.getContextPath() + "/");

    }
}
