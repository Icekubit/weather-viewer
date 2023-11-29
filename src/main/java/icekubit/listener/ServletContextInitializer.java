package icekubit.listener;

import icekubit.dao.UserDao;
import icekubit.dao.UserSessionDao;
import icekubit.service.AuthorizationService;
import icekubit.util.PasswordUtil;
import icekubit.service.RegistrationService;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class ServletContextInitializer implements ServletContextListener {
    private UserSessionDao userSessionDao;
    private UserDao userDao;
    private PasswordUtil passwordUtil;
    private AuthorizationService authorizationService;
    private RegistrationService registrationService;
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext servletContext = sce.getServletContext();

        userSessionDao = new UserSessionDao();
        userDao = new UserDao();
        passwordUtil = new PasswordUtil();
        authorizationService = new AuthorizationService(userSessionDao, userDao, passwordUtil);
        registrationService = new RegistrationService(userDao, passwordUtil);

        servletContext.setAttribute("authorizationService", authorizationService);
        servletContext.setAttribute("registrationService", registrationService);
    }
}
