package com.example.brewchat.mockers;

import com.example.brewchat.Domain.ChatGroup;

import java.util.ArrayList;

/**
 * Created by josh on 4/22/15.
 */
public class MockerUtil {

    public static ArrayList<ChatGroup> makeMockChatGroups() {

        ArrayList<ChatGroup> chatGroups = new ArrayList<>();

        for (int i = 0; i < 10; i++) {

            ChatGroup currentChat = new ChatGroup();
            currentChat.setTitle("Chat " + i);
            currentChat.setChatInfo("This is chat " + i);
            chatGroups.add(currentChat);
        }

        return  chatGroups;

    }

}
