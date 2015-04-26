package com.example.brewchat.events;

/**
 * Created by Ryanm14 on 4/25/2015.
 */
public class UserSignedUpEvent {
    private String username;

    public UserSignedUpEvent(String username){
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

}
