package com.it2go.jpa.services;

import com.it2go.jpa.application.UserSession;
import com.it2go.jpa.dao.EntityConcurrentModificationException;
import com.it2go.jpa.dao.EntityRemovedException;
import com.it2go.jpa.entities.Employee;
import com.it2go.jpa.persistence.EmployeeRepository;
import com.it2go.jpa.persistence.IEmployeeRepository;

import javax.ejb.EJB;
import javax.jws.WebService;
import java.util.List;

@WebService(endpointInterface = "com.it2go.jpa.services.EmployeeRepositoryService",
        serviceName = "EmployeeRepositoryService")
public class EmployeeRepositoryServiceImpl implements EmployeeRepositoryService {

    @EJB
    private IEmployeeRepository employeeRepository;

    @EJB
    private UserSession userSession;

    public Employee save(Employee employee) throws EntityConcurrentModificationException, EntityRemovedException {
        final Employee persistedEmpl = employeeRepository.persist(employee, userSession.getTestCreationUser());

        System.out.println("persist.getId().getClass() = " + persistedEmpl.getId().getClass());
        return persistedEmpl;
    }

    public Employee getEmployeeById(Long id) {
        return employeeRepository.findById(id);
//        return null;
    }

    @Override
    public List<Employee> findAll() {
        return employeeRepository.findAll();
    }
}
