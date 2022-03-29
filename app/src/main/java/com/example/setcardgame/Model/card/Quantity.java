package com.example.setcardgame.Model.card;

public enum Quantity {
    ONE("1"),
    TWO("2"),
    THREE("3");

    public final String label;

    Quantity(String label) {
        this.label = label;
    }

    public static Quantity getQuantityFromString(String QUANTITY){
        if (QUANTITY.equals("ONE")){
            return Quantity.ONE;
        }
        else if (QUANTITY.equals("TWO")){
            return Quantity.TWO;
        }

        return Quantity.THREE;
    }
}
