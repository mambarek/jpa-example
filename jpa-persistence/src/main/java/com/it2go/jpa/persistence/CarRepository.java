package com.it2go.jpa.persistence;

import com.it2go.jpa.dao.DomainEntity;
import com.it2go.jpa.dao.EntityConcurrentModificationException;
import com.it2go.jpa.dao.EntityNotFoundException;
import com.it2go.jpa.dao.EntityNotPersistedException;
import com.it2go.jpa.dao.EntityRemovedException;
import com.it2go.jpa.dao.IEntityDAO;
import com.it2go.jpa.entities.Car;
import com.it2go.jpa.entities.Person;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.List;

@Stateless
public class CarRepository implements ICarRepository {

    @Inject
    @DomainEntity(clazz = Car.class)
    private IEntityDAO<Car> carDAO;

    @Override
    public Car persist(Car entity, Person user) throws EntityConcurrentModificationException, EntityRemovedException {
        return carDAO.save(entity);
    }

    @Override
    public Car remove(Car entity) throws EntityNotPersistedException {
        return carDAO.delete(entity);
    }

    @Override
    public <Long> Car removeById(Long id) throws EntityNotFoundException {
        return carDAO.deleteByIdentityKey(id);
    }

    @Override
    public <K> Car findById(K id) {
        return carDAO.getByIdentityKey(id);
    }

    @Override
    public List<Car> findAll() {
        return carDAO.getAll();
    }

    @Override
    public int executeUpdate(String query) {
        return carDAO.executeUpdate(query);
    }
}
