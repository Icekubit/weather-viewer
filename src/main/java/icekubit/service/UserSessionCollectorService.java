package icekubit.service;

import icekubit.dao.UserSessionDao;

import java.time.LocalDateTime;

public class UserSessionCollectorService {
    private static UserSessionCollectorService instance;
    private UserSessionCollectorService() {}
    public static UserSessionCollectorService getInstance() {
        if (instance == null) {
            instance = new UserSessionCollectorService();
        }
        return instance;
    }
    public void removeExpiredUserSessions() {
        UserSessionDao.getInstance().deleteExpiredUserSessions(LocalDateTime.now());
    }
}
