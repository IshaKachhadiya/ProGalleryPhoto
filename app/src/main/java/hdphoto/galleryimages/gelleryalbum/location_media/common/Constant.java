package hdphoto.galleryimages.gelleryalbum.location_media.common;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import androidx.core.content.ContextCompat;
import androidx.exifinterface.media.ExifInterface;
import androidx.palette.graphics.Palette;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import hdphoto.galleryimages.gelleryalbum.location_media.event.LocationEvent;
import hdphoto.galleryimages.gelleryalbum.location_media.model.AlbumData;
import hdphoto.galleryimages.gelleryalbum.location_media.model.LocationImageData;
import hdphoto.galleryimages.gelleryalbum.location_media.model.PictureData;
import hdphoto.galleryimages.gelleryalbum.location_media.rx.RxBus;


public class Constant {
    public static final String DIRECTORY_DCIM_PATH;
    public static final String DIRECTORY_PICTURES_PATH;
    public static final String FOLDER_PATH;
    public static final String PREF_LANGUAGE = "pref_language";
    public static final String SHARED_PREFS = "Gallery";
    public static final String SHARED_PREFS_AD_COUNT = "timestamp_ad_count";
    public static final String SHARED_PREFS_AD_ON = "timestamp_ad_on";
    public static final String SHARED_PREFS_AD_TYPE = "timestamp_ad_type";
    public static final String SHARED_PREFS_APP_ID = "timestamp_app_id";
    public static final String SHARED_PREFS_BANNER = "timestamp_banner";
    public static final String SHARED_PREFS_DARK_MODE = "gallery_dark_mode";
    public static final String SHARED_PREFS_DATE = "timestamp_date";
    public static final String SHARED_PREFS_FB_BANNER = "timestamp_fb_banner";
    public static final String SHARED_PREFS_FB_INTERSTITIAL = "timestamp_fb_interstitial";
    public static final String SHARED_PREFS_FB_NATIVE = "timestamp_fb_native";
    public static final String SHARED_PREFS_GRID = "image_grid";
    public static final String SHARED_PREFS_IMAGE_SHOW = "timestamp_image_show";
    public static final String SHARED_PREFS_INSTALL = "timestamp_install";
    public static final String SHARED_PREFS_INTERSTITIAL = "timestamp_interstitial";
    public static final String SHARED_PREFS_NATIVE = "timestamp_native";
    public static final String SHARED_PREFS_OPEN = "timestamp_open";
    public static final String SHARED_PREFS_QUREKA_ADS = "timestamp_qureka_app";
    public static final String SHARED_PREFS_TAB_POS = "gallery_tab_pos";
    public static final String UNHIDE_PATH;
    public static final String UNHIDE_PATH_11;
    public static final String WHATSAPP_PATH;
    public static final String WHATSAPP_Q_PATH;
    public static ArrayList<AlbumData> albumList = null;
    public static LinkedHashMap<String, ArrayList<PictureData>> albumWisePictures = null;
    public static ArrayList<Object> allData = null;
    public static ArrayList<PictureData> copyMoveImageList = null;
    public static ArrayList<PictureData> copyMoveList = null;
    public static LinkedHashMap<String, ArrayList<PictureData>> dateWisePictures = null;
    public static LinkedHashMap<String, ArrayList<PictureData>> dateWisePicturesVideo = null;
    public static ArrayList<String> deleteList = null;
    public static ArrayList<PictureData> displayImageList = null;
    public static ArrayList<AlbumData> folderList = null;
    public static HashMap<String, ArrayList<PictureData>> locationHashmap = null;
    public static ArrayList<LocationImageData> locationImageDatas = null;
    public static LinkedHashMap<String, ArrayList<PictureData>> monthWisePictures = null;
    public static ArrayList<PictureData> photoColorList = null;
    public static ArrayList<Object> photoList = null;
    public static String songExtension = ".mp3";
    public static String songFile = "over_the_horizon";
    public static String songFolder = "Song";
    public static ArrayList<AlbumData> storyList;
    public static LinkedHashMap<String, ArrayList<PictureData>> thingImages;
    public static ArrayList<Object> videoList;
    public static AlbumData albumData = new AlbumData();
    public static Boolean LOCATION_GET = false;
    public static Boolean IMAGES_GET = false;
    public static Boolean IMAGES_COLOR_GET = false;
    public static Boolean VIDEO_GET = false;
    public static boolean OPEN_ADS = false;
    public static final String HIDE_FOLDER_NAME = ".PrivateData";
    public static final String HIDE_PATH = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getPath() + "/Gallery/" + HIDE_FOLDER_NAME;

