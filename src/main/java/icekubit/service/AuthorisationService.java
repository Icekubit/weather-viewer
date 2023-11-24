package icekubit.service;

import icekubit.dao.SessionDao;
import icekubit.dao.UserDao;
import icekubit.entity.User;
import icekubit.exception.InvalidPasswordException;
import icekubit.exception.NoSuchUserException;

import java.util.Optional;

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
            SessionDao.getInstance().delete(user.getUserSession());
        }
        return SessionService.getInstance().createSession(user.getId());
    }
}
