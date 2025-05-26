package com.example.sportsshop.utils;

import com.example.sportsshop.inventory.AbstractSportingGood;
import com.example.sportsshop.inventory.Ball; // Пример для дополнительной детализации
import com.example.sportsshop.inventory.Racket; // Пример для дополнительной детализации
import com.example.sportsshop.model.CartItem;
import com.example.sportsshop.model.ShoppingCart;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Предоставляет функциональность для генерации текстовых отчетов
 * о товарах и состоянии корзины покупок.
 */
public class ReportGenerator {

    private static final Logger LOGGER = Logger.getLogger(ReportGenerator.class.getName());
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * Конструктор по умолчанию.
     */
    public ReportGenerator() {
        // Возможная инициализация, если потребуется в будущем
    }

    /**
     * Генерирует подробный отчет по одному товару.
     *
     * @param product Товар, для которого генерируется отчет. Не может быть null.
     * @param filePath Полный путь к файлу, в который будет сохранен отчет. Не может быть null или пустым.
     * @return true, если отчет успешно сгенерирован и сохранен, false - в противном случае.
     */
    public boolean generateProductReport(AbstractSportingGood product, String filePath) {
        if (product == null) {
            LOGGER.log(Level.WARNING, "Товар для генерации отчета не может быть null.");
            return false;
        }
        if (filePath == null || filePath.trim().isEmpty()) {
            LOGGER.log(Level.WARNING, "Путь к файлу для отчета не может быть null или пустым.");
            return false;
        }

        StringBuilder reportContent = new StringBuilder();
        reportContent.append("ЗВІТ ПО ТОВАРУ\n");
        reportContent.append("========================================\n");
        reportContent.append("Дата генерації: ").append(LocalDateTime.now().format(dateTimeFormatter)).append("\n");
        reportContent.append("----------------------------------------\n");

        appendProductDetails(reportContent, product); // Используем вспомогательный метод

        reportContent.append("========================================\n");
        reportContent.append("Кінець звіту.\n");

        return saveReportToFile(reportContent.toString(), filePath);
    }

    /**
     * Генерирует отчет по списку всех имеющихся товаров (инвентаризационный отчет).
     *
     * @param products Список товаров. Может быть пустым, но не null.
     * @param filePath Полный путь к файлу, в который будет сохранен отчет. Не может быть null или пустым.
     * @return true, если отчет успешно сгенерирован и сохранен, false - в противном случае.
     */
    public boolean generateInventoryReport(List<AbstractSportingGood> products, String filePath) {
        if (products == null) {
            LOGGER.log(Level.WARNING, "Список товаров для инвентаризационного отчета не может быть null.");
            return false;
        }
        if (filePath == null || filePath.trim().isEmpty()) {
            LOGGER.log(Level.WARNING, "Путь к файлу для отчета не может быть null или пустым.");
            return false;
        }

        StringBuilder reportContent = new StringBuilder();
        reportContent.append("ЗВІТ ПРО НАЯВНІ ТОВАРИ (ІНВЕНТАРИЗАЦІЯ)\n");
        reportContent.append("===========================================================\n");
        reportContent.append("Дата генерації: ").append(LocalDateTime.now().format(dateTimeFormatter)).append("\n");
        reportContent.append("Загальна кількість унікальних позицій: ").append(products.size()).append("\n");
        reportContent.append("-----------------------------------------------------------\n\n");

        if (products.isEmpty()) {
            reportContent.append("Список товарів порожній.\n");
        } else {
            for (int i = 0; i < products.size(); i++) {
                reportContent.append("ТОВАР #").append(i + 1).append("\n");
                appendProductDetails(reportContent, products.get(i)); // Используем вспомогательный метод
                reportContent.append("-----------------------------------------------------------\n\n");
            }
        }

        reportContent.append("===========================================================\n");
        reportContent.append("Кінець звіту.\n");

        return saveReportToFile(reportContent.toString(), filePath);
    }


