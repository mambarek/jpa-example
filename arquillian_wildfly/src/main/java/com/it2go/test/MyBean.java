package com.it2go.test;

import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import java.io.Serializable;

@Stateful
public class MyBean implements Serializable {

    @PersistenceContext(type = PersistenceContextType.EXTENDED)
    private EntityManager entityManager;

    public void savePerson(final Person p) {
        entityManager.persist(p);
    }

    public Person getPerson(final long id) {
        return entityManager.find(Person.class, id);
    }
}