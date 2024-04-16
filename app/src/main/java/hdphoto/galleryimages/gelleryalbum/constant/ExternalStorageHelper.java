package hdphoto.galleryimages.gelleryalbum.constant;

import android.os.Environment;


public class ExternalStorageHelper {
    static boolean ext_storageReadable;
    static boolean ext_storageWritable;

    public static boolean isExternalStorageReadableAndWritable() {
        checkExternalStorage();
        return ext_storageReadable && ext_storageWritable;
    }

    private static void checkExternalStorage() {
        String externalStorageState = Environment.getExternalStorageState();
        if (externalStorageState.equals("mounted")) {
            ext_storageWritable = true;
            ext_storageReadable = true;
        } else if (externalStorageState.equals("mounted") || externalStorageState.equals("mounted_ro")) {
            ext_storageReadable = true;
            ext_storageWritable = false;
        } else {
            ext_storageWritable = false;
            ext_storageReadable = false;
        }
    }
}
