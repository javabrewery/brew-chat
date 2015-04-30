package com.example.brewchat.activities;

import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;

import com.example.brewchat.Application;
import com.example.brewchat.R;
import com.example.brewchat.events.UsersLoadedEvent;
import com.example.brewchat.fragments.ContactsFragment;
import com.example.brewchat.fragments.NavigationDrawerFragment;
import com.quickblox.core.exception.QBResponseException;

import butterknife.ButterKnife;
import butterknife.InjectView;
import de.greenrobot.event.EventBus;

public class ContactsActivity extends AppCompatActivity {

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
        try {
            ((Application) getApplicationContext()).getChatService().loadContacts();
        } catch (QBResponseException e) {
            e.printStackTrace();
        }
        getSupportFragmentManager().beginTransaction()
                .add(R.id.contacts_container, contactsFragment)
                .add(R.id.navigation_drawer_container, navigationDrawerFragment)
                .commit();
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    public void onEvent(UsersLoadedEvent event) {
        contactsFragment.setContacts(event.getUsers());
    }

}
