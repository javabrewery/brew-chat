package com.example.brewchat;

import android.os.StrictMode;
import android.util.Log;

import com.facebook.stetho.Stetho;

public class Application extends android.app.Application {
    private static final String TAG = "Application";

    final String APP_ID = "22306";
    final String AUTH_KEY = "S2pgg2kV3cfLUAm";
    final String AUTH_SECRET = "am-HzubL-aMmekY";

    ChatService chatService;

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

        this.chatService = new ChatService(getApplicationContext(), APP_ID, AUTH_KEY, AUTH_SECRET);
    }

    public ChatService getChatService() {
        return this.chatService;
    }
}
