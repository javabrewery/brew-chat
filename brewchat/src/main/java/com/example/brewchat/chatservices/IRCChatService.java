package com.example.brewchat.chatservices;

import android.util.Log;

import com.example.brewchat.domain.ChatGroup;
import com.example.brewchat.domain.ChatMessage;
import com.example.brewchat.domain.User;
import com.example.brewchat.events.GroupChatCreatedEvent;
import com.example.brewchat.events.MessageReceivedEvent;
import com.example.brewchat.events.UserLoggedEvent;
import com.example.brewchat.events.UserLogoutEvent;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSortedSet;

import org.pircbotx.Channel;
import org.pircbotx.ChannelListEntry;
import org.pircbotx.Configuration;
import org.pircbotx.PircBotX;
import org.pircbotx.exception.IrcException;
import org.pircbotx.hooks.Event;
import org.pircbotx.hooks.Listener;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.ConnectEvent;
import org.pircbotx.hooks.events.DisconnectEvent;
import org.pircbotx.hooks.events.JoinEvent;
import org.pircbotx.hooks.events.MessageEvent;

import java.io.IOException;
import java.util.ArrayList;

import de.greenrobot.event.EventBus;

public class IRCChatService implements IChatService {

    static final String TAG = IRCChatService.class.toString();

    PircBotX bot;

    Listener listener = new ListenerAdapter() {
        @Override
        public void onMessage(MessageEvent event) throws Exception {
            EventBus.getDefault().post(
                    new MessageReceivedEvent(
                            new ChatMessage(adaptIRCUser(event.getUser()), event.getMessage())));
        }

        @Override
        public void onConnect(ConnectEvent event) throws Exception {
            EventBus.getDefault().post(new UserLoggedEvent());
        }

        @Override
        public void onDisconnect(DisconnectEvent event) throws Exception {
            EventBus.getDefault().post(new UserLogoutEvent());
        }

        @Override
        public void onJoin(JoinEvent event) throws Exception {
            if (event.getUser() == event.getBot().getUserBot())
                EventBus.getDefault().post(new GroupChatCreatedEvent(adaptIRCChannel(event.getChannel())));
        }
    };

    // Adapters

    private User adaptIRCUser(org.pircbotx.User ircUser) {
        User user = new User();

        user.setId(0); // FIXME: Set UUID
        user.setName(ircUser.getNick());

        return user;
    }

    private ArrayList<User> adaptIRCUsers(ImmutableSortedSet<org.pircbotx.User> ircUsers) {
        ArrayList<User> users =  new ArrayList<>();

        for (org.pircbotx.User ircUser: ircUsers)
            users.add(adaptIRCUser(ircUser));

        return users;
    }

    private ChatGroup adaptIRCChannel(Channel ircChannel) {
        ChatGroup chatGroup = new ChatGroup(ircChannel.getName(), new ArrayList<Integer>()); // TODO: Set users
        chatGroup.setChatInfo(ircChannel.getTopic());

        return chatGroup;
    }

    private ArrayList<ChatGroup> adaptIRCChannels(ImmutableList<ChannelListEntry> ircChannels) {
        ArrayList chatGroups = new ArrayList<>();

        for (ChannelListEntry ircChannel: ircChannels) {
            chatGroups.add(adaptIRCChannels(ircChannels));
        }

        return chatGroups;
    }

    @Override
    public void login(String username, final String password) {
        Configuration configuration = new Configuration.Builder()
                .setName(username)
                .setLogin(username)
                .setAutoNickChange(true)
                .setServerHostname("irc.freenode.net")
                .addAutoJoinChannel("#thejavabrewery")
                .addListener(listener)
                .buildConfiguration();

        bot = new PircBotX(configuration);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    bot.startBot();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (IrcException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    public void register(String username, String password) {

    }

    @Override
    public void loadContacts() {

    }

    @Override
    public void logout() {
        bot.sendIRC().quitServer();
    }

    @Override
    public void getChatDialogs() {
        //bot.sendIRC().listChannels();
    }

    @Override
    public void addChatGroup(String title, ArrayList<Integer> userIds) {
        bot.sendIRC().joinChannel(title);
    }

    @Override
    public void sendMessage(User user, String message) {
        bot.sendIRC().message(user.getName(), message);;
    }

    @Override
    public void getPrivateChatHistory(User user) {

    }

    @Override
    public User getCurrentUser() {
        return adaptIRCUser(bot.getUserBot());
    }
}
