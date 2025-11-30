package com.example.minivocabularywithp2l;

import android.content.Context;
import android.content.SharedPreferences;

public class MySharedPreferences {
    private static final String PREF_NAME = "MiniVocabPref";
    private static final String KEY_EMAIL = "key_email";
    private static final String KEY_IS_ADMIN = "key_is_admin";
    private static final String KEY_DARK_MODE = "key_dark_mode";

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public MySharedPreferences(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void saveUserSession(String email, boolean isAdmin) {
        editor.putString(KEY_EMAIL, email);
        editor.putBoolean(KEY_IS_ADMIN, isAdmin);
        editor.apply();
    }

    public String getUserEmail() {
        return sharedPreferences.getString(KEY_EMAIL, "Guest");
    }

    public boolean isAdmin() {
        return sharedPreferences.getBoolean(KEY_IS_ADMIN, false);
    }

    public void clearUserSession() {
        editor.remove(KEY_EMAIL);
        editor.remove(KEY_IS_ADMIN);
        editor.apply();
    }

    public void setDarkMode(boolean isDarkMode) {
        editor.putBoolean(KEY_DARK_MODE, isDarkMode);
        editor.apply();
    }

    public boolean isDarkMode() {
        return sharedPreferences.getBoolean(KEY_DARK_MODE, false);
    }
}
