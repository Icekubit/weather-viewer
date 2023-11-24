package icekubit.service;

import icekubit.dao.UserSessionDao;
import icekubit.dao.UserDao;
import icekubit.entity.User;
import icekubit.entity.UserSession;
import icekubit.exception.InvalidPasswordException;
import icekubit.exception.NoSuchUserException;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

public class AuthorisationService {
    private static AuthorisationService instance;
    private AuthorisationService() {}
    public static AuthorisationService getInstance() {
        if (instance == null) {
            instance = new AuthorisationService();
        }
        return instance;
    }

    public String authoriseUser(String username, String password) {
        Optional<User> userOptional = UserDao.getInstance().getUserByUsername(username);
        if (userOptional.isEmpty()) {
            throw new NoSuchUserException();
        } else if (!userOptional.get().getPassword().equals(password)) {
            throw new InvalidPasswordException();
        }
        User user = userOptional.get();
        if (user.getUserSession() != null) {
            UserSessionDao.getInstance().delete(user.getUserSession().getId());
        }
        return createSession(user.getId());
    }

    public Optional<User> getUserForThisSession(String uuid) {
        Optional<UserSession> optionalSession = UserSessionDao.getInstance().findById(UUID.fromString(uuid));
        if (optionalSession.isPresent() && optionalSession.get().getExpiresAt().isAfter(LocalDateTime.now())) {
            return Optional.of(optionalSession.get().getUser());
        }
        return Optional.empty();
    }

    public void logout(String userSessionId) {
        UserSessionDao.getInstance().delete(UUID.fromString(userSessionId));
    }

    private String createSession(int userId) {
        UserSession userSession = new UserSession();
        userSession.setUser(UserDao.getInstance().getUserById(userId).get());
        userSession.setExpiresAt(LocalDateTime.now().plusHours(1));
        return UserSessionDao.getInstance().save(userSession);
    }
}
