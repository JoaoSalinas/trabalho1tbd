package controller;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import model.Department;
import persistence.DepartmentDAO;
import spark.ModelAndView;
import spark.TemplateViewRoute;

public class DepartmentController {

  private static DepartmentDAO departmentDAO = new DepartmentDAO();

  public static TemplateViewRoute addDepartment = (request, response) -> {
    Map<String, Object> model = new HashMap<String, Object>();
    JSONObject data = new JSONObject(request.body());

    Department department = new Department();

    try {
      department.setDescription(data.getString("description"));
      departmentDAO.create(department);
    } finally {
      model.put("departments", departmentDAO.findAll());
    }

    response.redirect("/setores");
    return new ModelAndView(model, "departments.mustache");
  };

  public static TemplateViewRoute listDepartments = (request, response) -> {
    Map<String, Object> model = new HashMap<String, Object>();

    DepartmentDAO departmentDAO = new DepartmentDAO();

    model.clear();
    model.put("departments", departmentDAO.findAll());
    return new ModelAndView(model, "departments.mustache");
  };

  public static TemplateViewRoute updateDepartment = (request, response) -> {
    Map<String, Object> model = new HashMap<String, Object>();
    JSONObject data = new JSONObject(request.body());    

    Department department = departmentDAO.findWithCode(Integer.parseInt(request.params("code")));
    department.setDescription(data.getString("description"));
    departmentDAO.update(department);

    model.put("departments", departmentDAO.findAll());

    return new ModelAndView(model, "departments.mustache");
  };

  public static TemplateViewRoute deleteDepartment = (request, response) -> {
    Map<String, Object> model = new HashMap<String, Object>();

    departmentDAO.remove(Integer.parseInt(request.params("code")));
    model.put("departments", departmentDAO.findAll());

    response.redirect("/setores");
    return new ModelAndView(model, "departments.mustache");
  };
}