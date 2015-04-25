package com.example.brewchat.domain;

import java.io.Serializable;

/**
 * Business Object for tracking chat groups
 * Currently has some dummy values for injection into UI
 *
 * Created by Josh
 */

public class ChatGroup implements Serializable {

    private String title;
    private String chatInfo;
    private ChatHistory history;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getChatInfo() {
        return chatInfo;
    }

    public void setChatInfo(String chatInfo) {
        this.chatInfo = chatInfo;
    }

    public ChatHistory getChatHistory() {
        return history;
    }

    public void setChatHistory(ChatHistory history) {
        this.history = history;
    }

}
