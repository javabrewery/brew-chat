package com.example.brewchat.activities;

import android.support.v7.app.AppCompatActivity;

import de.greenrobot.event.EventBus;

public class BaseActivity extends AppCompatActivity {

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

}
