package org.project.dbservice;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.internal.SessionFactoryImpl;
import org.hibernate.service.ServiceRegistry;
import org.project.dao.PatientDAO;
import org.project.dataset.Patient;
import org.project.dataset.Visit;

import org.hibernate.SessionFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

//todo make executor
public class DBService {
    private static final String hibernate_show_sql = "true";
    private static final String hibernate_hbm2ddl_auto = "update";

    private final SessionFactory sessionFactory;

    public DBService(String login, String pwd) throws HibernateException {
        Configuration configuration = getH2Configuration(login, pwd);

        sessionFactory = createSessionFactory(configuration);
    }

    private Configuration getH2Configuration(String login, String pwd) {
        Configuration configuration = new Configuration();
        configuration.addAnnotatedClass(Patient.class);
        configuration.addAnnotatedClass(Visit.class);

        configuration.setProperty("hibernate.dialect", "org.hibernate.dialect.H2Dialect");
        configuration.setProperty("hibernate.connection.driver_class", "org.h2.Driver");
        configuration.setProperty("hibernate.connection.url", "jdbc:h2:./DB/Data base;CIPHER=AES");
        configuration.setProperty("hibernate.connection.username", login);
        configuration.setProperty("hibernate.connection.password", pwd+" "+pwd);
        configuration.setProperty("hibernate.show_sql", hibernate_show_sql);
        configuration.setProperty("hibernate.hbm2ddl.auto", hibernate_hbm2ddl_auto);
        return configuration;
    }


    public List<Patient> getAllPatients() {
        try {
            Session session = sessionFactory.openSession();
            PatientDAO dao = new PatientDAO(session);
            List<Patient> patients = dao.getAll();
            session.close();
            return patients;
        } catch (HibernateException e) {
            System.out.println(e);
        }
        return null;
    }

    public Patient addPatient(String name, String surname, String patronymic, LocalDate birthdate, String address) {
        try {
            Session session = sessionFactory.openSession();
            Transaction transaction = session.beginTransaction();
            PatientDAO dao = new PatientDAO(session);
            Patient patient = dao.insertPatient(name,surname,patronymic,birthdate,address);
            transaction.commit();
            session.close();
            return patient;
        } catch (HibernateException e) {
            System.out.println(e);
        }
        return null;
    }

    public Visit addVisit(float cost, LocalDate visitDate, String healing, String diagnosis, Patient patient) {
        try {
            Session session = sessionFactory.openSession();
            Transaction transaction = session.beginTransaction();
            PatientDAO dao = new PatientDAO(session);
            Visit visit=dao.addVisit(cost, visitDate, healing, diagnosis, patient);
            transaction.commit();
            session.close();
            return visit;
        } catch (HibernateException e) {
            System.out.println(e);
        }
        return null;
    }

    public void printConnectInfo() {
        try {
            SessionFactoryImpl sessionFactoryImpl = (SessionFactoryImpl) sessionFactory;
            Connection connection = sessionFactoryImpl.getConnectionProvider().getConnection();
            System.out.println("DB name: " + connection.getMetaData().getDatabaseProductName());
            System.out.println("DB version: " + connection.getMetaData().getDatabaseProductVersion());
            System.out.println("Driver: " + connection.getMetaData().getDriverName());
            System.out.println("Autocommit: " + connection.getAutoCommit());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static SessionFactory createSessionFactory(Configuration configuration) {
        StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder();
        builder.applySettings(configuration.getProperties());
        ServiceRegistry serviceRegistry = builder.build();
        return configuration.buildSessionFactory(serviceRegistry);
    }

    public Set<Patient> getPatients(String surname, String name, String patronymic, LocalDate birthday) {
        try {
            Session session = sessionFactory.openSession();
            Transaction transaction = session.beginTransaction();

            PatientDAO dao = new PatientDAO(session);

            Set<Patient> patients = dao.get(surname,name,patronymic,birthday);

            transaction.commit();
            session.close();
            return patients;
        } catch (HibernateException e) {
            System.out.println(e);
        }
        return null;
    }

    public Patient getPatient(Patient patient) {
        try {
            Session session = sessionFactory.openSession();
            Transaction transaction = session.beginTransaction();

            PatientDAO dao = new PatientDAO(session);

            Patient result = (Patient) dao.get(patient.getSurname(),patient.getName(),patient.getPatronymic(),patient.getBirthdate()).toArray()[0];

            transaction.commit();
            session.close();
            return result;
        } catch (HibernateException e) {
            System.out.println(e);
        }
        return null;
    }

    public void delPatient(long patientId) {
        try {
            Session session = sessionFactory.openSession();
            Transaction transaction = session.beginTransaction();
            PatientDAO dao = new PatientDAO(session);

            dao.deletePatient(patientId);

            transaction.commit();
            session.close();
        } catch (HibernateException e) {
            System.out.println(e);
        }
    }

    public void delVisit(Patient patient, Visit visit) {
        try {
            Session session = sessionFactory.openSession();
            Transaction transaction = session.beginTransaction();
            PatientDAO dao = new PatientDAO(session);

            dao.deleteVisit(patient,visit);

            transaction.commit();
            session.close();
        } catch (HibernateException e) {
            System.out.println(e);
        }
    }

    public Patient updatePatient(long patientId,String surname,String name,String patronymic,LocalDate birthdate,String address) {
        try {
            Session session = sessionFactory.openSession();
            Transaction transaction = session.beginTransaction();
            PatientDAO dao = new PatientDAO(session);
            Patient patient=dao.updatePatient(patientId, surname, name, patronymic, birthdate,address);
            transaction.commit();
            session.close();
            return patient;
        } catch (HibernateException e) {
            System.out.println(e);
        }
        return null;
    }

    public Visit updateVisit(Visit visit) {
        try {
            Session session = sessionFactory.openSession();
            Transaction transaction = session.beginTransaction();
            PatientDAO dao = new PatientDAO(session);
            dao.updateVisit(visit);
            transaction.commit();
            session.close();
            return visit;
        } catch (HibernateException e) {
            System.out.println(e);
        }
        return null;
    }

    public void closeConnection() {
        sessionFactory.close();
    }

}
