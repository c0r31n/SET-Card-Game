package com.example.setcardgame.Model;

public enum Difficulty {
    EASY("easy"),
    NORMAL("normal");

    public final String label;

    Difficulty(String label) {
        this.label = label;
    }
}
