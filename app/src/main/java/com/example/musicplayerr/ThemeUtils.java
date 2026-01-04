package com.example.musicplayerr;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class ThemeUtils {
    private static final String PREFS_NAME = "ThemePrefs";
    private static final String KEY_THEME = "current_theme";

    // Call this in EVERY Activity's onCreate() BEFORE super.onCreate()
    public static void applyTheme(Activity activity) {
        SharedPreferences prefs = activity.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        int theme = prefs.getInt(KEY_THEME, androidx.appcompat.R.style.Theme_AppCompat_DayNight_DarkActionBar);
        activity.setTheme(theme);
    }

    // Call this when the menu button is clicked
    public static void saveTheme(Activity activity, int themeResId) {
        SharedPreferences prefs = activity.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        prefs.edit().putInt(KEY_THEME, themeResId).apply();
    }
}