package hdphoto.galleryimages.gelleryalbum.utils;

import android.graphics.Bitmap;
import android.net.Uri;

import androidx.lifecycle.MutableLiveData;

public class Utils {

    public static final String mediaPath = "_data";
    public static Bitmap imageBitmap = null;
    public static Uri imageUri = null;
    public static MutableLiveData<String> stringMutableLiveData = new MutableLiveData<>();

}
