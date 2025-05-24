module com.example.sportsshop {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.sportsshop to javafx.fxml;
    exports com.example.sportsshop;
}