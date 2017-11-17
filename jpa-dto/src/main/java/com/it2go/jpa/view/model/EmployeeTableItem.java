package com.it2go.jpa.view.model;

import java.util.Date;

public class EmployeeTableItem {
    private Long employeeId;
    private String firstName;
    private String lastName;
    private String createdByName;
    private Date creationDate;

    public EmployeeTableItem(Long employeeId, String firstName, String lastName, Date creationDate) {
        this.employeeId = employeeId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.creationDate = creationDate;
    }

    public EmployeeTableItem(Long employeeId, String firstName, String lastName, String createdByName, Date creationDate) {
        this.employeeId = employeeId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.createdByName = createdByName;
        this.creationDate = creationDate;
    }

    public String toString(){
        return String.format("EmployeeItem >> employeeId: %s firtName: %s lastName: %s crreatedBy: %s created at: %s", employeeId,firstName, lastName, createdByName, creationDate);
    }
}
