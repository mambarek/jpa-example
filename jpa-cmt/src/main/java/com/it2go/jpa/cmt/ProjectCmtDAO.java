package com.it2go.jpa.cmt;

import com.it2go.jpa.dao.DomainEntity;
import com.it2go.jpa.entities.Project;

import javax.ejb.Stateless;

@DomainEntity(clazz = Project.class)
public class ProjectCmtDAO extends EntityCmtDAO<Project> {
    @Override
    Class<Project> getEntityClass() {
        return Project.class;
    }
}
