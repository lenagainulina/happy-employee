package com.takeaway.happyemployee.model.persistence;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "employees")
@EntityListeners(AuditingEntityListener.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Employee {

    @ApiModelProperty(notes = "Automatically generated UUID of an employee")
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", columnDefinition = "VARCHAR(255)")
    private String id;

    @ApiModelProperty(notes = "Employee's e-mail")
    @Column(unique = true)
    private String eMail;

    @ApiModelProperty(notes = "Employee's full name, consists of a first and a last name")
    @Convert(converter = FullNameConverter.class)
    private FullName fullName;

    @ApiModelProperty(notes = "Employee's birthday")
    private LocalDate birthday;

    @ApiModelProperty(notes = "A list of employee's hobbies")
    @ElementCollection
    private List<String> hobbies;
}
