module nntc.svg52.fxapp {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.prefs; // Класс Preferences позволяет сохранять данные между сеансами приложения
    requires java.sql;


    opens nntc.tsvetkova.myapp to javafx.fxml;
    exports nntc.tsvetkova.myapp;
}