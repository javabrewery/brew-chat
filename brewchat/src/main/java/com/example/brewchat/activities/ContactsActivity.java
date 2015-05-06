package com.example.brewchat.activities;

import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.widget.Toast;

import com.example.brewchat.Application;
import com.example.brewchat.R;
import com.example.brewchat.events.UsersLoadedEvent;
import com.example.brewchat.events.UsersLoadingErrorEvent;
import com.example.brewchat.fragments.ContactsFragment;
import com.example.brewchat.fragments.NavigationDrawerFragment;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class ContactsActivity extends BaseActivity {

    private ContactsFragment contactsFragment;
    private NavigationDrawerFragment navigationDrawerFragment;

    @InjectView(R.id.main_drawer_layout)
    DrawerLayout mainDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
        ButterKnife.inject(this);

        contactsFragment = new ContactsFragment();
        navigationDrawerFragment = new NavigationDrawerFragment();

        Application.getChatService().loadContacts();

        getSupportFragmentManager().beginTransaction()
                .add(R.id.contacts_container, contactsFragment)
                .add(R.id.navigation_drawer_container, navigationDrawerFragment)
                .commit();
    }

    @SuppressWarnings("unused")
    public void onEvent(UsersLoadedEvent event) {
        contactsFragment.setContacts(event.getUsers());
    }

    @SuppressWarnings("unused")
    public void onEvent(UsersLoadingErrorEvent event) {
        StringBuilder builder = new StringBuilder();
        for (String error : event.getErrors()) {
            builder.append(error);
            builder.append("\n");
        }
        Toast.makeText(this, "Error loading users:\n" + builder.toString(), Toast.LENGTH_LONG).show();
    }

}
