package org.project.exceptions;

import javafx.scene.control.Alert;

public class WrongDateFormat extends Exception {
    public WrongDateFormat(String str) {
        Alert alert = new Alert(Alert.AlertType.ERROR);

        alert.setTitle("Ошибка");
        alert.setHeaderText("Неврный формат даты!");
        alert.setContentText("\""+str+"\" не соответствует \"дд.мм.гггг\"");
        alert.showAndWait();

    }
}
