package icekubit.service;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordService {
    private static PasswordService instance;
    private static final String salt = "$2a$10$6Eph/Z5xgF6r4nOWWqGHge";
    private PasswordService() {

    }
    public static PasswordService getInstance() {
        if (instance == null) {
            instance = new PasswordService();
        }
        return instance;
    }

    public boolean checkPassword(String passwordFromDatabase, String userInputPassword) {
        return BCrypt.hashpw(userInputPassword, salt).equals(passwordFromDatabase);
    }

    public String hashPassword(String password) {
        return BCrypt.hashpw(password, salt);
    }
}
