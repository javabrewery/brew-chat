package com.example.brewchat.domain;

/**
 * Business Object for tracking chat groups
 * Currently has some dummy values for injection into UI
 *
 * Created by Josh
 */

public class ChatGroup {

    private String title;
    private String chatInfo;

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


}
