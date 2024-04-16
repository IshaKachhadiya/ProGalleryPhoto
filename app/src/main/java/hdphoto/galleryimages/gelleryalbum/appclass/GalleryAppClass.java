package hdphoto.galleryimages.gelleryalbum.appclass;

import android.app.Application;
import android.content.SharedPreferences;

import com.iten.tenoku.utils.MyApplication;

import hdphoto.galleryimages.gelleryalbum.location_media.common.Constant;


public class GalleryAppClass extends MyApplication {
    GalleryAppClass galleryAppClass;
    GalleryAppClass myApplication;
    private static GalleryAppClass mInstance;

    static SharedPreferences.Editor prefEditor;
    static SharedPreferences preferences;
    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;

        galleryAppClass = this;
        myApplication = this;

        SharedPreferences sharedPreferences = getSharedPreferences("gallery", 0);
        preferences = sharedPreferences;
        SharedPreferences.Editor edit = sharedPreferences.edit();
        prefEditor = edit;
        edit.commit();
    }

    public static int getTabPos() {
        return preferences.getInt(Constant.SHARED_PREFS_TAB_POS, 0);
    }

    public static void putTabPos(int i) {
        prefEditor.putInt(Constant.SHARED_PREFS_TAB_POS, i);
        prefEditor.commit();
    }

    public static synchronized GalleryAppClass getInstance() {
        GalleryAppClass myApplication;
        synchronized (GalleryAppClass.class) {
            myApplication = mInstance;
        }
        return myApplication;
    }
}
