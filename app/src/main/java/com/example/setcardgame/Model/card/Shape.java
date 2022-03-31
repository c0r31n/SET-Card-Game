package com.example.setcardgame.Model.card;

public enum Shape {
    DIAMOND("d"),
    WAVY("s"),
    OVAL("p");

    public final String label;

    Shape(String label) {
        this.label = label;
    }

    public static Shape getShapeFromString(String SHAPE) {
        if (SHAPE.equals("DIAMOND")) {
            return Shape.DIAMOND;
        } else if (SHAPE.equals("WAVY")) {
            return Shape.WAVY;
        }

        return Shape.OVAL;
    }
}
