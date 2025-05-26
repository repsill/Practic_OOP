package com.example.sportsshop.inventory;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

public class Sportswear extends AbstractSportingGood {
    // Власні унікальні поля
    private final StringProperty clothingType; // T-Shirt, Shorts, Jacket, Leggings
    private final StringProperty size; // S, M, L, XL, XXL
    private final StringProperty color;
    private final StringProperty fabricMaterial; // Polyester, Cotton, Spandex
    private final BooleanProperty hasBreathabilityTech; // e.g. Dri-Fit, ClimaCool

    // Конструктори
    public Sportswear(String name, double price, String brand, String description,
                      String clothingType, String size, String color, String fabricMaterial, boolean hasBreathabilityTech) {
        super(name, price, brand, description);
        this.clothingType = new SimpleStringProperty(clothingType);
        this.size = new SimpleStringProperty(size);
        this.color = new SimpleStringProperty(color);
        this.fabricMaterial = new SimpleStringProperty(fabricMaterial);
        this.hasBreathabilityTech = new SimpleBooleanProperty(hasBreathabilityTech);
    }

    public Sportswear(String name, double price, String brand, String clothingType, String size, String color) {
        this(name, price, brand, "Спортивний одяг", clothingType, size, color, "N/A", false);
    }

    // Власні унікальні методи
    public String getCareInstructions() {
        String base = "Машинне прання в холодній воді. ";
        if (getFabricMaterial().toLowerCase().contains("spandex") || getHasBreathabilityTech()) {
            base += "Не використовувати відбілювач. Сушити на повітрі. ";
        }
        if (getColor().equalsIgnoreCase("білий")) {
            base += "Можна прати з білими речами.";
        } else {
            base += "Прати з подібними кольорами.";
        }
        return base;
    }

    // Перевантажений метод
    public String getCareInstructions(String specificConcern) {
        if (specificConcern.equalsIgnoreCase("stain")) {
            return "Для плям: спробуйте засіб для виведення плям перед пранням. " + getCareInstructions();
        }
        return getCareInstructions();
    }

    public boolean isSuitableForSeason(String season) { // "summer", "winter", "demi-season"
        switch (season.toLowerCase()) {
            case "summer":
                return getClothingType().matches("T-Shirt|Shorts") && getHasBreathabilityTech();
            case "winter":
                return getClothingType().matches("Jacket|Leggings") && (getFabricMaterial().contains("fleece") || getFabricMaterial().contains("wool"));
            case "demi-season":
                return true; // Більшість підходить
            default:
                return false;
        }
    }

    public void tryOnVirtual(String avatarGender, int avatarHeightCm) {
        System.out.println("Віртуальна примірка " + getName() + " (" + getSize() + ") на аватарі (" + avatarGender + ", " + avatarHeightCm + " см)... Виглядає чудово!");
    }

    @Override
    public String getSpecificDetails() {
        return String.format("Тип одягу: %s, Розмір: %s, Колір: %s, Матеріал: %s. Технологія дихання: %s. Інструкції по догляду: %s",
                getClothingType(), getSize(), getColor(), getFabricMaterial(), getHasBreathabilityTech() ? "Так" : "Ні", getCareInstructions(""));
    }

    // Геттери та Сеттери
    public String getClothingType() { return clothingType.get(); }
    public void setClothingType(String clothingType) { this.clothingType.set(clothingType); }

    public String getSize() { return size.get(); }
    public void setSize(String size) { this.size.set(size); }

    public String getColor() { return color.get(); }
    public void setColor(String color) { this.color.set(color); }

    public String getFabricMaterial() { return fabricMaterial.get(); }
    public void setFabricMaterial(String fabricMaterial) { this.fabricMaterial.set(fabricMaterial); }

    public boolean getHasBreathabilityTech() { return hasBreathabilityTech.get(); }
    public void setHasBreathabilityTech(boolean hasBreathabilityTech) { this.hasBreathabilityTech.set(hasBreathabilityTech); }
}