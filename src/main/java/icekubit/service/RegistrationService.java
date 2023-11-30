package icekubit.service;

import icekubit.dao.UserDao;
import icekubit.entity.User;
import icekubit.util.PasswordUtil;

public class RegistrationService {
    private final UserDao userDao;

    public RegistrationService(UserDao userDao) {
        this.userDao = userDao;
    }


    public int registerUser(String username, String password) {
        User user = new User();
        user.setLogin(username);
        user.setPassword(PasswordUtil.hashPassword(password));
        return userDao.save(user);
    }
}
