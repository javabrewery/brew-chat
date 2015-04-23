package com.example.brewchat.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jon on 22/04/15.
 */
public class ChatHistory implements Serializable{
    private ArrayList<ChatMessage> messages = new ArrayList<>();

    public List<ChatMessage> getMessages() {
        return messages;
    }

    public void addChatMessage(ChatMessage message) {
        messages.add(message);
    }

}
