package com.example.sportsshop.inventory;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Accessory extends AbstractSportingGood {
    // Власні унікальні поля
    private final StringProperty accessoryType; // Наприклад, "Рукавиці", "Шапка", "Пляшка для води", "Фітнес-трекер"
    private final StringProperty primaryMaterial;
    private final BooleanProperty isWaterproof;
    private final StringProperty targetActivity; // Наприклад, "Біг", "Велоспорт", "Загальні тренування"

    // Конструктори
    public Accessory(String name, double price, String brand, String description,
                     String accessoryType, String primaryMaterial, boolean isWaterproof, String targetActivity) {
        super(name, price, brand, description);
        this.accessoryType = new SimpleStringProperty(accessoryType);
        this.primaryMaterial = new SimpleStringProperty(primaryMaterial);
        this.isWaterproof = new SimpleBooleanProperty(isWaterproof);
        this.targetActivity = new SimpleStringProperty(targetActivity);
    }

    public Accessory(String name, double price, String brand, String accessoryType, String targetActivity) {
        this(name, price, brand, "Спортивний аксесуар", accessoryType, "N/A", false, targetActivity);
    }


    // Власні унікальні методи
    public String checkCompatibility(String sport) {
        if (getTargetActivity().equalsIgnoreCase(sport) || getTargetActivity().equalsIgnoreCase("Загальні тренування")) {
            return getName() + " сумісний з " + sport + ".";
        }
        return getName() + " може бути неоптимальним для " + sport + ", краще підходить для " + getTargetActivity() + ".";
    }

    public String getMaintenanceTips() {
        String tips = "Загальний догляд: ";
        if (getIsWaterproof()) {
            tips += "Регулярно перевіряйте водонепроникність. ";
        }
        if (getPrimaryMaterial().toLowerCase().contains("шкіра")) {
            tips += "Використовуйте засоби для догляду за шкірою. ";
        } else {
            tips += "Чистити згідно з інструкцією на етикетці. ";
        }
        return tips;
    }

    // Перевантажений метод
    public String getMaintenanceTips(boolean forLongTermStorage) {
        if (forLongTermStorage) {
            return getMaintenanceTips() + " Перед тривалим зберіганням ретельно очистіть та висушіть.";
        }
        return getMaintenanceTips();
    }


    public void activateSpecialFeature(String featureName) {
        if (getAccessoryType().equalsIgnoreCase("Фітнес-трекер") && featureName.equalsIgnoreCase("HRM")) {
            System.out.println("Активовано моніторинг серцевого ритму на " + getName());
        } else {
            System.out.println("Спеціальна функція '" + featureName + "' не підтримується або не знайдена для " + getName());
        }
    }

    @Override
    public String getSpecificDetails() {
        return String.format("Тип аксесуару: %s, Матеріал: %s. Водонепроникний: %s. Цільова активність: %s. %s",
                getAccessoryType(), getPrimaryMaterial(), getIsWaterproof() ? "Так" : "Ні", getTargetActivity(), getMaintenanceTips());
    }

    // Геттери та Сеттери
    public String getAccessoryType() { return accessoryType.get(); }
    public void setAccessoryType(String accessoryType) { this.accessoryType.set(accessoryType); }

    public String getPrimaryMaterial() { return primaryMaterial.get(); }
    public void setPrimaryMaterial(String primaryMaterial) { this.primaryMaterial.set(primaryMaterial); }

    public boolean getIsWaterproof() { return isWaterproof.get(); }
    public void setIsWaterproof(boolean isWaterproof) { this.isWaterproof.set(isWaterproof); }

    public String getTargetActivity() { return targetActivity.get(); }
    public void setTargetActivity(String targetActivity) { this.targetActivity.set(targetActivity); }
}