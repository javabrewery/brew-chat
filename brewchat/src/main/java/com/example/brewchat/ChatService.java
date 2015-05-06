package com.example.brewchat;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.example.brewchat.events.AuthenticationErrorEvent;
import com.example.brewchat.events.ChatServiceinitedEvent;
import com.example.brewchat.events.CreateChatError;
import com.example.brewchat.events.GetGroupChatsErrorEvent;
import com.example.brewchat.events.GetGroupChatsEvent;
import com.example.brewchat.events.GroupChatCreatedEvent;
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

import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPConnection;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import de.greenrobot.event.EventBus;

public class ChatService implements ConnectionListener,
        QBGroupChatManagerListener,
        QBParticipantListener,
        QBIsTypingListener,
        QBMessageListener,
        QBPrivacyListListener,
        QBPrivateChatManagerListener,
        QBRosterListener,
        QBSubscriptionListener {

    static final String TAG = ChatService.class.toString();

    QBChatService chatService;

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
                    chatService = QBChatService.getInstance();
                    chatService.addConnectionListener(ChatService.this);
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
                Log.d(TAG, session + " " + params);
                EventBus.getDefault().post(new UserLoggedEvent());
                try {
                    QBChatService.getInstance().startAutoSendPresence(30);
                } catch (SmackException.NotLoggedInException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(List errors) {
                Log.d(TAG, errors.toString());
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
                GroupChatCreatedEvent groupChatCreatedEvent = new GroupChatCreatedEvent(dialog);
                EventBus.getDefault().post(groupChatCreatedEvent);
                Log.d(TAG, "Created new Group Chat");
            }

            @Override
            public void onError(List<String> errors) {
                EventBus.getDefault().post(new CreateChatError(errors));
                Log.e(TAG, "Error in creating Group Chat");
            }
        });
    }

    public void loadContacts() throws QBResponseException {
        QBRoster roster = QBChatService.getInstance().getRoster();
        ArrayList<Integer> userIds = new ArrayList<>(roster.getEntries().size());
        for (QBRosterEntry user : roster.getEntries()) userIds.add(user.getUserId());
        if (roster.getEntries().size() == 0)
            EventBus.getDefault().post(new UsersLoadedEvent(new ArrayList<QBUser>()));

        QBUsers.getUsersByIDs(userIds, new QBPagedRequestBuilder(userIds.size(), 1),
                new QBEntityCallbackImpl<ArrayList<QBUser>>() {
                    @Override
                    public void onSuccess(ArrayList<QBUser> result, Bundle params) {
                        super.onSuccess(result, params);
                        EventBus.getDefault().post(new UsersLoadedEvent(result));
                    }

                    @Override
                    public void onError(List<String> errors) {
                        // TODO send out error event
                        super.onError(errors);
                        EventBus.getDefault().post(new UsersLoadingErrorEvent(errors));
                    }
                }
        );
    }

    public void register(String username, String password) {
        final QBUser user = new QBUser(username, password);
        QBAuth.createSession(new QBEntityCallbackImpl<QBSession>() {
            @Override
            public void onSuccess(QBSession session, Bundle params) {
                QBUsers.signUp(user, new QBEntityCallbackImpl<QBUser>() {
                    @Override
                    public void onSuccess(QBUser user, Bundle args) {
                        UserSignedUpEvent userSignedUpEvent = new UserSignedUpEvent(user.getLogin());
                        EventBus.getDefault().post(userSignedUpEvent);
                        Log.d(TAG, "User signed up");
                    }

                    @Override
                    public void onError(List<String> errors) {

                        EventBus.getDefault().post(new RegisterUserError(errors));
                        Log.e(TAG, "Error in signup");
                    }
                });
            }

            @Override
            public void onError(List errors) {
                Log.d(TAG, errors.toString());
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
                EventBus.getDefault().post(new GetGroupChatsEvent(dialogs));
            }

            @Override
            public void onError(List<String> errors) {
                EventBus.getDefault().post(new GetGroupChatsErrorEvent(errors));
            }
        });
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
        Log.d(TAG, "chatCreated: " + qbPrivateChat + " " + b);
    }

    // QBRosterListener

    @Override
    public void entriesDeleted(Collection<Integer> integers) {
        Log.d(TAG, "entriesDeleted: " + integers);
    }

    @Override
    public void entriesAdded(Collection<Integer> integers) {
        Log.d(TAG, "entriesAdded: " + integers);
    }

    @Override
    public void entriesUpdated(Collection<Integer> integers) {
        Log.d(TAG, "entriesUpdated: " + integers);
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
