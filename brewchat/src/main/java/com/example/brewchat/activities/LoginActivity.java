package com.example.brewchat.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.brewchat.Application;
import com.example.brewchat.R;
import com.example.brewchat.UserLoggedEvent;

import de.greenrobot.event.EventBus;


public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_screen);
        EventBus.getDefault().register(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            System.out.println("You should implement some settings");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //Simply for navigation purposes during Dev
    public void goToChatFragment(View view) {
        ((Application)getApplication()).getChatService().login("user5", "12345678");
    }

    @SuppressWarnings("unused")
    public void onEvent(UserLoggedEvent event) {
        Intent intent = new Intent(this, ChatManagerActivity.class);
        startActivity(intent);
    }
}
