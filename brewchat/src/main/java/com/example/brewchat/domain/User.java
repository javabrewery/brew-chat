package com.example.brewchat.domain;

import java.io.Serializable;

/**
 * Created by jon on 22/04/15.
 */
public class User implements Serializable {
    private int id = -1;
    private String name, username, password;

    public User(int id, String name) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.password = password;
    }

    public int getId() { return id;}

    public String getName() { return name; }

    public String getUsername() {
        return username;
    }

    public String getPassword() { return password; }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(int id) {
        this.id = id;
    }
}
