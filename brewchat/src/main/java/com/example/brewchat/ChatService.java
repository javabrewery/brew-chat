package com.example.brewchat;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.example.brewchat.domain.ChatGroup;
import com.example.brewchat.domain.ChatMessage;
import com.example.brewchat.domain.User;
import com.example.brewchat.events.AuthenticationErrorEvent;
import com.example.brewchat.events.ChatServiceinitedEvent;
import com.example.brewchat.events.CreateChatError;
import com.example.brewchat.events.GetGroupChatsErrorEvent;
import com.example.brewchat.events.GetGroupChatsEvent;
import com.example.brewchat.events.GroupChatCreatedEvent;
import com.example.brewchat.events.GroupMessageReceivedEvent;
import com.example.brewchat.events.MessageReceivedEvent;
import com.example.brewchat.events.RegisterUserError;
import com.example.brewchat.events.UserLoggedEvent;
import com.example.brewchat.events.UserSignedUpEvent;
import com.example.brewchat.events.UsersLoadedEvent;
import com.example.brewchat.events.UsersLoadingErrorEvent;
import com.quickblox.auth.QBAuth;
import com.quickblox.auth.model.QBSession;
import com.quickblox.chat.QBChat;
import com.quickblox.chat.QBChatService;
import com.quickblox.chat.QBGroupChat;
import com.quickblox.chat.QBGroupChatManager;
import com.quickblox.chat.QBPrivateChat;
import com.quickblox.chat.QBPrivateChatManager;
import com.quickblox.chat.QBRoster;
import com.quickblox.chat.exception.QBChatException;
import com.quickblox.chat.listeners.QBGroupChatManagerListener;
import com.quickblox.chat.listeners.QBIsTypingListener;
import com.quickblox.chat.listeners.QBMessageListener;
import com.quickblox.chat.listeners.QBParticipantListener;
import com.quickblox.chat.listeners.QBPrivacyListListener;
import com.quickblox.chat.listeners.QBPrivateChatManagerListener;
import com.quickblox.chat.listeners.QBRosterListener;
import com.quickblox.chat.listeners.QBSubscriptionListener;
import com.quickblox.chat.model.QBChatMessage;
import com.quickblox.chat.model.QBDialog;
import com.quickblox.chat.model.QBDialogType;
import com.quickblox.chat.model.QBPresence;
import com.quickblox.chat.model.QBPrivacyListItem;
import com.quickblox.chat.model.QBRosterEntry;
import com.quickblox.core.QBEntityCallbackImpl;
import com.quickblox.core.QBSettings;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.core.request.QBPagedRequestBuilder;
import com.quickblox.core.request.QBRequestGetBuilder;
import com.quickblox.users.QBUsers;
import com.quickblox.users.model.QBUser;

import org.apache.commons.collections4.BidiMap;
import org.apache.commons.collections4.bidimap.DualHashBidiMap;
import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smackx.muc.DiscussionHistory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import de.greenrobot.event.EventBus;

