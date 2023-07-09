package com.utp.proyecto_android.util;

public class User {

    private String name, email, password;
    private int idGender;

    public User(String name, int idGender, String email, String password) {
        this.name = name;
        this.idGender = idGender;
        this.email = email;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public String getValueGender() {
        switch (idGender) {
            case 1: return "Male";
            case 2: return "Female";
        }
        return "Select a option";
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
