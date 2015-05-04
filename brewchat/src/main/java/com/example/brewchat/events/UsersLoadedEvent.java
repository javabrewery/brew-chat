package com.example.brewchat.events;

import com.quickblox.users.model.QBUser;

import java.util.List;

/**
 * Created by jon on 29/04/15.
 */
public class UsersLoadedEvent {
    private List<QBUser> users;

    public UsersLoadedEvent(List<QBUser> users) {
        this.users = users;
    }

    public List<QBUser> getUsers() {
        return users;
    }
}
