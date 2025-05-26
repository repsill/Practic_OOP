package com.example.sportsshop.inventory;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import java.util.Objects;

/**
 * Представляє спортивний м'яч, успадковуючи властивості від AbstractSportingGood.
 * Додає специфічні характеристики для м'ячів, такі як тип, розмір, матеріал
 * та призначення.
 */
public class Ball extends AbstractSportingGood {

    // Власні унікальні поля для класу Ball
    private final StringProperty ballType;      // Тип м'яча, наприклад, "football", "basketball", "volleyball"
    private final IntegerProperty size;         // Розмір м'яча, числове значення (наприклад, 5 для футболу, 7 для баскетболу)
    private final StringProperty material;      // Матеріал, з якого виготовлено м'яч (наприклад, "leather", "synthetic", "rubber")
    private final StringProperty intendedUsage; // Призначення м'яча (наприклад, "indoor", "outdoor", "professional match", "training")

    /**
     * Повний конструктор для створення екземпляра м'яча з усіма деталями.
     *
     * @param name Назва м'яча. Не може бути null або порожнім.
     * @param price Ціна м'яча. Має бути позитивною.
     * @param brand Бренд виробника м'яча.
     * @param description Загальний опис м'яча.
     * @param ballType Тип м'яча (наприклад, "футбольний").
     * @param size Розмір м'яча (числове значення). Має бути позитивним.
     * @param material Матеріал, з якого виготовлено м'яч.
     * @param intendedUsage Призначення м'яча.
     * @throws IllegalArgumentException якщо назва, ціна або розмір невалідні.
     */
    public Ball(String name, double price, String brand, String description,
                String ballType, int size, String material, String intendedUsage) {
        super(name, price, brand, description); // Виклик конструктора батьківського класу
        if (size <= 0) {
            throw new IllegalArgumentException("Розмір м'яча має бути позитивним числом.");
        }
        this.ballType = new SimpleStringProperty(Objects.requireNonNullElse(ballType, "Не вказано"));
        this.size = new SimpleIntegerProperty(size);
        this.material = new SimpleStringProperty(Objects.requireNonNullElse(material, "N/A"));
        this.intendedUsage = new SimpleStringProperty(Objects.requireNonNullElse(intendedUsage, "Загальне призначення"));
    }

    /**
     * Скорочений конструктор для створення м'яча з основними параметрами.
     * Інші параметри (description, material, intendedUsage) встановлюються значеннями за замовчуванням.
     *
     * @param name Назва м'яча.
     * @param price Ціна м'яча.
     * @param brand Бренд м'яча.
     * @param ballType Тип м'яча.
     * @param size Розмір м'яча.
     */
    public Ball(String name, double price, String brand, String ballType, int size) {
        this(name, price, brand,
                "Спортивний м'яч типу " + Objects.requireNonNullElse(ballType, "не вказано"), // Динамічний опис за замовчуванням
                ballType, size, "Композитний", "Тренування/Аматорська гра");
    }

    /**
     * Конструктор за замовчуванням, необхідний для деяких фреймворків серіалізації (наприклад, Jackson).
     * Створює м'яч з мінімальними значеннями за замовчуванням.
     * **УВАГА:** Використовуйте з обережністю, переважно для внутрішніх потреб фреймворків.
     * Рекомендується використовувати більш деталізовані конструктори для створення об'єктів.
     */
    public Ball() {
        this("Невідомий м'яч", 1.0, "N/A", "Не вказано", 0);
        // Попередження або логування про використання конструктора за замовчуванням може бути корисним
        System.err.println("УВАГА: Створено екземпляр Ball за допомогою конструктора за замовчуванням. Переконайтеся, що поля будуть ініціалізовані пізніше.");
    }


    // Власні унікальні функціональні методи

