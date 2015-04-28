package com.example.brewchat.fragments;


import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.brewchat.R;
import com.example.brewchat.domain.ChatGroup;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * A simple {@link Fragment} subclass.
 */
public class GroupChatFragment extends Fragment {
    public static final String EXTRA_CHAT_GROUP = "chatgroup";
    private ChatGroup chatGroup;

    @InjectView(R.id.messages_recyclerview)
    RecyclerView messagesRecyclerView;

    public GroupChatFragment() {
    }

    public static GroupChatFragment newInstance(Bundle bundle) {
        GroupChatFragment gcf = new GroupChatFragment();
        gcf.setChatGroup((ChatGroup) bundle.getSerializable(EXTRA_CHAT_GROUP));

        return gcf;
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

        ButterKnife.inject(view);
        messagesRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        return view;
    }

    public void setChatGroup(ChatGroup chatGroup) {
        this.chatGroup = chatGroup;
    }
}
