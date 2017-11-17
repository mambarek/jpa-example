package com.it2go.jpa.umt;

import com.it2go.jpa.dao.DomainEntity;
import com.it2go.jpa.entities.Car;

@DomainEntity(clazz = Car.class)
public class CarUmtDAO extends EntityUmtDAO<Car>{
}
