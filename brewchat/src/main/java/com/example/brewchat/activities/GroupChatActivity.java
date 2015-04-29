package com.example.brewchat.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import com.example.brewchat.R;
import com.example.brewchat.adapters.ChatHistoryRecyclerAdapter;
import com.example.brewchat.domain.ChatGroup;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class GroupChatActivity extends AppCompatActivity {

    public static final String EXTRA_CHAT_GROUP = "chatgroup";

    @InjectView(R.id.messages_recyclerview)
    RecyclerView messagesRecyclerView;

    private ChatGroup chatGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_chat);
        ButterKnife.inject(this);
        messagesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        chatGroup = (ChatGroup) getIntent().getExtras().getSerializable(EXTRA_CHAT_GROUP);
        messagesRecyclerView.setAdapter(new ChatHistoryRecyclerAdapter(this, chatGroup.getChatHistory()));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_group_chat, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