    static {
        StringBuilder sb = new StringBuilder();
        sb.append(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getPath());
        sb.append("/Gallery");
        FOLDER_PATH = sb.toString();
        WHATSAPP_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/WhatsApp/Media/.Statuses";
        WHATSAPP_Q_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android/media/com.whatsapp/WhatsApp/Media/.Statuses";
        DIRECTORY_PICTURES_PATH = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getPath();
        DIRECTORY_DCIM_PATH = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getPath();
        UNHIDE_PATH = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getPath() + "/Gallery";
        UNHIDE_PATH_11 = Environment.DIRECTORY_PICTURES + "/Gallery";
        dateWisePictures = new LinkedHashMap<>();
        monthWisePictures = new LinkedHashMap<>();
        dateWisePicturesVideo = new LinkedHashMap<>();
        albumWisePictures = new LinkedHashMap<>();
        thingImages = new LinkedHashMap<>();
        photoList = new ArrayList<>();
        photoColorList = new ArrayList<>();
        allData = new ArrayList<>();
        deleteList = new ArrayList<>();
        copyMoveImageList = new ArrayList<>();
        folderList = new ArrayList<>();
        storyList = new ArrayList<>();
        locationImageDatas = new ArrayList<>();
        locationHashmap = new HashMap<>();
        videoList = new ArrayList<>();
        copyMoveList = new ArrayList<>();
        albumList = new ArrayList<>();
        displayImageList = new ArrayList<>();
    }

    public static boolean checkPermission(Activity activity, String str) {
        return ContextCompat.checkSelfPermission(activity, str) == 0;
    }

    static boolean checkStoragePermission(Context context) {
        return ContextCompat.checkSelfPermission(context, "android.permission.READ_EXTERNAL_STORAGE") == 0 && ContextCompat.checkSelfPermission(context, "android.permission.WRITE_EXTERNAL_STORAGE") == 0;
    }

    public static String getFilenameExtension(String str) {
        return str.substring(str.lastIndexOf(".") + 1);
    }
    
