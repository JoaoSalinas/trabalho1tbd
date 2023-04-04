package persistence;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import model.Employee;

public class EmployeeDAO {

    public EntityManager getEntityManager() {
        final EntityManagerFactory factory = Persistence.createEntityManagerFactory("trabalhotbd");        
        return factory.createEntityManager();
    }

    /* CREATE */
    public Employee create (Employee employee) {
        EntityManager entityManager = getEntityManager();
        
        try{
            entityManager.getTransaction().begin();
            entityManager.persist(employee);
            entityManager.getTransaction().commit();
        } finally {
            entityManager.close();
        }

        return employee;
    }
    
    /* READ (ONE) */
    public Employee findWithCode(int code) {
        EntityManager entityManager = getEntityManager();
        Employee employee = null;
        try {
            employee = entityManager.find(Employee.class, code);
        } finally {
            entityManager.close();
        }

        return employee;
    }

    /* READ (ALL) */
    public List<Employee> findAll() {
        EntityManager entityManager = getEntityManager();

        List<Employee> employees = new ArrayList<Employee>();

        try {
            employees = entityManager.createQuery("SELECT d FROM Employee d", Employee.class).getResultList();            
        } finally {
            entityManager.close();
        }

        return employees;        
    }


/* UPDATE */
    public Employee update(Employee employee) throws Exception{
        EntityManager entityManager = getEntityManager();

        try {
            if (!entityManager.contains(employee)) {
                if (entityManager.find(Employee.class, employee.getCode()) == null) {
                    throw new Exception("Erro ao atualizar o funcionário");
                }
            }          

            entityManager.getTransaction().begin();
            entityManager.merge(employee);
            entityManager.getTransaction().commit();
        } finally {
            entityManager.close();
        }

        return employee;
    }



    /* REMOVE */
    public void remove (int code) {
        EntityManager entityManager = getEntityManager();
        
        try{
            entityManager.getTransaction().begin();
            
            Employee employee = entityManager.find(Employee.class, code);
            
            entityManager.refresh(employee);
            entityManager.remove(employee);
            entityManager.getTransaction().commit();
        } finally {
            if (entityManager != null) {
                entityManager.close();
            }
        }
    }    

}
