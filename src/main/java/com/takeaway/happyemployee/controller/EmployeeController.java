package com.takeaway.happyemployee.controller;

import com.takeaway.happyemployee.model.client.EmployeeDto;
import com.takeaway.happyemployee.model.persistence.Employee;
import com.takeaway.happyemployee.service.EmployeeService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;

@SuppressWarnings("unused")
@RestController
@RequestMapping("/employees")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }
    @ApiOperation(value = "Add an employee")
    @PostMapping
    public ResponseEntity<Employee> postEmployee(@Valid @RequestBody EmployeeDto createdEmployee) {
        Employee savedEmployee = employeeService.saveEmployee(createdEmployee);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedEmployee);
    }

    @ApiOperation(value = "Fetch a list of all employees")
    @GetMapping
    public ResponseEntity<List<Employee>> getAllEmployees() {
        return ResponseEntity.ok().body(employeeService.findAllEmployees());
    }

    @ApiOperation(value="Fetch an employee by id")
    @GetMapping("/{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable(value = "id") String id) {
        return ResponseEntity.ok().body(employeeService.retrieveEmployeeById(id));
    }

    @ApiOperation(value = "Update an employee by id")
    @PutMapping("/{id}")
    public ResponseEntity<Employee> updateEmployee(@PathVariable(value = "id") String id, @Valid @RequestBody EmployeeDto updatedEmployee) {
        return ResponseEntity.ok().body(employeeService.saveUpdatedEmployee(id, updatedEmployee));
    }

    @ApiOperation(value = "Delete an employee by id")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteEmployee(@PathVariable(value = "id") String id) {
        employeeService.deleteEmployeeById(id);
        return ResponseEntity.ok().body("Employee profile was successfully deleted!");
    }
}
