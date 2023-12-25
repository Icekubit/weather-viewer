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
        User user = User.builder()
                .login(username)
                .password(PasswordUtil.hashPassword(password))
                .build();
        userDao.save(user);
    }

    private void validateCredentials(String username, String password) {
        if (username.length() < 3) {
            throw new LoginIsTooShortException();
        } else if (username.length() > 20) {
            throw new LoginIsTooLongException();
        }

        if (password.length() < 8) {
            throw new PasswordIsTooShortException();
        } else if (password.length() > 50) {
            throw new PasswordIsTooLongException();
        }
    }

}
