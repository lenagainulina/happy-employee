package com.takeaway.happyemployee.repository;

import com.takeaway.happyemployee.model.persistence.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, String> {
    boolean existsByEMail(String eMail);
}
