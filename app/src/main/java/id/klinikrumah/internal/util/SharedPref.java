package id.klinikrumah.internal.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import org.jetbrains.annotations.NotNull;

public class SharedPref {

    public static void delete(String prefName, @NotNull Context context, String KEY) {
        SharedPreferences credentialDataPref = context.getSharedPreferences(prefName, Activity.MODE_PRIVATE);
        Editor editorPreference = credentialDataPref.edit();
        editorPreference.remove(KEY);
        editorPreference.apply();
    }

    public static void putString(String prefName, @NotNull Context context, String KEY, String value) {
        SharedPreferences credentialDataPref = context.getSharedPreferences(prefName, Activity.MODE_PRIVATE);
        Editor editorPreference = credentialDataPref.edit();
        editorPreference.putString(KEY, value);
        editorPreference.apply();
    }

    public static String getString(String prefName, @NotNull Context context, String KEY) {
        SharedPreferences credentialDataPref = context.getSharedPreferences(prefName, Activity.MODE_PRIVATE);
        return credentialDataPref.getString(KEY, "");
    }

    public static void putInt(String prefName, @NotNull Context context, String KEY, int value) {
        SharedPreferences credentialDataPref = context.getSharedPreferences(prefName, Activity.MODE_PRIVATE);
        Editor editorPreference = credentialDataPref.edit();
        editorPreference.putInt(KEY, value);
        editorPreference.apply();
    }

    public static int getInt(String prefName, @NotNull Context context, String KEY) {
        SharedPreferences credentialDataPref = context.getSharedPreferences(prefName, Activity.MODE_PRIVATE);
        return credentialDataPref.getInt(KEY, 0);
    }

    public static void putBoolean(String prefName, @NotNull Context context, String KEY, Boolean value) {
        SharedPreferences credentialDataPref = context.getSharedPreferences(prefName, Activity.MODE_PRIVATE);
        Editor editorPreference = credentialDataPref.edit();
        editorPreference.putBoolean(KEY, value);
        editorPreference.apply();
    }

    public static boolean getBoolean(String prefName, @NotNull Context context, String KEY) {
        SharedPreferences credentialDataPref = context.getSharedPreferences(prefName, Activity.MODE_PRIVATE);
        return credentialDataPref.getBoolean(KEY, false);
    }

}
