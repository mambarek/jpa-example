package com.it2go.jpa.persistence;

import com.it2go.jpa.dao.DomainEntity;
import com.it2go.jpa.dao.EntityConcurrentModificationException;
import com.it2go.jpa.dao.EntityNotFoundException;
import com.it2go.jpa.dao.EntityNotPersistedException;
import com.it2go.jpa.dao.EntityRemovedException;
import com.it2go.jpa.dao.IEntityDAO;
import com.it2go.jpa.entities.Person;
import com.it2go.jpa.entities.Project;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.time.LocalDate;
import java.util.List;

@Stateless
public class ProjectRepository implements IProjectRepository {

    @Inject
    @DomainEntity(clazz = Project.class)
    private IEntityDAO<Project> projectDAO;

    @Override
    public Project persist(Project project, Person user) throws EntityConcurrentModificationException, EntityRemovedException {
        return projectDAO.save(project);
    }

    @Override
    public Project remove(Project entity) throws EntityNotPersistedException {
        return projectDAO.delete(entity);
    }

    @Override
    public <Long> Project removeById(Long id) throws EntityNotFoundException {
        return projectDAO.deleteByIdentityKey(id);
    }

    @Override
    public <Long> Project findById(Long id) {
        return projectDAO.getByIdentityKey(id);
    }

    @Override
    public List<Project> findAll() {
        return projectDAO.getAll();
    }

    @Override
    public int executeUpdate(String query) {
        return projectDAO.executeUpdate(query);
    }

    @Override
    public List<Project> findByName(String name) {
        final String query = "SELECT p FROM Project p WHERE p.name = ?1";

        return projectDAO.getByQuery(query, name);
    }

    @Override
    public List<Project> findByBeginDate(LocalDate beginDate) {
        final String query = "SELECT p FROM Project p WHERE p.beginDate = ?1";

        return projectDAO.getByQuery(query, beginDate);

    }


}
