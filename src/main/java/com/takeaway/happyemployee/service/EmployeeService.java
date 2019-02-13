package com.takeaway.happyemployee.service;

import com.takeaway.happyemployee.exception.EntityNotUniqueException;
import com.takeaway.happyemployee.exception.ResourceNotFoundException;
import com.takeaway.happyemployee.model.client.EmployeeDto;
import com.takeaway.happyemployee.model.persistence.Employee;
import com.takeaway.happyemployee.repository.EmployeeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeService {

    private EmployeeRepository employeeRepo;

    public EmployeeService(EmployeeRepository employeeRepo) {
        this.employeeRepo = employeeRepo;
    }

    public List<Employee> findAllEmployees() {
        return employeeRepo.findAll();
    }

    public Employee retrieveEmployeeById(String id) {
        return employeeRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Not found", "employee", id));
    }

    public Employee saveEmployee(EmployeeDto createdEmployee) {
        Employee employee = new Employee();
        employee.setEMail(createdEmployee.getEMail());
        employee.setFullName(createdEmployee.getFullName());
        employee.setBirthday(createdEmployee.getBirthday());
        employee.setHobbies(createdEmployee.getHobbies());

        String savedEMail = employee.getEMail();
        if (employeeRepo.existsByEMail(savedEMail)){
            throw new EntityNotUniqueException("This e-mail already exists");
        }

        return employeeRepo.save(employee);
    }

    public Employee saveUpdatedEmployee(String id, EmployeeDto updatedEmployee) {
        Employee employee = employeeRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Not found", "employee", id));
        String savedEMail = employee.getEMail();
        if (employeeRepo.existsByEMail(savedEMail)) {
            if (savedEMail.equals(updatedEmployee.getEMail())) {
                throw new EntityNotUniqueException("This e-mail already exists");
            }
        }
        updateNotEmptyFields(employee, updatedEmployee);

        return employeeRepo.save(employee);
    }

    private void updateNotEmptyFields(Employee employee, EmployeeDto updatedEmployee) {
        if (updatedEmployee.getEMail() != null) {
            employee.setEMail(updatedEmployee.getEMail());
        }

        if (updatedEmployee.getFullName() != null) {
            employee.setFullName(updatedEmployee.getFullName());
        }

        if (updatedEmployee.getBirthday() != null) {
            employee.setBirthday(updatedEmployee.getBirthday());
        }
        if (!updatedEmployee.getHobbies().isEmpty()) {
            employee.setHobbies(updatedEmployee.getHobbies());
        }
    }

    public void deleteEmployeeById(String id) {
        employeeRepo.deleteById(id);
    }
}