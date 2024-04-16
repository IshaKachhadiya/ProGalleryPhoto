package hdphoto.galleryimages.gelleryalbum.duplicate;

import android.content.Context;
import android.content.SharedPreferences;

import hdphoto.galleryimages.gelleryalbum.R;

public class LoginPreferenceManager {
    public static boolean delete = false;
    public Context context;

    public LoginPreferenceManager(Context context) {
        this.context = context;
    }

    public static void saveFavData(Context context, String str, boolean z) {
        SharedPreferences.Editor edit = context.getSharedPreferences(Utills.LOGIN_PREFERENCE, 0).edit();
        edit.putBoolean("fav:" + str, z);
        edit.commit();
    }

    public static boolean getFavData(Context context, String str) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Utills.LOGIN_PREFERENCE, 0);
        return sharedPreferences.getBoolean("fav:" + str, false);
    }

    public static String GetStringData(Context context, String str) {
        return context.getSharedPreferences(Utills.LOGIN_PREFERENCE, 0).getString(str, null);
    }
}