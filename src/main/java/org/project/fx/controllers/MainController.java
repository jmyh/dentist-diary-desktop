package org.project.fx.controllers;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.project.dataset.Patient;
import org.project.dataset.Visit;
import org.project.dbservice.DBService;
import org.project.exceptions.PatientNotFoundException;
import org.project.exceptions.RepeatedPatientException;
import org.project.exceptions.WrongDateFormat;
import org.project.fx.VisitTitledPane;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class MainController implements Controller {

    private static ControllerVisit controllerVisit;
    private static DBService dbService;
    private static Patient currentPatient;
    private static Set setDB;
    private static boolean update = false;
    private static String login;
    private static String pwd;

    @FXML
    private TextField surnameTF;

    @FXML
    private TextField nameTF;

    @FXML
    private TextField patronymicTF;

    @FXML
    private DatePicker birthdateDP;

    @FXML
    private TextArea addressTA;

    @FXML
    private Button addPatientBut;

    @FXML
    private Button searchBut;

    @FXML
    private Button delPatientBut;

    @FXML
    private Button clearBut;

    @FXML
    private Button updatePatientBut;

    @FXML
    private Button addVisitBut;

    @FXML
    private Button delVisitBut;

    @FXML
    private Button editVisitBut;

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private TableView<Patient> DBTable;

    @FXML
    private TableColumn<Patient, String> surnameCol;

    @FXML
    private TableColumn<Patient, String> nameCol;

    @FXML
    private TableColumn<Patient, String> patronymicCol;

    @FXML
    private TableColumn<Patient, LocalDate> birthdateCol;

    @FXML
    private TableColumn<Patient, String> addressCol;

    @FXML
    private Accordion accordion;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        clearAutData();
        setCheckText(surnameTF, "^[А-Яа-я]+$");
        setCheckText(nameTF, "^[А-Яа-я]+$");
        setCheckText(patronymicTF, "^[А-Яа-я]+$");

        setCheckText(birthdateDP);

        delVisitBut.setDisable(true);
        editVisitBut.setDisable(true);

        scrollPane.setContent(accordion);
        accordion.expandedPaneProperty().addListener((ov, old_val, new_val) -> {
            if (new_val!=null) {
                delVisitBut.setDisable(false);
                editVisitBut.setDisable(false);
            } else {
                delVisitBut.setDisable(true);
                editVisitBut.setDisable(true);
            }
        });

        BooleanBinding bb2 = new BooleanBinding() {
            {
                super.bind(surnameTF.textProperty(),
                        nameTF.textProperty(),
                        patronymicTF.textProperty(),
                        birthdateDP.getEditor().textProperty(),
                        addressTA.textProperty());
            }

            @Override
            protected boolean computeValue() {
                return (surnameTF.getText().isEmpty()
                        || nameTF.getText().isEmpty()
                        || patronymicTF.getText().isEmpty()
                        || birthdateDP.getEditor().getText().isEmpty()
                        || addressTA.getText().isEmpty());
            }
        };

        addPatientBut.disableProperty().bind(bb2);
        updatePatientBut.disableProperty().bind(bb2);

        BooleanBinding searchBb = new BooleanBinding() {
            {
                super.bind(surnameTF.textProperty(),
                        nameTF.textProperty(),
                        patronymicTF.textProperty(),
                        birthdateDP.getEditor().textProperty(),
                        addressTA.textProperty());
            }

            @Override
            protected boolean computeValue() {
                return (surnameTF.getText().isEmpty()
                        && nameTF.getText().isEmpty()
                        && patronymicTF.getText().isEmpty()
                        && birthdateDP.getEditor().getText().isEmpty()
                        && addressTA.getText().isEmpty());
            }
        };
        searchBut.disableProperty().bind(searchBb);

        delPatientBut.disableProperty().bind(Bindings.isEmpty(DBTable.getSelectionModel().getSelectedItems()));
        addVisitBut.disableProperty().bind(Bindings.isEmpty(DBTable.getSelectionModel().getSelectedItems()));

        surnameCol.setCellValueFactory(new PropertyValueFactory<>("surname"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        patronymicCol.setCellValueFactory(new PropertyValueFactory<>("patronymic"));
        addressCol.setCellValueFactory(new PropertyValueFactory<>("address"));

        birthdateCol.setCellValueFactory(cellData -> cellData.getValue().getBirthdateProperty());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

        birthdateCol.setCellFactory(column -> {
            return new TableCell<Patient, LocalDate>() {
                @Override
                protected void updateItem(LocalDate date, boolean empty) {
                    super.updateItem(date, empty);

                    if (date == null || empty) {
                        setText("");
                    } else {
                        setText(formatter.format(date));
                    }
                }
            };
        });
    }

    public void connectToDB() {
        dbService = new DBService(login, pwd);
        dbService.printConnectInfo();
        displayPatients();
    }


    @FXML
    public void addPatient(ActionEvent ae) {
        try {
            String surname = surnameTF.getText().trim();
            String name = nameTF.getText().trim();
            String patronymic = patronymicTF.getText().trim();

            LocalDate birthdate = birthdateDP.getValue();
            if (birthdate == null) throw new WrongDateFormat(birthdateDP.getEditor().getText());

            String address = addressTA.getText().trim();

            //check repeat objects
            if (setDB.add(new Patient(name, surname, patronymic, birthdate, address))) {

                Patient patient = dbService.addPatient(name, surname, patronymic, birthdate, address);
                displayPatients(patient);
            } else throw new RepeatedPatientException();
        } catch (RepeatedPatientException | WrongDateFormat ignored) {}
    }

    @FXML
    public void updatePatient() {
        try {
            String surname = surnameTF.getText().trim();
            String name = nameTF.getText().trim();
            String patronymic = patronymicTF.getText().trim();

            LocalDate birthdate = birthdateDP.getValue();
            if (birthdate == null) throw new WrongDateFormat(birthdateDP.getEditor().getText());

            String address = addressTA.getText().trim();
            currentPatient = dbService.updatePatient(currentPatient.getId(), surname, name, patronymic, birthdate, address);
            displayPatients(currentPatient);
        } catch (WrongDateFormat ignored) {}
    }

    @FXML
    public void search() {
        try {
            String surname = surnameTF.getText();
            String name = nameTF.getText().trim();
            String patronymic = patronymicTF.getText().trim();
            LocalDate birthdate = birthdateDP.getValue();

            Set<Patient> patients = dbService.getPatients(surname, name, patronymic, birthdate);

            if (patients != null && patients.size() != 0) {
                displayPatients(patients);
            } else throw new PatientNotFoundException();

        } catch (PatientNotFoundException ignored) {}
    }

    @FXML
    public void delPatient() {
        Optional<ButtonType> confirm = confirmDelPatient(currentPatient);
        if (confirm.get() == ButtonType.OK) {
            dbService.delPatient(currentPatient.getId());
            clearFields();
            displayPatients();
            displayVisits();
        }
    }

    @FXML
    public void clearFields() {
        surnameTF.clear();
        nameTF.clear();
        patronymicTF.clear();
        birthdateDP.setValue(null);
        addressTA.clear();
        accordion.getPanes().removeAll();

        DBTable.getSelectionModel().clearSelection();
        currentPatient = null;
        displayPatients();
    }

    @FXML
    public void selectPatient() {
        currentPatient = DBTable.getSelectionModel().getSelectedItem();
        if (currentPatient == null) return;
        surnameTF.setText(currentPatient.getSurname());
        nameTF.setText(currentPatient.getName());
        patronymicTF.setText(currentPatient.getPatronymic());
        birthdateDP.setValue(currentPatient.getBirthdate());
        addressTA.setText(currentPatient.getAddress());

        displayVisits();
    }

    @FXML
    public void addVisit() {
        showVisitForm(null);
    }

    @FXML
    public void deleteVisit() {
        VisitTitledPane expendedPane=(VisitTitledPane) accordion.getExpandedPane();
        Visit visit=expendedPane.getVisit();
        Optional<ButtonType> confirm = confirmDelVisit(visit);
        if (confirm.get() == ButtonType.OK) {
            dbService.delVisit(currentPatient, visit);
            displayVisits();
        }
    }

    @FXML
    public void updateVisit() {
        VisitTitledPane expendedPane=(VisitTitledPane) accordion.getExpandedPane();
        Visit visit=expendedPane.getVisit();
        update=true;
        showVisitForm(visit);
    }

    public void setCheckText(DatePicker datePicker) {
        String numberMatcher = "^[\\d.]+$";
        //t1 - новый текст, s - старый текст.
        datePicker.getEditor().textProperty().addListener((observableValue, s, t1) -> {
            if (!t1.isEmpty()) {
                if (!t1.matches(numberMatcher)) {
                    datePicker.getEditor().setText(s);
                } else {
                    datePicker.getEditor().setText(t1);
                }
            }
        });
    }

    private Optional<ButtonType> confirmDelPatient(Patient patient) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Удаление пациента");
        alert.setHeaderText("Вы уверены, что хотите удалить пациента.");
        alert.setContentText(patient.toString() + "\nУдалятся также все посещения для этого пациента!");
        return alert.showAndWait();
    }

    private void displayPatients(Set<Patient> patients) {
        ObservableList<Patient> listDB = FXCollections.observableArrayList(patients);
        DBTable.setItems(listDB);
    }

    private void displayPatients(Patient patient) {
        List<Patient> patientList = dbService.getAllPatients();
        //exclude replicate objects
        setDB = new HashSet(patientList);
        ObservableList<Patient> listDB = FXCollections.observableArrayList(patientList);
        DBTable.setItems(listDB);
        DBTable.getColumns().get(0).setVisible(false);
        DBTable.getColumns().get(0).setVisible(true);
        if (patient != null) {
            DBTable.getSelectionModel().select(patient);
            DBTable.getSelectionModel().focus(DBTable.getSelectionModel().getSelectedIndex());
            selectPatient();
        }
    }

    private void displayPatients() {
        List<Patient> patientList = dbService.getAllPatients();
        //exclude replicate objects
        setDB = new HashSet(patientList);
        ObservableList<Patient> listDB = FXCollections.observableArrayList(patientList);
        DBTable.setItems(listDB);
        DBTable.getColumns().get(0).setVisible(false);
        DBTable.getColumns().get(0).setVisible(true);
    }

    public void displayVisits() {

        accordion.getPanes().removeAll(accordion.getPanes());
        if(currentPatient!=null) {
            currentPatient = dbService.getPatient(currentPatient);
            List<Visit> visitsList = currentPatient.getVisits();
            visitsList.sort(Comparator.comparing(Visit::getVisitDate).reversed());
            for (Visit visit : visitsList)
                genVisitElements(visit);
        }
    }

    public void updateVisitPane(Visit visit) {
        VisitTitledPane expandedPane=(VisitTitledPane) accordion.getExpandedPane();
        if (expandedPane!=null)
            expandedPane.updateVisitPane(visit.getVisitDate(), visit.getCost(), visit.getHealing(), visit.getDiagnosis());

        displayVisits();

        accordion.getPanes().stream()
                .map(pane -> (VisitTitledPane) pane)
                .filter(v -> v.getVisit().equals(visit))
                .findFirst()
                .ifPresent(newExpendedPane -> newExpendedPane.setExpanded(true));

    }

    private void genVisitElements(Visit visit) {
        VisitTitledPane pane = new VisitTitledPane(visit);
        accordion.getPanes().add(pane);
    }

    public void showVisitForm(Visit visit) {
        Parent content = null;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/project/VisitForm.fxml"));
        try {
            content = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        controllerVisit = loader.getController();

        Stage visitStage = new Stage();

        if (!update) visitStage.setTitle("Новое посещение");
        else visitStage.setTitle("Посещение: ");

        visitStage.initModality(Modality.WINDOW_MODAL);
        visitStage.initOwner(surnameTF.getScene().getWindow());

        visitStage.setScene(new Scene(content));
        controllerVisit.setDefaultValues(visit);
        visitStage.show();
    }

    public Visit addOrUpdateVisitToPatient(float cost, LocalDate dateVisit, String healing, String diagnosis) {
        Visit visit;
        if (update) {
            update = false;

            VisitTitledPane expendedPane = (VisitTitledPane) accordion.getExpandedPane();
            Visit selectedVisit = expendedPane.getVisit();
            Visit newVisit = new Visit(cost, dateVisit, healing, diagnosis, currentPatient);
            newVisit.setId(selectedVisit.getId());
            visit = dbService.updateVisit(newVisit);
        } else visit = dbService.addVisit(cost, dateVisit, healing, diagnosis, currentPatient);
        return visit;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setPassword(String pwd) {
        this.pwd = pwd;
    }

    private void clearAutData() {
        login = null;
        pwd = null;
    }

    private Optional<ButtonType> confirmDelVisit(Visit visit) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Удаление посещения");
        alert.setHeaderText("Вы уверены, что хотите удалить данное посещение.");
        alert.setContentText(visit.toString());
        return alert.showAndWait();
    }
}
