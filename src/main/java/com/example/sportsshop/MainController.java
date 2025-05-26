package com.example.sportsshop;

// Імпорти для нових класів товарів
import com.example.sportsshop.inventory.*;
import com.example.sportsshop.model.ShoppingCart; // Залишається
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
    private ListView<AbstractSportingGood> productListView; // Змінено тип
    @FXML
    private Label productNameLabel;
    @FXML
    private Label productPriceLabel;
    @FXML
    private Label productDescriptionLabel; // Будемо використовувати для getSpecificDetails()
    @FXML
    private Button addToCartButton;
    @FXML
    private Label cartStatusLabel;
    @FXML
    private Button viewCartButton;

    private ObservableList<AbstractSportingGood> availableProducts; // Змінено тип
    private ShoppingCart shoppingCart;

    public void setShoppingCart(ShoppingCart cart) {
        this.shoppingCart = cart;
        updateCartStatusLabel();
    }

    @FXML
    public void initialize() {
        availableProducts = FXCollections.observableArrayList(
                new Ball("Футбольний м'яч Pro", 450.00, "Nike", "Професійний м'яч для матчів",
                        "football", 5, "Поліуретан", "professional match"),
                new Ball("Баскетбольний м'яч Street", 380.00, "Spalding", "Для гри на вулиці",
                        "basketball", 7, "Гума", "outdoor"),
                new Racket("Тенісна ракетка Aero", 2200.00, "Babolat", "Для атакуючих гравців",
                        "tennis", 100, 300, "16x19"),
                new Racket("Бадмінтонна ракетка Feather", 850.00, "Yonex", "Легка та маневрена",
                        "badminton", 285, 85, "22x23"),
                new Sportswear("Бігова футболка Cool", 700.00, "Adidas", "З технологією ClimaCool",
                        "T-Shirt", "M", "Синій", "Polyester", true),
                new Sportswear("Тренувальні штани Flex", 1200.00, "Under Armour", "Еластичні для свободи рухів",
                        "Leggings", "L", "Чорний", "Polyester/Spandex", true),
                new Footwear("Кросівки для бігу Pegasus", 3500.00, "Nike", "Амортизація та комфорт",
                        "Running", 42.5, "React Foam", true, "Traditional"),
                new Footwear("Баскетбольні кросівки LeBron", 4800.00, "Nike", "Підтримка та зчеплення",
                        "Basketball", 44.0, "Rubber/Zoom Air", true, "Traditional"),
                new Accessory("Фітнес-трекер Charge 5", 4200.00, "FitBit", "Відстеження активності та сну",
                        "Фітнес-трекер", "Пластик/Силікон", true, "Загальні тренування"),
                new Accessory("Пляшка для води Steel", 500.00, "HydroFlask", "Зберігає температуру",
                        "Пляшка для води", "Нержавіюча сталь", false, "Загальні тренування")
        );
        productListView.setItems(availableProducts);

        productListView.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showProductDetails(newValue)
        );

        updateCartStatusLabel();
    }

    private void showProductDetails(AbstractSportingGood product) { // Змінено тип параметра
        if (product != null) {
            productNameLabel.setText("Назва: " + product.getName() + " (" + product.getBrand() + ")");
            productPriceLabel.setText(String.format("Ціна: %.2f грн", product.getPrice()));
            // Використовуємо getSpecificDetails() для детального опису
            productDescriptionLabel.setText("Деталі: " + product.getSpecificDetails() + "\nЗагальний опис: " + product.getDescription());
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
        AbstractSportingGood selectedProduct = productListView.getSelectionModel().getSelectedItem(); // Змінено тип
        if (selectedProduct != null && shoppingCart != null) {
            shoppingCart.addProduct(selectedProduct, 1);
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
            cartController.setShoppingCart(shoppingCart);

            Stage cartStage = new Stage();
            cartStage.setTitle("Ваш кошик");
            cartStage.initModality(Modality.APPLICATION_MODAL);
            cartStage.setScene(new Scene(root));
            cartStage.setOnHidden(event -> updateCartStatusLabel());
            cartStage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Не вдалося завантажити cart-view.fxml");
        }
    }

    private void updateCartStatusLabel() {
        if (shoppingCart != null && !shoppingCart.getItems().isEmpty()) {
            int totalItems = shoppingCart.getItems().stream().mapToInt(item -> item.getQuantity()).sum(); // Рахуємо загальну кількість одиниць
            cartStatusLabel.setText(String.format("У кошику: %d од. товару на суму %.2f грн",
                    totalItems, shoppingCart.getTotalPrice()));
        } else {
            cartStatusLabel.setText("Кошик порожній");
        }
    }
}