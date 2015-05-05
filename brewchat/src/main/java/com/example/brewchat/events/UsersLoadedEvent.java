package com.example.brewchat.events;

import com.example.brewchat.domain.User;

import java.util.List;

/**
 * Created by jon on 29/04/15.
 */
public class UsersLoadedEvent {
    private List<User> users;

    public UsersLoadedEvent(List<User> users) {
        this.users = users;
    }

    public List<User> getUsers() {
        return users;
    }
}
