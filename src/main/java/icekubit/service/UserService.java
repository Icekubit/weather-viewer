package icekubit.service;

import icekubit.dao.UserDao;
import icekubit.entity.User;

public class UserService {
    private static UserService instance;
    private UserService() {}
    public static UserService getInstance() {
        if (instance == null) {
            instance = new UserService();
        }
        return instance;
    }


    public int save(String username, String password) {
        User user = new User();
        user.setLogin(username);
        user.setPassword(password);
        return UserDao.getInstance().save(user);
    }
}
