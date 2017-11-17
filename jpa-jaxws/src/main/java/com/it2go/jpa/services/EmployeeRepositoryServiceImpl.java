package com.it2go.jpa.services;

import com.it2go.jpa.application.UserSession;
import com.it2go.jpa.dao.EntityConcurrentModificationException;
import com.it2go.jpa.dao.EntityRemovedException;
import com.it2go.jpa.entities.Employee;
import com.it2go.jpa.persistence.EmployeeRepository;
import com.it2go.jpa.persistence.IEmployeeRepository;

import javax.ejb.EJB;
import javax.jws.WebService;

@WebService(endpointInterface = "com.it2go.jpa.services.EmployeeRepositoryService",
        serviceName = "EmployeeRepositoryService")
public class EmployeeRepositoryServiceImpl implements EmployeeRepositoryService {

    @EJB
    private IEmployeeRepository employeeRepository;

    @EJB
    private UserSession userSession;

    public Employee save(Employee employee) throws EntityConcurrentModificationException, EntityRemovedException {
        return employeeRepository.persist(employee, userSession.getTestCreationUser());
//        return null;
    }

    public Employee getEmployeeById(Long id) {
        return employeeRepository.findById(id);
//        return null;
    }
}
