package com.it2go.jpa.persistence;

import com.it2go.jpa.entities.Project;

import java.time.LocalDate;
import java.util.List;

public interface IProjectRepository extends IEntityRepository<Project> {
    public List<Project> findByName(String name);
    public List<Project> findByBeginDate(LocalDate beginDate);
}
