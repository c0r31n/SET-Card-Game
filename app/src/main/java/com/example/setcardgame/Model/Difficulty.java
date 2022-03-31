package com.example.setcardgame.Model;

public enum Difficulty {
    EASY("Easy"),
    NORMAL("Normal");

    public final String label;

    Difficulty(String label) {
        this.label = label;
    }

    public static Difficulty getDifficultyFromString(String label) {
        if (label.equals(EASY.toString())) {
            return EASY;
        }

        return NORMAL;
    }

    @Override
    public String toString() {
        return label;
    }
}
