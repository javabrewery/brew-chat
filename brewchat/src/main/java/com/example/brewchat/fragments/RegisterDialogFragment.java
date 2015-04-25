package com.example.brewchat.fragments;


import android.app.Activity;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.brewchat.R;
import com.example.brewchat.interfaces.RegisterDialogListener;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class RegisterDialogFragment extends DialogFragment {

    @InjectView(R.id.register_user_enter_username)
    EditText usernameEditText;
    @InjectView(R.id.register_user_enter_password)
    EditText passwordEditText;
    @InjectView(R.id.register_user_confirm_password)
    EditText confirmPasswordEditText;
    @InjectView(R.id.cancel_register_button)
    Button cancelButton;
    @InjectView(R.id.create_account_dialog_button)
    Button createButton;

    RegisterDialogListener registerDialogListener;


    // TODO: Rename and change types and number of parameters
    public static RegisterDialogFragment newInstance(Bundle saveInstanceState) {

        return new RegisterDialogFragment();
    }

    public RegisterDialogFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.register_user_dialog, container, false);
        ButterKnife.inject(this, view);
        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            registerDialogListener = (RegisterDialogListener) activity;
        } catch(Exception e) {
            Log.e("RegisterDialogFragment", "Activity must implement RegisterDialogListener");
        }

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String enteredPassword = passwordEditText.getText().toString();
                String confirmPassword = confirmPasswordEditText.getText().toString();
                if (enteredPassword.equals(confirmPassword)) {
                    registerDialogListener.onCreateAccount(usernameEditText.getText().toString(),
                            enteredPassword);
                    dismiss();
                } else {
                    Toast.makeText(getActivity(), "Passwords do not match",
                            Toast.LENGTH_LONG).show();
                }
            }

        });

    }
}
