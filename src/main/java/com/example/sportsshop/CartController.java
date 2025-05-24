package com.example.sportsshop;

import com.example.sportsshop.model.CartItem;
import com.example.sportsshop.model.ShoppingCart;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class CartController {

    @FXML
    private TableView<CartItem> cartTableView;
    @FXML
    private TableColumn<CartItem, String> productNameColumn;
    @FXML
    private TableColumn<CartItem, Double> productPriceColumn;
    @FXML
    private TableColumn<CartItem, Integer> quantityColumn;
    @FXML
    private TableColumn<CartItem, Double> totalItemPriceColumn;
    @FXML
    private Label totalCartPriceLabel;
    @FXML
    private Button removeItemButton;
    @FXML
    private Button clearCartButton;
    @FXML
    private Button closeCartButton;

    private ShoppingCart shoppingCart;

    public void setShoppingCart(ShoppingCart cart) {
        this.shoppingCart = cart;
        loadCartData();
    }

    @FXML
    public void initialize() {
        // Налаштування стовпців таблиці
        productNameColumn.setCellValueFactory(new PropertyValueFactory<>("productName")); // Використовуємо геттер з CartItem
        productPriceColumn.setCellValueFactory(new PropertyValueFactory<>("productPrice")); // Використовуємо геттер з CartItem
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        totalItemPriceColumn.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));

        // Кнопка "Видалити обране" активна тільки якщо щось обрано
        removeItemButton.disableProperty().bind(cartTableView.getSelectionModel().selectedItemProperty().isNull());
    }

    private void loadCartData() {
        if (shoppingCart != null) {
            cartTableView.setItems(shoppingCart.getItems());
            updateTotalCartPrice();
        }
    }

    @FXML
    private void handleRemoveItem() {
        CartItem selectedItem = cartTableView.getSelectionModel().getSelectedItem();
        if (selectedItem != null && shoppingCart != null) {
            shoppingCart.removeProduct(selectedItem);
            updateTotalCartPrice(); // Оновлюємо дані в таблиці та загальну суму
        }
    }

    @FXML
    private void handleClearCart() {
        if (shoppingCart != null) {
            shoppingCart.clearCart();
            updateTotalCartPrice(); // Оновлюємо дані в таблиці та загальну суму
        }
    }

    @FXML
    private void handleCloseCart() {
        Stage stage = (Stage) closeCartButton.getScene().getWindow();
        stage.close();
    }

    private void updateTotalCartPrice() {
        if (shoppingCart != null) {
            totalCartPriceLabel.setText(String.format("Загальна сума: %.2f грн", shoppingCart.getTotalPrice()));
        } else {
            totalCartPriceLabel.setText("Загальна сума: 0.00 грн");
        }
    }
}