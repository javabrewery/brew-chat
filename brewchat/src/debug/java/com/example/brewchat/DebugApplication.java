package com.example.brewchat;

import android.os.StrictMode;
import android.util.Log;

import com.facebook.stetho.Stetho;

public class DebugApplication extends android.app.Application {
    private static final String TAG = "DebugApplication";

    @Override
    public void onCreate() {
        super.onCreate();
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
}
