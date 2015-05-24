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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ToggleButton;

import com.example.brewchat.Application;
import com.example.brewchat.chatservices.QBChatService;
import com.example.brewchat.chatservices.FakeChatService;
import com.example.brewchat.R;
import com.example.brewchat.interfaces.LoginListener;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

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
    @InjectView(R.id.remember_checkbox)
    CheckBox rememberCheckBox;

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

        // for testing, set default values to save typing
        usernameEditText.setText("user1");
        passwordEditText.setText("12345678");

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginListener.buttonAuth(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString(), rememberCheckBox.isChecked());
            }
        });
    }

    public void updateTextFields(String username){
        usernameEditText.setText(username);
    }

    @OnClick(R.id.fake_toggleButton)
    @SuppressWarnings("unused")
    public void onFakeToggleClicked(View view) {
        boolean on = ((ToggleButton) view).isChecked();

        if (on) {
            Application.chatService = new FakeChatService();
        } else {
            Application.chatService = new QBChatService(getActivity().getApplicationContext(),
                    getString(R.string.app_id),
                    getString(R.string.auth_key),
                    getString(R.string.auth_secret));
        }
    }
}
