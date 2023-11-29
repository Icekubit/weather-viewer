package icekubit.listener;

import icekubit.dao.UserSessionDao;
import icekubit.util.PropertiesUtil;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

import java.time.LocalDateTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@WebListener
public class UserSessionCollector implements ServletContextListener {
    private ScheduledExecutorService scheduler;
    private UserSessionDao userSessionDao;
    private static final Long SESSION_CLEANUP_INTERVAL
            = Long.parseLong(PropertiesUtil.get("session.cleanup.interval"));

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        userSessionDao = new UserSessionDao();
        scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(() -> userSessionDao.deleteExpiredUserSessions(LocalDateTime.now())
                , 0, SESSION_CLEANUP_INTERVAL, TimeUnit.SECONDS);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        scheduler.shutdown();
    }
}