    public static void getLocationImages(Context context) {
        Uri uri;
        float floatValue;
        float floatValue2;
        ArrayList<PictureData> arrayList;
        locationImageDatas.clear();
        locationHashmap.clear();
//        if (checkStoragePermission(context)) {
            String[] strArr = {"_data", "bucket_display_name", "date_modified", "_display_name"};
            try {
                if (Build.VERSION.SDK_INT >= 29) {
                    uri = MediaStore.Images.Media.getContentUri("external");
                } else {
                    uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                }
                Cursor query = context.getContentResolver().query(uri, strArr, null, null, "date_modified DESC");
                if (query == null || !query.moveToFirst()) {
                    return;
                }
                do {
                    try {
                        String string = query.getString(query.getColumnIndexOrThrow("_data"));
                        String string2 = query.getString(query.getColumnIndexOrThrow("_display_name"));
                        String string3 = query.getString(query.getColumnIndexOrThrow("bucket_display_name"));
                        PictureData pictureData = new PictureData();
                        pictureData.setFilePath(string);
                        pictureData.setFileName(string2);
                        pictureData.setFolderName(string3);
                        ExifInterface exifInterface = new ExifInterface(string);
                        String attribute = exifInterface.getAttribute(ExifInterface.TAG_GPS_LATITUDE);
                        String attribute2 = exifInterface.getAttribute(ExifInterface.TAG_GPS_LATITUDE_REF);
                        String attribute3 = exifInterface.getAttribute(ExifInterface.TAG_GPS_LONGITUDE);
                        String attribute4 = exifInterface.getAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF);
                        if (attribute != null && attribute2 != null && attribute3 != null && attribute4 != null) {
                            if (attribute2.equals("N")) {
                                floatValue = convertToDegree(attribute).floatValue();
                            } else {
                                floatValue = 0.0f - convertToDegree(attribute).floatValue();
                            }
                            if (attribute4.equals(ExifInterface.LONGITUDE_EAST)) {
                                floatValue2 = convertToDegree(attribute3).floatValue();
                            } else {
                                floatValue2 = 0.0f - convertToDegree(attribute3).floatValue();
                            }
                            String address = getAddress(context, Float.valueOf(floatValue), Float.valueOf(floatValue2));
                            if (address != null) {
                                pictureData.setLat(String.valueOf(floatValue));
                                pictureData.setLongs(String.valueOf(floatValue2));
                                if (locationHashmap.containsKey(address)) {
                                    arrayList = locationHashmap.get(address);
                                } else {
                                    arrayList = new ArrayList<>();
                                }
                                arrayList.add(pictureData);
                                locationHashmap.put(address, arrayList);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } while (query.moveToNext());
                query.close();
                ArrayList arrayList2 = new ArrayList(locationHashmap.keySet());
                for (int i = 0; i < arrayList2.size(); i++) {
                    ArrayList<PictureData> arrayList3 = locationHashmap.get(arrayList2.get(i));
                    LocationImageData locationImageData = new LocationImageData();
                    locationImageData.setTitle((String) arrayList2.get(i));
                    locationImageData.setLat(arrayList3.get(0).getLat());
                    locationImageData.setLongs(arrayList3.get(0).getLongs());
                    locationImageData.setPictureData(arrayList3);
                    locationImageData.setSelect(false);
                    locationImageDatas.add(locationImageData);
                }
                LOCATION_GET = true;
                RxBus.getInstance().post(new LocationEvent(locationImageDatas));
                return;
            } catch (Exception e2) {
                e2.printStackTrace();
                return;
            }
//        }
//        LOCATION_GET = true;
    }

    public static void getLocationImagesNew(Context context) {
        float floatValue;
        float floatValue2;
        ArrayList<PictureData> arrayList;
        locationImageDatas.clear();
        locationHashmap.clear();
        Uri contentUri = MediaStore.Files.getContentUri("external");
        Cursor query = context.getContentResolver().query(contentUri, new String[]{"_data", "bucket_display_name", "date_modified", "_display_name"}, "media_type=1", null, "date_modified DESC");
        if (query != null) {
            query.moveToFirst();
            while (query.moveToNext()) {
                String string = query.getString(query.getColumnIndexOrThrow("_data"));
                String string2 = query.getString(query.getColumnIndexOrThrow("_display_name"));
                String string3 = query.getString(query.getColumnIndexOrThrow("bucket_display_name"));
                PictureData pictureData = new PictureData();
                pictureData.setFilePath(string);
                pictureData.setFileName(string2);
                pictureData.setFolderName(string3);
                String location = getLocation(string, context);
                Log.e("locationData", "locationName: " + location);
                try {
                    ExifInterface exifInterface = new ExifInterface(string);
                    exifInterface.getLatLong(new float[2]);
                    try {
                        String attribute = exifInterface.getAttribute(ExifInterface.TAG_GPS_LATITUDE);
                        String attribute2 = exifInterface.getAttribute(ExifInterface.TAG_GPS_LATITUDE_REF);
                        String attribute3 = exifInterface.getAttribute(ExifInterface.TAG_GPS_LONGITUDE);
                        String attribute4 = exifInterface.getAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF);
                        if (attribute != null && attribute2 != null && attribute3 != null && attribute4 != null) {
                            if (attribute2.equals("N")) {
                                floatValue = convertToDegree(attribute).floatValue();
                            } else {
                                floatValue = 0.0f - convertToDegree(attribute).floatValue();
                            }
                            if (attribute4.equals(ExifInterface.LONGITUDE_EAST)) {
                                floatValue2 = convertToDegree(attribute3).floatValue();
                            } else {
                                floatValue2 = 0.0f - convertToDegree(attribute3).floatValue();
                            }
                            try {
                                String address = getAddress(context, Float.valueOf(floatValue), Float.valueOf(floatValue2));
                                if (address != null) {
                                    pictureData.setLat(String.valueOf(floatValue));
                                    pictureData.setLongs(String.valueOf(floatValue2));
                                    if (locationHashmap.containsKey(address)) {
                                        arrayList = locationHashmap.get(address);
                                    } else {
                                        arrayList = new ArrayList<>();
                                    }
                                    arrayList.add(pictureData);
                                    locationHashmap.put(address, arrayList);
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    } catch (Exception e2) {
                        e2.printStackTrace();
                    }
                } catch (IOException e3) {
                    Log.d("Data", "getImages: ====> Error" + e3.getLocalizedMessage());
                }
            }
            query.close();
            ArrayList arrayList2 = new ArrayList(locationHashmap.keySet());
            for (int i = 0; i < arrayList2.size(); i++) {
                ArrayList<PictureData> arrayList3 = locationHashmap.get(arrayList2.get(i));
                LocationImageData locationImageData = new LocationImageData();
                locationImageData.setTitle((String) arrayList2.get(i));
                locationImageData.setLat(arrayList3.get(0).getLat());
                locationImageData.setLongs(arrayList3.get(0).getLongs());
                locationImageData.setPictureData(arrayList3);
                locationImageData.setSelect(false);
                locationImageDatas.add(locationImageData);
            }
            LOCATION_GET = true;
            RxBus.getInstance().post(new LocationEvent(locationImageDatas));
        }
    }

    private static String getLocation(String str, Context context) {
        ExifInterface exifInterface;
        float floatValue;
        float floatValue2;
        if (str == null) {
            return null;
        }
        try {
            exifInterface = new ExifInterface(str);
        } catch (IOException e) {
            Log.e("ECSGDHSFDYSG", e.getMessage() + "::");
            e.printStackTrace();
            exifInterface = null;
        }
        try {
            String attribute = exifInterface.getAttribute(ExifInterface.TAG_GPS_LATITUDE);
            String attribute2 = exifInterface.getAttribute(ExifInterface.TAG_GPS_LATITUDE_REF);
            String attribute3 = exifInterface.getAttribute(ExifInterface.TAG_GPS_LONGITUDE);
            String attribute4 = exifInterface.getAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF);
            if (attribute == null || attribute2 == null || attribute3 == null || attribute4 == null) {
                return null;
            }
            if (attribute2.equals("N")) {
                floatValue = convertToDegree(attribute).floatValue();
            } else {
                floatValue = 0.0f - convertToDegree(attribute).floatValue();
            }
            if (attribute4.equals(ExifInterface.LONGITUDE_EAST)) {
                floatValue2 = convertToDegree(attribute3).floatValue();
            } else {
                floatValue2 = 0.0f - convertToDegree(attribute3).floatValue();
            }
            try {
                return getAddress(context, Float.valueOf(floatValue), Float.valueOf(floatValue2));
            } catch (IOException e2) {
                e2.printStackTrace();
                return null;
            }
        } catch (Exception unused) {
            return null;
        }
    }

    public static void getColorImages(Context context) {
        IMAGES_COLOR_GET = false;
        Iterator<PictureData> it = photoColorList.iterator();
        while (it.hasNext()) {
            PictureData next = it.next();
            if (new File(next.getFilePath()).exists()) {
                next.setColorList(getAllColorInImages(next.getFilePath()));
            }
        }
        IMAGES_COLOR_GET = true;
    }

    private static ArrayList<Integer> getAllColorInImages(String str) {
        Bitmap decodeFile = null;
        final ArrayList<Integer> arrayList = new ArrayList<>();
        try {
            decodeFile = BitmapFactory.decodeFile(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (decodeFile == null) {
            return arrayList;
        }
        Palette.from(decodeFile).generate(new Palette.PaletteAsyncListener() { // from class: com.gallery.photos.pro.common.Constant$$ExternalSyntheticLambda0
            @Override // androidx.palette.graphics.Palette.PaletteAsyncListener
            public final void onGenerated(Palette palette) {
                Constant.lambda$getAllColorInImages$2(arrayList, palette);
            }
        });
        return arrayList;
    }

    
    public static  void lambda$getAllColorInImages$2(ArrayList arrayList, Palette palette) {
        List<Palette.Swatch> swatches = palette.getSwatches();
        if (swatches == null || swatches.size() == 0) {
            return;
        }
        for (Palette.Swatch swatch : swatches) {
            arrayList.add(Integer.valueOf(swatch.getRgb()));
        }
    }

    public static void getImages(Context context) {
        Uri uri;
        ArrayList<PictureData> arrayList;
        ArrayList<PictureData> arrayList2;
        ArrayList<PictureData> arrayList3;
        String str = "_display_name";
        String str2 = "date_modified";
        photoList.clear();
        photoColorList.clear();
        folderList.clear();
        storyList.clear();
        dateWisePictures.clear();
        monthWisePictures.clear();
        albumWisePictures.clear();
        IMAGES_GET = false;
//        if (checkStoragePermission(context)) {
            try {
                String[] strArr = {"_data", "bucket_display_name", "date_modified", "_display_name"};
                if (Build.VERSION.SDK_INT >= 29) {
                    uri = MediaStore.Images.Media.getContentUri("external");
                } else {
                    uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                }
                Cursor query = context.getContentResolver().query(uri, strArr, null, null, "date_modified DESC");
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMMM yyyy", Locale.ENGLISH);
                SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("MMM yyyy", Locale.ENGLISH);
                Calendar calendar = Calendar.getInstance();
                String format = simpleDateFormat.format(Long.valueOf(calendar.getTimeInMillis()));
                calendar.add(5, -1);
                String format2 = simpleDateFormat.format(Long.valueOf(calendar.getTimeInMillis()));
                if (query != null) {
                    query.moveToFirst();
                    ArrayList arrayList4 = new ArrayList();
                    query.moveToFirst();
                    while (!query.isAfterLast()) {
                        String string = query.getString(query.getColumnIndexOrThrow("_data"));
                        String string2 = query.getString(query.getColumnIndexOrThrow(str));
                        String string3 = query.getString(query.getColumnIndexOrThrow("bucket_display_name"));
                        long j = query.getLong(query.getColumnIndexOrThrow(str2)) * 1000;
                        String format3 = simpleDateFormat2.format(Long.valueOf(j));
                        String str3 = str;
                        String format4 = simpleDateFormat.format(Long.valueOf(j));
                        if (format4.equals(format)) {
                            format4 = "Today";
                        } else if (format4.equals(format2)) {
                            format4 = "Yesterday";
                        }
                        String str4 = str2;
                        PictureData pictureData = new PictureData();
                        pictureData.setFilePath(string);
                        pictureData.setFileName(string2);
                        pictureData.setFolderName(string3);
                        pictureData.setDate(j);
                        arrayList4.add(pictureData);
                        photoColorList.add(pictureData);
                        if (dateWisePictures.containsKey(format4)) {
                            arrayList = dateWisePictures.get(format4);
                            if (arrayList == null) {
                                arrayList = new ArrayList<>();
                            }
                        } else {
                            arrayList = new ArrayList<>();
                        }
                        arrayList.add(pictureData);
                        dateWisePictures.put(format4, arrayList);
                        if (albumWisePictures.containsKey(string3)) {
                            arrayList2 = albumWisePictures.get(string3);
                            if (arrayList2 == null) {
                                arrayList2 = new ArrayList<>();
                            }
                        } else {
                            arrayList2 = new ArrayList<>();
                        }
                        arrayList2.add(pictureData);
                        albumWisePictures.put(string3, arrayList2);
                        if (monthWisePictures.containsKey(format3)) {
                            arrayList3 = monthWisePictures.get(format3);
                            if (arrayList3 == null) {
                                arrayList3 = new ArrayList<>();
                            }
                        } else {
                            arrayList3 = new ArrayList<>();
                        }
                        arrayList3.add(pictureData);
                        monthWisePictures.put(format3, arrayList3);
                        query.moveToNext();
                        str = str3;
                        str2 = str4;
                    }
                    query.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                ArrayList arrayList5 = new ArrayList(albumWisePictures.keySet());
                for (int i = 0; i < arrayList5.size(); i++) {
                    ArrayList<PictureData> arrayList6 = albumWisePictures.get(arrayList5.get(i));
                    if (arrayList6 != null && arrayList6.size() != 0) {
                        AlbumData albumData4 = new AlbumData();
                        albumData4.setTitle((String) arrayList5.get(i));
                        albumData4.setPictureData(arrayList6);
                        albumData4.setFolderPath(new File(arrayList6.get(0).getFilePath()).getParentFile().getPath());
                        folderList.add(albumData4);
                    }
                }
                ArrayList arrayList7 = new ArrayList(dateWisePictures.keySet());
                for (int i2 = 0; i2 < arrayList7.size(); i2++) {
                    ArrayList<PictureData> arrayList8 = dateWisePictures.get(arrayList7.get(i2));
                    if (arrayList8 != null && arrayList8.size() != 0) {
                        AlbumData albumData5 = new AlbumData();
                        albumData5.setTitle((String) arrayList7.get(i2));
                        albumData5.setPictureData(arrayList8);
                        photoList.add(albumData5);
                        photoList.addAll(arrayList8);
                    }
                }
                Set<String> keySet = monthWisePictures.keySet();
                ArrayList arrayList9 = new ArrayList(keySet);
                for (int i3 = 0; i3 < keySet.size(); i3++) {
                    ArrayList<PictureData> arrayList10 = monthWisePictures.get(arrayList9.get(i3));
                    if (arrayList10 != null && arrayList10.size() != 0) {
                        AlbumData albumData6 = new AlbumData();
                        albumData6.setTitle((String) arrayList9.get(i3));
                        albumData6.setPictureData(arrayList10);
                        storyList.add(albumData6);
                    }
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
//        }
        IMAGES_GET = true;
    }


    public static void getVideoList(Context context) {
        Uri uri;
        ArrayList arrayList;
        VIDEO_GET = true;
        videoList.clear();
//        if (checkStoragePermission(context)) {
            LinkedHashMap linkedHashMap = new LinkedHashMap();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMMM yyyy", Locale.ENGLISH);
            Calendar calendar = Calendar.getInstance();
            String format = simpleDateFormat.format(Long.valueOf(calendar.getTimeInMillis()));
            calendar.add(5, -1);
            String format2 = simpleDateFormat.format(Long.valueOf(calendar.getTimeInMillis()));
            if (Build.VERSION.SDK_INT >= 29) {
                uri = MediaStore.Video.Media.getContentUri("external");
            } else {
                uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
            }
            try {
                Cursor query = context.getContentResolver().query(uri, new String[]{"_data", "_display_name", "_data", "duration", "date_modified"}, null, null, "date_modified DESC");
                if (query != null) {
                    int columnIndexOrThrow = query.getColumnIndexOrThrow("_data");
                    int columnIndexOrThrow2 = query.getColumnIndexOrThrow("duration");
                    query.moveToFirst();
                    while (!query.isAfterLast()) {
                        String string = query.getString(query.getColumnIndexOrThrow("_data"));
                        String string2 = query.getString(query.getColumnIndexOrThrow("_display_name"));
                        String format3 = simpleDateFormat.format(Long.valueOf(query.getLong(query.getColumnIndexOrThrow("date_modified")) * 1000));
                        if (format3.equals(format)) {
                            format3 = "Today";
                        } else if (format3.equals(format2)) {
                            format3 = "Yesterday";
                        }
                        String durationString = getDurationString(query.getInt(columnIndexOrThrow2));
                        PictureData pictureData = new PictureData();
                        pictureData.setFilePath(string);
                        pictureData.setFileName(string2);
                        pictureData.setThumbnails(query.getString(columnIndexOrThrow));
                        pictureData.setDuration(durationString);
                        pictureData.setVideo(true);
                        if (linkedHashMap.containsKey(format3)) {
                            arrayList = (ArrayList) linkedHashMap.get(format3);
                            if (arrayList == null) {
                                arrayList = new ArrayList();
                            }
                        } else {
                            arrayList = new ArrayList();
                        }
                        arrayList.add(pictureData);
                        linkedHashMap.put(format3, arrayList);
                        query.moveToNext();
                    }
                    query.close();
                }
            } catch (Exception unused) {
            }
            ArrayList arrayList2 = new ArrayList(linkedHashMap.keySet());
            for (int i = 0; i < arrayList2.size(); i++) {
                ArrayList<PictureData> arrayList3 = (ArrayList) linkedHashMap.get(arrayList2.get(i));
                if (arrayList3 != null && arrayList3.size() != 0) {
                    AlbumData albumData4 = new AlbumData();
                    albumData4.setTitle((String) arrayList2.get(i));
                    albumData4.setPictureData(arrayList3);
                    videoList.add(albumData4);
                    videoList.addAll(arrayList3);
                }
            }
//        }
        VIDEO_GET = true;
    }

    public static String getDurationString(int i) {
        String valueOf;
        String valueOf2;
        String valueOf3;
        long j = i;
        long hours = TimeUnit.MILLISECONDS.toHours(j);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(j);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(j) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(j));
        if (minutes < 10) {
            valueOf = "0" + minutes;
        } else if (minutes >= 60) {
            long j2 = minutes % 60;
            if (j2 < 10) {
                valueOf = "0" + j2;
            } else {
                valueOf = String.valueOf(j2);
            }
        } else {
            valueOf = String.valueOf(minutes);
        }
        if (seconds < 10) {
            valueOf2 = "0" + seconds;
        } else {
            valueOf2 = String.valueOf(seconds);
        }
        if (hours < 10) {
            valueOf3 = "0" + hours;
        } else {
            valueOf3 = String.valueOf(hours);
        }
        if (hours == 0) {
            return valueOf + ":" + valueOf2;
        }
        return valueOf3 + ":" + valueOf + ":" + valueOf2;
    }

    public static Float convertToDegree(String str) {
        String[] split = str.split(",", 3);
        String[] split2 = split[0].split("/", 2);
        Double valueOf = Double.valueOf(new Double(split2[0]).doubleValue() / new Double(split2[1]).doubleValue());
        String[] split3 = split[1].split("/", 2);
        Double valueOf2 = Double.valueOf(new Double(split3[0]).doubleValue() / new Double(split3[1]).doubleValue());
        String[] split4 = split[2].split("/", 2);
        return new Float(valueOf.doubleValue() + (valueOf2.doubleValue() / 60.0d) + (Double.valueOf(new Double(split4[0]).doubleValue() / new Double(split4[1]).doubleValue()).doubleValue() / 3600.0d));
    }

    private static String getAddress(Context context, Float f, Float f2) throws IOException {
        List<Address> fromLocation = new Geocoder(context, Locale.getDefault()).getFromLocation(f.floatValue(), f2.floatValue(), 1);
        String locality = fromLocation.get(0).getLocality();
        String subLocality = fromLocation.get(0).getSubLocality();
        subLocality.concat(", " + locality);
        return locality == null ? "ab" : locality;
    }
}
