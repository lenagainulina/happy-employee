package com.takeaway.happyemployee.model.client;

import com.takeaway.happyemployee.model.persistence.FullName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDto {

    @Email
    private String eMail;

    private FullName fullName;

    private String firstName;

    private String lastName;

    private LocalDate birthday;

    private List<String> hobbies;
}
