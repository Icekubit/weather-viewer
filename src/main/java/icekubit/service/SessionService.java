package icekubit.service;

import icekubit.dao.UserSessionDao;
import icekubit.dao.UserDao;
import icekubit.entity.UserSession;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

public class SessionService {
    private static SessionService instance;
    private SessionService() {}
    public static SessionService getInstance() {
        if (instance == null) {
            instance = new SessionService();
        }
        return instance;
    }

    public String createSession(int userId) {
        UserSession userSession = new UserSession();
        userSession.setUser(UserDao.getInstance().getUserById(userId).get());
        userSession.setExpiresAt(LocalDateTime.now().plusHours(1));
        return UserSessionDao.getInstance().save(userSession);
    }

    public boolean isAuthorised(String uuid) {
        Optional<UserSession> optionalSession = UserSessionDao.getInstance().findById(UUID.fromString(uuid));
        if (optionalSession.isEmpty()) {
            return false;
        } else {
            UserSession userSession = optionalSession.get();
            return userSession.getExpiresAt().isAfter(LocalDateTime.now());
        }
    }
}
