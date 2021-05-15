package org.project.exceptions;

import javafx.scene.control.Alert;

public class WrongPasswordException extends Exception {

    public WrongPasswordException() {
        Alert alert = new Alert(Alert.AlertType.ERROR);

        alert.setTitle("Ошибка входа");
        alert.setHeaderText("Неверный логин или пароль");
        alert.setContentText("Нажмите кнопку \"Ок\" для повторного ввода.");
        alert.showAndWait();
    }

}
