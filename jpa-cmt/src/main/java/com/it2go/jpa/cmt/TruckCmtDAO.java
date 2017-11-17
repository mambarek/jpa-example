package com.it2go.jpa.cmt;

import com.it2go.jpa.dao.DomainEntity;
import com.it2go.jpa.entities.Truck;

@DomainEntity(clazz = Truck.class)
public class TruckCmtDAO extends EntityCmtDAO<Truck> {
    @Override
    Class<Truck> getEntityClass() {
        return Truck.class;
    }
}
