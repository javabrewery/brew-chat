package com.example.brewchat.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.brewchat.Application;
import com.example.brewchat.R;
import com.example.brewchat.events.AuthenticationErrorEvent;
import com.example.brewchat.events.RegisterUserError;
import com.example.brewchat.events.UserLoggedEvent;
import com.example.brewchat.events.UserSignedUpEvent;
import com.example.brewchat.fragments.LoginFragment;
import com.example.brewchat.fragments.RegisterDialogFragment;
import com.example.brewchat.interfaces.LoginListener;
import com.example.brewchat.interfaces.RegisterDialogListener;

public class LoginActivity extends BaseActivity implements RegisterDialogListener,
        LoginListener{

    LoginFragment loginFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_screen);
        if (savedInstanceState == null) {
            loginFragment = new LoginFragment();

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.login_screen_frame, loginFragment)
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
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

    public void buttonAuth(String username, String password) {
        Application.getChatService().login(username, password);
    }

    public void showRegisterDialog(View view) {
        RegisterDialogFragment fragment = new RegisterDialogFragment();
        fragment.show(getSupportFragmentManager(), "RegisterDialogFragment");
    }

    @Override
    public void onCreateAccount(String username, String password) {
        Application.getChatService().register(username, password);
    }

    @SuppressWarnings("unused")
    public void onEventMainThread(UserLoggedEvent event) {
        Intent intent = new Intent(this, ChatManagerActivity.class);
        startActivity(intent);
    }

    @SuppressWarnings("unused")
    public void onEventMainThread(UserSignedUpEvent event) {
        Toast.makeText(this,getString(R.string.account_created_toast),Toast.LENGTH_LONG).show();
        loginFragment.updateTextFields(event.getUsername());
    }

    @SuppressWarnings("unused")
    public void onEventMainThread(AuthenticationErrorEvent event){
        Toast.makeText(this, getString(R.string.login_error_toast), Toast.LENGTH_LONG).show();
    }

    @SuppressWarnings("unused")
    public void onEventMainThread(RegisterUserError event) {
        Toast.makeText(this, "Error in registration", Toast.LENGTH_LONG).show();
    }

}