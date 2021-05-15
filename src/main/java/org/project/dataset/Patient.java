package org.project.dataset;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "patients")
public class Patient implements Serializable { // Serializable Important to Hibernate!
   // private static final long serialVersionUID = -8706689714326132798L;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "name")
    private String name;

    @Column(name = "surname")
    private String surname;

    @Column(name = "patronymic")
    private String patronymic;  //отчество

    @Column(name = "birthdate")
    private LocalDate birthdate;

    @Column(name = "address")
    private String address;

    @OneToMany (mappedBy="patient", fetch=FetchType.EAGER, cascade=CascadeType.ALL)
    private List<Visit> visits;

    //Important to Hibernate!
    @SuppressWarnings("UnusedDeclaration")
    public Patient() {
    }



    public Patient(String name, String surname, String patronymic, LocalDate birthdate, String address, List<Visit> visits) {
        this.name = name;
        this.surname = surname;
        this.patronymic = patronymic;
        this.birthdate = birthdate;
        this.address = address;
        this.visits = visits; //todo cloneable
    }

    public Patient(String name, String surname, String patronymic, LocalDate birthdate, String address) {
        this.name = name;
        this.surname = surname;
        this.patronymic = patronymic;
        this.birthdate = birthdate;
        this.address = address;
    }

//    public Patient(Patient currentPatient) {
//    }

    public long getId() { return id; }

    public void setId(long id) { this.id = id; }

    public String getName() { return name;}

    public void setName(String name) { this.name = name; }

    public String getSurname() { return surname;}

    public void setSurname(String surname) { this.surname = surname; }

    public String getPatronymic() { return patronymic; }

    public void setPatronymic(String patronymic) { this.patronymic = patronymic; }

    public LocalDate getBirthdate() { return birthdate; }

    public ObjectProperty<LocalDate> getBirthdateProperty() { return (new SimpleObjectProperty<LocalDate>(birthdate)); }

    public void setBirthdate(LocalDate birthdate) { this.birthdate = birthdate; }

    public String getAddress() { return address; }

    public void setAddress(String address) { this.address = address; }

    public List<Visit> getVisits() { return visits; } //todo проверить cloneable

    public void setVisits(List<Visit> visits) { this.visits = visits; }

    public void addVisit(Visit visit) {
        if (this.visits==null) {
            visits=new ArrayList();
            visits.add(visit);
        } else visits.add(visit);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        Patient patient = (Patient) o;
        return name.equals(patient.name) &&
                surname.equals(patient.surname) &&
                patronymic.equals(patient.patronymic) &&
                birthdate.equals(patient.birthdate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, surname, patronymic, birthdate);
    }

    @Override
    public String toString() {
        return "Patient{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", patronymic='" + patronymic + '\'' +
                ", birthdate=" + birthdate +
                ", address='" + address + '\'' +
                '}';
    }
}


