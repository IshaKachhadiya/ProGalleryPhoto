package hdphoto.galleryimages.gelleryalbum.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;


public class SharePrefUtil {
    private static SharedPreferences mSharePref;

    public static void init(Context context) {
        if (mSharePref == null) {
            mSharePref = PreferenceManager.getDefaultSharedPreferences(context);
        }
    }

    public static void forceTutorial(Context context) {
        SharedPreferences.Editor edit = context.getSharedPreferences("data", 0).edit();
        edit.putBoolean("ringtone_tutorial", true);
        edit.apply();
    }

    public static boolean isTutorial(Context context) {
        return context.getSharedPreferences("data", 0).getBoolean("ringtone_tutorial", false);
    }
}
