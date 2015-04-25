package com.example.brewchat.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.example.brewchat.R;
import com.example.brewchat.fragments.AuthenticationModelFragment;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class LoginActivity extends AppCompatActivity {

    private static final String MODEL = "AuthModelFragment";
    @InjectView(R.id.login_username_edittext) EditText username;
    @InjectView(R.id.login_password_edittext) EditText password;
    AuthenticationModelFragment auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_screen);
        ButterKnife.inject(this);
        if (savedInstanceState == null) {
            auth = new AuthenticationModelFragment();

            getSupportFragmentManager().beginTransaction()
                    .add(auth, MODEL)
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
    public void goToChatFragment(View view) {
        auth.login(username.getText().toString(), password.getText().toString());
        Log.d("LoginActivity","Trying to login");
    }

    public void goToChatFragment() {
        Intent intent = new Intent(this, ChatManagerActivity.class);
        startActivity(intent);
    }
}
