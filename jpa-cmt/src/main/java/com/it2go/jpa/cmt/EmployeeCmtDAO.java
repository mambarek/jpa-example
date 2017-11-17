package com.it2go.jpa.cmt;


import com.it2go.jpa.dao.DomainEntity;
import com.it2go.jpa.entities.Employee;

@DomainEntity(clazz = Employee.class)
public class EmployeeCmtDAO extends EntityCmtDAO<Employee> {

    @Override
    Class<Employee> getEntityClass() {
        return Employee.class;
    }
}
