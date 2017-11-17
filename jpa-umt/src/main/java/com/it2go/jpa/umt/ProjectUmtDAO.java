package com.it2go.jpa.umt;

import com.it2go.jpa.dao.DomainEntity;
import com.it2go.jpa.entities.Project;

@DomainEntity(clazz = Project.class)
public class ProjectUmtDAO extends EntityUmtDAO<Project> {

    //@Override
    protected Class<Project> getEntityClass() {
        return Project.class;
    }

}
