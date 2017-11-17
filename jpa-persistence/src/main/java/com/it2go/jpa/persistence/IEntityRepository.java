package com.it2go.jpa.persistence;

import com.it2go.jpa.dao.EntityConcurrentModificationException;
import com.it2go.jpa.dao.EntityNotFoundException;
import com.it2go.jpa.dao.EntityNotPersistedException;
import com.it2go.jpa.dao.EntityRemovedException;
import com.it2go.jpa.entities.DomainEntity;
import com.it2go.jpa.entities.Person;

import java.util.List;

public interface IEntityRepository<T extends DomainEntity> {

    public T persist(T entity, Person user) throws EntityConcurrentModificationException, EntityRemovedException;
    public T remove(T entity) throws EntityNotPersistedException;
    public <K> T removeById(K id) throws EntityNotFoundException;
    public <K> T findById(K id);
    public List<T> findAll();
    public int executeUpdate(final String query);
}
