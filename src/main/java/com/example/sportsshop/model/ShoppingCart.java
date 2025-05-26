package com.example.sportsshop.model;

import com.example.sportsshop.inventory.AbstractSportingGood; // Змінено імпорт
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

    public void addProduct(AbstractSportingGood product, int quantity) { // Змінено тип параметра
        for (CartItem item : items) {
            // Порівнюємо по ID для унікальності товару, а не тільки по імені
            if (item.getProduct().getId().equals(product.getId())) {
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