package com.example.brewchat.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.example.brewchat.R;
import com.example.brewchat.activities.LoginActivity;
import com.quickblox.auth.QBAuth;
import com.quickblox.auth.model.QBSession;
import com.quickblox.chat.QBChatService;
import com.quickblox.core.LogLevel;
import com.quickblox.core.QBEntityCallbackImpl;
import com.quickblox.core.QBSettings;
import com.quickblox.users.model.QBUser;

import java.util.List;

/**
 * Created by Ryanm14 on 4/24/2015.
 */
public class AuthenticationModelFragment extends Fragment {

    private static final String TAG = "AuthModelFragment";
    private static final String APP_ID = "22306";
    private static final String AUTH_KEY = "S2pgg2kV3cfLUAm";
    private static final String AUTH_SECRET = "am-HzubL-aMmekY";
    QBChatService chatService;

    public AuthenticationModelFragment() {
    }

    public static AuthenticationModelFragment newInstance(Bundle savedInstanceState) {
        return new AuthenticationModelFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        QBSettings.getInstance().fastConfigInit(
                getString(R.string.app_id), getString(R.string.auth_key),
                getString(R.string.auth_secret));
        QBSettings.getInstance().setLogLevel(LogLevel.DEBUG);
        Log.d(TAG, "Initializing");
        if (!QBChatService.isInitialized()) {
            QBChatService.init(getActivity());
            Log.d(TAG, "isInitialized");
        }
        chatService = QBChatService.getInstance();
    }

    public void login(String username, String password) {
        Log.d(TAG, "Creating User");
        final QBUser user = new QBUser(username, password);
        Log.d(TAG, "User Created");
        QBAuth.createSession(user, new QBEntityCallbackImpl<QBSession>() {
            @Override
            public void onSuccess(QBSession session, Bundle params) {
                Log.d(TAG, "Creating Session");
                user.setId(session.getUserId());

                chatService.login(user, new QBEntityCallbackImpl() {
                    @Override
                    public void onSuccess() {
                        Log.d(TAG, "User Authenticated");
                        LoginActivity login = (LoginActivity) getActivity();
                        login.goToChatFragment();
                    }

                    @Override
                    public void onError(List errors) {
                        Log.e(TAG, "Error in Authentication");
                        for (int i = 0; i < errors.size(); i++) {
                            Log.e(TAG, "ERROR" + i + errors.get(i).toString());
                        }
                        //Add toast to Login Activity
                    }
                });
            }

            @Override
            public void onError(List<String> errors) {
                Log.e(TAG, "Check credentials");
            }
        });

    }
}
