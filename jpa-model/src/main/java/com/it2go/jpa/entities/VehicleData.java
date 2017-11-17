package com.it2go.jpa.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Embeddable
public class VehicleData {

    @Basic
    @Column(name = "WHEELS_COUNT")
    private int wheelsCount;

    @Basic
    @Column(name = "DOORS_COUNT")
    private int doorsCount;

    @Basic
    @Column(name = "ENGINE_POWER")
    private int enginePower;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "BESITZER_ID")
    private Person owner;

    @OneToMany
    private List<Person> drivers = new ArrayList<>();
}
