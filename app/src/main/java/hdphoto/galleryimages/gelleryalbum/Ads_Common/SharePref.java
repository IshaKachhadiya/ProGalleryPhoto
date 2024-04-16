package hdphoto.galleryimages.gelleryalbum.Ads_Common;

import android.content.Context;

public class SharePref {
    private static final String PREF_CNT_OPEN_APP = "PREF_CNT_OPEN_APP";
    private static final String PREF_NAME = "APPLICATION_FIND_PHONE";

    public static int getPrefCntOpenApp(Context context) {
        return context.getSharedPreferences(PREF_NAME, 0).getInt(PREF_CNT_OPEN_APP, 0);
    }

    public static void putPrefCntOpenApp(Context context, int i) {
        context.getSharedPreferences(PREF_NAME, 0).edit().putInt(PREF_CNT_OPEN_APP, i).apply();
    }
}
