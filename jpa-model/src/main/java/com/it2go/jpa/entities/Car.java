package com.it2go.jpa.entities;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Delegate;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Car extends DomainEntity {

    @Embedded
    @Delegate
    private VehicleData vehicleData = new VehicleData();

    @ElementCollection
    @CollectionTable(name = "CAR_OPTIONS", joinColumns = @JoinColumn(name = "CAR_ID"))
    @Column(name = "OPTION")
    private List<String> options = new ArrayList<>();
}
