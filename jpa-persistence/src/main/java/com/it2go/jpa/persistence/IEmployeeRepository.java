package com.it2go.jpa.persistence;

import com.it2go.jpa.entities.Employee;
import com.it2go.jpa.entities.Gender;

import javax.ejb.Local;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Local
public interface IEmployeeRepository extends IEntityRepository<Employee>{

    public List<Employee> findByFirstName(String name);
    public List<Employee> findByLastName(String name);
    public List<Employee> findByFullName(String firstName, String lastName);
    public List<Employee> findByBirthDate(LocalDate birthDate);
    public List<Employee> findByGender(Gender gender);
    public Date getLastUpdate(Employee employee);
}
