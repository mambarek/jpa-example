package com.it2go.jpa.application;

import com.it2go.jpa.dao.DomainEntity;
import com.it2go.jpa.dao.IEntityDAO;
import com.it2go.jpa.entities.Person;
import com.it2go.jpa.persistence.IPersonRepository;
import com.it2go.jpa.persistence.PersonRepository;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.List;

@Stateless
public class UserSession {

    @EJB
    private IPersonRepository personRepository;

    /*
    @Inject
    @DomainEntity(clazz = Person.class)
    private IEntityDAO<Person> personDAO;
    */
    public Person getLoggedInUser(){
        final Person user;

        final List<Person> byFullName = personRepository.findByFullName("Admin", "man");
        if(byFullName.size() > 0)
            user = byFullName.get(0);
        else
            user = null;

        return user;
    }

    public Person getTestCreationUser(){
        final Person user;


        String query = "SELECT p FROM Person p WHERE p.firstName = 'Admin' and p.lastName = 'man'";

        //final List<Person> byFullName = personDAO.getByQuery(query);
        final List<Person> byFullName = personRepository.findByFullName("Admin", "man");

        if(byFullName.size() > 0)
            user = byFullName.get(0);
        else
            user = null;

        return user;
    }

    public Person getTestUpdateUser(){
        final Person user;
        //PersonUmtDAO personUmtDAO = new PersonUmtDAO();
        String query = "SELECT p FROM Person p WHERE p.firstName = 'Update' and p.lastName = 'man'";
        //final List<Person> byFullName = personDAO.getByQuery(query);
        final List<Person> byFullName = personRepository.findByFullName("Update", "man");
        if(byFullName.size() > 0)
            user = byFullName.get(0);
        else
            user = null;

        return user;
    }
}
