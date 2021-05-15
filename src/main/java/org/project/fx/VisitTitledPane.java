package org.project.fx;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import org.project.dataset.Visit;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class VisitTitledPane extends TitledPane {
    private Visit visit;

    private TextField costTF;
    private TextArea healingTA;
    private TextArea diagnosisTA;

    public VisitTitledPane(Visit visit) {

        setText(visit.getVisitDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));


        VBox pane=new VBox();

        HBox costPane=new HBox();
        costPane.getChildren().add(new Label("Стоимость (руб.):"));
        costTF=new TextField(Float.toString(visit.getCost()));
        costTF.setEditable(false);

        HBox.setHgrow(costTF, Priority.ALWAYS);
        HBox.setMargin(costTF, new Insets(0,0,0,15));
        costPane.getChildren().add(costTF);
        pane.getChildren().add(costPane);

        pane.getChildren().add(new Label("Диагноз"));

        diagnosisTA=new TextArea();
        diagnosisTA.setText(visit.getDiagnosis());
        diagnosisTA.setEditable(false);
        diagnosisTA.setMinHeight(50);
        diagnosisTA.setPrefHeight(1000);
        diagnosisTA.setWrapText(true);
        pane.getChildren().add(diagnosisTA);

        pane.getChildren().add(new Label("Лечение"));

        healingTA=new TextArea();
        healingTA.setText(visit.getHealing());
        healingTA.setEditable(false);
        healingTA.setMinHeight(50);
        healingTA.setPrefHeight(1000);
        healingTA.setWrapText(true);

        pane.getChildren().add(healingTA);
        setContent(pane);
        this.visit=visit;
    }

    public void updateVisitPane(LocalDate newDate, float newCost, String newHealing, String newDiagnosis) {
        this.visit.setVisitDate(newDate);
        this.visit.setCost(newCost);
        this.visit.setHealing(newHealing);
        this.visit.setDiagnosis(newDiagnosis);
        setText(newDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
        costTF.setText(Float.toString(newCost));
        healingTA.setText(newHealing);
        diagnosisTA.setText(newDiagnosis);
    }

    public Visit getVisit() {
        return visit;
    }
}
