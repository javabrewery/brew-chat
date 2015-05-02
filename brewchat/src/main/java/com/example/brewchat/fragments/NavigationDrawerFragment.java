package com.example.brewchat.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.brewchat.R;
import com.example.brewchat.events.UserLogoutEvent;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

public class NavigationDrawerFragment extends Fragment {
    @InjectView(R.id.logout_button)
    LinearLayout logoutLinearLayout;

    public NavigationDrawerFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_navigation_drawer, container, false);
        ButterKnife.inject(this, layout);
        return layout;
    }

    @OnClick(R.id.logout_button)
    public void logout() {
        EventBus.getDefault().post(new UserLogoutEvent());
    }


}