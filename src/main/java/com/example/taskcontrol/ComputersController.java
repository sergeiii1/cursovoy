package com.example.taskcontrol;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Класс, представляющий контроллер для управления списком компьютеров.
 */
public class ComputersController implements Initializable {
    /**
     * Список компьютеров.
     */
    public ListView<Computer> computersList;

    /**
     * Метод для инициализации контроллера.
     * @param url - ссылка на ресурс
     * @param resourceBundle - ресурс
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        DatabaseAdapter.getDBConnection();
        Computers computers = new Computers();
        computersList.setItems(computers.observableList);
        DatabaseAdapter.getComputers(computers.observableList);
    }

    /**
     * Метод для выбора компьютера из списка.
     * @throws IOException - исключение ввода-вывода
     */
    @FXML
    private void select() throws IOException {
        if(!computersList.getSelectionModel().isEmpty()) {
            computersList.getScene().getWindow().hide();
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("computer-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            ((ComputerController) fxmlLoader.getController()).init(computersList.getSelectionModel().getSelectedItem().id);
            stage.setTitle("Управление компьютером");
            stage.setScene(scene);
            stage.show();
        }
    }

    /**
     * Метод для перехода на панель администратора.
     * @throws IOException - исключение ввода-вывода
     */
    @FXML
    private void admin() throws IOException {
        computersList.getScene().getWindow().hide();
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Панель администратора");
        stage.setScene(scene);
        stage.show();
    }

}