    /**
     * Перевіряє стан накачування м'яча на основі поточного тиску.
     *
     * @param currentPressurePsi Поточний тиск у м'ячі в PSI (фунтах на квадратний дюйм).
     *                           Має бути невід'ємним.
     * @return Рядок, що описує стан накачування (наприклад, "Потрібно підкачати!", "Тиск в нормі", "Перекачаний!").
     */
    public String checkInflationStatus(double currentPressurePsi) {
        if (currentPressurePsi < 0) {
            return "Поточний тиск не може бути від'ємним.";
        }
        double recommendedPsi = getRecommendedPressurePsi();
        if (currentPressurePsi < recommendedPsi * 0.8) { // Якщо тиск менше 80% від рекомендованого
            return String.format("Потрібно підкачати! Поточний тиск: %.1f PSI (рекомендовано: %.1f PSI)", currentPressurePsi, recommendedPsi);
        } else if (currentPressurePsi > recommendedPsi * 1.2) { // Якщо тиск більше 120% від рекомендованого
            return String.format("Перекачаний! Обережно! Поточний тиск: %.1f PSI (рекомендовано: %.1f PSI)", currentPressurePsi, recommendedPsi);
        }
        return String.format("Тиск в нормі. Поточний тиск: %.1f PSI (рекомендовано: %.1f PSI)", currentPressurePsi, recommendedPsi);
    }

    /**
     * Повертає рекомендований тиск для накачування даного типу та розміру м'яча в PSI.
     *
     * @return Рекомендований тиск в PSI.
     */
    public double getRecommendedPressurePsi() {
        // Логіка може бути розширена для більшої точності
        String typeLower = getBallType().toLowerCase();
        switch (typeLower) {
            case "football":
            case "футбольний":
                if (getSize() == 5) return 11.0; // Стандартний розмір 5
                if (getSize() == 4) return 9.0;  // Розмір 4
                return 8.0; // Інші розміри
            case "basketball":
            case "баскетбольний":
                if (getSize() == 7) return 8.0;  // Стандартний розмір 7
                if (getSize() == 6) return 7.5; // Розмір 6 (жіночий)
                return 7.0; // Інші розміри
            case "volleyball":
            case "волейбольний":
                return 4.5; // Стандартний тиск для волейбольних м'ячів
            case "handball":
            case "гандбольний":
                if (getSize() == 3) return 9.0;
                if (getSize() == 2) return 8.0;
                return 7.0;
            default:
                return 7.0; // Загальне значення за замовчуванням для невідомих типів
        }
    }

    /**
     * Описує процес чищення м'яча, використовуючи вказаний метод.
     * Виводить інформацію про чищення в консоль.
     *
     * @param cleaningMethod Метод чищення (наприклад, "волога тканина", "спеціальний засіб").
     *                       Якщо null або порожній, виводиться повідомлення про це.
     */
    public void cleanBall(String cleaningMethod) {
        if (cleaningMethod == null || cleaningMethod.trim().isEmpty()){
            System.out.println("Метод чищення не вказано для м'яча '" + getName() + "'.");
            return;
        }
        System.out.println("Чищення м'яча '" + getName() + "' (матеріал: " + getMaterial() + "). Метод: " + cleaningMethod + ".");
    }

    /**
     * Перевантажений метод. Описує процес чищення м'яча стандартним методом
     * (волога тканина з м'яким миючим засобом).
     */
    public void cleanBall() {
        cleanBall("волога тканина з м'яким миючим засобом");
    }

