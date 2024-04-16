package hdphoto.galleryimages.gelleryalbum.utils;

import androidx.exifinterface.media.ExifInterface;
import java.util.ArrayList;


public class PrivateMasterPassword {
    ArrayList<String> msAArray;
    ArrayList<String> msBArray;
    ArrayList<String> msCArray;

    public ArrayList<String> GetMSArrayA() {
        msAArray = new ArrayList<>();
        msAArray.add("5");
        msAArray.add(ExifInterface.GPS_MEASUREMENT_2D);
        msAArray.add("6");
        msAArray.add("9");
        return msAArray;
    }

    public ArrayList<String> GetMSArrayB() {
        msBArray = new ArrayList<>();
        msBArray.add("4");
        msBArray.add("8");
        msBArray.add("6");
        msBArray.add("5");
        return msBArray;
    }

    public ArrayList<String> GetMSArrayC() {
        msCArray = new ArrayList<>();
        msCArray.add("7");
        msCArray.add("5");
        msCArray.add("9");
        msCArray.add(ExifInterface.GPS_MEASUREMENT_2D);
        return msCArray;
    }
}
