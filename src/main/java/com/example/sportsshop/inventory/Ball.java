package com.example.sportsshop.inventory;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Ball extends AbstractSportingGood {
    // Власні унікальні поля (мінімум 4)
    private final StringProperty ballType; // e.g., football, basketball, volleyball
    private final IntegerProperty size;     // e.g., 5, 7
    private final StringProperty material; // e.g., leather, synthetic
    private final StringProperty intendedUsage; // e.g., indoor, outdoor, professional match

    // Конструктори (максимальна кількість)
    public Ball(String name, double price, String brand, String description,
                String ballType, int size, String material, String intendedUsage) {
        super(name, price, brand, description);
        this.ballType = new SimpleStringProperty(ballType);
        this.size = new SimpleIntegerProperty(size);
        this.material = new SimpleStringProperty(material);
        this.intendedUsage = new SimpleStringProperty(intendedUsage);
    }

    public Ball(String id, String name, double price, String brand, String description,
                String ballType, int size, String material, String intendedUsage) {
        super(id, name, price, brand, description);
        this.ballType = new SimpleStringProperty(ballType);
        this.size = new SimpleIntegerProperty(size);
        this.material = new SimpleStringProperty(material);
        this.intendedUsage = new SimpleStringProperty(intendedUsage);
    }

    public Ball(String name, double price, String brand, String ballType, int size) {
        this(name, price, brand, "Спортивний м'яч", ballType, size, "N/A", "General purpose");
    }


    // Власні унікальні функціональні методи (мінімум 3)
    public String checkInflationStatus(double currentPressurePsi) {
        double recommendedPsi = getRecommendedPressurePsi();
        if (currentPressurePsi < recommendedPsi * 0.8) {
            return "Потрібно підкачати! Тиск: " + currentPressurePsi + " PSI (рекомендовано: " + recommendedPsi + " PSI)";
        } else if (currentPressurePsi > recommendedPsi * 1.2) {
            return "Перекачаний! Тиск: " + currentPressurePsi + " PSI (рекомендовано: " + recommendedPsi + " PSI)";
        }
        return "Тиск в нормі: " + currentPressurePsi + " PSI (рекомендовано: " + recommendedPsi + " PSI)";
    }

    public double getRecommendedPressurePsi() {
        switch (getBallType().toLowerCase()) {
            case "football": return getSize() == 5 ? 11.0 : 9.0;
            case "basketball": return getSize() == 7 ? 8.0 : 7.0;
            case "volleyball": return 4.5;
            default: return 7.0;
        }
    }

    public void cleanBall(String cleaningMethod) {
        System.out.println("Чистка м'яча " + getName() + " методом: " + cleaningMethod + " (матеріал: " + getMaterial() + ")");
    }

    // Перевантажений метод
    public void cleanBall() {
        cleanBall("волога тканина");
    }

    @Override
    public String getSpecificDetails() {
        return String.format("Тип м'яча: %s, Розмір: %d, Матеріал: %s, Призначення: %s. Рекомендований тиск: %.1f PSI.",
                getBallType(), getSize(), getMaterial(), getIntendedUsage(), getRecommendedPressurePsi());
    }

    // Геттери та Сеттери для власних полів
    public String getBallType() { return ballType.get(); }
    public StringProperty ballTypeProperty() { return ballType; }
    public void setBallType(String ballType) { this.ballType.set(ballType); }

    public int getSize() { return size.get(); }
    public IntegerProperty sizeProperty() { return size; }
    public void setSize(int size) { this.size.set(size); }

    public String getMaterial() { return material.get(); }
    public StringProperty materialProperty() { return material; }
    public void setMaterial(String material) { this.material.set(material); }

    public String getIntendedUsage() { return intendedUsage.get(); }
    public StringProperty intendedUsageProperty() { return intendedUsage; }
    public void setIntendedUsage(String intendedUsage) { this.intendedUsage.set(intendedUsage); }
}