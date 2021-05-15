package org.project.fx.controllers;

import javafx.fxml.Initializable;
import javafx.scene.control.TextField;

public interface Controller extends Initializable {

    default public void setCheckText(TextField textField, String matcher) {
        //t1 - новый текст, s - старый текст.
        textField.textProperty().addListener((observableValue, s, t1) -> {
            if (!t1.isEmpty()) {
                if (!t1.matches(matcher)) {
                    textField.setText(s);
                } else {
                    textField.setText(t1);
                }
            }
        });
    }
}
