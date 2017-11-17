package com.it2go.jpa.entities;

public enum Gender {
    male,
    female;

    public boolean isMale(){
        return this == male;
    }

    public boolean isFemale(){
        return this == female;
    }
}
