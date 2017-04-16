package com.example.brewchat.events;

import com.example.brewchat.domain.ChatMessage;

/**
 * Created by jon on 08/05/15.
 */
public class GroupMessageReceivedEvent {
    private int groupId;
    private ChatMessage message;

    public GroupMessageReceivedEvent(int groupId, ChatMessage message) {
        this.groupId = groupId;
        this.message = message;
    }

    public ChatMessage getMessage() {
        return message;
    }

    public int getGroupId() {
        return groupId;
    }
}
