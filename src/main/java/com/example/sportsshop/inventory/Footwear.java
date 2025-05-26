package com.example.sportsshop.inventory;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;


public class Footwear extends AbstractSportingGood {
    // Власні унікальні поля
    private final StringProperty shoeType; // Running, Training, Basketball, Casual
    private final DoubleProperty shoeSizeEU; // Європейський розмір
    private final StringProperty soleMaterial; // Rubber, Foam, EVA
    private final BooleanProperty hasArchSupport;
    private final StringProperty lacingSystem; // Traditional, BOA, Quick-lace

    // Конструктори
    public Footwear(String name, double price, String brand, String description,
                    String shoeType, double shoeSizeEU, String soleMaterial, boolean hasArchSupport, String lacingSystem) {
        super(name, price, brand, description);
        this.shoeType = new SimpleStringProperty(shoeType);
        this.shoeSizeEU = new SimpleDoubleProperty(shoeSizeEU);
        this.soleMaterial = new SimpleStringProperty(soleMaterial);
        this.hasArchSupport = new SimpleBooleanProperty(hasArchSupport);
        this.lacingSystem = new SimpleStringProperty(lacingSystem);
    }

    public Footwear(String name, double price, String brand, String shoeType, double shoeSizeEU) {
        this(name, price, brand, "Спортивне взуття", shoeType, shoeSizeEU, "N/A", false, "Traditional");
    }

    // Власні унікальні методи
    public String getRecommendedTerrain() {
        switch (getShoeType().toLowerCase()) {
            case "running": return "Асфальт, бігова доріжка";
            case "trail running": return "Пересічена місцевість, лісові стежки";
            case "training": return "Тренажерний зал, фітнес-студія";
            case "basketball": return "Баскетбольний майданчик (паркет)";
            default: return "Різноманітне покриття";
        }
    }

    // Перевантажений метод
    public String getRecommendedTerrain(boolean forCompetition) {
        String base = getRecommendedTerrain();
        if (forCompetition && getShoeType().equalsIgnoreCase("running")) {
            return base + " (для змагань рекомендується легка модель)";
        }
        return base;
    }

    public void checkWear(int monthsOfUse) {
        if (monthsOfUse > 12 && getSoleMaterial().equalsIgnoreCase("Foam")) {
            System.out.println("Рекомендується перевірити амортизацію підошви для " + getName() + ". Можливий знос.");
        } else if (monthsOfUse > 18) {
            System.out.println("Рекомендується перевірити загальний стан " + getName() + ".");
        } else {
            System.out.println(getName() + " ймовірно в хорошому стані.");
        }
    }

    public String suggestCleaningMethod() {
        if (getSoleMaterial().equalsIgnoreCase("EVA") || getSoleMaterial().equalsIgnoreCase("Foam")){
            return "Чистити м'якою щіткою та мильним розчином. Не замочувати.";
        }
        return "Чистити вологою тканиною. Для сильних забруднень - спеціальний засіб для взуття.";
    }

    @Override
    public String getSpecificDetails() {
        return String.format("Тип взуття: %s, Розмір (EU): %.1f, Матеріал підошви: %s. Підтримка склепіння: %s. Система шнурівки: %s. Рекомендоване покриття: %s.",
                getShoeType(), getShoeSizeEU(), getSoleMaterial(), getHasArchSupport() ? "Так" : "Ні", getLacingSystem(), getRecommendedTerrain());
    }

    // Геттери та Сеттери
    public String getShoeType() { return shoeType.get(); }
    public void setShoeType(String shoeType) { this.shoeType.set(shoeType); }

    public double getShoeSizeEU() { return shoeSizeEU.get(); }
    public void setShoeSizeEU(double shoeSizeEU) { this.shoeSizeEU.set(shoeSizeEU); }

    public String getSoleMaterial() { return soleMaterial.get(); }
    public void setSoleMaterial(String soleMaterial) { this.soleMaterial.set(soleMaterial); }

    public boolean getHasArchSupport() { return hasArchSupport.get(); }
    public void setHasArchSupport(boolean hasArchSupport) { this.hasArchSupport.set(hasArchSupport); }

    public String getLacingSystem() { return lacingSystem.get(); }
    public void setLacingSystem(String lacingSystem) { this.lacingSystem.set(lacingSystem); }
}