package com.example.brewchat.fragments;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.brewchat.R;
import com.example.brewchat.adapters.ContactsRecyclerAdapter;
import com.example.brewchat.domain.User;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContactsFragment extends Fragment {

    RecyclerView recyclerView;
    private boolean attached = false;
    List<User> delayedUpdatedContacts = null;

    public ContactsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_contact, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.contacts_list);

        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (delayedUpdatedContacts != null)
            recyclerView.setAdapter(new ContactsRecyclerAdapter(activity, delayedUpdatedContacts));
        delayedUpdatedContacts = null;
        attached = true;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        attached = false;
    }

    public void setContacts(List<User> contacts) {
        if (attached)
            recyclerView.setAdapter(new ContactsRecyclerAdapter(getActivity(), contacts));
        else
            delayedUpdatedContacts = contacts;
    }

}
