package com.it2go.jpa.umt;

import com.it2go.jpa.dao.IEntityDAO;
import com.it2go.jpa.entities.Person;

import javax.enterprise.context.ApplicationScoped;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;

@ApplicationScoped
public class DaoFactory {

    //@Inject
    //@New
    private PersonUmtDAO personUmtDAO;

    //@Produces
    //@DomainEntity(value = Entities.Person)
    public IEntityDAO<Person> create() {
        return (IEntityDAO<Person>) Proxy.newProxyInstance(getClass().getClassLoader(), new Class[]{IEntityDAO.class},
                new InvocationHandler() {
                    private final IEntityDAO<Person> entityDAO = personUmtDAO;

                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        System.out.println("Start Validation for " + method.getName() + "(" + Arrays.toString(args) + ")");
                        final Object result = method.invoke(entityDAO, args);
                        System.out.println("End Validation");
                        return result;
                    }
                });

    }
}
