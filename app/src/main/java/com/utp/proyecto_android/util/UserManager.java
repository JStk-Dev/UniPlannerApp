package com.utp.proyecto_android.util;

import java.util.ArrayList;
import java.util.List;

public class UserManager {
    private static UserManager instance;

    // Lista de usuarios registrados
    private List<User> userList;

    private UserManager() {
        userList = new ArrayList<User>();
    }

    public static UserManager getInstance() {
        if (instance == null) {
            instance = new UserManager();
        }
        return instance;
    }

    public void addUser(User user) {
        userList.add(user);
    }

    /**
     * Valida las credenciales del usuario
     * @param email "String" - Email del usuario
     * @param password "String" - Password del usuario
     * @return "boolean" - True si el usuario existe. De lo contrario retorna False.
     */
    public boolean isValidCredentials(String email, String password) {
        for (User user : userList) {
            if (user.getEmail().equals(email) && user.getPassword().equals(password)) {
                return true;
            }
        }
        return false;
    }
}

