package icekubit.service;

import icekubit.dao.SessionDao;
import icekubit.dao.UserDao;
import icekubit.entity.Session;
import icekubit.entity.User;

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
        Session session = new Session();
        session.setUser(UserDao.getInstance().getUserById(userId).get());
        session.setExpiresAt(LocalDateTime.now().plusHours(1));
        return SessionDao.getInstance().save(session);
    }

    public boolean isAuthorised(String uuid) {
        Optional<Session> optionalSession = SessionDao.getInstance().findById(UUID.fromString(uuid));
        if (optionalSession.isEmpty()) {
            return false;
        } else {
            Session session = optionalSession.get();
            return session.getExpiresAt().isAfter(LocalDateTime.now());
        }
    }
}
