package com.example.brewchat.events;

import com.example.brewchat.domain.ChatMessage;

/**
 * Created by jon on 06/05/15.
 */
public class MessageReceivedEvent {
    private ChatMessage message;

    public MessageReceivedEvent(ChatMessage message) {
        this.message = message;
    }

    public ChatMessage getMessage() {
        return message;
    }

}
