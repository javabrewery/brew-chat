package com.example.brewchat.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.brewchat.R;
import com.example.brewchat.activities.ContactsActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class NavigationDrawerFragment extends Fragment {

    @OnClick(R.id.contacts_nav_item)
    void contactsClicked() {
        startActivity(new Intent(getActivity(), ContactsActivity.class));
    }

    public NavigationDrawerFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_navigation_drawer, container, false);
        ButterKnife.inject(this, view);
        return view;
    }


}