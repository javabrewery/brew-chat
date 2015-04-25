package com.example.brewchat;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.quickblox.auth.QBAuth;
import com.quickblox.auth.model.QBSession;
import com.quickblox.chat.QBChat;
import com.quickblox.chat.QBChatService;
import com.quickblox.chat.QBGroupChat;
import com.quickblox.chat.QBPrivateChat;
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
import com.quickblox.chat.model.QBPresence;
import com.quickblox.chat.model.QBPrivacyListItem;
import com.quickblox.core.QBEntityCallbackImpl;
import com.quickblox.core.QBSettings;
import com.quickblox.users.model.QBUser;

import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPConnection;

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

    public ChatService(Context context, String appId, String authKey, String authSecret) {
        Log.d(TAG, "ChatService: " + context);

        QBSettings.getInstance().fastConfigInit(appId, authKey, authSecret);

        //QBChatService.setDebugEnabled(true);

        if (!QBChatService.isInitialized()) {
            QBChatService.init(context);
            chatService = QBChatService.getInstance();
            chatService.addConnectionListener(this);
        }

    }

    public void login(String username, String password) {
        final QBUser user = new QBUser("user5", "12345678");

        QBAuth.createSession(user, new QBEntityCallbackImpl<QBSession>() {
            @Override
            public void onSuccess(QBSession session, Bundle params) {
                Log.d(TAG, session + " " + params);

                user.setId(session.getUserId());

                QBChatService.getInstance().login(user, new QBEntityCallbackImpl() {
                    @Override
                    public void onSuccess() {

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
                    }
                });
            }

            @Override
            public void onError(List<String> errors) {
                Log.d(TAG, errors.toString());
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
