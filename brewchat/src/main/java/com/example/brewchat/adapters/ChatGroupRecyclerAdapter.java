package com.example.brewchat.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.brewchat.domain.ChatGroup;
import com.example.brewchat.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by josh on 4/22/15.
 *
 * RecyclerAdapter for displaying ChatGroups
 */
public class ChatGroupRecyclerAdapter extends RecyclerView.Adapter<ChatGroupRecyclerAdapter.MyViewHolder> {

    LayoutInflater inflater;
    List<ChatGroup> chatGroups;


    public ChatGroupRecyclerAdapter(Context context, ArrayList<ChatGroup> chatGroups) {
        inflater = LayoutInflater.from(context);
        this.chatGroups = chatGroups;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.chat_group_card_layout, parent, false);
        return (new MyViewHolder(view));

    }

    @Override
    public void onBindViewHolder(ChatGroupRecyclerAdapter.MyViewHolder holder, int position) {
        ChatGroup currentChatGroup = chatGroups.get(position);
        holder.title.setText(currentChatGroup.getTitle());
        holder.description.setText(currentChatGroup.getChatInfo());
    }

    @Override
    public int getItemCount() {
        return chatGroups.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        TextView title;
        TextView description;

        public MyViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title_text_view);
            description = (TextView) itemView.findViewById(R.id.description_text_view);

        }
    }
}
