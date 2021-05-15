package org.project.dao;


import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.project.dataset.Patient;
import org.project.dataset.Visit;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;


public class PatientDAO {

    private Session session;

    public PatientDAO(Session session) {
        this.session = session;
    }

    public Patient get(long id) throws HibernateException {
        return (Patient) session.get(Patient.class, id);
    }

    public HashSet<Patient> get(String surname, String name, String patronymic, LocalDate birthdate) throws HibernateException {
        Criteria criteria = session.createCriteria(Patient.class);
        List<Patient> patients=null;
        if(surname!=null && !surname.equals(""))
            criteria.add(Restrictions.eq("surname", surname));
        if(name!=null && !name.equals(""))
            criteria.add(Restrictions.eq("name", name));
        if(patronymic!=null && !patronymic.equals(""))
            criteria.add(Restrictions.eq("patronymic", patronymic));
        if(birthdate!=null)
                criteria.add(Restrictions.eq("birthdate", birthdate));
        return new HashSet<Patient>(criteria.list());
    }

    public List<Patient> getAll() throws HibernateException {
        Criteria criteria = session.createCriteria(Patient.class);
        return new ArrayList<>(new HashSet<Patient>(criteria.list()));
    }

    public Patient insertPatient(String name, String surname, String patronymic, LocalDate birthdate, String address) throws HibernateException {
        Patient patient=new Patient(name,surname,patronymic, birthdate, address);
        session.save(patient);
        return patient;
    }

    public Visit addVisit(float cost, LocalDate visitDate, String healing, String diagnosis, Patient patient) {
        Visit visit=new Visit(cost, visitDate, healing, diagnosis, patient);
        session.save(visit);
        return visit;
    }

    public void deletePatient(long patientId) {
        Patient patient=(Patient) session.load(Patient.class,patientId);
        if (patient != null) {
            session.delete(patient);
        }
//        String hql = "delete from Patient where id= :patientId";
//        session.createQuery(hql).setLong("patientId", patientId).executeUpdate();

    }

    public void deleteVisit(Patient patient, Visit visit) {
        String hql="delete from Visit where id= :visitId";
        patient.getVisits().remove(visit);
        session.createQuery(hql).setLong("visitId", visit.getId()).executeUpdate();
    }

    public Patient updatePatient(long patientId, String surname, String name, String patronymic, LocalDate birthdate, String address) {
        Patient updatablePatient=(Patient) session.load(Patient.class,patientId);

        updatablePatient.setSurname(surname);
        updatablePatient.setName(name);
        updatablePatient.setPatronymic(patronymic);
        updatablePatient.setBirthdate(birthdate);
        updatablePatient.setAddress(address);

        session.update(updatablePatient);

        return updatablePatient;
        //session.update(patient);
    }

    public Visit updateVisit(Visit visit) {
        Visit loadedVisit= (Visit) session.load(Visit.class,visit.getId());
        if(loadedVisit!=null) {
            loadedVisit.setVisitDate(visit.getVisitDate());
            loadedVisit.setCost(visit.getCost());
            loadedVisit.setHealing(visit.getHealing());
            loadedVisit.setDiagnosis(visit.getDiagnosis());
            session.update(loadedVisit);
        }
        return loadedVisit;
    }

}
