package com.example.brewchat.events;

import com.example.brewchat.domain.ChatGroup;

import java.util.ArrayList;

/**
 * Event for populating the chat manager chat groups list
 *
 * Created by josh on 4/28/15.
 */
public class GetGroupChatsEvent {
    private ArrayList<ChatGroup> chatGroups;

    public ArrayList<ChatGroup> getChatGroups() {
        return chatGroups;
    }

    public GetGroupChatsEvent(ArrayList<ChatGroup> chatGroups) {
        this.chatGroups = chatGroups;
    }
}
