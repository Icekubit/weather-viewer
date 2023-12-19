package icekubit.util;


import at.favre.lib.crypto.bcrypt.BCrypt;

public class PasswordUtil {

    public static boolean checkPassword(String passwordFromDatabase, String userInputPassword) {
        return BCrypt.verifyer().verify(userInputPassword.toCharArray(), passwordFromDatabase).verified;
    }

    public static String hashPassword(String password) {
        return BCrypt.withDefaults().hashToString(12, password.toCharArray());
    }
}
