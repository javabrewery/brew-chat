package com.example.brewchat.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.example.brewchat.R;
import com.example.brewchat.fragments.ChatManagerFragment;
import com.example.brewchat.fragments.NavigationDrawerFragment;

public class ChatManagerActivity extends AppCompatActivity {

    private ChatManagerFragment chatManagerFragment;
    private NavigationDrawerFragment navigationDrawerFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_manager);
        if (savedInstanceState == null) {
            chatManagerFragment = new ChatManagerFragment();
            navigationDrawerFragment = new NavigationDrawerFragment();

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.chat_manager_container, chatManagerFragment)
                    .commit();

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.navigation_drawer_container, navigationDrawerFragment)
                    .commit();
        }
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
        }

        return super.onOptionsItemSelected(item);
    }

}
