package com.takeaway.happyemployee.service;

import com.takeaway.happyemployee.exception.EntityNotUniqueException;
import com.takeaway.happyemployee.exception.ResourceNotFoundException;
import com.takeaway.happyemployee.model.client.EmployeeDto;
import com.takeaway.happyemployee.model.persistence.Employee;
import com.takeaway.happyemployee.model.persistence.FullName;
import com.takeaway.happyemployee.repository.EmployeeRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.*;

@SuppressWarnings("unused")
@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.AUTO_CONFIGURED)
public class EmployeeServiceTest {
    @TestConfiguration
    @EnableJpaAuditing
    public static class EmployeeServiceTestConfiguration {
        @Autowired
        private EmployeeRepository employeeRepo;

        @Bean
        public EmployeeService createEmployeeService(){
            return new EmployeeService(employeeRepo);
        }
    }

    @Autowired
    private EmployeeService employeeService;

    private Employee savedEmployee;

    private EmployeeDto newEmployee;

    private EmployeeDto emptyNewEmployee;

    private EmployeeDto updatedEmployee;

    private EmployeeDto partiallyUpdatedEmployee;

    @Before
    public void setUp(){
        newEmployee = mockNewEmployee();
        emptyNewEmployee = mockEmptyNewEmployee();
        savedEmployee = employeeService.saveEmployee(newEmployee);
        updatedEmployee = mockUpdatedEmployee();
        partiallyUpdatedEmployee = mockPartiallyUpdatedEmployee();
    }

    @Test
    public void saveNewEmployee(){
        assertNotNull(savedEmployee);

        assertEquals(newEmployee.getEMail(),savedEmployee.getEMail());
        assertEquals(newEmployee.getBirthday(), savedEmployee.getBirthday());
        assertEquals(newEmployee.getHobbies(), newEmployee.getHobbies());
        assertEquals(newEmployee.getFullName(), savedEmployee.getFullName());
    }

    @Test //checks if the completely empty employee profile can be saved
    public void saveEmptyNewEmployee(){
        Employee savedEmptyEmployee = employeeService.saveEmployee(emptyNewEmployee);

        assertNotNull(savedEmptyEmployee);

        assertEquals(emptyNewEmployee.getEMail(),savedEmptyEmployee.getEMail());
        assertEquals(emptyNewEmployee.getBirthday(), savedEmptyEmployee.getBirthday());
        assertEquals(emptyNewEmployee.getHobbies(), savedEmptyEmployee.getHobbies());
        assertEquals(emptyNewEmployee.getFullName(), savedEmptyEmployee.getFullName());
    }

    @Test(expected = EntityNotUniqueException.class)
    public void saveNewEmployeeEmailExists(){
        Employee savedEmployee2 = employeeService.saveEmployee(newEmployee);
    }

    @Test
    public void saveFullyUpdatedEmployee(){
        Employee savedUpdatedEmployee = employeeService.saveUpdatedEmployee(savedEmployee.getId(), updatedEmployee);

        assertNotNull(savedUpdatedEmployee);

        assertEquals(updatedEmployee.getEMail(), savedUpdatedEmployee.getEMail());
        assertEquals(updatedEmployee.getBirthday(), savedUpdatedEmployee.getBirthday());
        assertEquals(updatedEmployee.getHobbies(), savedUpdatedEmployee.getHobbies());
        assertEquals(updatedEmployee.getFullName(), savedUpdatedEmployee.getFullName());
        assertEquals(updatedEmployee.getFullName().getFirstName(), savedUpdatedEmployee.getFullName().getFirstName());
        assertEquals(updatedEmployee.getFullName().getLastName(),savedUpdatedEmployee.getFullName().getLastName());
    }

    @Test //check that null fields are not updated
    public void savePartiallyUpdatedEmployee(){
        Employee savedUpdatedEmployee = employeeService.saveUpdatedEmployee(savedEmployee.getId(), partiallyUpdatedEmployee);

        assertNotNull(savedUpdatedEmployee);

        assertEquals(partiallyUpdatedEmployee.getEMail(), savedUpdatedEmployee.getEMail());
        assertEquals(partiallyUpdatedEmployee.getBirthday(), savedUpdatedEmployee.getBirthday());
        assertEquals(partiallyUpdatedEmployee.getHobbies(), savedUpdatedEmployee.getHobbies());
        // following fields must not be updated
        assertEquals(savedEmployee.getFullName(), savedUpdatedEmployee.getFullName());
        assertEquals(savedEmployee.getFullName().getFirstName(), savedUpdatedEmployee.getFullName().getFirstName());
        assertEquals(savedEmployee.getFullName().getLastName(),savedUpdatedEmployee.getFullName().getLastName());
    }

