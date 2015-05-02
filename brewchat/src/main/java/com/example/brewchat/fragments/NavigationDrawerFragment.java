package com.example.brewchat.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.brewchat.R;
import com.example.brewchat.interfaces.LogoutListener;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class NavigationDrawerFragment extends Fragment {
    LogoutListener logoutListener;
    @InjectView(R.id.logout_button)
    LinearLayout logoutLinearLayout;

    public NavigationDrawerFragment() {
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try{
        logoutListener = (LogoutListener) activity;
        } catch (Exception name) {
        Log.e("NavDrawerFragment","Activity must implement LogoutListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_navigation_drawer, container, false);
        ButterKnife.inject(this,layout);
        return layout;
    }
    @OnClick (R.id.logout_button)
    public void logout(){
        logoutListener.logout();
    }



}