public class ChatService implements IChatService,
        ConnectionListener,
        QBGroupChatManagerListener,
        QBParticipantListener,
        QBIsTypingListener,
        QBMessageListener,
        QBPrivacyListListener,
        QBPrivateChatManagerListener,
        QBRosterListener,
        QBSubscriptionListener {

    static final String TAG = ChatService.class.toString();

    protected User currentUser = null;

    // Need to keep track of the JID of the chat rooms, but don't want to expose that outside of this ChatService
    BidiMap<Integer, String> keyMap;
    // Need to keep track of the QBGroupChats manually, as getGroupChat and createGroupChat aren't working as expected
    HashMap<String, QBGroupChat> chatMap;

    public ChatService(final Context context, final String appId, final String authKey, final String authSecret) {
        new Thread(new Runnable() {
            @Override
            public void run() {

                QBSettings.getInstance().fastConfigInit(appId, authKey, authSecret);

                if (BuildConfig.DEBUG) {
                    QBChatService.setDebugEnabled(true);
                }

                if (!QBChatService.isInitialized()) {
                    QBChatService.init(context);
                    QBChatService.getInstance().addConnectionListener(ChatService.this);
                }
                EventBus.getDefault().post(new ChatServiceinitedEvent());
            }
        }).start();

    }

    public void login(String username, String password) {
        final QBUser user = new QBUser(username, password);
        QBAuth.createSession(user, new QBEntityCallbackImpl<QBSession>() {
            @Override
            public void onSuccess(QBSession session, Bundle params) {
                user.setId(session.getUserId());
                QBChatService.getInstance().login(user, new QBEntityCallbackImpl() {
                    @Override
                    public void onSuccess() {
                        QBChatService.getInstance().getPrivateChatManager()
                                .addPrivateChatManagerListener(ChatService.this);
                        QBChatService.getInstance().getGroupChatManager()
                                .addGroupChatManagerListener(ChatService.this);
                        currentUser = new User();
                        currentUser.setName(user.getFullName());
                        currentUser.setEmail(user.getEmail());
                        currentUser.setLogin(user.getLogin());
                        currentUser.setLastRequestAt(user.getLastRequestAt());
                        currentUser.setId(user.getId());
                        EventBus.getDefault().post(new UserLoggedEvent());
                    }

                    @Override
                    public void onError(List errors) {
                        EventBus.getDefault().post(new AuthenticationErrorEvent(errors));
                    }
                });
                try {
                    QBChatService.getInstance().startAutoSendPresence(30);
                } catch (SmackException.NotLoggedInException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(List errors) {
                EventBus.getDefault().post(new AuthenticationErrorEvent(errors));
            }
        });
    }

    public void logout() {
        try {
            QBChatService.getInstance().logout();
        } catch (SmackException.NotConnectedException e) {
            e.printStackTrace();
        }
    }

    public void addChatGroup(String name, ArrayList<Integer> userIds) {
        QBDialog dialog = new QBDialog();
        dialog.setName(name);
        dialog.setType(QBDialogType.GROUP);
        dialog.setOccupantsIds(userIds);

        QBGroupChatManager groupChatManager = QBChatService.getInstance().getGroupChatManager();
        groupChatManager.createDialog(dialog, new QBEntityCallbackImpl<QBDialog>() {
            @Override
            public void onSuccess(QBDialog dialog, Bundle args) {
                Random random = new Random();
                int id = random.nextInt();
                ChatGroup chatGroup = new ChatGroup(id, dialog.getName(), dialog.getOccupants());
                keyMap.put(id, dialog.getRoomJid());
                EventBus.getDefault().post(new GroupChatCreatedEvent(chatGroup));
            }

            @Override
            public void onError(List<String> errors) {
                EventBus.getDefault().post(new CreateChatError(errors));
            }
        });
    }

    public void sendMessage(User user, String message) {
        QBPrivateChatManager privateChatManager = QBChatService.getInstance().getPrivateChatManager();
        QBPrivateChat chat = privateChatManager.getChat(user.getId());
        if (chat == null) chat = privateChatManager.createChat(user.getId(), this);
        try {
            chat.sendMessage(message);
        } catch (XMPPException | SmackException.NotConnectedException e) {
            // TODO something better
            e.printStackTrace();
        }
    }

    public void sendMessage(ChatGroup group, String message) {
        QBGroupChatManager manager = QBChatService.getInstance().getGroupChatManager();

        String jid = keyMap.get(group.getId());
        QBGroupChat groupChat = chatMap.get(jid);
        try {
            groupChat.sendMessage(message);
        } catch (XMPPException | SmackException.NotConnectedException e) {
            e.printStackTrace();
        }
    }

    public void loadContacts() {
        QBRoster roster = QBChatService.getInstance().getRoster();
        ArrayList<Integer> userIds = new ArrayList<>(roster.getEntries().size());
        for (QBRosterEntry user : roster.getEntries()) userIds.add(user.getUserId());
        if (roster.getEntries().size() == 0)
            // Arraylist's internal algorithm defaults to reserving 10 slots in memory,
            // so here we just force it to reserve 0 slots for our empty list.
            EventBus.getDefault().post(new UsersLoadedEvent(new ArrayList<User>(0)));

        QBUsers.getUsersByIDs(userIds, new QBPagedRequestBuilder(userIds.size(), 1),
                new QBEntityCallbackImpl<ArrayList<QBUser>>() {
                    @Override
                    public void onSuccess(ArrayList<QBUser> results, Bundle params) {
                        super.onSuccess(results, params);
                        ArrayList<User> users = new ArrayList<>(results.size());
                        for (QBUser result : results) {
                            // There mus be a more efficient, or at least better looking, way of doing this...
                            User user = new User();
                            user.setId(result.getId());
                            user.setName(result.getFullName());
                            user.setEmail(result.getEmail());
                            user.setLogin(result.getLogin());
                            user.setLastRequestAt(result.getLastRequestAt());
                            users.add(user);
                        }

                        EventBus.getDefault().post(new UsersLoadedEvent(users));
                    }

                    @Override
                    public void onError(List<String> errors) {
                        EventBus.getDefault().post(new UsersLoadingErrorEvent(errors));
                    }
                }
        );
    }

    public void joinChatGroup(ChatGroup chatGroup) {
        QBGroupChatManager manager = QBChatService.getInstance().getGroupChatManager();
        QBGroupChat groupChat;
        if (manager.getGroupChat(keyMap.get(chatGroup.getId())) != null) {
            groupChat = manager.getGroupChat(keyMap.get(chatGroup.getId()));
        } else {
            groupChat = manager.createGroupChat(keyMap.get(chatGroup.getId()));
        }
        chatMap.put(groupChat.getJid(), groupChat);
        if (!groupChat.isJoined()) {
            try {
                groupChat.join(new DiscussionHistory());
                groupChat.addMessageListener(this);
            } catch (XMPPException | SmackException e) {
                e.printStackTrace();
            }
        }
    }

    // Internal use only. Run from background thread only!
    private QBUser getUserById(int id) throws QBResponseException {
        return QBUsers.getUser(id);
    }

    public void register(String username, String password) {
        final QBUser user = new QBUser(username, password);
        QBAuth.createSession(new QBEntityCallbackImpl<QBSession>() {
            @Override
            public void onSuccess(QBSession session, Bundle params) {
                QBUsers.signUp(user, new QBEntityCallbackImpl<QBUser>() {
                    @Override
                    public void onSuccess(QBUser user, Bundle args) {
                        EventBus.getDefault().post(new UserSignedUpEvent(user.getLogin()));
                    }

                    @Override
                    public void onError(List<String> errors) {
                        EventBus.getDefault().post(new RegisterUserError(errors));
                    }
                });
            }

            @Override
            public void onError(List errors) {
                EventBus.getDefault().post(new AuthenticationErrorEvent(errors));
            }
        });
    }

    public void getChatDialogs() {
        QBRequestGetBuilder requestGetBuilder = new QBRequestGetBuilder();
        requestGetBuilder.setPagesLimit(100);

        QBChatService.getChatDialogs(null, requestGetBuilder, new QBEntityCallbackImpl<ArrayList<QBDialog>>() {
            @Override
            public void onSuccess(ArrayList<QBDialog> dialogs, Bundle args) {
                ArrayList<ChatGroup> chatGroups = new ArrayList<>();
                keyMap = new DualHashBidiMap<Integer, String>();
                chatMap = new HashMap<String, QBGroupChat>();

                Random random = new Random();

                for (QBDialog dialog : dialogs) {
                    int id = random.nextInt();
                    keyMap.put(id, dialog.getRoomJid());

                    ChatGroup group = new ChatGroup(id, dialog.getName(), dialog.getOccupants());
                    chatGroups.add(group);
                }

                EventBus.getDefault().post(new GetGroupChatsEvent(chatGroups));
            }

            @Override
            public void onError(List<String> errors) {
                EventBus.getDefault().post(new GetGroupChatsErrorEvent(errors));
            }
        });
    }

    public void getPrivateChatHistory(User user) {
        // TODO figure out how to make this work
    }

    public User getCurrentUser() {
        return currentUser;
    }

    // ConnectionListener

    @Override
    public void connected(XMPPConnection xmppConnection) {
        Log.d(TAG, "connected: " + xmppConnection);
    }

    @Override
    public void authenticated(XMPPConnection xmppConnection) {
        Log.d(TAG, "connected: " + xmppConnection);
    }

    @Override
    public void connectionClosed() {
        Log.d(TAG, "connectionClosed");
    }

    @Override
    public void connectionClosedOnError(Exception e) {
        Log.d(TAG, "connectionClosedOnError: " + e);
    }

    @Override
    public void reconnectingIn(int i) {
        Log.d(TAG, "reconnectingIn: " + i);
    }

    @Override
    public void reconnectionSuccessful() {
        Log.d(TAG, "reconnectionSuccessful");
    }

    @Override
    public void reconnectionFailed(Exception e) {
        Log.d(TAG, "reconnectionFailed: " + e);
    }

    // QBGroupChatManagerListener

    @Override
    public void chatCreated(QBGroupChat qbGroupChat) {
        Log.d(TAG, "chatCreated: " + qbGroupChat);
    }

    // QBParticipantListener

    @Override
    public void processPresence(QBGroupChat qbGroupChat, QBPresence qbPresence) {
        Log.d(TAG, "processPresence: " + qbGroupChat + " " + qbPresence);
    }

    // QBIsTypingListener

    @Override
    public void processUserIsTyping(Object o) {
        Log.d(TAG, "processUserIsTyping: " + o);
    }

    @Override
    public void processUserStopTyping(Object o) {
        Log.d(TAG, "processUserStopTyping: " + o);
    }

    // QBMessageListener

    @Override
    public void processMessage(QBChat qbChat, QBChatMessage qbChatMessage) {
        // TODO THIS IS A MESS! CLEAN IT BEFORE PUSHING!
        User u = null;
        if (qbChat instanceof QBPrivateChat) {
            try {
                try {
                    ((QBPrivateChat) qbChat).readMessage(qbChatMessage.getId());
                } catch (XMPPException | SmackException.NotConnectedException e) {
                    e.printStackTrace();
                }
                QBUser sender = getUserById(qbChatMessage.getSenderId());
                u = new User();
                u.setId(sender.getId());
                u.setLastRequestAt(u.getLastRequestAt());
                u.setLogin(sender.getLogin());
                u.setEmail(sender.getEmail());
                u.setName(sender.getFullName());
            } catch (QBResponseException e) {
                e.printStackTrace();
                return;
            }
        } else if (qbChat instanceof QBGroupChat) {
            QBGroupChat qbGroupChat = (QBGroupChat) qbChat;
            QBUser sender = null;
            try {
                sender = getUserById(qbChatMessage.getSenderId());
            } catch (QBResponseException e) {
                e.printStackTrace();
            }
            u = new User();
            u.setId(sender.getId());
            u.setLastRequestAt(u.getLastRequestAt());
            u.setLogin(sender.getLogin());
            u.setEmail(sender.getEmail());
            u.setName(sender.getFullName());
            EventBus.getDefault().post(
                    new GroupMessageReceivedEvent(
                            keyMap.getKey(qbGroupChat.getJid()),
                            new ChatMessage(u, qbChatMessage.getBody()
                            )
                    )
            );
        } else {
            EventBus.getDefault().post(
                    new MessageReceivedEvent(
                            new ChatMessage(u, qbChatMessage.getBody())
                    )
            );
        }
        Log.d(TAG, "processMessage: " + qbChat + " " + qbChatMessage);
    }

    @Override
    public void processError(QBChat qbChat, QBChatException e, QBChatMessage qbChatMessage) {
        Log.d(TAG, "processError: " + qbChat + " " + e + " " + qbChatMessage);
    }

    @Override
    public void processMessageDelivered(QBChat qbChat, String s) {
        Log.d(TAG, "processMessageDelivered: " + qbChat + " " + s);
    }

    @Override
    public void processMessageRead(QBChat qbChat, String s) {
        Log.d(TAG, "processMessageRead: " + qbChat + " " + s);
    }

    // QBPrivacyListListener

    @Override
    public void setPrivacyList(String s, List<QBPrivacyListItem> qbPrivacyListItems) {
        Log.d(TAG, "setPrivacyList: " + s + " " + qbPrivacyListItems);
    }

    @Override
    public void updatedPrivacyList(String s) {
        Log.d(TAG, "updatedPrivacyList: " + s);
    }

    // QBPrivateChatManagerListener

    @Override
    public void chatCreated(QBPrivateChat qbPrivateChat, boolean b) {
        qbPrivateChat.addIsTypingListener(this);
        qbPrivateChat.addMessageListener(this);
        Log.d(TAG, "chatCreated: " + qbPrivateChat + " " + b);
    }

    // QBRosterListener

    @Override
    public void entriesDeleted(Collection<Integer> collection) {
        Log.d(TAG, "entriesDeleted: " + collection);
    }

    @Override
    public void entriesAdded(Collection<Integer> collection) {
        Log.d(TAG, "entriesAdded: " + collection);
    }

    @Override
    public void entriesUpdated(Collection<Integer> collection) {
        Log.d(TAG, "entriesUpdated: " + collection);
    }

    @Override
    public void presenceChanged(QBPresence qbPresence) {
        Log.d(TAG, "presenceChanged: " + qbPresence);
    }

    // QBSubscriptionListener

    @Override
    public void subscriptionRequested(int i) {
        Log.d(TAG, "subscriptionRequested: " + i);
    }
}
