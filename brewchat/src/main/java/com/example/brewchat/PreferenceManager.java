package com.example.brewchat;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by jon on 14/05/15.
 */
public class PreferenceManager {

    private static final String DEFAULT_NAME = "preferences";

    private static Context context;
    private static boolean inited = false;

    public static void init(Context context) {
        if (!inited) PreferenceManager.context = context.getApplicationContext();
        inited = true;
    }

    public static void savePreference(String key, String value) {
        if (!inited) throw new IllegalStateException("Must call PreferenceManager.init() first!");
        SharedPreferences.Editor editor =
                context.getSharedPreferences(DEFAULT_NAME, Context.MODE_PRIVATE).edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static String loadPreference(String key) {
        if (!inited) throw new IllegalStateException("Must call PreferenceManager.init() first!");
        return context.getSharedPreferences(DEFAULT_NAME, Context.MODE_PRIVATE).getString(key, null);
    }

}
