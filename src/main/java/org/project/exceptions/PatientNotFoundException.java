package org.project.exceptions;

import javafx.scene.control.Alert;

public class PatientNotFoundException extends Exception {
    public PatientNotFoundException() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);

        alert.setTitle("Пациент не найден");
        alert.setHeaderText("Запрашиваемый пациент не найден в базе данных!");
        alert.showAndWait();
    }
}
