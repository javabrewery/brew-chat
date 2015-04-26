package com.example.brewchat.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.brewchat.R;
import com.example.brewchat.interfaces.LoginListener;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Fragment that host login/register activities
 *
 * Created by josh on 4/24/15.
 */

public class LoginFragment extends Fragment {

    @InjectView(R.id.login_button)
    Button loginButton;
    @InjectView(R.id.login_username_edittext)
    EditText usernameEditText;
    @InjectView(R.id.login_password_edittext)
    EditText passwordEditText;

    LoginListener loginListener;

    public static LoginFragment newInstance(Bundle savedInstanceState) {
        return new LoginFragment();
    }

    public LoginFragment() {
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            loginListener = (LoginListener) activity;
        } catch(Exception e) {
            Log.e("LoginFragment", "Activity must implement LoginListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.login_fragment, container, false);
        ButterKnife.inject(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginListener.buttonAuth(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString());
            }
        });
    }
    public void updateTextFields(String username){
        usernameEditText.setText(username);
    }
}
