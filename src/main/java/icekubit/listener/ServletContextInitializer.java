package icekubit.listener;

import icekubit.dao.LocationDao;
import icekubit.dao.UserDao;
import icekubit.dao.UserSessionDao;
import icekubit.service.AuthorizationService;
import icekubit.service.UserWeatherService;
import icekubit.service.WeatherApiService;
import icekubit.service.RegistrationService;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class ServletContextInitializer implements ServletContextListener {
    private UserSessionDao userSessionDao;
    private UserDao userDao;
    private LocationDao locationDao;
    private AuthorizationService authorizationService;
    private RegistrationService registrationService;
    private UserWeatherService userWeatherService;
    private WeatherApiService weatherApiService;


    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext servletContext = sce.getServletContext();

        userSessionDao = new UserSessionDao();
        userDao = new UserDao();
        locationDao = new LocationDao();
        authorizationService = new AuthorizationService(userSessionDao, userDao);
        registrationService = new RegistrationService(userDao);
        weatherApiService = new WeatherApiService();
        userWeatherService = new UserWeatherService(locationDao, weatherApiService);

        servletContext.setAttribute("authorizationService", authorizationService);
        servletContext.setAttribute("registrationService", registrationService);
        servletContext.setAttribute("weatherService", weatherApiService);
        servletContext.setAttribute("userWeatherService", userWeatherService);
    }
}
