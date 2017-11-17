package com.it2go.jpa.entities;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.experimental.Delegate;

import javax.persistence.Basic;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@Entity
public class Truck extends DomainEntity {

    @Basic
    @Column(name = "TRAILER_LENGTH")
    private long trailerLength;

    @Embedded
    @Delegate
    @NonNull
    private VehicleData vehicleData = new VehicleData();

    @ElementCollection
    @CollectionTable( name = "TRUCK_OPTIONS", joinColumns = @JoinColumn(name = "TRUCK_ID"))
    private List<String> options = new ArrayList<>();
}
