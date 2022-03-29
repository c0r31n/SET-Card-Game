package com.example.setcardgame.Model;

public class Username {
    private static String username;

    public static void setUsername(String username) {
        Username.username = username;
    }

    public static String getUsername() {
        return username;
    }
}
