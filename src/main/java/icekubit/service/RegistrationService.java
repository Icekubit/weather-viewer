package icekubit.service;

import icekubit.dao.UserDao;
import icekubit.entity.User;
import icekubit.exception.*;
import icekubit.util.PasswordUtil;

public class RegistrationService {
    private final UserDao userDao;

    public RegistrationService(UserDao userDao) {
        this.userDao = userDao;
    }


    public void registerUser(String username, String password) {
        validateCredentials(username, password);
        User user = new User();
        user.setLogin(username);
        user.setPassword(PasswordUtil.hashPassword(password));
        // check if user exists - can't rely on case-sensitive username unique constraint
        if (userDao.findByLogin(username).isPresent()) {
            throw new UserAlreadyExistException("This user already exists");
        }
        userDao.save(user);
    }

    private void validateCredentials(String username, String password) {
        if (username.length() > 20) {
            throw new LoginIsTooLongException();
        }
        if (username.length() < 3) {
            throw new LoginIsTooShortException();
        }
        if (password.length() > 50) {
            throw new PasswordIsTooLongException();
        }
        if (password.length() < 8) {
            throw new PasswordIsTooShortException();
        }
    }

}
