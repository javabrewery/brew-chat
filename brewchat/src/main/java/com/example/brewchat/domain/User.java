package com.example.brewchat.domain;

import java.io.Serializable;

/**
 * Created by jon on 22/04/15.
 */
public class User implements Serializable {
    private int id = -1;
    private String name;

    public User(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
