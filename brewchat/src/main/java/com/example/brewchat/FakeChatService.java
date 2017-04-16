package com.example.brewchat;

import com.example.brewchat.domain.ChatGroup;
import com.example.brewchat.domain.User;
import com.example.brewchat.events.GetGroupChatsEvent;
import com.example.brewchat.events.GroupChatCreatedEvent;
import com.example.brewchat.events.UserLoggedEvent;
import com.example.brewchat.events.UserSignedUpEvent;
import com.example.brewchat.events.UsersLoadedEvent;

import java.util.ArrayList;
import java.util.Date;

import de.greenrobot.event.EventBus;

public class FakeChatService implements IChatService {

    static final int DEFAULT_DELAY = 1 * 1000;

    private void delay(int seconds) {
        try {
            Thread.sleep(seconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void login(String username, String password) {
        new Thread(new Runnable() {
            public void run() {
                delay(DEFAULT_DELAY);

                EventBus.getDefault().post(new UserLoggedEvent());
            }
        }).start();
    }

    @Override
    public void register(String username, String password) {
        new Thread(new Runnable() {
            public void run() {
                delay(DEFAULT_DELAY);

                EventBus.getDefault().post(new UserSignedUpEvent("Fake User"));
            }
        }).start();
    }

    @Override
    public void loadContacts() {
        new Thread(new Runnable() {
            public void run() {
                delay(DEFAULT_DELAY);

                ArrayList<User> users = new ArrayList<>();

                User user = new User();
                user.setId(1);
                user.setName("Fake User");
                user.setEmail("fake@email.com");
                user.setLogin("Fake");
                user.setLastRequestAt(new Date());
                users.add(user);

                users.add(user);

                EventBus.getDefault().post(new UsersLoadedEvent(users));
            }
        }).start();
    }

    @Override
    public void logout() {
        new Thread(new Runnable() {
            public void run() {
                delay(DEFAULT_DELAY);

                // TODO
            }
        }).start();
    }

    @Override
    public void getChatDialogs() {
        new Thread(new Runnable() {
            public void run() {
                delay(DEFAULT_DELAY);

                ArrayList<ChatGroup> chatGroups = new ArrayList<>();

                ArrayList<Integer> ids = new ArrayList<>();
                ids.add(1);

                chatGroups.add(new ChatGroup(1, "Fake Group", ids));

                EventBus.getDefault().post(new GetGroupChatsEvent(chatGroups));
            }
        }).start();
    }

    @Override
    public void addChatGroup(String title, final ArrayList<Integer> userIds) {
        new Thread(new Runnable() {
            public void run() {
                delay(DEFAULT_DELAY);

                EventBus.getDefault().post(new GroupChatCreatedEvent(new ChatGroup(1, "Group", userIds)));
            }
        }).start();
    }

    @Override
    public void sendMessage(User user, String message) {
        // Do nothing.
    }

    @Override
    public void sendMessage(ChatGroup group, String message) {

    }

    @Override
    public void getPrivateChatHistory(User user) {
    }

    public User getCurrentUser() {
        User user = new User();
        user.setId(1);
        user.setName("Fake User");
        user.setEmail("fake@email.com");
        user.setLogin("Fake");
        user.setLastRequestAt(new Date());
        return user;
    }

    @Override
    public void joinChatGroup(ChatGroup group) {

    }
}
