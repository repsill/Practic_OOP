package com.example.sportsshop.inventory;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Racket extends AbstractSportingGood {
    // Власні унікальні поля
    private final StringProperty sportType; // tennis, badminton, squash
    private final DoubleProperty headSizeSqIn; // площа голови в кв. дюймах
    private final DoubleProperty weightGr; // вага в грамах
    private final StringProperty stringPattern; // наприклад, "16x19"

    // Конструктори
    public Racket(String name, double price, String brand, String description,
                  String sportType, double headSizeSqIn, double weightGr, String stringPattern) {
        super(name, price, brand, description);
        this.sportType = new SimpleStringProperty(sportType);
        this.headSizeSqIn = new SimpleDoubleProperty(headSizeSqIn);
        this.weightGr = new SimpleDoubleProperty(weightGr);
        this.stringPattern = new SimpleStringProperty(stringPattern);
    }

    public Racket(String name, double price, String brand, String sportType, double weightGr) {
        this(name, price, brand, "Спортивна ракетка", sportType, 100.0, weightGr, "N/A");
    }

    // Власні унікальні методи
    public String recommendStringTension(String playerLevel) { // beginner, intermediate, advanced
        double baseTension = sportType.get().equalsIgnoreCase("tennis") ? 55 : 22; // lbs
        switch (playerLevel.toLowerCase()) {
            case "intermediate": baseTension += (sportType.get().equalsIgnoreCase("tennis") ? 2 : 1); break;
            case "advanced": baseTension += (sportType.get().equalsIgnoreCase("tennis") ? 4 : 2); break;
        }
        return String.format("Рекомендована натяжка для рівня '%s': %.1f lbs", playerLevel, baseTension);
    }

    // Перевантажений метод
    public String recommendStringTension() {
        return recommendStringTension("intermediate");
    }

    public void changeGrip(String newGripType) {
        System.out.println("Заміна обмотки на ракетці " + getName() + " на тип: " + newGripType);
    }

    public boolean checkBalance(double balancePointCm) { // від ручки
        double idealBalance = getWeightGr() > 300 ? 32.0 : 34.0; // Приклад логіки
        return Math.abs(balancePointCm - idealBalance) < 1.5; // в межах 1.5 см
    }

    @Override
    public String getSpecificDetails() {
        return String.format("Тип спорту: %s, Голова: %.1f кв.дюйм, Вага: %.1f гр, Струнна формула: %s. %s",
                getSportType(), getHeadSizeSqIn(), getWeightGr(), getStringPattern(), recommendStringTension());
    }

    // Геттери та Сеттери
    public String getSportType() { return sportType.get(); }
    public StringProperty sportTypeProperty() { return sportType; }
    public void setSportType(String sportType) { this.sportType.set(sportType); }

    public double getHeadSizeSqIn() { return headSizeSqIn.get(); }
    public DoubleProperty headSizeSqInProperty() { return headSizeSqIn; }
    public void setHeadSizeSqIn(double headSizeSqIn) { this.headSizeSqIn.set(headSizeSqIn); }

    public double getWeightGr() { return weightGr.get(); }
    public DoubleProperty weightGrProperty() { return weightGr; }
    public void setWeightGr(double weightGr) { this.weightGr.set(weightGr); }

    public String getStringPattern() { return stringPattern.get(); }
    public StringProperty stringPatternProperty() { return stringPattern; }
    public void setStringPattern(String stringPattern) { this.stringPattern.set(stringPattern); }
}