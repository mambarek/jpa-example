package com.it2go.jpa.dao;

public class EntityRemovedException extends Exception {
    @Override
    public String getMessage() {
        return "Entity was removed!";
    }
}
