module com.example.sportsshop {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.sportsshop to javafx.fxml;
    exports com.example.sportsshop;
    exports com.example.sportsshop.model;
    opens com.example.sportsshop.model to javafx.fxml;
}