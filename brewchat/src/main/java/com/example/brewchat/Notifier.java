package com.example.brewchat;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.example.brewchat.activities.ChatActivity;
import com.example.brewchat.domain.ChatMessage;
import com.example.brewchat.events.GroupMessageReceivedEvent;
import com.example.brewchat.events.MessageReceivedEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by jon on 14/05/15.
 */
public class Notifier {

    private static Notifier instance = null;

    public static Notifier getInstance(Context context) {
        if (instance == null) {
            if (context == null) throw new IllegalArgumentException("Context must not be null.");
            else instance = new Notifier(context);
        }
        return instance;
    }

    private Context context;
    private NotificationManagerCompat notificationManager;
    private HashMap<Integer, List<ChatMessage>> previousMessages;

    private Notifier(Context context) {
        this.context = context.getApplicationContext();
        EventBus.getDefault().register(this);
        notificationManager = NotificationManagerCompat.from(context);
        previousMessages = new HashMap<>();
    }

    public void onEvent(MessageReceivedEvent event) {
        ChatMessage message = event.getMessage();
        int notificationId = message.getSender().getId();

        Intent intent = new Intent(context, ChatActivity.class);
        intent.putExtra(ChatActivity.EXTRA_IS_PRIVATE_CHAT, true);
        intent.putExtra(ChatActivity.EXTRA_PRIVATE_CHAT_USER, message.getSender());

        PendingIntent pendingIntent =
                PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        notificationManager.notify(notificationId,
                buildNotification(notificationId, message, pendingIntent));
    }

    public void onEvent(GroupMessageReceivedEvent event) {
        ChatMessage message = event.getMessage();
        // Don't show a notification if we're the sender
        if (message.getSender().getId() == Application.getChatService().getCurrentUser().getId())
            return;
        int notificationId = message.getSender().getId();

        Intent intent = new Intent(context, ChatActivity.class);
        intent.putExtra(ChatActivity.EXTRA_CHAT_GROUP,
                Application.getChatService().getChatGroup(event.getGroupId()));

        PendingIntent pendingIntent =
                PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        notificationManager.notify(notificationId,
                buildNotification(notificationId, message, pendingIntent));
    }

    private Notification buildNotification(int notificationId, ChatMessage message, PendingIntent action) {

        List<ChatMessage> messageList = previousMessages.get(notificationId);
        if (messageList == null) messageList = new ArrayList<>(1);
        messageList.add(message);
        previousMessages.put(notificationId, messageList);

        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
        inboxStyle.setBigContentTitle("New Messages");
        for (ChatMessage aMessage : messageList) {
            inboxStyle.addLine(aMessage.getSender().getBestDisplayableName() + ": " + aMessage.getMessage());
        }

        return new NotificationCompat.Builder(context)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setAutoCancel(true)
                .setContentTitle("Message from " + message.getSender().getBestDisplayableName())
                .setContentText(message.getMessage())
                .setSmallIcon(R.drawable.ic_mms_white_24dp)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setContentIntent(action)
                .setStyle(inboxStyle)
                .build();
    }

    public static class NotificationDismissedReceiver extends BroadcastReceiver {

        protected static final String NOTIFICATION_ID = "notification-id";

        @Override
        public void onReceive(Context context, Intent intent) {
            int notificationId = intent.getExtras().getInt(NOTIFICATION_ID);
            Log.d(getClass().getSimpleName(), "Notification dismissed " + notificationId);
            // Make it null so it will be reinstantiated next time we try to access it
            Notifier.getInstance(context).previousMessages.put(notificationId,null);
        }
    }

}
