package com.example.brewchat.activities;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.brewchat.Application;
import com.example.brewchat.R;
import com.example.brewchat.events.CreateChatError;
import com.example.brewchat.events.GetGroupChatsErrorEvent;
import com.example.brewchat.events.GetGroupChatsEvent;
import com.example.brewchat.events.GroupChatCreatedEvent;
import com.example.brewchat.fragments.ChatManagerFragment;
import com.example.brewchat.fragments.CreateChatDialogFragment;
import com.example.brewchat.fragments.NavigationDrawerFragment;
import com.example.brewchat.interfaces.AddChatGroupListener;
import com.example.brewchat.interfaces.LogoutListener;

import java.util.ArrayList;

public class ChatManagerActivity extends BaseActivity implements AddChatGroupListener, LogoutListener{

    private static final String TAG = ChatManagerActivity.class.getSimpleName();

    ChatManagerFragment chatManagerFragment;
    NavigationDrawerFragment navigationDrawerFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_manager);
        if (savedInstanceState == null) {
            chatManagerFragment = new ChatManagerFragment();
            navigationDrawerFragment = new NavigationDrawerFragment();

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.chat_manager_container, chatManagerFragment)
                    .add(R.id.navigation_drawer_container, navigationDrawerFragment)
                    .commit();
        }

        Application.getChatService().getChatDialogs();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_chat_manager, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        } else if (id == R.id.action_add_group_chat) {
            showAddDialog();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showAddDialog() {
        CreateChatDialogFragment fragment = new CreateChatDialogFragment();
        fragment.show(getSupportFragmentManager(), "CreateChatDialogFragment");
    }

    public void addChatGroup(String title, ArrayList<Integer> userIds) {
        Application.getChatService().addChatGroup(title, userIds);
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle(getString(R.string.logout_title_alert)); // Sets title for your alertbox
        alertDialog.setMessage(getString(R.string.logout_confirm_alert)); // Message to be displayed on alertbox
        /* When positive (yes/ok) is clicked */
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Application.getChatService().logout();
                Log.d(TAG, "User logged out");
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

    @SuppressWarnings("unused")
    public void onEventMainThread(GroupChatCreatedEvent event) {
        Toast.makeText(this, getString(R.string.chat_created_toast), Toast.LENGTH_LONG).show();
        chatManagerFragment.addChatGroup(event.getDialog());
    }

    @SuppressWarnings("unused")
    public void onEventMainThread(CreateChatError event) {
        //TODO make more informative error message
        Toast.makeText(this, getString(R.string.create_chat_error_toast), Toast.LENGTH_LONG).show();
    }

    @SuppressWarnings("unused")
    public void onEventMainThread(GetGroupChatsEvent event) {
        chatManagerFragment.setChatGroupList(event.getChatGroups());
    }

    @SuppressWarnings("unused")
    public void onEventMainThread(GetGroupChatsErrorEvent event) {
        Toast.makeText(this, "Error pulling chats groups from server", Toast.LENGTH_LONG).show();
        for (String error : event.getErrors()) {
            Log.e("ChatManagerActivity", error);
        }
    }
}


