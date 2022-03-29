package com.example.setcardgame.Model.card;

public class Card {
    private Color color;
    private Shape shape;
    private Quantity quantity;

    public Card(Color color, Shape shape, Quantity quantity) {
        this.color = color;
        this.shape = shape;
        this.quantity = quantity;
    }

    public Card(String color, String shape, String quantity) {
        this.color = Color.getColorFromString(color);
        this.shape = Shape.getShapeFromString(shape);
        this.quantity = Quantity.getQuantityFromString(quantity);
    }

    public Color getColor() {
        return color;
    }

    public Shape getShape() {
        return shape;
    }

    public Quantity getQuantity() {
        return quantity;
    }

    public String toString(){
        return color.label + shape.label + quantity.label;
    }
}
