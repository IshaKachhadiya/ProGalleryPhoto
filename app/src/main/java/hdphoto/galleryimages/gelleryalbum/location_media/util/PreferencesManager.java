package hdphoto.galleryimages.gelleryalbum.location_media.util;

import android.content.Context;
import android.content.SharedPreferences;

import hdphoto.galleryimages.gelleryalbum.location_media.common.Constant;


public class PreferencesManager {
    public static void saveImageShowADs(Context context, Boolean bool) {
        SharedPreferences.Editor edit = context.getSharedPreferences(Constant.SHARED_PREFS, 0).edit();
        edit.putBoolean(Constant.SHARED_PREFS_IMAGE_SHOW, bool.booleanValue());
        edit.apply();
    }

    public static int getImageGrid(Context context) {
        return context.getSharedPreferences(Constant.SHARED_PREFS, 0).getInt(Constant.SHARED_PREFS_GRID, 4);
    }
}
