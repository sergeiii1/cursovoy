package com.example.taskcontrol;

import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Класс представляет объект задачи ошибки.
 * Содержит информацию о тексте ошибки, типе ошибки, дате и времени возникновения, ID компьютера, на котором произошла ошибка,
 * флаге исправления ошибки и ID задачи.
 */
public class ErrorTask extends Text {

    String error; // Текст ошибки
    TypeError type; // Тип ошибки
    LocalDateTime dateTime; // Дата и время возникновения ошибки
    int computerID; // ID компьютера, на котором произошла ошибка
    boolean fixed; // Флаг исправления ошибки
    int id; // ID задачи

    /**
     * Конструктор объекта задачи ошибки.
     * @param error Текст ошибки
     * @param dateTime Дата и время возникновения ошибки
     * @param fixed Флаг исправления ошибки
     * @param computerID ID компьютера, на котором произошла ошибка
     * @param type Тип ошибки
     * @param id ID задачи
     */
    public ErrorTask(String error, LocalDateTime dateTime, boolean fixed, int computerID, TypeError type, int id) {
        super();
        this.computerID = computerID;
        this.type = type;
        this.error = error;
        this.id = id;
        this.fixed = fixed;
        this.dateTime = dateTime;
        switch (type) {
            case ERROR -> this.setFill(Color.YELLOW);
            case CRITICAL -> this.setFill(Color.RED);
            case WARNING -> this.setFill(Color.GREEN);
        }
        this.setText(this.toString());
    }

    /**
     * Метод, возвращающий строковое представление задачи ошибки.
     * @return Строковое представление задачи ошибки
     */
    @Override
    public String toString() {
        String fixedStr = "";
        if (fixed) fixedStr = "(исправлено)";
        return error + "(" + dateTime.format(DateTimeFormatter.ofPattern("yy.MM.dd HH:mm:ss")) + ") " + fixedStr;
    }
}
