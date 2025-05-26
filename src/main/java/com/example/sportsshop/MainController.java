package com.example.sportsshop;

// Импорты для новых классов товаров
import com.example.sportsshop.inventory.*;
import com.example.sportsshop.model.CartItem; // Добавлено для подсчета общего количества, если используется getTotalItemCount из ShoppingCart
import com.example.sportsshop.model.ShoppingCart;
import com.example.sportsshop.utils.ReportGenerator; // << НОВЫЙ ИМПОРТ
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert; // << НОВЫЙ ИМПОРТ
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.FileChooser; // << НОВЫЙ ИМПОРТ
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File; // << НОВЫЙ ИМПОРТ
import java.io.IOException;
import java.util.ArrayList; // << НОВЫЙ ИМПОРТ
import java.util.List;      // << НОВЫЙ ИМПОРТ

public class MainController {

    @FXML
    private ListView<AbstractSportingGood> productListView;
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

    // << НОВОЕ ПОЛЕ ДЛЯ КНОПКИ ОТЧЕТА (убедитесь, что fx:id="generateReportButton" есть в FXML)
    @FXML
    private Button generateReportButton;

    private ObservableList<AbstractSportingGood> availableProducts;
    private ShoppingCart shoppingCart;
    private ReportGenerator reportGenerator; // << НОВОЕ ПОЛЕ

    public void setShoppingCart(ShoppingCart cart) {
        this.shoppingCart = cart;
        updateCartStatusLabel();
    }

    @FXML
    public void initialize() {
        reportGenerator = new ReportGenerator(); // << ИНИЦИАЛИЗАЦИЯ ГЕНЕРАТОРА
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

    private void showProductDetails(AbstractSportingGood product) {
        if (product != null) {
            productNameLabel.setText("Назва: " + product.getName() + " (" + product.getBrand() + ")");
            productPriceLabel.setText(String.format("Ціна: %.2f грн", product.getPrice()));
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
        AbstractSportingGood selectedProduct = productListView.getSelectionModel().getSelectedItem();
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
            showAlert(Alert.AlertType.ERROR, "Помилка", "Кошик не ініціалізовано. Спочатку додайте товари.");
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
            showAlert(Alert.AlertType.ERROR, "Помилка завантаження", "Не вдалося завантажити вікно кошика.");
        }
    }

    private void updateCartStatusLabel() {
        if (shoppingCart != null && !shoppingCart.getItems().isEmpty()) {
            // Используем getTotalItemCount(), если он есть в ShoppingCart
            // В противном случае, оставляем текущий способ подсчета
            int totalItems;
            try {
                // Пытаемся вызвать getTotalItemCount, если он существует
                // Это более правильный способ, если ShoppingCart инкапсулирует эту логику
                totalItems = shoppingCart.getTotalItemCount();
            } catch (NoSuchMethodError | AbstractMethodError e) {
                // Если метода нет, считаем по-старому (менее идеально)
                System.err.println("Метод getTotalItemCount() не знайдено в ShoppingCart, рахуємо вручну.");
                totalItems = shoppingCart.getItems().stream().mapToInt(CartItem::getQuantity).sum();
            }

            cartStatusLabel.setText(String.format("У кошику: %d од. товару на суму %.2f грн",
                    totalItems, shoppingCart.getTotalPrice()));
        } else {
            cartStatusLabel.setText("Кошик порожній");
        }
    }

    // << НОВЫЙ МЕТОД ОБРАБОТЧИК >>
    @FXML
    private void handleGenerateInventoryReport() {
        if (availableProducts == null || availableProducts.isEmpty()) {
            showAlert(Alert.AlertType.INFORMATION, "Інформація", "Список товарів порожній. Звіт не буде згенеровано.");
            return;
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Зберегти звіт по інвентаризації");
        fileChooser.setInitialFileName("inventory_report_" + System.currentTimeMillis() + ".txt"); // Уникальное имя файла
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Текстові файли (*.txt)", "*.txt"));

        // Получаем текущее окно для отображения диалога
        File file = fileChooser.showSaveDialog(generateReportButton.getScene().getWindow());

        if (file != null) {
            List<AbstractSportingGood> productsToReport = new ArrayList<>(availableProducts);
            boolean success = reportGenerator.generateInventoryReport(productsToReport, file.getAbsolutePath());

            if (success) {
                showAlert(Alert.AlertType.INFORMATION, "Успіх", "Звіт успішно згенеровано та збережено до:\n" + file.getAbsolutePath());
            } else {
                showAlert(Alert.AlertType.ERROR, "Помилка", "Не вдалося згенерувати або зберегти звіт.");
            }
        } else {
            System.out.println("Збереження звіту скасовано користувачем.");
        }
    }

    // << НОВЫЙ ВСПОМОГАТЕЛЬНЫЙ МЕТОД >>
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}