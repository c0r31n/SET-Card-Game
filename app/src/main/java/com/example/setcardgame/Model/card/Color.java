package com.example.setcardgame.Model.card;

public enum Color {
    GREEN("g"),
    RED("r"),
    PURPLE("p");

    public final String label;

    Color(String label) {
        this.label = label;
    }

    public static Color getColorFromString(String COLOR){
        if (COLOR.equals("GREEN")){
            return Color.GREEN;
        }
        else if (COLOR.equals("RED")){
            return Color.RED;
        }

        return Color.PURPLE;
    }
}
