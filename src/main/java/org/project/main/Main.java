package org.project.main;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.project.fx.controllers.login.LoginController;
import org.project.fx.controllers.login.RegistrationController;

import java.io.File;
import java.io.IOException;

public class Main extends Application {

    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        boolean first=checkFirstStart();
        if(first) {
            File dir=new File("DB");
            if(!dir.exists()) dir.mkdir();
            showRegistrationForm(primaryStage);
        } else {
            showLoginForm(primaryStage);
        }
    }

    public void showLoginForm(Stage primaryStage) {


        Parent content=null;
        FXMLLoader loader=new FXMLLoader(getClass().getResource("/org/project/LoginForm.fxml"));
        System.out.println(getClass().getResource("/org/project/LoginForm.fxml"));
        try {
            content = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }


        LoginController loginController=(LoginController) loader.getController();
        loginController.setPrimaryStage(primaryStage);

        Stage stage=new Stage();
        stage.setTitle("Вход");
        stage.setScene(new Scene(content));
        stage.setOnCloseRequest(e -> {Platform.exit();   System.exit(0);});
        stage.show();
    }

    private void showRegistrationForm(Stage primaryStage) {
        Parent content = null;
        System.out.println(getClass().getResource("/org/project/LoginForm.fxml"));
        FXMLLoader loader=new FXMLLoader(getClass().getResource("/org/project/RegistrationForm.fxml"));
        try {
            content = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        RegistrationController regController=loader.getController();
        regController.setPrimaryStage(primaryStage);

        Stage stage=new Stage();

        stage.setTitle("Регистрация");
        stage.setOnCloseRequest(e -> { Platform.exit();   System.exit(0);});
        stage.setScene(new Scene(content));
        stage.show();
    }

    private boolean checkFirstStart() {
        return !(new File("DB/Data Base.mv.db").exists());
    }



}
