package com.example.brewchat.domain;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Business Object for tracking chat groups
 * Currently has some dummy values for injection into UI
 * <p/>
 * Created by Josh
 */

public class ChatGroup implements Serializable {

    private int id;
    private String title;
    private String chatInfo;
    private ChatHistory history = new ChatHistory();
    private ArrayList<Integer> chatUsers;

    public ChatGroup(int id, String title, ArrayList<Integer> chatUsers) {
        this.setId(id);
        this.title = title;
        this.chatUsers = chatUsers;
    }

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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
