package com.example.brewchat.events;

import com.quickblox.chat.model.QBChatMessage;

import java.util.ArrayList;

/**
 * Created by josh on 4/29/15.
 */
public class GetChatHistoryEvent {
    private ArrayList<QBChatMessage> qbChatMessageArrayList;


    public GetChatHistoryEvent(ArrayList<QBChatMessage> qbChatMessageArrayList) {
        this.qbChatMessageArrayList = qbChatMessageArrayList;
    }

    public ArrayList<QBChatMessage> getQbChatMessageArrayList() {
        return qbChatMessageArrayList;
    }
}
