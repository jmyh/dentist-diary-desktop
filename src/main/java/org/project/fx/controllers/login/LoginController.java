package org.project.fx.controllers.login;

import javafx.application.Platform;
import javafx.beans.binding.BooleanBinding;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.hibernate.HibernateException;
import org.project.exceptions.WrongPasswordException;
import org.project.fx.controllers.Controller;
import org.project.fx.controllers.MainController;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Controller {
    private Stage primaryStage;
    private static MainController mainController;

    @FXML
    private TextField loginTF;

    @FXML
    private TextField pwdTF;

    @FXML
    private Button loginBut;

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage=primaryStage;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        BooleanBinding bb = new BooleanBinding() {
            {
                super.bind(loginTF.textProperty(),
                        pwdTF.textProperty());
            }

            @Override
            protected boolean computeValue() {
                return (loginTF.getText().isEmpty()
                        || pwdTF.getText().isEmpty());
            }
        };
    }

    @FXML
    public void login() throws IOException, WrongPasswordException {
        try {
            startMainForm(loginTF.getText(), pwdTF.getText());
            Stage stage = (Stage) loginBut.getScene().getWindow();
            stage.close();
        } catch (HibernateException ex) {
            throw new WrongPasswordException();
        }
    }

    public void startMainForm(String login, String pwd) throws IOException {
        FXMLLoader loader=new FXMLLoader(getClass().getResource("/org/project/MainFormEx.fxml"));
        Parent content = null;
        try {
            content = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        mainController=loader.getController();
        mainController.setLogin(login);
        mainController.setPassword(pwd);
        mainController.connectToDB();

        primaryStage.setTitle("Dentist Diary");
        primaryStage.setScene(new Scene(content));

        primaryStage.setOnCloseRequest(e -> { Platform.exit();   System.exit(0);});
        primaryStage.show();
    }

    public static MainController getMainController() {
        return mainController;
    }
}
