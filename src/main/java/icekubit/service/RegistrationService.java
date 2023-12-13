package icekubit.service;

import icekubit.dao.UserDao;
import icekubit.entity.User;
import icekubit.exception.UserAlreadyExistException;
import icekubit.util.PasswordUtil;

public class RegistrationService {
    private final UserDao userDao;

    public RegistrationService(UserDao userDao) {
        this.userDao = userDao;
    }


    public void registerUser(String username, String password) {
        User user = new User();
        user.setLogin(username);
        user.setPassword(PasswordUtil.hashPassword(password));
        // check if user exists - can't rely on case-sensitive username unique constraint
        if (userDao.findByLogin(username).isPresent()) {
            throw new UserAlreadyExistException("This user already exists");
        }
        userDao.save(user);
    }
}
