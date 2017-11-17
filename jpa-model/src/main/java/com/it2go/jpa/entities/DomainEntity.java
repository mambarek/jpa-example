package com.it2go.jpa.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;
import java.util.Objects;


@Getter
@Setter
@MappedSuperclass
public abstract class DomainEntity extends AbstractEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;

    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;

    @OneToOne(fetch = FetchType.LAZY)
    private Person createdBy;

    //@Version
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdateTime;

    @OneToOne
    private Person updatedBy;

    @Override
    public boolean equals(Object o) {
        // the same object
        if (this == o) {
            return true;
        }
        // check if same class
        if (this.getClass() != o.getClass()) {
            return false;
        }

        DomainEntity other = (DomainEntity) o;

        //check lstupdate the DB entity should not be changed
        if (!Objects.equals(this.lastUpdateTime, other.lastUpdateTime)) {
            return false;
        }

        // check if same id
        if (this.getId() != null && this.getId().equals(other.getId())) {
            return true;
        }

        // same class no id so return super.equals
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        int hashCode = 11; // willkürlicher Initialwert
        int multi = 29; // nicht zu große, zufällig gewählte Primzahl als Multiplikator
        hashCode += (this.getId() == null) ? 0 : this.getId().hashCode();

        if (this.lastUpdateTime != null)
            hashCode = hashCode * multi + this.getLastUpdateTime().hashCode();

        return hashCode;
    }

    //@Override
    // public int hashCode(){return -31;}
}
