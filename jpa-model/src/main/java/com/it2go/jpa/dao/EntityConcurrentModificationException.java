package com.it2go.jpa.dao;

public class EntityConcurrentModificationException extends Exception {
    @Override
    public String getMessage() {
        return "Entity was modified by another process, pleas reload it!";
    }
}
