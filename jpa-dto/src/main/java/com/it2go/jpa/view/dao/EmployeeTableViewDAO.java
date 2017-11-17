package com.it2go.jpa.view.dao;

import com.it2go.jpa.entities.Employee;
import com.it2go.jpa.entities.Person;
import com.it2go.jpa.view.model.EmployeeTableItem;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CompoundSelection;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.Metamodel;
import java.util.List;
import java.util.Map;

public class EmployeeTableViewDAO {

    @Inject
    private EntityManager entityManager;

    public List<EmployeeTableItem> getEmployeeItems() {
        String query = "select new com.it2go.jpa.view.model.EmployeeTableItem(empl.id, empl.firstName, empl.lastName, p.firstName, empl.creationDate)" +
                " from Employee as empl left outer join empl.createdBy as p";

        final List<EmployeeTableItem> result;

        TypedQuery<EmployeeTableItem> typedQuery = entityManager.createQuery(query, EmployeeTableItem.class);
        result = typedQuery.getResultList();

        return result;
    }

    public List<EmployeeTableItem> getEmployeeItems(Map<String, Object> params) {
        final StringBuilder query = new StringBuilder();

        query.append("select new com.it2go.jpa.view.model.EmployeeTableItem(empl.id, empl.firstName, empl.lastName, p.firstName, empl.creationDate)" +
                " from Employee as empl left outer join empl.createdBy as p ");

        String where = "";

        if(params != null){
            query.append(" where ");
            params.forEach((s, o) -> query.append("empl." + s + "=").append("'"+ o +"'"));
        }

        //System.out.println(">>> query = " + query);

        final List<EmployeeTableItem> result;

        TypedQuery<EmployeeTableItem> typedQuery = entityManager.createQuery(query.toString(), EmployeeTableItem.class);
        result = typedQuery.getResultList();

        return result;
    }

    public List<EmployeeTableItem> getItemsUsingCriteria(Map<String, Object> map){

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();

        CriteriaQuery<EmployeeTableItem> criteriaQuery = criteriaBuilder.createQuery(EmployeeTableItem.class);
        Root<Employee> employeeRoot = criteriaQuery.from(Employee.class);

        Join<Employee,Person> join = employeeRoot.join("createdBy", JoinType.LEFT);

        final CompoundSelection<EmployeeTableItem> compoundSelection = criteriaBuilder.construct(EmployeeTableItem.class,
                employeeRoot.get("id"),
                employeeRoot.get("firstName"),
                employeeRoot.get("lastName"),
                employeeRoot.get("createdBy").get("firstName"),
                employeeRoot.get("creationDate"));

        final Order orderBy = criteriaBuilder.asc(employeeRoot.get("firstName"));
        final CriteriaQuery<EmployeeTableItem> select = criteriaQuery.select(compoundSelection).orderBy(orderBy);
        final TypedQuery<EmployeeTableItem> query = entityManager.createQuery(select);
        //query.setMaxResults(2);
        query.setFirstResult(1);
        final List<EmployeeTableItem> resultList = query.getResultList();
        System.out.println("resultList = " + resultList.size());

        return resultList;
    }
}