    /**
     * Генерирует отчет по содержимому корзины покупок.
     *
     * @param cart Корзина покупок. Не может быть null.
     * @param filePath Полный путь к файлу, в который будет сохранен отчет. Не может быть null или пустым.
     * @return true, если отчет успешно сгенерирован и сохранен, false - в противном случае.
     */
    public boolean generateShoppingCartReport(ShoppingCart cart, String filePath) {
        if (cart == null) {
            LOGGER.log(Level.WARNING, "Корзина для генерации отчета не может быть null.");
            return false;
        }
        if (filePath == null || filePath.trim().isEmpty()) {
            LOGGER.log(Level.WARNING, "Путь к файлу для отчета не может быть null или пустым.");
            return false;
        }

        StringBuilder reportContent = new StringBuilder();
        reportContent.append("ЗВІТ ПО КОШИКУ ПОКУПЦЯ\n");
        reportContent.append("========================================================================\n"); // Увеличил ширину
        reportContent.append("Дата генерації: ").append(LocalDateTime.now().format(dateTimeFormatter)).append("\n");
        reportContent.append("------------------------------------------------------------------------\n");

        if (cart.getItems().isEmpty()) {
            reportContent.append("Кошик порожній.\n");
        } else {
            // Форматирование таблицы
            String format = "%-35s | %12s | %7s | %15s\n";
            reportContent.append(String.format(format, "Назва товару", "Ціна (грн)", "К-сть", "Всього (грн)"));
            reportContent.append("-".repeat(78)).append("\n"); // Разделитель

            for (CartItem item : cart.getItems()) {
                AbstractSportingGood product = item.getProduct();
                reportContent.append(String.format(format,
                        truncateString(product.getName(), 33), // Обрезаем длинные названия
                        String.format("%.2f", product.getPrice()),
                        item.getQuantity(),
                        String.format("%.2f", item.getTotalPrice())));
            }
            reportContent.append("-".repeat(78)).append("\n"); // Разделитель
            reportContent.append(String.format("%-58s %15.2f грн\n", // Скорректировал отступ
                    "ЗАГАЛЬНА СУМА:", cart.getTotalPrice()));

            int totalItems = 0;
            try {
                totalItems = cart.getTotalItemCount(); // Если метод существует
            } catch (NoSuchMethodError | AbstractMethodError e) {
                LOGGER.log(Level.INFO, "Метод getTotalItemCount() не найден в ShoppingCart, подсчитываем вручную.");
                for(CartItem item : cart.getItems()){
                    totalItems += item.getQuantity();
                }
            }
            reportContent.append(String.format("%-58s %15d од.\n", // Скорректировал отступ
                    "ЗАГАЛЬНА КІЛЬКІСТЬ ОДИНИЦЬ:", totalItems));
        }

        reportContent.append("========================================================================\n");
        reportContent.append("Кінець звіту.\n");

        return saveReportToFile(reportContent.toString(), filePath);
    }


    /**
     * Вспомогательный метод для добавления деталей товара в StringBuilder.
     * Этот метод используется всеми методами генерации отчетов для унификации.
     *
     * @param sb StringBuilder, в который добавляется информация о товаре.
     * @param product Товар, детали которого нужно добавить.
     */
    private void appendProductDetails(StringBuilder sb, AbstractSportingGood product) {
        sb.append("ID Товару:         ").append(product.getId()).append("\n");
        sb.append("Назва:             ").append(product.getName()).append("\n");
        sb.append("Бренд:             ").append(product.getBrand()).append("\n");
        sb.append(String.format("Ціна:              %.2f грн\n", product.getPrice()));
        sb.append("Загальний опис:    ").append(product.getDescription()).append("\n");

        // Используем полиморфизм для получения специфических деталей
        sb.append("Специфічні деталі: ").append(product.getSpecificDetails()).append("\n");

        // Пример дополнительной детализации с использованием instanceof, если это необходимо
        if (product instanceof Ball) {
            Ball ball = (Ball) product;
            sb.append("Тип м'яча:         ").append(ball.getBallType()).append("\n");
            sb.append("Рекоменд. тиск:    ").append(String.format("%.1f PSI\n", ball.getRecommendedPressurePsi()));
        } else if (product instanceof Racket) {
            Racket racket = (Racket) product;
            sb.append("Тип ракетки:       ").append(racket.getSportType()).append("\n");
            // Пример вызова другого метода из класса Racket
            sb.append("Рекоменд. натяжка (середн.): ").append(racket.recommendStringTension("intermediate")).append("\n");
        }
        // Добавьте аналогичные блоки для Footwear, Sportswear, Accessory, если нужна специфическая детализация сверх getSpecificDetails()
        sb.append("Рекомендації збер.:").append(product.getStorageRecommendations()).append("\n");
    }

    /**
     * Вспомогательный метод для сохранения сгенерированного текстового отчета в файл.
     * Создает родительские директории, если они не существуют.
     *
     * @param reportContent Строковое содержимое отчета.
     * @param filePath Полный путь к файлу для сохранения.
     * @return true, если сохранение прошло успешно, false - в противном случае.
     */
    private boolean saveReportToFile(String reportContent, String filePath) {
        File file = new File(filePath);
        File parentDir = file.getParentFile();

        // Создаем родительские директории, если их нет
        if (parentDir != null && !parentDir.exists()) {
            if (!parentDir.mkdirs()) {
                LOGGER.log(Level.SEVERE, "Не удалось создать родительские директории для файла отчета: " + filePath);
                return false;
            }
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(reportContent);
            LOGGER.log(Level.INFO, "Отчет успешно сохранен в: " + filePath);
            return true;
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Ошибка сохранения отчета в файл " + filePath, e);
            return false;
        }
    }

    /**
     * Вспомогательный метод для обрезки строки до максимальной длины, добавляя "..." если она была обрезана.
     * @param str Исходная строка.
     * @param maxLength Максимальная длина.
     * @return Обрезанная строка.
     */
    private String truncateString(String str, int maxLength) {
        if (str == null) {
            return "";
        }
        if (str.length() <= maxLength) {
            return str;
        }
        return str.substring(0, maxLength - 3) + "...";
    }
}