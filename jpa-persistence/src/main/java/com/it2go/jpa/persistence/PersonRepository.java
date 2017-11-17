package com.it2go.jpa.persistence;

import com.it2go.jpa.application.UserSession;
import com.it2go.jpa.dao.DomainEntity;
import com.it2go.jpa.dao.EntityConcurrentModificationException;
import com.it2go.jpa.dao.EntityNotFoundException;
import com.it2go.jpa.dao.EntityNotPersistedException;
import com.it2go.jpa.dao.EntityRemovedException;
import com.it2go.jpa.dao.IEntityDAO;
import com.it2go.jpa.entities.Gender;
import com.it2go.jpa.entities.Person;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Stateless
public class PersonRepository implements IPersonRepository {

/*    @Inject
    private UserSession userSession;*/

    @Inject
    @DomainEntity(clazz = Person.class)
    private IEntityDAO<Person> personDAO;

    @Override
    public List<Person> findByFirstName(String firstName) {
        final String query = "SELECT p FROM Person p WHERE p.firstName = ?1";

        return personDAO.getByQuery(query, firstName);
    }

    @Override
    public List<Person> findByLastName(String lastName) {
        final String query = "SELECT p FROM Person p WHERE p.lastName = ?1";

        return personDAO.getByQuery(query, lastName);
    }

    @Override
    public List<Person> findByFullName(String firstName, String lastName) {
        final String query = "SELECT p FROM Person p WHERE p.firstName = ?1 and p.lastName = ?2";

        return personDAO.getByQuery(query, firstName, lastName);
    }

    @Override
    public List<Person> findByBirthDate(LocalDate birthDate) {
        final String query = "SELECT p FROM Person p WHERE p.birthDate = ?1";
        Date date = Date.from(birthDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        return personDAO.getByQuery(query, date);
    }


    @Override
    public Person persist(Person person, Person user) throws EntityConcurrentModificationException, EntityRemovedException {
        Objects.requireNonNull(person);

        if(person.isNew()){
            person.setCreationDate(new Date());
            person.setCreatedBy(user);
        }else{
            person.setUpdatedBy(user);
            person.setLastUpdateTime(new Date());
        }
        // check children
/*        if(person.getChildren() != null && person.getChildren().size() > 0){
            for (Person child: person.getChildren()) {
                if (child.isNew()) {
                    child.setCreatedBy(userSession.getTestCreationUser());
                    child.setCreationDate(new Date());
                } else {
                    child.setUpdatedBy(userSession.getTestUpdateUser());
                    child.setLastUpdateTime(Timestamp.from((new Date()).toInstant()));
                }
            }
        }*/
        return personDAO.save(person);
    }

    @Override
    public Person remove(Person person) throws EntityNotPersistedException {
        return personDAO.delete(person);
    }

    @Override
    public <Long> Person removeById(Long id) throws EntityNotFoundException {
        return personDAO.deleteByIdentityKey(id);
    }

    @Override
    public <K> Person findById(K id) {
        return this.personDAO.getByIdentityKey(id);
    }

    @Override
    public List<Person> findAll() {
        return personDAO.getAll();
    }

    @Override
    public int executeUpdate(String query) {
        return personDAO.executeUpdate(query);
    }

    @Override
    public List<Person> findByGender(Gender gender){
        String query = "SELECT p from Person p WHERE p.gender = ?1";

        return personDAO.getByQuery(query, gender);
    }

    @Override
    public List<Person> testCriteriaAPI() {
        return personDAO.testCriteriaAPI();
    }
}
