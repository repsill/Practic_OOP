package com.example.sportsshop.inventory;

import javafx.beans.property.*;
import java.util.Objects;
import java.util.UUID;

/**
 * Фундаментальний абстрактний клас для всіх спортивних товарів.
 * Забезпечує базову функціональність, спільні властивості та методи.
 */
public abstract class AbstractSportingGood {
    // Спільні змінні для спадкоємців
    protected final StringProperty id;
    protected final StringProperty name;
    protected final DoubleProperty price;
    protected final StringProperty brand;
    protected final StringProperty description;

    /**
     * Конструктор для створення нового товару з автоматично генерованим ID.
     * @param name Назва товару. Не може бути null або порожнім.
     * @param price Ціна товару. Має бути позитивною.
     * @param brand Бренд товару.
     * @param description Опис товару.
     * @throws IllegalArgumentException якщо назва або ціна невалідні.
     */
    public AbstractSportingGood(String name, double price, String brand, String description) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Назва товару не може бути порожньою.");
        }
        if (price <= 0) {
            throw new IllegalArgumentException("Ціна товару має бути позитивною.");
        }
        this.id = new SimpleStringProperty(UUID.randomUUID().toString());
        this.name = new SimpleStringProperty(name);
        this.price = new SimpleDoubleProperty(price);
        this.brand = new SimpleStringProperty(Objects.requireNonNullElse(brand, "N/A"));
        this.description = new SimpleStringProperty(Objects.requireNonNullElse(description, "Без опису."));
    }

    /**
     * Конструктор для створення товару з існуючим ID (наприклад, при завантаженні з БД).
     * @param id Унікальний ідентифікатор товару. Не може бути null або порожнім.
     * @param name Назва товару.
     * @param price Ціна товару.
     * @param brand Бренд товару.
     * @param description Опис товару.
     * @throws IllegalArgumentException якщо ID, назва або ціна невалідні.
     */
    public AbstractSportingGood(String id, String name, double price, String brand, String description) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("ID товару не може бути порожнім.");
        }
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Назва товару не може бути порожньою.");
        }
        if (price <= 0) {
            throw new IllegalArgumentException("Ціна товару має бути позитивною.");
        }
        this.id = new SimpleStringProperty(id);
        this.name = new SimpleStringProperty(name);
        this.price = new SimpleDoubleProperty(price);
        this.brand = new SimpleStringProperty(Objects.requireNonNullElse(brand, "N/A"));
        this.description = new SimpleStringProperty(Objects.requireNonNullElse(description, "Без опису."));
    }

    /**
     * Розраховує ціну зі знижкою.
     * @param discountPercentage Відсоток знижки (0-100).
     * @return Ціна зі знижкою. Якщо відсоток невалідний, повертає поточну ціну.
     */
    public double getDiscountedPrice(double discountPercentage) {
        if (discountPercentage < 0 || discountPercentage > 100) {
            System.err.println("Некоректний відсоток знижки: " + discountPercentage + ". Знижка не застосована.");
            return getPrice();
        }
        return getPrice() * (1 - discountPercentage / 100.0);
    }

    /**
     * Розраховує ціну зі знижкою (відсотковою та фіксованою).
     * @param discountPercentage Відсоток знижки (0-100).
     * @param flatDiscount Фіксована сума знижки (застосовується після відсоткової).
     * @return Ціна зі знижкою. Ціна не може бути від'ємною.
     */
    public double getDiscountedPrice(double discountPercentage, double flatDiscount) {
        double priceAfterPercentage = getDiscountedPrice(discountPercentage);
        if (flatDiscount < 0) {
            System.err.println("Фіксована знижка не може бути від'ємною. Знижка " + flatDiscount + " не застосована.");
            return priceAfterPercentage;
        }
        return Math.max(0, priceAfterPercentage - flatDiscount);
    }

    /**
     * Перевіряє, чи товар підходить для певного рівня майстерності.
     * Базова реалізація, може бути перевизначена у спадкоємцях.
     * @param skillLevel Рівень майстерності (наприклад, "beginner", "intermediate", "professional").
     *                   Не може бути null або порожнім.
     * @return true, якщо підходить або рівень не вказано, false - якщо рівень невалідний.
     */
    public boolean isSuitableForSkillLevel(String skillLevel) {
        if (skillLevel == null || skillLevel.trim().isEmpty()) {
            System.err.println("Рівень майстерності не вказано для перевірки.");
            return true; // Вважаємо, що підходить, якщо не вказано
        }
        // Тут може бути більш складна логіка або делегування спадкоємцям
        System.out.println("Перевірка придатності " + getName() + " для рівня '" + skillLevel + "'. Базова реалізація: так.");
        return true;
    }

    /**
     * Повертає рекомендовані умови зберігання товару.
     * @return Рядок з рекомендаціями.
     */
    public String getStorageRecommendations() {
        return "Зберігати в сухому, прохолодному місці, подалі від прямих сонячних променів та джерел тепла.";
    }

    /**
     * Оновлює ціну товару.
     * @param newPrice Нова ціна. Має бути позитивною.
     */
    public void updatePrice(double newPrice) {
        if (newPrice <= 0) {
            System.err.println("Ціна товару " + getName() + " не може бути нульовою або від'ємною. Залишено стару ціну: " + getPrice());
            return;
        }
        this.price.set(newPrice);
        System.out.println("Ціну для " + getName() + " оновлено на " + String.format("%.2f", newPrice));
    }

    /**
     * Повертає коротку інформацію про товар (ID, Назва, Бренд).
     * @return Рядок з короткою інформацією.
     */
    public String getBasicInfo() {
        return String.format("ID: %s, Назва: %s, Бренд: %s, Ціна: %.2f грн",
                getId(), getName(), getBrand(), getPrice());
    }

    /**
     * Повертає детальний опис, специфічний для типу товару.
     * Має бути реалізований у класах-спадкоємцях.
     * @return Детальний опис.
     */
    public abstract String getSpecificDetails();

    // --- Геттери та Сеттери ---
    public String getId() { return id.get(); }
    public StringProperty idProperty() { return id; }

    public String getName() { return name.get(); }
    public StringProperty nameProperty() { return name; }
    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            System.err.println("Назва товару не може бути порожньою. Зміни не застосовано.");
            return;
        }
        this.name.set(name);
    }

    public double getPrice() { return price.get(); }
    public DoubleProperty priceProperty() { return price; }
    /**
     * Встановлює нову ціну. Для оновлення ціни рекомендується використовувати {@link #updatePrice(double)}.
     * @param price нова ціна.
     */
    public void setPrice(double price) {
        updatePrice(price); // Делегуємо updatePrice для валідації
    }

    public String getBrand() { return brand.get(); }
    public StringProperty brandProperty() { return brand; }
    public void setBrand(String brand) {
        this.brand.set(Objects.requireNonNullElse(brand, "N/A"));
    }

    public String getDescription() { return description.get(); }
    public StringProperty descriptionProperty() { return description; }
    public void setDescription(String description) {
        this.description.set(Objects.requireNonNullElse(description, "Без опису."));
    }

    @Override
    public String toString() {
        return String.format("%s (%s) - %.2f грн", getName(), getBrand(), getPrice());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbstractSportingGood that = (AbstractSportingGood) o;
        return Objects.equals(getId(), that.getId()); // Порівняння за ID
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId()); // Хеш-код на основі ID
    }
}