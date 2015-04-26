package com.example.brewchat.mockers;

/**
 * Created by josh on 4/22/15.
 * <p/>
 * Mocks data for use in UI
 */
public class MockerUtil {

    /* public static ArrayList<ChatGroup> makeMockChatGroups() {

        ArrayList<ChatGroup> chatGroups = new ArrayList<>();

        for (int i = 0; i < 10; i++) {

            ChatGroup currentChat = new ChatGroup();
            currentChat.setTitle("Chat " + i);
            currentChat.setChatInfo("This is chat " + i);
            ChatHistory history = new ChatHistory();
            for (int j = 0; j < 10; j++) {
                int userNumber = (j % 2) + 1;
                User user = new User(userNumber, "User" + userNumber);
                history.addChatMessage(new ChatMessage(user, "Sample message #" + (j + 1)));
            }
            currentChat.setChatHistory(history);
            chatGroups.add(currentChat);
        }

        return chatGroups;

    } */

}
