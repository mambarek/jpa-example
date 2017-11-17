package com.it2go.jpa.persistence;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Alternative;
import javax.enterprise.inject.Produces;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

@ApplicationScoped
public class EmfProducer {

    @Produces
    private EntityManagerFactory create() {
        return Persistence.createEntityManagerFactory("test");
    }

    //@Produces
    //@Alternative
/*    private EntityManagerFactory createEclipseLink() {
        return Persistence.createEntityManagerFactory("h2-eclipselink");
    }*/


}
