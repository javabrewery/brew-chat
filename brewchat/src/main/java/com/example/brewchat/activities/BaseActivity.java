package com.example.brewchat.activities;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.example.brewchat.Application;
import com.example.brewchat.R;
import com.example.brewchat.events.UserLogoutEvent;

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

    private void displayLogoutConfimationDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle(getString(R.string.logout_title_alert));
        alertDialog.setMessage(getString(R.string.logout_confirm_alert));
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                logout();
            }
        });

        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialog.show();
    }

    private void logout() {
        Application.getChatService().logout();
        Log.d("Base Activity", "User logged out");
        Toast.makeText(BaseActivity.this, "Successfully Logged Out", Toast.LENGTH_LONG).show();
        finish();
    }

    @SuppressWarnings("unused")
    public void onEventMainThread(UserLogoutEvent event) {
        displayLogoutConfimationDialog();
    }

}
