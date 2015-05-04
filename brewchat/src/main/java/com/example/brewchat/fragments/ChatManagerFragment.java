package com.example.brewchat.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.brewchat.R;
import com.example.brewchat.adapters.ChatGroupRecyclerAdapter;
import com.example.brewchat.domain.ChatGroup;

import java.util.ArrayList;

/**
 * Created by Josh
 */

public class ChatManagerFragment extends Fragment {
    ChatGroupRecyclerAdapter adapter;

    public static ChatManagerFragment newInstance(Bundle savedInstanceState) {
        return new ChatManagerFragment();
    }

    public ChatManagerFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View layout = inflater.inflate(R.layout.chat_manager_fragment, container, false);

        RecyclerView recyclerView = (RecyclerView) layout.findViewById(R.id.chatgroup_recycler_view);

        //Fill with mocked data for testing UI
        adapter = new ChatGroupRecyclerAdapter(getActivity());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        return layout;
    }
    public void addChatGroup(ChatGroup chatGroup){
        adapter.addChatGroup(chatGroup);
        adapter.notifyDataSetChanged();
    }

    public void setChatGroupList(ArrayList<ChatGroup> chatGroupList) {
        adapter.setChatGroupList(chatGroupList);
        adapter.notifyDataSetChanged();
    }

}
