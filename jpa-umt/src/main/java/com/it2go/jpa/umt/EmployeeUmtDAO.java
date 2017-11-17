package com.it2go.jpa.umt;

import com.it2go.jpa.dao.DomainEntity;
import com.it2go.jpa.entities.Employee;

@DomainEntity(clazz = Employee.class)
public class EmployeeUmtDAO extends EntityUmtDAO<Employee> {

}
