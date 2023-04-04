package view;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import config.MustacheTemplateEngine;
import controller.DepartmentController;
import controller.EmployeeController;
import controller.PatientController;
import controller.TreatmentController;
import model.Department;
import model.Employee;
import model.Patient;
import model.Treatment;
import persistence.DepartmentDAO;
import persistence.EmployeeDAO;
import persistence.PatientDAO;
import persistence.TreatmentDAO;
import spark.ModelAndView;
import spark.Spark;

import static spark.Spark.path;
import static spark.Spark.get;
import static spark.Spark.post;

import java.util.Date;
import java.util.HashMap;

public class Main {

    public static EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("trabalhotbd");

    public static void main(String[] args) throws Exception {
        Spark.staticFileLocation("/static");

        path("/", () -> {
            get("", (req, res) -> new ModelAndView(new HashMap<>(), "home.mustache"),
                    new MustacheTemplateEngine("pages"));

            path("/setores", () -> {
                get("", DepartmentController.listDepartments, new MustacheTemplateEngine("pages"));
                post("/adicionar", DepartmentController.addDepartment, new MustacheTemplateEngine("pages"));
                post("/atualizar/:code", DepartmentController.updateDepartment, new MustacheTemplateEngine("pages"));
                post("/deletar/:code", DepartmentController.deleteDepartment, new MustacheTemplateEngine("pages"));
            });

            path("/pacientes", () -> {
                get("", PatientController.listPatients, new MustacheTemplateEngine("pages"));
                post("/adicionar", PatientController.addPatient, new MustacheTemplateEngine("pages"));
                post("/atualizar/:code", PatientController.updatePatient, new MustacheTemplateEngine("pages"));
                post("/deletar/:code", PatientController.deletePatient, new MustacheTemplateEngine("pages"));
            });

            path("/funcionarios", () -> {
                get("", EmployeeController.listEmployees, new MustacheTemplateEngine("pages"));
                post("/adicionar", EmployeeController.addEmployee, new MustacheTemplateEngine("pages"));
                post("/atualizar/:code", EmployeeController.updateEmployee, new MustacheTemplateEngine("pages"));
                post("/deletar/:code", EmployeeController.deleteEmployee, new MustacheTemplateEngine("pages"));
            });

            path("/atendimentos", () -> {
                get("", TreatmentController.listTreatments, new MustacheTemplateEngine("pages"));
                post("/adicionar", TreatmentController.addTreatment, new MustacheTemplateEngine("pages"));
                post("/atualizar/:code", TreatmentController.updateTreatment, new MustacheTemplateEngine("pages"));
                post("/deletar/:code", TreatmentController.deleteTreatment, new MustacheTemplateEngine("pages"));
            });
        });

        DepartmentDAO departmentDAO = new DepartmentDAO();
        Department department = new Department("Cardiologia");
        departmentDAO.create(department);

        PatientDAO patientDAO = new PatientDAO();
        Patient patient = new Patient("joao", "123.456.789-10");
        patientDAO.create(patient);

        EmployeeDAO employeeDAO = new EmployeeDAO();
        Employee employee = new Employee("kim", "kim@email.com", "123456", department);
        employeeDAO.create(employee);

        TreatmentDAO treatmentDAO = new TreatmentDAO();
        treatmentDAO.create(new Treatment(employee, patient, new Date(), "Virose",
                "Paciente vomitando sangue e com tíbia exposta devido a acidente com caminhão."));
    }
}