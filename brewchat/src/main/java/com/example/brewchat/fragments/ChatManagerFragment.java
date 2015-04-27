package com.example.brewchat.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.brewchat.R;
import com.example.brewchat.adapters.ChatGroupRecyclerAdapter;
import com.example.brewchat.mockers.MockerUtil;

/**
 * Created by Josh
 */

public class ChatManagerFragment extends Fragment {

    Toolbar mainToolbar = null;

    // Dummy listener, in case the activity doesn't implement it.
    OnViewLoadedListener loadedListner = new OnViewLoadedListener() {
        @Override
        public void onViewLoaded() {
        }
    };

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
        ChatGroupRecyclerAdapter adapter = new ChatGroupRecyclerAdapter(getActivity(), MockerUtil.makeMockChatGroups());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mainToolbar = (Toolbar) layout.findViewById(R.id.main_toolbar);

        loadedListner.onViewLoaded();

        return layout;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof OnViewLoadedListener)
            loadedListner = (OnViewLoadedListener) activity;
    }

    public Toolbar getMainToolbar() {
        return mainToolbar;
    }

    public interface OnViewLoadedListener {
        public abstract void onViewLoaded();
    }


}
