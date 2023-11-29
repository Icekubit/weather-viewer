package icekubit.util;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordUtil {
    private static final String salt = "$2a$10$6Eph/Z5xgF6r4nOWWqGHge";

    public boolean checkPassword(String passwordFromDatabase, String userInputPassword) {
        return BCrypt.hashpw(userInputPassword, salt).equals(passwordFromDatabase);
    }

    public String hashPassword(String password) {
        return BCrypt.hashpw(password, salt);
    }
}
