package com.example.brewchat.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.brewchat.R;
import com.example.brewchat.adapters.ChatHistoryRecyclerAdapter;
import com.example.brewchat.domain.ChatGroup;
import com.example.brewchat.interfaces.GetHistoryListener;
import com.quickblox.chat.model.QBChatMessage;
import com.quickblox.chat.model.QBDialog;

import java.lang.reflect.Array;
import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * A simple {@link Fragment} subclass.
 */
public class GroupChatFragment extends Fragment {
    public static final String EXTRA_CHAT_GROUP = "chatgroup";
    private QBDialog chatGroup;
    private ArrayList<QBChatMessage> chatHistory;
    private ChatHistoryRecyclerAdapter chatHistoryRecyclerAdapter;
    private GetHistoryListener getHistoryListener;


    @InjectView(R.id.messages_recyclerview)
    RecyclerView messagesRecyclerView;

    public GroupChatFragment() {
    }

    public static GroupChatFragment newInstance(Bundle bundle) {
        GroupChatFragment gcf = new GroupChatFragment();
        gcf.setChatGroup((QBDialog) bundle.getSerializable(EXTRA_CHAT_GROUP));

        return gcf;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getHistoryListener = (GetHistoryListener) getActivity();
        getHistoryListener.getHistory(chatGroup);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_group_chat, container, false);

        ButterKnife.inject(this, view);
        messagesRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        chatHistoryRecyclerAdapter = new ChatHistoryRecyclerAdapter(getActivity(), chatHistory);
        messagesRecyclerView.setAdapter(chatHistoryRecyclerAdapter);

        return view;
    }

    public void setChatGroup(QBDialog chatGroup) {
        this.chatGroup = chatGroup;
    }
    public void setChatHistory(ArrayList<QBChatMessage> chatHistory) {this.chatHistory = chatHistory;}

}
