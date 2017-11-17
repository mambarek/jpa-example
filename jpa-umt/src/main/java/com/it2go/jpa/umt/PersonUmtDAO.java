package com.it2go.jpa.umt;

import com.it2go.jpa.dao.DomainEntity;
import com.it2go.jpa.entities.Person;

@DomainEntity(clazz = Person.class)
public class PersonUmtDAO extends EntityUmtDAO<Person> {
   // @Override
    protected Class<Person> getEntityClass() {
        return Person.class;
    }
}
