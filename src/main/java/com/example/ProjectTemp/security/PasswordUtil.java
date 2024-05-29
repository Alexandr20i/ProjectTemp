package com.example.ProjectTemp.security;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordUtil {

    // Метод для хэширования пароля
    public static String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt(12));
    }

    // Метод для проверки пароля
    public static boolean checkPassword(String plainPassword, String hashedPassword) {
        return BCrypt.checkpw(plainPassword, hashedPassword);
    }
}
