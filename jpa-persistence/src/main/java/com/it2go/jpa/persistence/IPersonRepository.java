package com.it2go.jpa.persistence;

import com.it2go.jpa.entities.Gender;
import com.it2go.jpa.entities.Person;

import java.time.LocalDate;
import java.util.List;

public interface IPersonRepository extends IEntityRepository<Person> {

    public List<Person> findByFirstName(String name);
    public List<Person> findByLastName(String name);
    public List<Person> findByFullName(String firstName, String lastName);
    public List<Person> findByBirthDate(LocalDate birthDate);
    public List<Person> findByGender(Gender gender);

    public List<Person> testCriteriaAPI();
}
