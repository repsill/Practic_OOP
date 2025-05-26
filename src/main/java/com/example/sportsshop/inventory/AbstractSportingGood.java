package com.example.sportsshop.inventory;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.property.DoubleProperty;

import java.util.UUID; // Для унікального ID

public abstract class AbstractSportingGood {
    // Спільні змінні для спадкоємців
    protected final StringProperty id;         // Унікальний ідентифікатор товару
    protected final StringProperty name;
    protected final DoubleProperty price;
    protected final StringProperty brand;
    protected final StringProperty description; // Загальний опис

    // Конструктори
    public AbstractSportingGood(String name, double price, String brand, String description) {
        this.id = new SimpleStringProperty(UUID.randomUUID().toString()); // Автоматична генерація ID
        this.name = new SimpleStringProperty(name);
        this.price = new SimpleDoubleProperty(price);
        this.brand = new SimpleStringProperty(brand);
        this.description = new SimpleStringProperty(description);
    }

    public AbstractSportingGood(String id, String name, double price, String brand, String description) {
        this.id = new SimpleStringProperty(id);
        this.name = new SimpleStringProperty(name);
        this.price = new SimpleDoubleProperty(price);
        this.brand = new SimpleStringProperty(brand);
        this.description = new SimpleStringProperty(description);
    }

    // ---- Методи згідно вимог (не менше 4 функціональних) ----

    /**
     * Розраховує ціну зі знижкою.
     * @param discountPercentage Відсоток знижки (наприклад, 10 для 10%).
     * @return Ціна зі знижкою.
     */
    public double getDiscountedPrice(double discountPercentage) {
        if (discountPercentage < 0 || discountPercentage > 100) {
            System.out.println("Відсоток знижки має бути між 0 та 100.");
            return getPrice();
        }
        return getPrice() * (1 - discountPercentage / 100.0);
    }

    /**
     * Перевантажений метод для розрахунку ціни зі знижкою та фіксованою знижкою.
     * @param discountPercentage Відсоток знижки.
     * @param flatDiscount Фіксована сума знижки (застосовується після відсоткової).
     * @return Ціна зі знижкою.
     */
    public double getDiscountedPrice(double discountPercentage, double flatDiscount) {
        double priceAfterPercentage = getDiscountedPrice(discountPercentage);
        return Math.max(0, priceAfterPercentage - flatDiscount); // Ціна не може бути від'ємною
    }


    /**
     * Перевіряє, чи товар підходить для певного рівня майстерності.
     * @param skillLevel Рівень майстерності ("beginner", "intermediate", "professional").
     * @return true, якщо підходить, false - інакше.
     */
    public boolean isSuitableForSkillLevel(String skillLevel) {
        // Базова реалізація, може бути перевизначена у спадкоємцях
        System.out.println("Перевірка придатності для рівня " + skillLevel + " для " + getName());
        return true; // За замовчуванням підходить для всіх
    }

    /**
     * Повертає рекомендовані умови зберігання товару.
     * @return Рядок з рекомендаціями.
     */
    public String getStorageRecommendations() {
        return "Зберігати в сухому, прохолодному місці, подалі від прямих сонячних променів.";
    }

    // ---- Власні функціональні методи (не менше 2) ----

    /**
     * Оновлює ціну товару.
     * @param newPrice Нова ціна.
     */
    public void updatePrice(double newPrice) {
        if (newPrice > 0) {
            this.price.set(newPrice);
            System.out.println("Ціну для " + getName() + " оновлено на " + newPrice);
        } else {
            System.out.println("Ціна не може бути нульовою або від'ємною.");
        }
    }
    /**
     * Повертає коротку інформацію про товар (ID, Назва, Бренд).
     * @return Рядок з короткою інформацією.
     */
    public String getBasicInfo() {
        return String.format("ID: %s, Назва: %s, Бренд: %s", getId(), getName(), getBrand());
    }

    // ---- Абстрактний метод, який мають реалізувати спадкоємці ----
    /**
     * Повертає детальний опис, специфічний для типу товару.
     * @return Детальний опис.
     */
    public abstract String getSpecificDetails();


    // ---- Геттери та Сеттери (інкапсуляція) ----
    public String getId() { return id.get(); }
    public StringProperty idProperty() { return id; }
    // Немає сеттера для ID, бо він генерується або встановлюється раз при створенні

    public String getName() { return name.get(); }
    public StringProperty nameProperty() { return name; }
    public void setName(String name) { this.name.set(name); }

    public double getPrice() { return price.get(); }
    public DoubleProperty priceProperty() { return price; }
    public void setPrice(double price) { // Вже є updatePrice, але для повноти
        if (price > 0) this.price.set(price);
    }

    public String getBrand() { return brand.get(); }
    public StringProperty brandProperty() { return brand; }
    public void setBrand(String brand) { this.brand.set(brand); }

    public String getDescription() { return description.get(); }
    public StringProperty descriptionProperty() { return description; }
    public void setDescription(String description) { this.description.set(description); }

    @Override
    public String toString() { // Для відображення в ListView
        return getName() + " (" + getBrand() + ") - " + String.format("%.2f грн", getPrice());
    }
}