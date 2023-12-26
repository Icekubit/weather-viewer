package icekubit.service;

import icekubit.dao.UserSessionDao;
import icekubit.dao.UserDao;
import icekubit.entity.User;
import icekubit.entity.UserSession;
import icekubit.exception.InvalidPasswordException;
import icekubit.exception.NoSuchUserException;
import icekubit.util.PasswordUtil;
import icekubit.util.PropertiesUtil;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

public class AuthorizationService {

    private final UserSessionDao userSessionDao;
    private final UserDao userDao;
    private static final Long SESSION_DURATION = Long.parseLong(PropertiesUtil.get("session.duration"));

    public AuthorizationService(UserSessionDao userSessionDao, UserDao userDao) {
        this.userSessionDao = userSessionDao;
        this.userDao = userDao;
    }


    public UUID assignSessionToUser(String username, String password) {
        Optional<User> userOptional = userDao.findByLogin(username);
        if (userOptional.isEmpty()) {
            throw new NoSuchUserException();
        } else if (!PasswordUtil.checkPassword(userOptional.get().getPassword(), password)) {
            throw new InvalidPasswordException();
        }
        User user = userOptional.get();
        return saveSession(user.getId());
    }

    public Optional<User> getUserForThisSession(String uuid) {
        Optional<UserSession> optionalSession = userSessionDao.findById(UUID.fromString(uuid));
        if (optionalSession.isPresent() && optionalSession.get().getExpiresAt().isAfter(LocalDateTime.now())) {
            return Optional.of(optionalSession.get().getUser());
        }
        return Optional.empty();
    }

    public void logout(User user) {
        userSessionDao.deleteByUserId(user.getId());
    }

    private UUID saveSession(int userId) {
        UserSession userSession = new UserSession();
        userSession.setUser(userDao.findById(userId).get());
        userSession.setExpiresAt(LocalDateTime.now().plusSeconds(SESSION_DURATION));
        return userSessionDao.save(userSession);
    }

}


























