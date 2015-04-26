package com.example.brewchat.events;

import java.util.ArrayList;

/**
 * Created by Ryan on 4/25/2015.
 */
public class GroupChatCreatedEvent{
    private String name;
    private ArrayList<Integer> userIds;

    public GroupChatCreatedEvent(String name, ArrayList<Integer> ids){
        this.name = name;
        this.userIds = userIds;

    }

    public ArrayList<Integer> getUserIds() {
        return userIds;
    }

    public String getName() {
        return name;
    }
}
