package org.project.dataset;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "visits")
public class Visit implements Serializable { // Serializable Important to Hibernate!
    // private static final long serialVersionUID = -8706689714326132798L;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "cost")
    private float cost;

    @Column(name="visit_date")
    private LocalDate visitDate;

    @Column(name = "healing")
    private String healing;

    @Column(name = "diagnosis")
    private String diagnosis;

    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "patient_id")
    private Patient patient;

    //Important to Hibernate!
    @SuppressWarnings("UnusedDeclaration")
    public Visit() {
    }

    public Visit(float cost, LocalDate visitDate, String healing, String diagnosis, Patient patient) {
        this.cost = cost;
        this.visitDate = visitDate;
        this.healing = healing;
        this.diagnosis = diagnosis;
        this.patient = patient;
    }

    public long getId() { return id; }

    public void setId(long id) { this.id = id; }

    public float getCost() {
        return cost;
    }

    public LocalDate getVisitDate() {
        return visitDate;
    }

    public String getHealing() {
        return healing;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public Patient getPatient() { return patient; }

    public void setPatient(Patient patient) { this.patient = patient; }

    public void setCost(float cost) {
        this.cost = cost;
    }

    public void setVisitDate(LocalDate visitDate) {
        this.visitDate = visitDate;
    }

    public void setHealing(String healing) {
        this.healing = healing;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        Visit visit = (Visit) o;
        return id == visit.getId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Visit{" +
                "id=" + id +
                ", cost=" + cost +
                ", visitDate=" + visitDate +
                ", healing='" + healing + '\'' +
                ", diagnosis='" + diagnosis + '\'' +
                ", patient=" + patient +
                '}';
    }
}
