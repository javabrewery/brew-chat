package com.example.brewchat.activities;

import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.brewchat.R;
import com.example.brewchat.adapters.ChatHistoryRecyclerAdapter;
import com.example.brewchat.domain.ChatGroup;
import com.example.brewchat.fragments.NavigationDrawerFragment;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class GroupChatActivity extends AppCompatActivity {

    public static final String EXTRA_CHAT_GROUP = "chatgroup";

    @InjectView(R.id.messages_recyclerview)
    RecyclerView messagesRecyclerView;

    @InjectView(R.id.main_drawer_layout)
    DrawerLayout mainDrawerLayout;

    @InjectView(R.id.main_toolbar)
    Toolbar mainToolbar;

    private NavigationDrawerFragment navigationDrawerFragment;

    private ChatGroup chatGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_chat);
        ButterKnife.inject(this);
        messagesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        chatGroup = (ChatGroup) getIntent().getExtras().getSerializable(EXTRA_CHAT_GROUP);
        messagesRecyclerView.setAdapter(new ChatHistoryRecyclerAdapter(this, chatGroup.getChatHistory()));

        navigationDrawerFragment = new NavigationDrawerFragment();

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.navigation_drawer_container, navigationDrawerFragment)
                .commit();

        setSupportActionBar(mainToolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mainDrawerLayout, mainToolbar, R.string.drawer_open, R.string.drawer_close
        );
        mainDrawerLayout.setDrawerListener(toggle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        toggle.syncState();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_group_chat, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
