package com.example.brewchat;

import android.os.StrictMode;
import android.util.Log;

import com.facebook.stetho.Stetho;

public class Application extends android.app.Application {

    private static final String TAG = Application.class.getSimpleName();

    public static IChatService chatService;

    private static Notifier notifier;

    @Override
    public void onCreate() {
        super.onCreate();

        if (BuildConfig.DEBUG) {
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                    .detectAll()
                    .penaltyLog()
                    .penaltyFlashScreen()
                    .build());
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                    .detectAll()
                    .penaltyLog()
                    .build());

            //Enable Stetho for debugging
            Stetho.initialize(
                    Stetho.newInitializerBuilder(this)
                            .enableDumpapp(
                                    Stetho.defaultDumperPluginsProvider(this))
                            .enableWebKitInspector(
                                    Stetho.defaultInspectorModulesProvider(this))
                            .build());

            Log.d(TAG, "Stetho and StrictMode Initialized");
        }

        chatService = new ChatService(getApplicationContext(),
                getString(R.string.app_id),
                getString(R.string.auth_key),
                getString(R.string.auth_secret));

        PreferenceManager.init(this);

        notifier = Notifier.getInstance(this);
    }

    public static IChatService getChatService() {
        return Application.chatService;
    }
}
