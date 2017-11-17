package com.it2go.jpa.dao;

import com.it2go.jpa.entities.DomainEntity;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface IEntityDAO<T extends DomainEntity> {

    public T save(final T entity) throws EntityConcurrentModificationException, EntityRemovedException;
    public T delete(T entity) throws EntityNotPersistedException;
    public <K> T deleteByIdentityKey(final K key) throws EntityNotFoundException;
    public <K> T getByIdentityKey(final K key);
    public List<T> getAll();
    public List<T> getByQuery(final String jpaQuery, Object...params);
    public List<T> getByQuery(final String jpaQuery, Map<String,Object> paramMap);
    public int executeUpdate(final String query);
    public Date getLastUpdateTime(T entity);
    public List<T> testCriteriaAPI();
}
