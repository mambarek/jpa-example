package com.it2go.jpa.view;

import com.it2go.jpa.view.dao.EmployeeTableViewDAO;
import com.it2go.jpa.view.model.EmployeeTableItem;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.List;
import java.util.Map;

@Stateless
public class EmployeeViewRepository {

    @Inject
    private EmployeeTableViewDAO employeeTableViewDAO;

    public List<EmployeeTableItem> getEmployeeItems() {
        return employeeTableViewDAO.getEmployeeItems();
    }

    public List<EmployeeTableItem> getEmployeeItems(Map<String, Object> params) {
        return employeeTableViewDAO.getEmployeeItems(params);
    }

    public List<EmployeeTableItem> testCriteria(){
        return employeeTableViewDAO.getItemsUsingCriteria(null);
    }
}
