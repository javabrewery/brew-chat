package com.example.brewchat.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v7.widget.Toolbar;

import com.example.brewchat.Application;
import com.example.brewchat.R;
import com.example.brewchat.adapters.ChatHistoryRecyclerAdapter;
import com.example.brewchat.domain.ChatGroup;
import com.example.brewchat.events.GetChatHistoryEvent;
import com.example.brewchat.events.GetGroupChatsEvent;
import com.example.brewchat.fragments.ChatManagerFragment;
import com.example.brewchat.fragments.GroupChatFragment;
import com.example.brewchat.interfaces.GetHistoryListener;
import com.quickblox.chat.model.QBDialog;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class GroupChatActivity extends AppCompatActivity implements GetHistoryListener {
    private GroupChatFragment groupChatFragment;


    @InjectView(R.id.app_bar)
    Toolbar toolbar;

    public static String EXTRA_CHAT_GROUP = "chatgroup";
    private ChatGroup chatGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_chat);
        ButterKnife.inject(this);
        setSupportActionBar(toolbar);

        if (savedInstanceState == null) {
            groupChatFragment = GroupChatFragment.newInstance(getIntent().getExtras());

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.chat_group_activity_container, groupChatFragment)
                    .commit();
        }
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

    @Override
    public void getHistory(QBDialog dialog) {
        ((Application)getApplication()).getChatService().getChatHistory(dialog);
    }

    public void onEvent(GetChatHistoryEvent event) {
        groupChatFragment.setChatHistory(event.getQbChatMessageArrayList());
    }
}
