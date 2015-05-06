package com.example.brewchat.events;

import com.example.brewchat.domain.ChatGroup;

/**
 * Event for notifying creation of new QB chat group
 *
 * Created by Ryan on 4/25/2015.
 */
public class GroupChatCreatedEvent{

    private ChatGroup chatGroup;

    public GroupChatCreatedEvent(ChatGroup chatGroup){
        this.chatGroup = chatGroup;
    }

    public ChatGroup getChatGroup() {
        return chatGroup;
    }

}
