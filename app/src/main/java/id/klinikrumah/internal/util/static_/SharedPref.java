package id.klinikrumah.internal.util.static_;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import id.klinikrumah.internal.App;

public class SharedPref {
    private static final String CACHE = "cache";
    private static SharedPreferences sharedPref = App.getInstance().getSharedPreferences(CACHE, Activity.MODE_PRIVATE);
    private static Editor editor = sharedPref.edit();

    public static void delete(String KEY) {
        editor.remove(KEY);
        editor.apply();
    }

    public static void putString(String KEY, String value) {
        editor.putString(KEY, value);
        editor.apply();
    }

    public static String getString(String KEY) {
        return sharedPref.getString(KEY, "");
    }

    public static void putInt(String KEY, int value) {
        editor.putInt(KEY, value);
        editor.apply();
    }

    public static int getInt(String KEY) {
        return sharedPref.getInt(KEY, 0);
    }

    public static void putBoolean(String KEY, Boolean value) {
        editor.putBoolean(KEY, value);
        editor.apply();
    }

    public static boolean getBoolean(String KEY) {
        return sharedPref.getBoolean(KEY, false);
    }
}