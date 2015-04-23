package com.example.brewchat.domain;

import java.io.Serializable;

/**
 * Created by jon on 22/04/15.
 */
public class ChatMessage  implements Serializable {
    private User sender;
    private String message;

    public ChatMessage(User sender, String message) {
        this.sender = sender;
        this.message = message;
    }

    public User getSender() {
        return sender;
    }

    public String getMessage() {
        return message;
    }
}
