package controller;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import model.Patient;
import persistence.PatientDAO;
import spark.ModelAndView;
import spark.TemplateViewRoute;

public class PatientController {

    private static PatientDAO patientDAO = new PatientDAO();

    public static TemplateViewRoute addPatient = (request, response) -> {
        Map<String, Object> model = new HashMap<String, Object>();
        JSONObject data = new JSONObject(request.body());

        Patient patient = new Patient();

        try {
            patient.setCpf(data.getString("cpf"));
            patient.setName(data.getString("name"));
            patientDAO.create(patient);
        } finally {
            model.put("patients", patientDAO.findAll());
        }

        return new ModelAndView(model, "patients.mustache");
    };

    public static TemplateViewRoute listPatients = (request, response) -> {
        Map<String, Object> model = new HashMap<String, Object>();

        PatientDAO patientDAO = new PatientDAO();

        model.clear();
        model.put("patients", patientDAO.findAll());
        return new ModelAndView(model, "patients.mustache");
    };

    public static TemplateViewRoute updatePatient = (request, response) -> {
        Map<String, Object> model = new HashMap<String, Object>();
        JSONObject data = new JSONObject(request.body());

        System.out.println(data);

        Patient patient = patientDAO.findWithCode(Integer.parseInt(request.params("code")));
        patient.setCpf(data.getString("cpf"));
        patient.setName(data.getString("name"));

        patientDAO.update(patient);

        model.put("patients", patientDAO.findAll());
        response.redirect("/pacientes");
        return new ModelAndView(model, "patients.mustache");
    };

    public static TemplateViewRoute deletePatient = (request, response) -> {
        Map<String, Object> model = new HashMap<String, Object>();

        patientDAO.remove(Integer.parseInt(request.params("code")));
        model.put("patients", patientDAO.findAll());

        response.redirect("/pacientes");
        return new ModelAndView(model, "patients.mustache");
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