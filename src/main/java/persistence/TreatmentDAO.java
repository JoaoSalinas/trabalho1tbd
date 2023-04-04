package persistence;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import model.Treatment;

public class TreatmentDAO {

    public EntityManager getEntityManager() {
        final EntityManagerFactory factory = Persistence.createEntityManagerFactory("trabalhotbd");        
        return factory.createEntityManager();
    }

    /* CREATE */
    public Treatment create (Treatment treatment) {
        EntityManager entityManager = getEntityManager();
        
        try{
            entityManager.getTransaction().begin();
            entityManager.persist(treatment);
            entityManager.getTransaction().commit();
        } finally {
            entityManager.close();
        }

        return treatment;
    }
    
    /* READ (ONE) */
    public Treatment findWithCode(int code) {
        EntityManager entityManager = getEntityManager();
        Treatment treatment = null;
        try {
            treatment = entityManager.find(Treatment.class, code);
        } finally {
            entityManager.close();
        }

        return treatment;
    }

    /* READ (ALL) */
    public List<Treatment> findAll() {
        EntityManager entityManager = getEntityManager();

        List<Treatment> treatments = new ArrayList<Treatment>();

        try {
            treatments = entityManager.createQuery("SELECT d FROM Treatment d", Treatment.class).getResultList();            
        } finally {
            entityManager.close();
        }

        return treatments;        
    }


/* UPDATE */
    public Treatment update(Treatment treatment) throws Exception{
        EntityManager entityManager = getEntityManager();
        
        try {
            if(!entityManager.contains(treatment)) {
                if(entityManager.find(Treatment.class, treatment.getCode()) == null) {
                    throw new Exception("Erro ao atualizar o atendimento");
                } 
            }
            entityManager.getTransaction().begin();
            entityManager.merge(treatment);
            entityManager.getTransaction().commit();

        } finally {
            entityManager.close();
        }

        return treatment;
    }


    /* REMOVE */
    public void remove (int code) {
        EntityManager entityManager = getEntityManager();

        try{
            entityManager.getTransaction().begin();
            Treatment treatment = entityManager.find(Treatment.class, code);

            entityManager.refresh(treatment);
            entityManager.remove(treatment);            
            entityManager.getTransaction().commit();
        } finally {
            if (entityManager != null) {
                entityManager.close();
            }
        }
    }    
}
