package icekubit.servlet;


import icekubit.entity.User;
import icekubit.util.HibernateUtil;
import icekubit.util.ThymeleafUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import java.io.IOException;
import java.util.List;

@WebServlet("/test")
public class TestServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        TemplateEngine templateEngine = (TemplateEngine) req.getServletContext().getAttribute("templateEngine");
        WebContext context = ThymeleafUtil.buildWebContext(req, resp, req.getServletContext());
        List<String> message = List.of("привет", "из", "тестового", "сервлета");
        context.setVariable("message", message);

        User testUser = new User();
        testUser.setLogin("Test_user3456");
        testUser.setPassword("1234");


        Session session = HibernateUtil.getSessionFactory().openSession();
        try (session) {
            Transaction transaction = session.beginTransaction();

            session.persist(testUser);

            transaction.commit();
        } catch (Exception e) {
            throw e;
        }

        templateEngine.process("test-thymeleaf", context, resp.getWriter());

    }
}
