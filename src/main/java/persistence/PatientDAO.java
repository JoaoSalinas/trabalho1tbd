package persistence;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import model.Patient;

public class PatientDAO {

    public EntityManager getEntityManager() {
        final EntityManagerFactory factory = Persistence.createEntityManagerFactory("trabalhotbd");
        return factory.createEntityManager();
    }

    /* CREATE */
    public Patient create(Patient patient) {
        EntityManager entityManager = getEntityManager();

        try {
            entityManager.getTransaction().begin();
            entityManager.persist(patient);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            entityManager.close();
        }

        return patient;
    }

    /* READ (ONE) */
    public Patient findWithCode(int code) {
        EntityManager entityManager = getEntityManager();
        Patient patient = null;
        try {
            patient = entityManager.find(Patient.class, code);
        } finally {
            entityManager.close();
        }

        return patient;
    }

    /* READ (ALL) */
    public List<Patient> findAll() {
        EntityManager entityManager = getEntityManager();

        List<Patient> patients = new ArrayList<Patient>();

        try {
            patients = entityManager.createQuery("SELECT d FROM Patient d", Patient.class).getResultList();
        } finally {
            entityManager.close();
        }

        return patients;
    }

    /* UPDATE */
    public Patient update(Patient patient) throws Exception {
        EntityManager entityManager = getEntityManager();

        try {
            if (!entityManager.contains(patient)) {
                if (entityManager.find(Patient.class, patient.getCode()) == null) {
                    throw new Exception("Erro ao atualizar o patiente");
                }
            }

            entityManager.getTransaction().begin();
            entityManager.merge(patient);
            entityManager.getTransaction().commit();
        } finally {
            entityManager.close();
        }

        return patient;
    }

    /* REMOVE */
    public void remove(int code) {
        EntityManager entityManager = getEntityManager();

        try {
            entityManager.getTransaction().begin();
            Patient patient;
            patient = entityManager.getReference(Patient.class, code);

            entityManager.remove(patient);
            entityManager.getTransaction().commit();
        } finally {
            if (entityManager != null) {
                entityManager.close();
            }
        }

    }
}
