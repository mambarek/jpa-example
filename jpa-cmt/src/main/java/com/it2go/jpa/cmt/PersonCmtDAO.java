package com.it2go.jpa.cmt;

import com.it2go.jpa.dao.DomainEntity;
import com.it2go.jpa.entities.Person;

@DomainEntity(clazz = Person.class)
public class PersonCmtDAO extends EntityCmtDAO<Person> {
    @Override
    Class<Person> getEntityClass() {
        return Person.class;
    }
}
