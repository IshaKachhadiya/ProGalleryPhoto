package hdphoto.galleryimages.gelleryalbum.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class Preference {
    public static final String PREF_NAME = "PREF_PROFILE";
    public static final int MODE = Context.MODE_PRIVATE;

    private static final String KEY_FIRST_LAUNCH = "firstLaunch";

    public static boolean isFirstLaunch(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getBoolean(KEY_FIRST_LAUNCH, true);
    }

    public static void setFirstLaunchDone(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(KEY_FIRST_LAUNCH, false);
        editor.apply();
    }

    public static String getString(Context context, String key, String defValue) {
        return getPreferences(context).getString(key, defValue);
    }

    public static SharedPreferences getPreferences(Context context) {
        return context.getSharedPreferences(PREF_NAME, MODE);
    }

}

