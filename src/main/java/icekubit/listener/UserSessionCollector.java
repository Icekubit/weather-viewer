package icekubit.listener;

import icekubit.service.UserSessionCollectorService;
import icekubit.util.PropertiesUtil;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@WebListener
public class UserSessionCollector implements ServletContextListener {
    private ScheduledExecutorService scheduler;
    private static final Long SESSION_CLEANUP_INTERVAL
            = Long.parseLong(PropertiesUtil.get("session.cleanup.interval"));

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(() -> UserSessionCollectorService.getInstance().removeExpiredUserSessions()
                , 0, SESSION_CLEANUP_INTERVAL, TimeUnit.SECONDS);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        scheduler.shutdown();
    }
}