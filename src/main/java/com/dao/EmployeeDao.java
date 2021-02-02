package com.dao;

import java.util.ArrayList;
import javax.servlet.http.HttpServletRequest;

import com.model.Employee;

public interface EmployeeDao {
    Employee getEmployee(int empId);

    Employee getEmployee(HttpServletRequest request);

    void addEmployee(HttpServletRequest request);

    boolean isUsernameValid(String username);

    ArrayList<Employee> getEmployeeList(String searchString);

    void deleteEmployee(int empId);

    void updateEmployee(HttpServletRequest request);

    boolean employeeIdAlreadyExists(int empId);
}
