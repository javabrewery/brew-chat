package com.example.brewchat.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.brewchat.R;
import com.example.brewchat.domain.ChatHistory;
import com.example.brewchat.domain.ChatMessage;
import com.quickblox.chat.model.QBChatMessage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 * Created by jon on 22/04/15.
 */
public class ChatHistoryRecyclerAdapter extends RecyclerView.Adapter<ChatHistoryRecyclerAdapter.ChatHistoryViewHolder> {

    private Context context;
    private ArrayList<QBChatMessage> messages;
    private LayoutInflater inflater;

    public ChatHistoryRecyclerAdapter(Context context, ArrayList<QBChatMessage> messages) {
        this.context = context;
        this.messages = messages;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public ChatHistoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.chat_message_card_layout, parent, false);
        return (new ChatHistoryViewHolder(view));
    }

    @Override
    public void onBindViewHolder(ChatHistoryViewHolder holder, int position) {
        if (messages == null) {

        } else {
            holder.username.setText(messages.get(position).getSenderId().toString());
            holder.message.setText(messages.get(position).getBody());
        }
    }

    @Override
    public int getItemCount() {
        if (messages == null) {
            return 0;
        }
        return messages.size();
    }

    class ChatHistoryViewHolder extends RecyclerView.ViewHolder {

        View itemView;
        TextView username;
        TextView message;

        public ChatHistoryViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            username = (TextView) itemView.findViewById(R.id.username_text_view);
            message = (TextView) itemView.findViewById(R.id.message_text_view);
        }
    }

}
