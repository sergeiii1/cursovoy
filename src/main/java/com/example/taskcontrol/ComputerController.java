package com.example.taskcontrol;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Objects;
import java.util.ResourceBundle;

/**
 * Класс, представляющий контроллер для управления компьютером.
 */
public class ComputerController {
    /**
     * Текстовое поле для вывода ошибок.
     */
    public TextField errorText;

    /**
     * Выбор типа ошибки.
     */
    public ChoiceBox<TypeError> typeError;

    /**
     * Идентификатор компьютера.
     */
    int computerID;

    /**
     * Метод для инициализации контроллера.
     * @param computerID - идентификатор компьютера
     */
    public void init(int computerID)
    {
        this.computerID=computerID;
        typeError.getItems().addAll(TypeError.values());
        typeError.getSelectionModel().select(0);
    }

    /**
     * Метод для добавления ошибки.
     */
    @FXML
    private void addError()
    {
        if(!Objects.equals(errorText.getText(), "")) {
            DatabaseAdapter.getDBConnection();
            DatabaseAdapter.addErrorTask(new ErrorTask(errorText.getText(), LocalDateTime.now(), false, computerID,typeError.getSelectionModel().getSelectedItem(), -1));
            new Alert(Alert.AlertType.INFORMATION,"Ошибка отправлена администратору!", ButtonType.CLOSE).show();
        }
    }
}
