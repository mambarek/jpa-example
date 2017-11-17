package com.it2go.jpa.entities;

import lombok.EqualsAndHashCode;

import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;

@MappedSuperclass
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class AbstractEntity<K extends Serializable> implements Serializable {

    //@Id
    public abstract K getId();

    public abstract void setId(K k);

    public boolean isNew(){
        return this.getId() == null;
    }
}
