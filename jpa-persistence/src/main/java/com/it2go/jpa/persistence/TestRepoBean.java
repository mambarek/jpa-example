package com.it2go.jpa.persistence;

import com.it2go.jpa.application.UserSession;
import com.it2go.jpa.dao.DomainEntity;
import com.it2go.jpa.dao.EntityConcurrentModificationException;
import com.it2go.jpa.dao.EntityNotPersistedException;
import com.it2go.jpa.dao.EntityRemovedException;
import com.it2go.jpa.dao.IEntityDAO;
import com.it2go.jpa.entities.Employee;
import com.it2go.jpa.entities.Gender;
import com.it2go.jpa.entities.Person;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Stateless
public class TestRepoBean {

    @EJB
    private UserSession userSession;

    @Inject
    @DomainEntity(clazz = Employee.class)
    private IEntityDAO<Employee> employeeDAO;


    //@Inject
   // private EmployeeCmtDAO employeeDAO;

    public Employee save(Employee employee) throws EntityConcurrentModificationException, EntityRemovedException {
        return employeeDAO.save(employee);
    }

    //@Override
    public List<Employee> findByFirstName(String firstName) {
        String query = "SELECT employee FROM Employee employee WHERE employee.firstName = ?1";

        return employeeDAO.getByQuery(query, firstName);
    }

    //@Override
    public List<Employee> findByLastName(String lastName) {

        String query = "SELECT employee FROM Employee employee WHERE employee.lastName = ?1";

        return employeeDAO.getByQuery(query, lastName);
    }

    //@Override
    public List<Employee> findByFullName(String firstName, String lastName) {
        String query = "SELECT employee FROM Employee employee WHERE employee.firstName = ?1 and employee.lastName = ?2";

        return employeeDAO.getByQuery(query, firstName, lastName);
    }

    //@Override
/*
    public List<Employee> findByBirthDate(LocalDate birthDate) {
        //String query = "SELECT employee FROM Employee employee WHERE employee.birthDate = :birthDate";
        String query = "SELECT employee FROM Employee employee WHERE employee.birthDate = ?1";
        EmployeeUmtDAO employeeUmtDAO = new EmployeeUmtDAO();

        return employeeUmtDAO.getByQuery(query, new Date());
    }
*/


    //@Override
    public Employee persist(Employee employee) throws EntityConcurrentModificationException, EntityRemovedException {
        Objects.requireNonNull(employee);
        // check children
        if (employee.getChildren() != null && employee.getChildren().size() > 0) {
            for (Person child : employee.getChildren()) {
                if (child.isNew()) {
                    child.setCreatedBy(userSession.getTestCreationUser());
                    child.setCreationDate(new Date());
                } else {
                    child.setUpdatedBy(userSession.getTestUpdateUser());
                    child.setLastUpdateTime(Timestamp.from((new Date()).toInstant()));
                }
            }
        }
        return this.employeeDAO.save(employee);
    }

    //@Override
    public Employee remove(Employee entity) throws EntityNotPersistedException {
        return employeeDAO.delete(entity);
    }

    //@Override
    public <Long> Employee findById(Long id) {
        return this.employeeDAO.getByIdentityKey(id);
    }

    //@Override
    public List<Employee> findAll() {
        return employeeDAO.getAll();
    }

    //@Override
    public int executeUpdate(String query) {
        return employeeDAO.executeUpdate(query);
    }

    //@Override
    public List<Employee> findByGender(Gender gender) {
        String query = "SELECT e from Employee e WHERE e.gender = ?1";

        return employeeDAO.getByQuery(query, gender);
    }
}
