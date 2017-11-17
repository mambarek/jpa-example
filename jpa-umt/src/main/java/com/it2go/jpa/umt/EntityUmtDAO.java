package com.it2go.jpa.umt;

import com.it2go.jpa.application.UserSession;
import com.it2go.jpa.dao.IEntityDAO;
import com.it2go.jpa.entities.DomainEntity;
import com.it2go.jpa.entities.Person;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityNotFoundException;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.lang.reflect.ParameterizedType;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public abstract class EntityUmtDAO<T extends DomainEntity> implements IEntityDAO<T> {

    @Inject
    private EntityManagerFactory emf;

    private Class<T> entityClass;

    @Override
    public <Long> T deleteByIdentityKey(Long key) throws com.it2go.jpa.dao.EntityNotFoundException {
        return null; // TODO
    }
//protected abstract Class<T> getEntityClass();

    @Inject
    private UserSession userSession;

    @PostConstruct
    public void init(){
        ParameterizedType genericSuperclass = (ParameterizedType) getClass().getGenericSuperclass();
        this.entityClass = (Class<T>) genericSuperclass.getActualTypeArguments()[0];
    }

    @Override
    public T save(T entity) throws RuntimeException {
        System.out.println(">>> EntityUmtDAO call !!! <<<");
        Objects.requireNonNull(entity);
        final T res;
        final EntityManager em = emf.createEntityManager();
        final EntityTransaction tx = em.getTransaction();
        try {
            if(entity.isNew()) {

                entity.setCreationDate(new Date());
                entity.setCreatedBy(userSession.getTestCreationUser());
            }
            else{
/*
                T db_entity = this.getByIdentityKey(entity.getId());
                if (!db_entity.getLastUpdateTime().equals(entity.getLastUpdateTime()))
                    throw new ConcurrentModificationException();
*/

                entity.setUpdatedBy(userSession.getTestUpdateUser());
              //  entity.setLastUpdateTime(Timestamp.from((new Date()).toInstant()));
            }
            tx.begin();
            res =  em.merge(entity);
            //em.persist(entity);

            em.flush();
            tx.commit();
        } catch (RuntimeException ex) {
            if (tx != null && tx.isActive()) tx.rollback();
            throw ex;
        } finally {
            em.close();
        }

        return res;
    }

    @Override
    public T delete(T entity) throws RuntimeException  {

        Objects.requireNonNull(entity);
        if(entity.isNew()) throw new EntityNotFoundException();

        final T merged;
        final EntityManager em = emf.createEntityManager();
        final EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            merged  = em.find(entityClass, entity.getId());
            em.remove(merged);
            em.flush();
            tx.commit();
        } catch (RuntimeException ex) {
            if (tx != null && tx.isActive()) tx.rollback();
            throw ex;
        } finally {
            em.close();
        }

        return merged;
    }

    @Override
    public <K> T getByIdentityKey(K key) {
        final T entity;
        final EntityManager em = emf.createEntityManager();
        try {
            entity = em.find(entityClass, key);
        } finally {
            em.close();
        }

        return entity;
    }

    @Override
    public List<T> getAll() {
        final String simpleName = entityClass.getSimpleName();
        final String query = String.format("SELECT entity FROM %s entity", simpleName);
        final EntityManager em = emf.createEntityManager();
        final List<T> result;
        final TypedQuery<T> typedQuery;

        try {
            typedQuery = em.createQuery(query, entityClass);
            result = typedQuery.getResultList();
        } finally {
            em.close();
        }

        return result;
    }

    @Override
    public List<T> getByQuery(String jpaQuery, Object... params) {
        final EntityManager em = emf.createEntityManager();
        final List<T> result;
        TypedQuery<T> typedQuery;
        try {
            typedQuery = em.createQuery(jpaQuery, entityClass);

            if (params != null) {
                for (int i = 0; i < params.length; i++) {
                    typedQuery.setParameter(i+1, params[i]);
                }
            }

            result = typedQuery.getResultList();
            if(entityClass.equals(Person.class)) {
                for (T person : result) {
                    ((Person)person).getParents().size();
                    ((Person)person).getChildren().size();
                }
            }
        } finally {

            em.close();
        }

        return result;
    }

    @Override
    public List<T> getByQuery(String jpaQuery, Map<String, Object> paramMap) {
        final List<T> result;
        final EntityManager em = emf.createEntityManager();

        try{
            TypedQuery<T> typedQuery = em.createQuery(jpaQuery, entityClass);

            paramMap.forEach(typedQuery::setParameter);

            result = typedQuery.getResultList();

        }finally {
            em.close();
        }

        return result;
    }

    @Override
    public int executeUpdate(String query) {
        final int res;
        final EntityManager em = emf.createEntityManager();
        final EntityTransaction tx = em.getTransaction();
        try{
            tx.begin();
            res = em.createQuery(query).executeUpdate();
            tx.commit();
        }catch (RuntimeException ex) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            throw ex;
        } finally {
            em.close();
        }

        return res;
    }

    @Override
    public Date getLastUpdateTime(T entity) {
        return null;
    }

    public List<T> testCriteriaAPI(){

        final  EntityManager em = emf.createEntityManager();
        final CriteriaBuilder criteriaBuilder = emf.getCriteriaBuilder();
        CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(entityClass);

        Root<T> root = criteriaQuery.from(entityClass);

        return em.createQuery(criteriaQuery).getResultList();

    }


}
