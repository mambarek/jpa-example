package com.it2go.jpa.cmt;

import com.it2go.jpa.dao.DomainEntity;
import com.it2go.jpa.entities.Car;

@DomainEntity(clazz = Car.class)
public class CarCmtDAO extends EntityCmtDAO<Car> {
    @Override
    Class<Car> getEntityClass() {
        return Car.class;
    }
}
