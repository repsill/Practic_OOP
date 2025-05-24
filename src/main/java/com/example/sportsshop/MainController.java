package com.example.sportsshop;

import com.example.sportsshop.model.Product;
import com.example.sportsshop.model.ShoppingCart;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class MainController {

    @FXML
    private ListView<Product> productListView;
    @FXML
    private Label productNameLabel;
    @FXML
    private Label productPriceLabel;
    @FXML
    private Label productDescriptionLabel;
    @FXML
    private Button addToCartButton;
    @FXML
    private Label cartStatusLabel;
    @FXML
    private Button viewCartButton;

    private ObservableList<Product> availableProducts;
    private ShoppingCart shoppingCart; // Спільний кошик

    public MainController() {
        // Конструктор викликається перед initialize()
    }

    // Метод для ін'єкції ShoppingCart з MainApp
    public void setShoppingCart(ShoppingCart cart) {
        this.shoppingCart = cart;
        updateCartStatusLabel(); // Оновити статус при встановленні
    }

    @FXML
    public void initialize() {
        availableProducts = FXCollections.observableArrayList(
                new Product("Футбольний м'яч", 350.00, "Професійний футбольний м'яч, розмір 5."),
                new Product("Баскетбольний м'яч", 420.50, "Для гри в залі та на вулиці, Spalding."),
                new Product("Тенісна ракетка", 1200.00, "Легка ракетка для початківців, Wilson."),
                new Product("Кросівки для бігу", 2500.75, "Амортизуючі кросівки, Asics Gel."),
                new Product("Коврик для йоги", 500.00, "Нековзкий коврик, товщина 6мм.")
        );
        productListView.setItems(availableProducts);

        productListView.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showProductDetails(newValue)
        );

        // Ініціалізація полів, якщо shoppingCart ще не встановлено
        if (shoppingCart == null) {
            // shoppingCart буде встановлено через setShoppingCart
            // Тому тут можна залишити порожнім або встановити початковий статус
            updateCartStatusLabel();
        }
    }

    private void showProductDetails(Product product) {
        if (product != null) {
            productNameLabel.setText("Назва: " + product.getName());
            productPriceLabel.setText(String.format("Ціна: %.2f грн", product.getPrice()));
            productDescriptionLabel.setText("Опис: " + product.getDescription());
            addToCartButton.setDisable(false);
        } else {
            productNameLabel.setText("Назва: ");
            productPriceLabel.setText("Ціна: ");
            productDescriptionLabel.setText("Опис: ");
            addToCartButton.setDisable(true);
        }
    }

    @FXML
    private void handleAddToCart() {
        Product selectedProduct = productListView.getSelectionModel().getSelectedItem();
        if (selectedProduct != null && shoppingCart != null) {
            shoppingCart.addProduct(selectedProduct, 1); // Додаємо 1 одиницю
            System.out.println(selectedProduct.getName() + " додано в кошик.");
            updateCartStatusLabel();
        }
    }

    @FXML
    private void handleViewCart() {
        if (shoppingCart == null) {
            System.err.println("Кошик не ініціалізовано!");
            return;
        }
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("cart-view.fxml"));
            Parent root = loader.load();

            CartController cartController = loader.getController();
            cartController.setShoppingCart(shoppingCart); // Передаємо той самий екземпляр кошика

            Stage cartStage = new Stage();
            cartStage.setTitle("Ваш кошик");
            cartStage.initModality(Modality.APPLICATION_MODAL); // Блокує головне вікно
            cartStage.setScene(new Scene(root));

            // Додамо слухача для оновлення статусу кошика при закритті вікна кошика
            cartStage.setOnHidden(event -> updateCartStatusLabel());

            cartStage.showAndWait(); // Показує вікно і чекає його закриття

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Не вдалося завантажити cart-view.fxml");
        }
    }

    private void updateCartStatusLabel() {
        if (shoppingCart != null && !shoppingCart.getItems().isEmpty()) {
            cartStatusLabel.setText(String.format("У кошику: %d товар(ів) на суму %.2f грн",
                    shoppingCart.getItems().size(), shoppingCart.getTotalPrice()));
        } else {
            cartStatusLabel.setText("Кошик порожній");
        }
    }
}