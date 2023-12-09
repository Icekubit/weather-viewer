package icekubit.servlet;

import icekubit.entity.Location;
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
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Stream;

@WebServlet("/add_location")
public class AddLocationServlet extends BaseServlet {

//    private AuthorizationService authorizationService;
    private UserWeatherService userWeatherService;
    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        ServletContext servletContext = config.getServletContext();
//        authorizationService = (AuthorizationService) servletContext.getAttribute("authorizationService");
        userWeatherService = (UserWeatherService) servletContext.getAttribute("userWeatherService");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        getUserIfCookieSessionExist(req).ifPresent(
                user -> userWeatherService.save(
                        Location.builder()
                                .name(req.getParameter("name"))
                                .latitude(new BigDecimal(req.getParameter("latitude")))
                                .longitude(new BigDecimal(req.getParameter("longitude")))
                                .user(user)
                                .build()
                )
        );
        resp.sendRedirect("/");

    }
}
