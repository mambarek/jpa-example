package com.it2go.test;

import javax.ejb.Stateless;

@Stateless
public class LoginBean {
    public String login(String name) {
        System.out.println("LoginBeab login name = " + name);
        return "Hello, " + name;
    }
}