    @Test(expected = EntityNotUniqueException.class)
    public void saveFullyUpdatedEmployeeEmailExists(){
        Employee savedUpdatedEmployee = employeeService.saveUpdatedEmployee(savedEmployee.getId(), updatedEmployee);
        Employee savedUpdatedEmployee2 = employeeService.saveUpdatedEmployee(savedEmployee.getId(), updatedEmployee);
    }

    @Test
    public void findAllEmployees(){
        List<Employee> employees = employeeService.findAllEmployees();

        assertFalse(employees.isEmpty());

        assertEquals(savedEmployee.getId(),employees.get(0).getId());
        assertEquals(savedEmployee.getEMail(), employees.get(0).getEMail());
        assertEquals(savedEmployee.getFullName(), employees.get(0).getFullName());
        assertEquals(savedEmployee.getBirthday(), employees.get(0).getBirthday());
        assertEquals(savedEmployee.getHobbies(), employees.get(0).getHobbies());
    }

    @Test
    public void retrieveEmployeeById(){
        Employee retrievedEmployee = employeeService.retrieveEmployeeById(savedEmployee.getId());

        assertNotNull(retrievedEmployee);

        assertEquals(savedEmployee.getId(),retrievedEmployee.getId());
        assertEquals(savedEmployee.getEMail(), retrievedEmployee.getEMail());
        assertEquals(savedEmployee.getFullName(), retrievedEmployee.getFullName());
        assertEquals(savedEmployee.getBirthday(), retrievedEmployee.getBirthday());
        assertEquals(savedEmployee.getHobbies(), retrievedEmployee.getHobbies());
    }

    @Test(expected = ResourceNotFoundException.class)
    public void retrieveEmployeeById_notFound(){
        String id = "1234UUID";
        employeeService.retrieveEmployeeById(id);
    }

    @Test
    public void deleteEmployee_success(){
        int before = employeeService.findAllEmployees().size();
        employeeService.deleteEmployeeById(savedEmployee.getId());

        int after = employeeService.findAllEmployees().size();

        assertEquals((before - 1), after);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void deleteEmployee_notFound(){
        String id = "1234UUID";
        employeeService.deleteEmployeeById(id);
    }

    @Test
    public void saveFullName_concatFirstAndLastName(){
        FullName fullNameAfterUpdate = new FullName();
        fullNameAfterUpdate.setFirstName("Lena");
        fullNameAfterUpdate.setLastName("Gainulina");

        Employee savedUpdatedEmployee = employeeService.saveUpdatedEmployee(savedEmployee.getId(), partiallyUpdatedEmployee);

        assertEquals(fullNameAfterUpdate, savedUpdatedEmployee.getFullName());
        assertEquals(fullNameAfterUpdate.getFirstName(), savedUpdatedEmployee.getFullName().getFirstName());
        assertEquals(fullNameAfterUpdate.getLastName(),savedUpdatedEmployee.getFullName().getLastName());
    }

    private Employee mockEmployee(){
        Employee employee = new Employee();
        employee.setId(UUID.randomUUID().toString());
        employee.setEMail(mockNewEmployee().getEMail());
        employee.setFullName(mockNewEmployee().getFullName());
        employee.setBirthday(mockNewEmployee().getBirthday());
        employee.setHobbies(mockNewEmployee().getHobbies());
        return employee;
    }

    private EmployeeDto mockNewEmployee(){
        EmployeeDto employee = new EmployeeDto();
        employee.setEMail("aoneko@gmx.de");
        employee.setFullName(mockFullName("Lena", "Gainulina"));
        employee.setBirthday(LocalDate.of(1996,4,24));
        employee.setHobbies(mockHobbyList());
        return employee;
    }

    private EmployeeDto mockEmptyNewEmployee(){
        EmployeeDto employee = new EmployeeDto();
        employee.setEMail(null);
        employee.setFullName(null);
        employee.setBirthday(null);
        employee.setHobbies(null);
        return employee;
    }

    private EmployeeDto mockUpdatedEmployee(){
        EmployeeDto employee = new EmployeeDto();
        employee.setEMail("aoneko2@gmx.de");
        employee.setFullName(mockFullName("", "???"));
        employee.setBirthday(LocalDate.of(1986,4,24));
        employee.setHobbies(mockHobbyList());
        return employee;
    }

    private EmployeeDto mockPartiallyUpdatedEmployee(){
        EmployeeDto employee = new EmployeeDto();
        employee.setEMail("aoneko3@gmx.de");
        employee.setFullName(null);
        employee.setBirthday(LocalDate.of(1986,4,24));
        employee.setHobbies(mockHobbyList());
        return employee;
    }

    private FullName mockFullName(String firstName, String lastName){
        FullName fullName = new FullName();
        fullName.setFirstName(firstName);
        fullName.setLastName(lastName);
        return fullName;
    }

    private List<String> mockHobbyList(){
        List<String> hobbyList=new ArrayList<>();
        hobbyList.add("yoga");
        hobbyList.add("running");
        return hobbyList;
    }
}
