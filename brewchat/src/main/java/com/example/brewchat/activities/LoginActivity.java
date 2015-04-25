package com.example.brewchat.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.brewchat.Application;
import com.example.brewchat.R;
import com.example.brewchat.UserLoggedEvent;
import com.example.brewchat.fragments.LoginFragment;
import com.example.brewchat.fragments.RegisterDialogFragment;
import com.example.brewchat.interfaces.LoginListener;
import com.example.brewchat.interfaces.RegisterDialogListener;

import de.greenrobot.event.EventBus;


public class LoginActivity extends AppCompatActivity implements RegisterDialogListener,
        LoginListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_screen);
        EventBus.getDefault().register(this);
        if (savedInstanceState == null) {
            Fragment fragment = new LoginFragment();

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.login_screen_frame, fragment)
                    .commit();
        }
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
    public void buttonAuth(String username, String password) {
        ((Application)getApplication()).getChatService().login(username, password);
    }

    @SuppressWarnings("unused")
    public void onEvent(UserLoggedEvent event) {
        Intent intent = new Intent(this, ChatManagerActivity.class);
        startActivity(intent);
    }

    public void error(){
        Toast.makeText(LoginActivity.this, "Error in logging in", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onCreateAccount(String username, String password) {
        ((Application)getApplication()).getChatService().register(username, password);
    }

    public void showRegisterDialog(View view) {
        RegisterDialogFragment fragment = new RegisterDialogFragment();
        fragment.show(getSupportFragmentManager(), "RegisterDialogFragment");
    }
}
