package com.example.brewchat.chatservices;

import com.example.brewchat.domain.User;

import java.util.ArrayList;

public interface IChatService {

    void login(String username, String password);

    void register(String username, String password);

    void loadContacts();

    void logout();

    void getChatDialogs();

    void addChatGroup(String title, ArrayList<Integer> userIds);

    void sendMessage(User user, String message);

    void getPrivateChatHistory(User user);

    User getCurrentUser();
}
