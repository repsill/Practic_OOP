package com.example.sportsshop.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ShoppingCart {
    private final ObservableList<CartItem> items;

    public ShoppingCart() {
        this.items = FXCollections.observableArrayList();
    }

    public ObservableList<CartItem> getItems() {
        return items;
    }

    public void addProduct(Product product, int quantity) {
        for (CartItem item : items) {
            if (item.getProduct().getName().equals(product.getName())) {
                item.setQuantity(item.getQuantity() + quantity);
                return;
            }
        }
        items.add(new CartItem(product, quantity));
    }

    public void removeProduct(CartItem cartItem) {
        items.remove(cartItem);
    }

    public void clearCart() {
        items.clear();
    }

    public double getTotalPrice() {
        return items.stream().mapToDouble(CartItem::getTotalPrice).sum();
    }
}