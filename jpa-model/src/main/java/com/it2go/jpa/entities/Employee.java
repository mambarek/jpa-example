package com.it2go.jpa.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@Entity
@XmlRootElement
public class Employee extends Person {

    private double salary;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "employee", orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Project> projects = new ArrayList<>();

    @Override
    public String toString() {
        Person p = (Person)this;
        return String.format("Employee[(%s)%s %s (%s EUR)]",this.getId(), this.getFirstName(), this.getLastName(), this.getSalary());
    }

    public void addProject(Project project){
        Objects.requireNonNull(project);
        project.setEmployee(this);

        projects.add(project);
    }

    public void removeProject(Project project){
        Objects.requireNonNull(project);
        project.setEmployee(null);
        this.projects.remove(project);
    }

}