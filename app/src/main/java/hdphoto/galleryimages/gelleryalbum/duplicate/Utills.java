package hdphoto.galleryimages.gelleryalbum.duplicate;

import android.os.Environment;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class Utills {
    public static final String LOGIN_PREFERENCE = "momentsgallery";
    public static ArrayList<pictureFacer> images = new ArrayList<>();
    public static int Hieght = 0;
    public static int Width = 0;

    public static boolean isAlbumLongPress = false;


    public static int getWidth(float f) {
        return (int) ((f * Width) / 100.0f);
    }

    public static int getHeight(float f) {
        return (int) ((f * Hieght) / 100.0f);
    }
}