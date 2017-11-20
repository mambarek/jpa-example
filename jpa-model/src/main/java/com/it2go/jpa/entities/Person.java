package com.it2go.jpa.entities;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Email;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.transaction.Transactional;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@SuppressWarnings("JpaDataSourceORMInspection")
@Entity
@Getter
@Setter
@Table(name = "PERSON")
public class Person extends DomainEntity{

    @Basic
    @Column(name = "FIRST_NAME", nullable = false)
    @Size(min = 3, max = 100)
    @NotNull
    private String firstName;

    @Basic
    @Column(name = "LAST_NAME", nullable = false)
    @Size(min = 3, max = 100)
    @NotNull
    private String lastName;

    @Basic
    @Column(name = "BIRTH_DATE")
    @Temporal(TemporalType.DATE)
    private Date birthDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "GENDER")
    private Gender gender = Gender.male;

    @Email(regexp = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])")
    private String mymail;

    @Embedded
    @AttributeOverrides(
            @AttributeOverride(name = "email", column = @Column(name = "E_MAIL"))
    )
    private EmailAddress embeddedEmail;

    @ElementCollection
    @CollectionTable(name = "EMAIL", joinColumns=@JoinColumn(name="OWNER_ID"))
    //private List<String> emails = new ArrayList<>();
    private List<EmailAddress> emails = new ArrayList<>();

    //@ManyToMany(mappedBy = "father", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    // LAZY-loading dont work with user managed persistence. the EntityManager is closed after
    // every transaction, fetching collections causes exception because the EM is closed (no connection to DB)
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
        /*
    @JoinTable(
            name="PERSON_CHILDREN",
            joinColumns=@JoinColumn(name="PERSON_ID", referencedColumnName="ID"),
            inverseJoinColumns=@JoinColumn(name="CHILD_ID", referencedColumnName="ID"))
    */
    private List<Person> children = new ArrayList<>();

    @ManyToMany(mappedBy = "children", fetch = FetchType.LAZY)
    private List<Person> parents = new ArrayList<>();

/*    @OneToOne
    private Person father;
    @OneToOne
    private Person mother;*/

    public void addChild(Person child){
        Objects.requireNonNull(child);

        child.addParent(this);

        this.children.add(child);
    }

    public void addParent(Person parent){
        this.parents.add(parent);
    }

    @Override
    public String toString() {
        return String.format("Person[(%s)%s %s]", this.getId(), this.getFirstName(), this.getLastName());
    }
}
