package com.example.brewchat.domain;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by jon on 22/04/15.
 */
public class User implements Serializable {
    // Mostly just copied from QBUser
    private int id;
    private String name;
    private String email;
    private String login;
    private Date lastRequestAt;

    public User() {
    }

    public int getId() { return id;}

    public String getName() { return name; }
    
    public void setName(String name) {
        this.name = name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public Date getLastRequestAt() {
        return lastRequestAt;
    }

    public void setLastRequestAt(Date lastRequestAt) {
        this.lastRequestAt = lastRequestAt;
    }
}
