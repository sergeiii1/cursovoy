package com.example.taskcontrol;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class HelloController implements Initializable {
    public ListView<Computer> computersList;
    public TextField computerTitle;
    public ListView<ErrorTask> eventList;
    Computers computers;
    ObservableList<ErrorTask> errorTasks= FXCollections.observableArrayList();;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        computers = new Computers();
        DatabaseAdapter.getDBConnection();
        DatabaseAdapter.getComputers(computers.observableList);
        computersList.setItems(computers.observableList);
        computersList.getSelectionModel().selectedItemProperty().addListener(e->{
            errorTasks.clear();
            DatabaseAdapter.getTasksNonFixed(errorTasks,computersList.getSelectionModel().getSelectedItem().id);
            eventList.setItems(errorTasks);
        });
    }
    @FXML
    private void fix()
    {
        if(eventList.getSelectionModel().getSelectedItem()!=null) {
            eventList.getSelectionModel().getSelectedItem().fixed = true;
            DatabaseAdapter.editTask(eventList.getSelectionModel().getSelectedItem());
            eventList.refresh();
        }
    }
    /**
     * new computer
     */
    @FXML
    private void addComputer()
    {
        computers.add(computerTitle.getText());
    }
    @FXML
    private void copyComputer() {
        if(computersList.getSelectionModel().getSelectedItem()!=null)
            computers.observableList.add(DatabaseAdapter.getComputer(DatabaseAdapter.addComputer(computersList.getSelectionModel().getSelectedItem().clone())));
    }
    /**
     * remove computer
     */
    @FXML
    private void removeComputer()
    {
        if(computersList.getSelectionModel().getSelectedItem()!=null)
            computers.remove(computersList.getSelectionModel().getSelectedItem());
    }
}