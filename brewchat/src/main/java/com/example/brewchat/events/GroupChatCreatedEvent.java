package com.example.brewchat.events;

import com.quickblox.chat.model.QBDialog;

import java.util.ArrayList;

/**
 * Event for notifying creation of new QB chat group
 *
 * Created by Ryan on 4/25/2015.
 */
public class GroupChatCreatedEvent{

    private QBDialog dialog;

    public GroupChatCreatedEvent(QBDialog dialog){
        this.dialog = dialog;
    }

    public QBDialog getDialog() {
        return dialog;
    }

}
