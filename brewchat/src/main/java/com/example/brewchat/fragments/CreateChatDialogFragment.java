package com.example.brewchat.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.brewchat.R;
import com.example.brewchat.interfaces.AddChatGroupListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Dialog for constructing new QB chat group
 *
 * Created by josh on 4/26/15.
 */
public class CreateChatDialogFragment extends DialogFragment {
    @InjectView (R.id.create_chat_name_editText)
    EditText editTextChatName;
    @InjectView (R.id.create_chat_addusers_editText)
    EditText editTextAddUsers;
    @InjectView (R.id.create_chat_cancel_button)
    Button buttonCancel;

    AddChatGroupListener addChatGroupListener;

    public static CreateChatDialogFragment newInstance(Bundle savedInstanceState) {
        return new CreateChatDialogFragment();
    }

    public CreateChatDialogFragment() {
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            addChatGroupListener = (AddChatGroupListener) activity;
        } catch(Exception e) {
            Log.e("CreateChatDialogFrag", "Activity must implement AddChatGroupListener");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.create_chat_dialog,container,false);
        ButterKnife.inject(this, view);

        // for testing purposes - saves typing
        editTextChatName.setText("Test group chat");
        editTextAddUsers.setText("2965508,2965510,2965514");
        return view;
    }


    @OnClick(R.id.create_chat_create_button)
    public void createChat() {
        ArrayList<String> userStringIds = getUserStringIds();
        ArrayList<Integer> userIds = getUserIds(userStringIds);
        addChatGroupListener.addChatGroup(editTextChatName.getText().toString(), userIds);
        dismiss();
    }

    @OnClick(R.id.create_chat_cancel_button)
    public void cancel() {
        dismiss();
    }

    private ArrayList<Integer> getUserIds(List<String> userStringIds) {
        ArrayList<Integer> userIds = new ArrayList<>();
        for(String user: userStringIds){
            userIds.add(Integer.parseInt(user));
        }
        return userIds;
    }

    private ArrayList<String> getUserStringIds() {
        ArrayList<String> list = new ArrayList<>();
        Collections.addAll(list, editTextAddUsers.getText().toString().split(","));
        return list;
    }

}
