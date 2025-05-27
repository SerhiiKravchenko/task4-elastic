package com.epam.learn.elastic.task4.controller;

import com.epam.learn.elastic.task4.dto.Employee;
import com.epam.learn.elastic.task4.service.EmployeeService;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("employee")
public class EmployeeController {

    private EmployeeService service;

    public EmployeeController(EmployeeService service) {
        this.service = service;
    }

    @GetMapping
    public List<Employee> getAllEmployee() {
        return service.getAllEmployee();
    }

    @GetMapping("/{employeeId}")
    public Employee getEmployeeById(@PathVariable String employeeId) {
        return service.getEmployeeById(employeeId);
    }

    @PostMapping("/{employeeId}")
    public String createEmployee(@PathVariable String employeeId, @RequestBody Employee employee) {
        return service.createEmployee(employeeId, employee);
    }

    @DeleteMapping("/{employeeId}")
    public void deleteEmployeeById(@PathVariable String employeeId) {
        service.deleteEmployee(employeeId);
    }

    @GetMapping("search/{field}/{value}")
    public List<Employee> searchEmployeeByFieldAndValue(@PathVariable String field, @PathVariable String value) {
        return service.searchEmployeeByFieldAndValue(field, value);
    }

    @GetMapping("aggregation/{field}/{metricType}/{metricField}")
    public String getAggregation(@PathVariable String field, @PathVariable String metricType, @PathVariable String metricField) {
        return service.getAggregation(field, metricType, metricField);
    }
}
