package hdphoto.galleryimages.gelleryalbum.sorting;

import android.content.Context;
import android.content.SharedPreferences;
import hdphoto.galleryimages.gelleryalbum.utils.FolderPath;


public class LoginPreferenceUtilsData {
    public Context context;

    public LoginPreferenceUtilsData(Context context) {
        this.context = context;
    }

    public static void SaveStringData(Context context, String str, String str2) {
        SharedPreferences.Editor edit = context.getSharedPreferences(FolderPath.LOGIN_PREFERENCE_DATA, 0).edit();
        edit.putString(str, str2);
        edit.commit();
    }

    public static String GetStringData(Context context, String str) {
        return context.getSharedPreferences(FolderPath.LOGIN_PREFERENCE_DATA, 0).getString(str, null);
    }
}
