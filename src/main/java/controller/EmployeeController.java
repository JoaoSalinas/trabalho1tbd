package controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import model.Department;
import model.Employee;
import persistence.DepartmentDAO;
import persistence.EmployeeDAO;
import spark.ModelAndView;
import spark.TemplateViewRoute;

public class EmployeeController {

    private static EmployeeDAO employeeDAO = new EmployeeDAO();
    private static DepartmentDAO departmentDAO = new DepartmentDAO();

    public static TemplateViewRoute addEmployee = (request, response) -> {
        Map<String, Object> model = new HashMap<String, Object>();
        JSONObject data = new JSONObject(request.body());

        Employee employee = new Employee();

        try {
            employee.setName(data.getString("name"));
            employee.setEmail(data.getString("email"));
            employee.setPassword(data.getString("password"));
            employee.setDepartment(departmentDAO.findWithCode(Integer.parseInt(data.getString("department"))));
            employeeDAO.create(employee);
        } finally {
            model.put("employees", employeeDAO.findAll());
            model.put("selecteddepartment", departmentDAO.findWithCode(Integer.parseInt(data.getString("department"))));
        }

        response.redirect("/funcionarios");
        return new ModelAndView(model, "employees.mustache");
    };

    public static TemplateViewRoute listEmployees = (request, response) -> {
        Map<String, Object> model = new HashMap<String, Object>();
        Map<String, Object> employeeModel = new HashMap<String, Object>();

        EmployeeDAO employeeDAO = new EmployeeDAO();

        List<Object> employees = new ArrayList<Object>();

        List<Department> departments = departmentDAO.findAll();

        for (Employee employee : employeeDAO.findAll()) {
            for (Department department : departments) {
                if (employee.getDepartment().getCode() == department.getCode()) {
                    employeeModel = new HashMap<>();

                    employeeModel.put("code", employee.getCode());
                    employeeModel.put("name", employee.getName());
                    employeeModel.put("email", employee.getEmail());
                    employeeModel.put("password", employee.getPassword());
                    employeeModel.put("department", employee.getDepartment());

                    List<Department> departmentsEdit = new ArrayList<Department>(departments);
                    departmentsEdit.remove(department);
                    employeeModel.put("departments", departmentsEdit);
                }
            }

            employees.add(employeeModel);
        }

        model.clear();
        model.put("employees", employees);
        model.put("departments", departments);
        return new ModelAndView(model, "employees.mustache");
    };

    public static TemplateViewRoute updateEmployee = (request, response) -> {
        Map<String, Object> model = new HashMap<String, Object>();
    JSONObject data = new JSONObject(request.body());    

        Employee employee = employeeDAO.findWithCode(Integer.parseInt(request.params("code")));
        employee.setName(data.getString("name"));
        employee.setEmail(data.getString("email"));
        employee.setPassword(data.getString("password"));
        employee.setDepartment(departmentDAO.findWithCode(Integer.parseInt(data.getString("department"))));

        employeeDAO.update(employee);

        model.put("employee", employeeDAO.findAll());
        
        return new ModelAndView(model, "employees.mustache");
    };

    public static TemplateViewRoute deleteEmployee = (request, response) -> {
        Map<String, Object> model = new HashMap<String, Object>();

        employeeDAO.remove(Integer.parseInt(request.params("code")));
        model.put("employees", employeeDAO.findAll());

        response.redirect("/funcionarios");
        return new ModelAndView(model, "employees.mustache");
    };
}