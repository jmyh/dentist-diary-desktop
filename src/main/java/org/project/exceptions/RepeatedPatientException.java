package org.project.exceptions;

import javafx.scene.control.Alert;

public class RepeatedPatientException extends Exception {
    public RepeatedPatientException() {
        Alert alert = new Alert(Alert.AlertType.ERROR);

        alert.setTitle("Ошибка");
        alert.setHeaderText("Ошибка при добавлении нового пациента");
        alert.setContentText("Вы не можете добавить нового пациента, так как он уже записан в базу данных\n" +
                "Нажмите кнопку \"Ок\" для продолжения работы");
        alert.showAndWait();
    }
}
