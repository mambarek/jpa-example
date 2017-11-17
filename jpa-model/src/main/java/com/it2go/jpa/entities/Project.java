package com.it2go.jpa.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@SuppressWarnings("JpaDataSourceORMInspection")
@Data
@Entity
public class Project extends DomainEntity {

    private String name;
    private double budget;
    private Date begin;
    private Date end;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "EMPL_ID")
    private Employee employee;

}
