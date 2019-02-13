package com.takeaway.happyemployee.model.client;

import com.takeaway.happyemployee.model.persistence.FullName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDao {

    private String id;

    private String eMail;

    private FullName fullName;

    private LocalDate birthday;

    private List<String> hobbies;
}