    /**
     * Перевіряє, чи підходить м'яч для гри на певному типі покриття.
     * @param surfaceType Тип покриття (наприклад, "трава", "паркет", "асфальт", "пісок").
     * @return true, якщо м'яч підходить, false - інакше.
     */
    public boolean isSuitableForSurface(String surfaceType) {
        if (surfaceType == null || surfaceType.trim().isEmpty()) {
            System.err.println("Тип покриття не вказано для перевірки придатності м'яча.");
            return false; // Або true, якщо вважати, що підходить для будь-якого невідомого
        }
        String usageLower = getIntendedUsage().toLowerCase();
        String materialLower = getMaterial().toLowerCase();
        String surfaceLower = surfaceType.toLowerCase();

        if (usageLower.contains("outdoor") || usageLower.contains("вулиц") || usageLower.contains("street")) {
            return surfaceLower.matches("трава|асфальт|гумове покриття|пісок|грунт");
        }
        if (usageLower.contains("indoor") || usageLower.contains("зал")) {
            return surfaceLower.matches("паркет|спортивний лінолеум|гумове покриття");
        }
        // Якщо м'яч універсальний або для тренувань, може підходити для більшості
        if (usageLower.contains("універсальний") || usageLower.contains("тренуван") || usageLower.contains("general")) {
            return true;
        }
        // Деякі матеріали краще для певних покриттів
        if (materialLower.contains("гума") && surfaceLower.equals("асфальт")) return true;
        if (materialLower.contains("шкіра") && surfaceLower.equals("паркет")) return true;


        System.out.println("М'яч " + getName() + " (" + getIntendedUsage() + ") може бути неоптимальним для покриття: " + surfaceType);
        return false; // За замовчуванням, якщо немає чіткої відповідності
    }

    // Реалізація абстрактного методу з батьківського класу

    /**
     * Повертає специфічні деталі для м'яча, включаючи його тип, розмір,
     * матеріал, призначення та рекомендований тиск.
     *
     * @return Рядок з детальним описом характеристик м'яча.
     */
    @Override
    public String getSpecificDetails() {
        return String.format("Тип м'яча: %s. Розмір: %d. Матеріал: %s. Призначення: %s. Рекомендований тиск: %.1f PSI.",
                getBallType(), getSize(), getMaterial(), getIntendedUsage(), getRecommendedPressurePsi());
    }

    // Геттери та Сеттери для власних полів класу Ball
    // Забезпечують інкапсуляцію та контроль над зміною стану об'єкта.

    public String getBallType() {
        return ballType.get();
    }
    public StringProperty ballTypeProperty() {
        return ballType;
    }
    public void setBallType(String ballType) {
        this.ballType.set(Objects.requireNonNullElse(ballType, "Не вказано"));
    }

    public int getSize() {
        return size.get();
    }
    public IntegerProperty sizeProperty() {
        return size;
    }
    public void setSize(int size) {
        if (size <= 0) {
            System.err.println("Некоректний розмір м'яча: " + size + ". Розмір має бути позитивним. Зміна не застосована.");
            return;
        }
        this.size.set(size);
    }

    public String getMaterial() {
        return material.get();
    }
    public StringProperty materialProperty() {
        return material;
    }
    public void setMaterial(String material) {
        this.material.set(Objects.requireNonNullElse(material, "N/A"));
    }

    public String getIntendedUsage() {
        return intendedUsage.get();
    }
    public StringProperty intendedUsageProperty() {
        return intendedUsage;
    }
    public void setIntendedUsage(String intendedUsage) {
        this.intendedUsage.set(Objects.requireNonNullElse(intendedUsage, "Загальне призначення"));
    }

    // Перевизначення методів equals, hashCode та toString для коректної роботи з колекціями
    // та для зручного представлення об'єкта у вигляді рядка.

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false; // Перевірка рівності на рівні батьківського класу (за ID)
        Ball ball = (Ball) o;
        return getSize() == ball.getSize() &&
                Objects.equals(getBallType(), ball.getBallType()) &&
                Objects.equals(getMaterial(), ball.getMaterial()) &&
                Objects.equals(getIntendedUsage(), ball.getIntendedUsage());
    }

    @Override
    public int hashCode() {
        // Використовуємо поля, що визначають унікальність Ball, плюс хеш-код батька
        return Objects.hash(super.hashCode(), getBallType(), getSize(), getMaterial(), getIntendedUsage());
    }

    @Override
    public String toString() {
        // Розширений toString, що включає специфічні деталі м'яча
        return String.format("%s [Тип: %s, Розмір: %d, Матеріал: %s, Призначення: %s]",
                super.toString(), // Використовує toString батьківського класу
                getBallType(),
                getSize(),
                getMaterial(),
                getIntendedUsage());
    }
}