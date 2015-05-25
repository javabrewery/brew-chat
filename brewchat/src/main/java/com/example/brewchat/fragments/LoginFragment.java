package com.example.brewchat.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.ToggleButton;

import com.example.brewchat.Application;
import com.example.brewchat.R;
import com.example.brewchat.chatservices.FakeChatService;
import com.example.brewchat.chatservices.IRCChatService;
import com.example.brewchat.chatservices.QBChatService;
import com.example.brewchat.interfaces.LoginListener;

import java.util.Arrays;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Fragment that host login/register activities
 *
 * Created by josh on 4/24/15.
 */

public class LoginFragment extends Fragment {

    @InjectView(R.id.fake_toggleButton)
    Spinner chatServiceSpinner;
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

        ((AppCompatActivity)getActivity()).getSupportActionBar().hide();

        // for testing, set default values to save typing
        usernameEditText.setText("user1");
        passwordEditText.setText("12345678");

        String[] chatServices = {"QuickBlox", "IRC", "Fake"};

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                                                                android.R.layout.simple_list_item_1,
                                                                chatServices);

        chatServiceSpinner.setAdapter(adapter);
        chatServiceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    Application.chatService = new QBChatService(getActivity().getApplicationContext(),
                            getString(R.string.app_id),
                            getString(R.string.auth_key),
                            getString(R.string.auth_secret));
                } else if (i == 1) {
                    Application.chatService = new IRCChatService();
                } else {
                    Application.chatService = new FakeChatService();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

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

}
