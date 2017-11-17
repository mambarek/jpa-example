package com.it2go.jpa.services;

import com.it2go.jpa.dao.EntityConcurrentModificationException;
import com.it2go.jpa.dao.EntityRemovedException;
import com.it2go.jpa.entities.Employee;

import javax.ejb.Local;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

@WebService
public interface EmployeeRepositoryService {

    @WebMethod
    public Employee save(Employee employee) throws EntityConcurrentModificationException, EntityRemovedException;

    @WebMethod
    public Employee getEmployeeById(Long id);
}
