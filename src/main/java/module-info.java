module com.example.taskcontrol {
    requires javafx.controls;
    requires javafx.fxml;
    requires j.interop;
    requires java.logging;
    requires jna;
    requires platform;
    requires com4j;
    requires java.sql;
    requires h2;

    opens com.example.taskcontrol to javafx.fxml;
    exports com.example.taskcontrol;
}