package com.example.sportsshop;

import com.example.sportsshop.model.ShoppingCart;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApp extends Application {

    private ShoppingCart shoppingCart; // Спільний кошик для всього додатку

    @Override
    public void init() throws Exception {
        // Ініціалізуємо кошик один раз при запуску додатку
        shoppingCart = new ShoppingCart();
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("main-view.fxml"));
            Parent root = loader.load();

            // Отримуємо контролер та передаємо йому кошик
            MainController mainController = loader.getController();
            mainController.setShoppingCart(shoppingCart);

            Scene scene = new Scene(root);
            // Можна додати CSS файл, якщо він є
            // scene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());

            primaryStage.setTitle("Магазин Спорттоварів");
            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Помилка завантаження FXML: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}