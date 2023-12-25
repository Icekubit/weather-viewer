package icekubit.servlet;

import icekubit.entity.Location;
import icekubit.entity.User;
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
        userOptional.ifPresent(
                user -> userWeatherService.save(
                        Location.builder()
                                .name(req.getParameter("name"))
                                .latitude(Double.parseDouble(req.getParameter("latitude")))
                                .longitude(Double.parseDouble(req.getParameter("longitude")))
                                .user(user)
                                .build()
                )
        );
        resp.sendRedirect(req.getContextPath() + "/");

    }
}
