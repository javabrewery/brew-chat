package com.example.brewchat;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import com.example.brewchat.activities.ChatActivity;
import com.example.brewchat.domain.ChatMessage;
import com.example.brewchat.events.GroupMessageReceivedEvent;
import com.example.brewchat.events.MessageReceivedEvent;

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

    private Notifier(Context context) {
        this.context = context.getApplicationContext();
        EventBus.getDefault().register(this);
        notificationManager = NotificationManagerCompat.from(context);
    }

    public void onEvent(MessageReceivedEvent event) {
        ChatMessage message = event.getMessage();
        int notificationId = message.getSender().getId();

        Intent intent = new Intent(context, ChatActivity.class);
        intent.putExtra(ChatActivity.EXTRA_IS_PRIVATE_CHAT, true);
        intent.putExtra(ChatActivity.EXTRA_PRIVATE_CHAT_USER, message.getSender());

        PendingIntent pendingIntent =
                PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        notificationManager.notify(notificationId, buildNotification(message, pendingIntent));
    }

    public void onEvent(GroupMessageReceivedEvent event) {
        ChatMessage message = event.getMessage();
        int notificationId = message.getSender().getId();

        Intent intent = new Intent(context, ChatActivity.class);
        intent.putExtra(ChatActivity.EXTRA_CHAT_GROUP,
                Application.getChatService().getChatGroup(event.getGroupId()));

        PendingIntent pendingIntent =
                PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        notificationManager.notify(notificationId, buildNotification(message, pendingIntent));
    }

    private Notification buildNotification(ChatMessage message, PendingIntent action) {
        return new NotificationCompat.Builder(context)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setAutoCancel(true)
                .setContentTitle("Message from " + message.getSender().getBestDisplayableName())
                .setContentText(message.getMessage())
                .setSmallIcon(R.drawable.ic_mms_grey_800_24dp)
                .setContentIntent(action)
                .build();
    }

}
