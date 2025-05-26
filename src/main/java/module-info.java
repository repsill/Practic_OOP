module com.example.sportsshop {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.logging;


    opens com.example.sportsshop to javafx.fxml;
    exports com.example.sportsshop;
    exports com.example.sportsshop.model;
    exports com.example.sportsshop.utils;
    opens com.example.sportsshop.model to javafx.fxml;
}