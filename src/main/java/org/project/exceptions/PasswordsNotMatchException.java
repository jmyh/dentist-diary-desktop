package org.project.exceptions;

import javafx.scene.control.Alert;

public class PasswordsNotMatchException extends Exception {
    public PasswordsNotMatchException() {
        Alert alert = new Alert(Alert.AlertType.ERROR);

        alert.setTitle("Ошибка");
        alert.setHeaderText("Введенные пароли не совпадают");
        alert.setContentText("Нажмите кнопку \"Ок\" для повторного ввода.");
        alert.showAndWait();
    }
}
