package icekubit.servlet;

import icekubit.dto.LocationDto;
import icekubit.exception.UserAlreadyExistException;
import icekubit.service.WeatherService;
import icekubit.util.ThymeleafUtil;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import java.io.IOException;
import java.util.List;

@WebServlet("/search_location")
public class SearchingLocationsServlet extends HttpServlet {

    private WeatherService weatherService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        ServletContext servletContext = config.getServletContext();
        weatherService = (WeatherService) servletContext.getAttribute("weatherService");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        TemplateEngine templateEngine = (TemplateEngine) req.getServletContext().getAttribute("templateEngine");
        WebContext context = ThymeleafUtil.buildWebContext(req, resp, req.getServletContext());
        List<LocationDto> locations = null;

        try {
            locations = weatherService.searchLocationsByName(req.getParameter("location"));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        context.setVariable("locations", locations);

        templateEngine.process("searching-location", context, resp.getWriter());
    }
}
