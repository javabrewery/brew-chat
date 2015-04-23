package com.example.brewchat.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.brewchat.R;
import com.example.brewchat.domain.ChatHistory;

/**
 * Created by jon on 22/04/15.
 */
public class ChatHistoryRecyclerAdapter extends RecyclerView.Adapter<ChatHistoryRecyclerAdapter.ChatHistoryViewHolder> {

    private Context context;
    private ChatHistory history;
    private LayoutInflater inflater;

    public ChatHistoryRecyclerAdapter(Context context, ChatHistory history) {
        this.context = context;
        this.history = history;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public ChatHistoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.chat_message_card_layout, parent, false);
        return (new ChatHistoryViewHolder(view));
    }

    @Override
    public void onBindViewHolder(ChatHistoryViewHolder holder, int position) {
        holder.username.setText(history.getMessages().get(position).getSender().getName());
        holder.message.setText(history.getMessages().get(position).getMessage());
    }

    @Override
    public int getItemCount() {
        return history.getMessages().size();
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
