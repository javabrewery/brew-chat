package com.example.brewchat.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.brewchat.R;
import com.example.brewchat.activities.GroupChatActivity;
import com.example.brewchat.domain.ChatGroup;

import java.util.ArrayList;

/**
 * Created by josh on 4/22/15.
 * <p/>
 * RecyclerAdapter for displaying ChatGroups
 */
public class ChatGroupRecyclerAdapter extends RecyclerView.Adapter<ChatGroupRecyclerAdapter.MyViewHolder> {

    Context context;
    LayoutInflater inflater;
    ArrayList<ChatGroup> chatGroups = new ArrayList<>();

    public ChatGroupRecyclerAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
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
//        holder.description.setText(currentChatGroup.);
        holder.itemView.setOnClickListener(new JoinChatClickListener(position));
    }

    public void addChatGroup(ChatGroup chatGroup){
        chatGroups.add(chatGroup);
    }

    public void setChatGroupList(ArrayList<ChatGroup> chatGroupList){
        chatGroups.addAll(chatGroupList);
    }

    @Override
    public int getItemCount() {
        return chatGroups.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        View itemView;
        TextView title;
        TextView description;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            title = (TextView) itemView.findViewById(R.id.title_text_view);
            description = (TextView) itemView.findViewById(R.id.description_text_view);
        }
    }

    class JoinChatClickListener implements View.OnClickListener {
        private int position;

        public JoinChatClickListener(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View view) {
            Intent i = new Intent(context, GroupChatActivity.class);
            i.putExtra(GroupChatActivity.EXTRA_CHAT_GROUP, chatGroups.get(position));
            context.startActivity(i);
        }
    }

}
