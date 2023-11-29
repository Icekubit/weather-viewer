package icekubit.service;

import icekubit.dao.UserDao;
import icekubit.entity.User;
import icekubit.util.PasswordUtil;

public class RegistrationService {
    private final UserDao userDao;
    private final PasswordUtil passwordUtil;

    public RegistrationService(UserDao userDao, PasswordUtil passwordUtil) {
        this.userDao = userDao;
        this.passwordUtil = passwordUtil;
    }


    public int registerUser(String username, String password) {
        User user = new User();
        user.setLogin(username);
        user.setPassword(passwordUtil.hashPassword(password));
        return userDao.save(user);
    }
}
