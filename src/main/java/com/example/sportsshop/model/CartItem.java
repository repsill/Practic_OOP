package com.example.sportsshop.model;

import com.example.sportsshop.inventory.AbstractSportingGood; // Змінено імпорт
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class CartItem {
    private final AbstractSportingGood product; // Змінено тип з Product на AbstractSportingGood
    private final IntegerProperty quantity;

    public CartItem(AbstractSportingGood product, int quantity) { // Змінено тип параметра
        this.product = product;
        this.quantity = new SimpleIntegerProperty(quantity);
    }

    public AbstractSportingGood getProduct() { // Змінено тип, що повертається
        return product;
    }

    public int getQuantity() {
        return quantity.get();
    }

    public void setQuantity(int quantity) {
        this.quantity.set(quantity);
    }

    public IntegerProperty quantityProperty() {
        return quantity;
    }

    public double getTotalPrice() {
        return product.getPrice() * quantity.get();
    }

    // Для TableView (залишаються такими ж, бо AbstractSportingGood має ці методи)
    public String getProductName() {
        return product.getName();
    }

    public double getProductPrice() {
        return product.getPrice();
    }
}