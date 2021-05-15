package org.project.fx.controllers.login;

import javafx.beans.binding.BooleanBinding;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.project.main.Main;
import org.project.dbservice.DBService;
import org.project.exceptions.PasswordsNotMatchException;
import org.project.fx.controllers.Controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class RegistrationController implements Controller {
    private Stage primaryStage;
    @FXML
    private TextField loginTF;

    @FXML
    private TextField pwdTF;

    @FXML
    private TextField repeatPwdTF;

    @FXML
    private Button regBut;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        BooleanBinding bb = new BooleanBinding() {
            {
                super.bind(loginTF.textProperty(),
                        pwdTF.textProperty(),
                        repeatPwdTF.textProperty());
            }

            @Override
            protected boolean computeValue() {
                return (loginTF.getText().isEmpty()
                        || pwdTF.getText().isEmpty()
                        || repeatPwdTF.getText().isEmpty());
            }
        };
    }

    @FXML
    public void registration() throws IOException {
        if(pwdTF.getText().equals(repeatPwdTF.getText())) {
            DBService dbService=new DBService(loginTF.getText(),pwdTF.getText());
            dbService.closeConnection();

            Main main=new Main();
            main.showLoginForm(primaryStage);
            Stage stage = (Stage) regBut.getScene().getWindow();
            stage.close();
        } else new PasswordsNotMatchException();

    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage=primaryStage;
    }

}
