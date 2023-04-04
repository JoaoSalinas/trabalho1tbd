package controller;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import model.Employee;
import model.Patient;
import model.Treatment;
import persistence.EmployeeDAO;
import persistence.PatientDAO;
import persistence.TreatmentDAO;
import spark.ModelAndView;
import spark.TemplateViewRoute;

public class TreatmentController {

    private static TreatmentDAO treatmentDAO = new TreatmentDAO();
    private static PatientDAO patientDAO = new PatientDAO();
    private static EmployeeDAO employeeDAO = new EmployeeDAO();

    public static TemplateViewRoute addTreatment = (request, response) -> {
        Map<String, List<Treatment>> model = new HashMap<String, List<Treatment>>();
        JSONObject data = new JSONObject(request.body());

        System.out.println(data);

        Treatment treatment = new Treatment();

        try {
            treatment.setPatient(patientDAO.findWithCode(Integer.parseInt(data.getString("patient"))));
            treatment.setEmployee(employeeDAO.findWithCode(Integer.parseInt(data.getString("employee"))));
            treatment.setDate(new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(data.getString("date") + " " + data.getString("time")));
            treatment.setDiagnostic(data.getString("diagnostic"));
            treatment.setObservation(data.getString("observation"));

            treatmentDAO.create(treatment);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            model.put("treatments", treatmentDAO.findAll());
        }
        return new ModelAndView(model, "treatments.mustache");
    };

    public static TemplateViewRoute listTreatments = (request, response) -> {
        Map<String, Object> model = new HashMap<String, Object>();
        Map<String, Object> patientModel = new HashMap<String, Object>();
        Map<String, Object> employeeModel = new HashMap<String, Object>();
        Map<String, Object> treatmentModel = new HashMap<String, Object>();

        List<Object> treatments = new ArrayList<Object>();

        List<Patient> patients = patientDAO.findAll();
        List<Employee> employees = employeeDAO.findAll();

        for (Treatment treatment : treatmentDAO.findAll()) {
            treatmentModel = new HashMap<String, Object>();
            for (Patient patient : patients) {
                if (treatment.getPatient().getCode() == patient.getCode()) {
                    patientModel = new HashMap<>();

                    patientModel.put("code", patient.getCode());
                    patientModel.put("name", patient.getName());
                    patientModel.put("cpf", patient.getCpf());

                    List<Patient> patientsEdit = new ArrayList<Patient>(patients);
                    patientsEdit.remove(patient);
                    patientModel.put("patients", patientsEdit);
                }
            }
            for (Employee employee : employees) {
                if (treatment.getEmployee().getCode() == employee.getCode()) {
                    employeeModel = new HashMap<>();

                    employeeModel.put("code", employee.getCode());
                    employeeModel.put("name", employee.getName());
                    employeeModel.put("email", employee.getEmail());
                    employeeModel.put("password", employee.getPassword());
                    employeeModel.put("department", employee.getDepartment());

                    List<Employee> employeesEdit = new ArrayList<Employee>(employees);
                    employeesEdit.remove(employee);
                    employeeModel.put("employees", employeesEdit);
                }
            }
            treatmentModel.put("code", treatment.getCode());

            System.out.println("## HORA FORMATADA: ##" +  new SimpleDateFormat("hh:MM").format(treatment.getDate()));
            treatmentModel.put("dateFormatted", new SimpleDateFormat("yyyy-MM-dd").format(treatment.getDate()));
            treatmentModel.put("timeFormatted", new SimpleDateFormat("HH:mm").format(treatment.getDate()));

            treatmentModel.put("diagnostic", treatment.getDiagnostic());
            treatmentModel.put("observation", treatment.getObservation());

            treatmentModel.put("patient", patientModel);
            treatmentModel.put("employee", employeeModel);
            treatments.add(treatmentModel);
        }

        model.clear();
        model.put("treatments", treatments);
        model.put("patients", patients);
        model.put("employees", employees);

        return new ModelAndView(model, "treatments.mustache");
    };

    public static TemplateViewRoute updateTreatment = (request, response) -> {
        Map<String, Object> model = new HashMap<String, Object>();
        JSONObject data = new JSONObject(request.body());

        System.out.println(data);

        Treatment treatment = treatmentDAO.findWithCode(Integer.parseInt(request.params("code")));
        treatment.setPatient(patientDAO.findWithCode(Integer.parseInt(data.getString("patient"))));
        treatment.setEmployee(employeeDAO.findWithCode(Integer.parseInt(data.getString("employee"))));

        treatment.setDate(new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(data.getString("date") + " " + data.getString("time")));

        treatment.setDiagnostic(data.getString("diagnostic"));
        treatment.setObservation(data.getString("observation"));

        treatmentDAO.update(treatment);

        model.put("treatment", treatmentDAO.findAll());

        return new ModelAndView(model, "employees.mustache");
    };

    public static TemplateViewRoute deleteTreatment = (request, response) -> {
        Map<String, Object> model = new HashMap<String, Object>();

        treatmentDAO.remove(Integer.parseInt(request.params("code")));
        model.put("treatments", treatmentDAO.findAll());

        response.redirect("/atendimentos");
        return new ModelAndView(model, "treatments.mustache");
    };

    private static Map<String, String> getRequestData(String body) {
        Map<String, String> data = new HashMap<String, String>();

        for (String keyValue : body.split("&")) {
            String[] tokens = keyValue.split("=");
            String key = tokens[0];
            String value = tokens[1];

            data.put(key, value);
        }

        return data;
    }
}