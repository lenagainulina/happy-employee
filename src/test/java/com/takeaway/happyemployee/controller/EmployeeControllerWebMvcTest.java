package com.takeaway.happyemployee.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.takeaway.happyemployee.exception.ResourceNotFoundException;
import com.takeaway.happyemployee.model.client.EmployeeDto;
import com.takeaway.happyemployee.model.persistence.Employee;
import com.takeaway.happyemployee.model.persistence.FullName;
import com.takeaway.happyemployee.service.EmployeeService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static java.util.Collections.singletonList;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebAppConfiguration
@WebMvcTest(EmployeeController.class)
public class EmployeeControllerWebMvcTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private EmployeeService employeeService;

    private Employee employee;

    private EmployeeDto employeeNew;

    @Before
    public void setUp() {
        employee = mockEmployee();
        employeeNew = mockEmployeeNew();
    }

    @Test
    public void createEmployee_success() throws Exception {
        String json = mapper.writeValueAsString(employeeNew);
        given(this.employeeService.saveEmployee(employeeNew)).willReturn(employee);

        mvc.perform(post("/employees").with(httpBasic("user", "password"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isCreated());
    }

    @Test
    public void getAllEmployees_success() throws Exception {
        List<Employee> allEmployees = singletonList(employee);
        given(this.employeeService.findAllEmployees()).willReturn(allEmployees);

        mvc.perform(get("/employees")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(employee.getId()))
                .andExpect(jsonPath("$[0].fullName").value(employee.getFullName()))
                .andExpect(jsonPath("$[0].email").value(employee.getEMail()))
                .andExpect(jsonPath("$[0].hobbies").value(employee.getHobbies()))
                .andDo(print());
        verify(employeeService, times(1)).findAllEmployees();
        verifyNoMoreInteractions(employeeService);
    }

    @Test
    public void getEmployeeById_success() throws Exception {
        String id = employee.getId();
        when(this.employeeService.retrieveEmployeeById(id)).thenReturn(employee);

        mvc.perform(get("/employees/{id}", id))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.id").value(employee.getId()))
                .andExpect(jsonPath("$.fullName").value(employee.getFullName()))
                .andExpect(jsonPath("$.email").value(employee.getEMail()))
                .andExpect(jsonPath("$.hobbies").value(employee.getHobbies()));
        verify(employeeService, times(1)).retrieveEmployeeById(id);
        verifyNoMoreInteractions(employeeService);
    }

    @Test
    public void getEmployeeById_notFound() throws Exception {
        String id = "123fakeUUID";
        when(this.employeeService.retrieveEmployeeById(id)).thenThrow(new ResourceNotFoundException("Not found", "business profile", id));
        mvc.perform(get("/employees/{id}", id)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    public void updateEmployeeById_success() throws Exception {
        EmployeeDto updatedEmployeeDto = mockEmployeeUpdateDto();
        Employee updatedEmployee = mockUpdatedEmployee();
        String id = updatedEmployee.getId();
        String json = mapper.writeValueAsString(updatedEmployeeDto);

        when(this.employeeService.saveUpdatedEmployee(id, updatedEmployeeDto)).thenReturn(updatedEmployee);

        mvc.perform(put("/employees/{id}", id).with(httpBasic("user", "password"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(updatedEmployee.getId()))
                .andExpect(jsonPath("$.fullName").value(updatedEmployee.getFullName()))
                .andExpect(jsonPath("$.email").value(updatedEmployee.getEMail()))
                .andExpect(jsonPath("$.hobbies").value(updatedEmployee.getHobbies()));
    }

    @Test
    public void updateEmployeeById_notFound() throws Exception {
        EmployeeDto updatedEmployeeDto = mockEmployeeUpdateDto();
        String id = "123fakeUUID";
        String json = mapper.writeValueAsString(updatedEmployeeDto);
        when(this.employeeService.saveUpdatedEmployee(id, updatedEmployeeDto)).thenThrow(new ResourceNotFoundException("Not found", "business profile", id));
        mvc.perform(put("/employees/{id}", id).with(httpBasic("user", "password"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    public void deleteEmployeeById_success() throws Exception {
        String id = "123fakeUUID";
        mvc.perform(delete("/employees/{id}", id).with(httpBasic("user", "password"))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    public void deleteEmployeeById_notFound() throws Exception {
        String id = "123fakeUUID";
        doThrow(new ResourceNotFoundException()).when(employeeService).deleteEmployeeById(id);
        mvc.perform(delete("/employees/{id}", id).with(httpBasic("user", "password"))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    private EmployeeDto mockEmployeeNew() {
        EmployeeDto employee = new EmployeeDto();
        List<String> hobbies = mockHobbyList();
        employee.setEMail("aoneko3@gmx.de");
        employee.setFullName(mockFullName(null,"???"));
        employee.setBirthday(LocalDate.of(1986, 4, 24));
        employee.setHobbies(hobbies);
        return employee;
    }

    private EmployeeDto mockEmployeeUpdateDto() {
        EmployeeDto employee = new EmployeeDto();
        List<String> hobbies = mockHobbyList();
        employee.setEMail("aoneko2@gmx.de");
        employee.setFullName(mockFullName("Lena","Gainulina"));
        employee.setBirthday(LocalDate.of(1996, 4, 24));
        employee.setHobbies(hobbies);
        return employee;
    }

    private Employee mockUpdatedEmployee() {
        Employee employee = new Employee();
        employee.setId(UUID.randomUUID().toString());
        employee.setEMail(mockEmployeeUpdateDto().getEMail());
        employee.setFullName(mockFullName("Lena","Gainulina"));
        employee.setBirthday(mockEmployeeUpdateDto().getBirthday());
        employee.setHobbies(mockEmployeeUpdateDto().getHobbies());
        return employee;
    }

    private Employee mockEmployee() {
        Employee employee = new Employee();
        employee.setId(UUID.randomUUID().toString());
        employee.setEMail(mockEmployeeNew().getEMail());
        employee.setFullName(mockFullName(null,"???"));
        employee.setBirthday(mockEmployeeNew().getBirthday());
        employee.setHobbies(mockHobbyList());
        return employee;
    }

    private List<String> mockHobbyList() {
        List<String> hobbyList = new ArrayList<>();
        hobbyList.add("yoga");
        hobbyList.add("running");
        return hobbyList;
    }

    private FullName mockFullName(String firstName, String lastName){
        FullName fullName = new FullName();
        fullName.setFirstName(firstName);
        fullName.setLastName(lastName);
        return fullName;
    }
}
