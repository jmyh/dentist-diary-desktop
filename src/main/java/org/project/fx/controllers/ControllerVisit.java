package org.project.fx.controllers;

import javafx.beans.binding.BooleanBinding;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.project.dataset.Visit;
import org.project.exceptions.WrongDateFormat;
import org.project.fx.controllers.login.LoginController;

import java.net.URL;
import java.util.ResourceBundle;

public class ControllerVisit implements  Controller {
    private MainController mainController;

    @FXML
    private DatePicker dateVisit;

    @FXML
    private TextField costTF;

    @FXML
    private TextArea diagnosisTA;

    @FXML
    private TextArea healingTA;

    @FXML
    private Button saveBut;

    @FXML
    private Button cancelBut;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        mainController= LoginController.getMainController();
        setCheckText(costTF,"^[\\d\\.]+$");

        BooleanBinding bb = new BooleanBinding() {
            {
                super.bind(diagnosisTA.textProperty(),
                        healingTA.textProperty(),
                        dateVisit.getEditor().textProperty(),
                        costTF.textProperty());
            }

            @Override
            protected boolean computeValue() {
                return (diagnosisTA.getText().isEmpty() ||
                        healingTA.getText().isEmpty() ||
                        dateVisit.getValue()==null ||
                        costTF.getText().isEmpty());
            }
        };

        saveBut.disableProperty().bind(bb);
    }

    public void setDefaultValues(Visit visit) {
        if(visit!=null) {
            dateVisit.setValue(visit.getVisitDate());
            costTF.setText(Float.toString(visit.getCost()));
            diagnosisTA.setText(visit.getDiagnosis());
            healingTA.setText(visit.getHealing());
        }
    }

    @FXML
    public void saveVisit() throws WrongDateFormat {
        if (dateVisit.getValue()==null)
            throw new WrongDateFormat("Дата визита");
        Visit visit=mainController.addOrUpdateVisitToPatient(
                Float.parseFloat(costTF.getText().replace(",",".")),
                dateVisit.getValue(),
                healingTA.getText().trim(),
                diagnosisTA.getText().trim());
        cancel();
        mainController.updateVisitPane(visit);
    }

    @FXML
    public void cancel() {
        Stage stage = (Stage) cancelBut.getScene().getWindow();
        stage.close();
    }
}
