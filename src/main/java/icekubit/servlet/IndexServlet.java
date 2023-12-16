package icekubit.servlet;

import icekubit.dto.WeatherDto;
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

@WebServlet("")
public class IndexServlet extends BaseServlet {
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
        WebContext context = ThymeleafUtil.buildWebContext(req, resp, req.getServletContext());
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            String username = user.getLogin();
            context.setVariable("username", username);
            try {
                List<WeatherDto> userLocations = userWeatherService.getUserLocations(user);
                context.setVariable("userLocations", userLocations);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        templateEngine.process("index", context, resp.getWriter());

    }
}
