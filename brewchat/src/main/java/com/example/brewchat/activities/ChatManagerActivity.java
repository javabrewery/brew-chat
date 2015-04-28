package com.example.brewchat.activities;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.brewchat.Application;
import com.example.brewchat.R;
import com.example.brewchat.domain.ChatGroup;
import com.example.brewchat.events.CreateChatError;
import com.example.brewchat.events.GroupChatCreatedEvent;
import com.example.brewchat.fragments.ChatManagerFragment;
import com.example.brewchat.fragments.CreateChatDialogFragment;
import com.example.brewchat.interfaces.AddChatGroupListener;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import de.greenrobot.event.EventBus;

public class ChatManagerActivity extends AppCompatActivity implements AddChatGroupListener{
    private static final String TAG = "ChatManagerActivity";
    ChatManagerFragment chatManagerFragment;

    @InjectView(R.id.app_bar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_manager);
        ButterKnife.inject(this);
        setSupportActionBar(toolbar);
        if (savedInstanceState == null) {
            chatManagerFragment = new ChatManagerFragment();

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.chat_manager_container, chatManagerFragment)
                    .commit();
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @SuppressWarnings("unused")
    public void onEvent(GroupChatCreatedEvent event) {
        Toast.makeText(this,getString(R.string.chat_created_toast),Toast.LENGTH_LONG).show();
        chatManagerFragment.addChatGroup(new ChatGroup(event.getName(), event.getUserIds()));
    }

    public void onEvent(CreateChatError event) {
        //TODO make more informative error message
        Toast.makeText(this,getString(R.string.create_chat_error_toast), Toast.LENGTH_LONG).show();
    }

    public void addChatGroup(String title, ArrayList<Integer> userIds) {
        ((Application)getApplication()).getChatService().addChatGroup(title, userIds);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_chat_manager, menu);
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
        } else if(id == R.id.action_add_group_chat) {
            showAddDialog();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showAddDialog() {
        CreateChatDialogFragment fragment = new CreateChatDialogFragment();
        fragment.show(getSupportFragmentManager(), "CreateChatDialogFragment");
    }


    @Override
    public void onBackPressed() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle(getString(R.string.logout_title_alert)); // Sets title for your alertbox
        alertDialog.setMessage(getString(R.string.logout_confirm_alert)); // Message to be displayed on alertbox
        /* When positive (yes/ok) is clicked */
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                ((Application)getApplication()).getChatService().logout();
                Log.d(TAG,"User logged out");
                finish();
                Toast.makeText(ChatManagerActivity.this, "Successfully Logged Out", Toast.LENGTH_LONG).show();
            }
        });

        /* When negative (No/cancel) button is clicked*/
        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialog.show();
    }
}


