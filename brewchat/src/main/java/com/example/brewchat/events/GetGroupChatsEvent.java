package com.example.brewchat.events;

import com.quickblox.chat.model.QBDialog;

import java.util.ArrayList;

/**
 * Event for populating the chat manager chat groups list
 *
 * Created by josh on 4/28/15.
 */
public class GetGroupChatsEvent {
    private ArrayList<QBDialog> chatGroups;

    public ArrayList<QBDialog> getChatGroups() {
        return chatGroups;
    }

    public GetGroupChatsEvent(ArrayList<QBDialog> chatGroups) {
        this.chatGroups = chatGroups;
    }
}
