package persistence;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import model.Department;

public class DepartmentDAO {

    public EntityManager getEntityManager() {
        final EntityManagerFactory factory = Persistence.createEntityManagerFactory("trabalhotbd");
        return factory.createEntityManager();
    }

    /* CREATE */
    public Department create(Department department) {
        EntityManager entityManager = getEntityManager();

        try {
            entityManager.getTransaction().begin();
            entityManager.persist(department);
            entityManager.getTransaction().commit();
        } finally {
            entityManager.close();
        }

        return department;
    }

    /* READ (ONE) */
    public Department findWithCode(int code) {
        EntityManager entityManager = getEntityManager();
        Department department = null;
        try {
            department = entityManager.find(Department.class, code);
        } finally {
            entityManager.close();
        }

        return department;
    }

    /* READ (ALL) */
    public List<Department> findAll() {
        EntityManager entityManager = getEntityManager();
        List<Department> departments = new ArrayList<Department>();

        try {
            departments = entityManager.createQuery("SELECT d FROM Department d", Department.class).getResultList();
            departments.forEach(department -> entityManager.refresh(department));
        } finally {
            entityManager.close();
        }

        return departments;
    }

    /* UPDATE */
    public Department update(Department department) throws Exception {
        EntityManager entityManager = getEntityManager();        

        try {
            if (!entityManager.contains(department)) {
                if (entityManager.find(Department.class, department.getCode()) == null) {
                    throw new Exception("Erro ao atualizar o setor");
                } 
            }
            entityManager.getTransaction().begin();
            department = entityManager.merge(department);
            entityManager.getTransaction().commit();
            entityManager.refresh(department);
        } finally {
            entityManager.close();
        }

        return department;
    }

    /* REMOVE */
    public void remove (int code) {
        EntityManager entityManager = getEntityManager();        

        try {
            entityManager.getTransaction().begin();
            
            Department department;            
            department = entityManager.getReference(Department.class, code);     
            
            entityManager.refresh(department);
            entityManager.remove(department);
            entityManager.getTransaction().commit();            
        } finally {
            if (entityManager != null) {
                entityManager.close();
            }
        }
    }
}
