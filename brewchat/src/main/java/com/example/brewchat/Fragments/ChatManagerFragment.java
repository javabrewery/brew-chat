package com.example.brewchat.Fragments;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.brewchat.Domain.ChatGroup;
import com.example.brewchat.mockers.MockerUtil;

import com.example.brewchat.Adapters.ChatGroupRecyclerAdapter;
import com.example.brewchat.R;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by josh on 4/22/15.
 */

public class ChatManagerFragment extends Fragment{

    private OnFragmentInteractionListener mListener;
    private RecyclerView recyclerView;
    private ChatGroupRecyclerAdapter adapter;


    public static ChatManagerFragment newInstance(Bundle savedInstanceState) {

        return new ChatManagerFragment();
    }

    public ChatManagerFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View layout = inflater.inflate(R.layout.chat_manager_fragment, container, false);

        recyclerView = (RecyclerView) layout.findViewById(R.id.chatgroup_recycler_view);

        adapter = new ChatGroupRecyclerAdapter(getActivity(), MockerUtil.makeMockChatGroups());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        return layout;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }
}
