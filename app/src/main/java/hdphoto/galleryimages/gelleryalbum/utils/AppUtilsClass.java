package hdphoto.galleryimages.gelleryalbum.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.IntentSender;
import android.database.Cursor;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.text.format.DateUtils;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.exifinterface.media.ExifInterface;



import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import hdphoto.galleryimages.gelleryalbum.R;
import hdphoto.galleryimages.gelleryalbum.appclass.GalleryAppClass;
import hdphoto.galleryimages.gelleryalbum.fragment.BaseFragment;
import hdphoto.galleryimages.gelleryalbum.fragment.ImageFragment;
import hdphoto.galleryimages.gelleryalbum.fragment.VideoFragment;
import hdphoto.galleryimages.gelleryalbum.getdata.MomentData;
import hdphoto.galleryimages.gelleryalbum.location_media.activity.SplashScreenActivity;
import hdphoto.galleryimages.gelleryalbum.model.DataFileModel;
import hdphoto.galleryimages.gelleryalbum.model.FolderModel;
import hdphoto.galleryimages.gelleryalbum.sorting.LoginPreferenceUtilsData;
import hdphoto.galleryimages.gelleryalbum.sorting.LoginPreferenceUtilsFolder;
import hdphoto.galleryimages.gelleryalbum.sorting.SortingDataDialog;
import hdphoto.galleryimages.gelleryalbum.sorting.SortingFolderDialog;

@SuppressWarnings("all")
public class AppUtilsClass {
    public static final String INTENT_EXTRA_ALBUM = "album";
    public static String ascendingStr = "Ascending";
    public static String bucketId = null;
    public static String descendingStr = "Descending";
    public static String lastModifiedStr = "Last modifyDate";
    public static File[] listFile = null;
    public static String nameStr = "Name";
    public static String rootMainDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM) + File.separator + GalleryAppClass.getInstance().getResources().getString(R.string.app_name);
    static String[] permissions = {"android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.ACCESS_FINE_LOCATION", "android.permission.ACCESS_COARSE_LOCATION", "android.permission.CAMERA"};
    static String[] permissionssss = {"android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.CAMERA", "android.permission.READ_EXTERNAL_STORAGE"};
    public static LinkedHashMap<String, ArrayList<DataFileModel>> imageFolderHashMap = new LinkedHashMap<>();
    public static LinkedHashMap<String, ArrayList<DataFileModel>> videoFolderHashMap = new LinkedHashMap<>();
    public static int REQUEST_PERM_DELETE = 786;
    public static ArrayList<FolderModel> accessfolderListFor11 = new ArrayList<>();
    public static LinkedHashMap<String, ArrayList<DataFileModel>> photosDataHashMap = new LinkedHashMap<>();
    public static String rootMainDCIMDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM) + File.separator;

    public static void insertUri(Context context, File file) {
        String str = (file.getAbsolutePath().endsWith(".mp4") || file.getAbsolutePath().endsWith(".MP4") || file.getAbsolutePath().endsWith(".3gp") || file.getAbsolutePath().endsWith(".3GP") || file.getAbsolutePath().endsWith(".mkv") || file.getAbsolutePath().endsWith(".MKV") || file.getAbsolutePath().endsWith(".wmv") || file.getAbsolutePath().endsWith(".WMV")) ? "vid" : (file.getAbsolutePath().endsWith(".jpg") || file.getAbsolutePath().endsWith(".JPG") || file.getAbsolutePath().endsWith(".jpeg") || file.getAbsolutePath().endsWith(".JPEG") || file.getAbsolutePath().endsWith(".png") || file.getAbsolutePath().endsWith(".PNG") || file.getAbsolutePath().endsWith(".gif") || file.getAbsolutePath().endsWith(".GIF")) ? "img" : "";
        try {
            ContentValues contentValues = new ContentValues();
            if (str.equals("img")) {
                contentValues.put(Utils.mediaPath, file.getAbsolutePath());
                context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
            } else if (str.equals("vid")) {
                contentValues.put(Utils.mediaPath, file.getAbsolutePath());
                context.getContentResolver().insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, contentValues);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String GetDateFromTimeStampBase(long j) {
        return new SimpleDateFormat("EEE MMM dd yyyy hh:mm:ss zzz").format(new Date(j));
    }

    public static void RefreshImageAlbum(Activity activity) {
    }

    public static void RefreshVideoAlbum(Activity activity) {
    }

    public static void RefreshMoment(Activity activity) {
        SplashScreenActivity.getMomentData = new MomentData(activity);
        if (SplashScreenActivity.getMomentData.getMomentAllAsync == null) {
            SplashScreenActivity.getMomentData.getMomentAllAsync.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[0]);
        } else if (SplashScreenActivity.getMomentData.getMomentAllAsync.getStatus() != AsyncTask.Status.RUNNING) {
            SplashScreenActivity.getMomentData.getMomentAllAsync.cancel(true);
            SplashScreenActivity.getMomentData = new MomentData(activity);
            SplashScreenActivity.getMomentData.getMomentAllAsync.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[0]);
        }
    }

    public static void RefreshPhotoVideo(Activity activity) {
        try {
            if (SplashScreenActivity.getMomentData.getMomentAllAsync == null) {
                SplashScreenActivity.getMomentData = new MomentData(activity);
                SplashScreenActivity.getMomentData.getMomentAllAsync.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[0]);
            } else if (SplashScreenActivity.getMomentData.getMomentAllAsync.getStatus() != AsyncTask.Status.RUNNING) {
                SplashScreenActivity.getMomentData.getMomentAllAsync.cancel(true);
                SplashScreenActivity.getMomentData = new MomentData(activity);
                SplashScreenActivity.getMomentData.getMomentAllAsync.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[0]);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean isYesterdayIF(long j) {
        return DateUtils.isToday(j + 86400000);
    }

    public static boolean isTodayIF(long j) {
        return DateUtils.isToday(j);
    }

    public static void ScanImageAlbumListData(Activity activity) {
        ArrayList<DataFileModel> arrayList;
        try {
            imageFolderHashMap = new LinkedHashMap<>();
            if (ConstantArrayClass.imageAlbumArrayList != null) {
                ConstantArrayClass.imageAlbumArrayList.clear();
            }
            if (ConstantArrayClass.imageAlbumArrayList2 != null) {
                ConstantArrayClass.imageAlbumArrayList2.clear();
            }
            String[] strArr = {"_id", "bucket_id", "bucket_display_name", Utils.mediaPath, "date_added"};
            Cursor query = activity.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, strArr, null, null, "date_added DESC");
            if (query != null) {
                query.getCount();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE, MMM dd yyyy", Locale.US);
                new SimpleDateFormat("dd MMM yyyy", Locale.US);
                while (query.moveToNext()) {
                    long j = query.getLong(query.getColumnIndex(strArr[0]));
                    String string = query.getString(query.getColumnIndex(strArr[1]));
                    String string2 = query.getString(query.getColumnIndex(strArr[2]));
                    String string3 = query.getString(query.getColumnIndex(strArr[3]));
                    String string4 = query.getString(query.getColumnIndex(strArr[4]));
                    if (!TextUtils.isEmpty(string4)) {
                        long parseLong = Long.parseLong(string4) * 1000;
                        if (!isTodayIF(parseLong) && !isYesterdayIF(parseLong)) {
                            simpleDateFormat.format(Long.valueOf(parseLong));
                        }
                        try {
                            DataFileModel gMDataFileModel = new DataFileModel();
                            gMDataFileModel.path = string3;
                            gMDataFileModel.folderName = string2;
                            gMDataFileModel.modifiedDate = string4;
                            gMDataFileModel.duration = 0L;
                            gMDataFileModel.id = j;
                            gMDataFileModel.mediaType = "1";
                            gMDataFileModel.isSelected = false;
                            gMDataFileModel.bucket_id = string;
                            if (imageFolderHashMap.containsKey(string2)) {
                                arrayList = imageFolderHashMap.get(string2);
                            } else {
                                arrayList = new ArrayList<>();
                            }
                            arrayList.add(gMDataFileModel);
                            imageFolderHashMap.put(string2, arrayList);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                query.close();
            }
            Set<String> keySet = imageFolderHashMap.keySet();
            ArrayList arrayList2 = new ArrayList();
            arrayList2.addAll(keySet);
            ArrayList arrayList3 = new ArrayList();
            for (int i = 0; i < arrayList2.size(); i++) {
                try {
                    FolderModel gMFolderModel = new FolderModel();
                    gMFolderModel.foldername = arrayList2.get(i).toString();
                    gMFolderModel.bucket_id = imageFolderHashMap.get(arrayList2.get(i)).get(0).bucket_id;
                    gMFolderModel.folderPath = imageFolderHashMap.get(arrayList2.get(i)).get(0).path;
                    gMFolderModel.coverThumb = imageFolderHashMap.get(arrayList2.get(i)).get(0).path;
                    gMFolderModel.pathlist = imageFolderHashMap.get(arrayList2.get(i));
                    arrayList3.add(gMFolderModel);
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
            if (ConstantArrayClass.imageAlbumsList == null) {
                ConstantArrayClass.imageAlbumsList = new ArrayList<>();
            }
            if (ConstantArrayClass.imageAlbumsList != null) {
                ConstantArrayClass.imageAlbumsList.clear();
            }
            ConstantArrayClass.imageAlbumsList.addAll(arrayList3);
            if (ImageFragment.imageFolderAdapter != null) {
                ImageFragment.imageFolderAdapter.refreshData(ConstantArrayClass.imageAlbumsList);
            }
            ImageFragment.imageFolderAdapter.notifyDataSetChanged();
            new SortingFolderDialog(activity, ConstantArrayClass.imageAlbumsList, null).Sorting(LoginPreferenceUtilsFolder.GetStringData(activity, SortingDataDialog.SortingName), LoginPreferenceUtilsFolder.GetStringData(activity, SortingDataDialog.SortingType));
        } catch (Exception e3) {
            e3.printStackTrace();
        }
    }

    public static ArrayList<String> GetImagePathList(String str, Activity activity) {
        ArrayList<String> arrayList = new ArrayList<>();
        Cursor query = activity.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new String[]{Utils.mediaPath, "bucket_display_name"}, "bucket_id = ?", new String[]{str}, "date_modified DESC");
        if (query.moveToFirst()) {
            int columnIndexOrThrow = query.getColumnIndexOrThrow(Utils.mediaPath);
            do {
                arrayList.add(query.getString(columnIndexOrThrow));
            } while (query.moveToNext());
            query.close();
            return arrayList;
        }
        query.close();
        return arrayList;
    }
    public static boolean isYesterdayVF(long j) {
        return DateUtils.isToday(j + 86400000);
    }

    public static boolean isTodayVF(long j) {
        return DateUtils.isToday(j);
    }

    public static void ScanVideoAlbumListData(Activity activity) {
        ArrayList<DataFileModel> arrayList;
        try {
            videoFolderHashMap = new LinkedHashMap<>();
            if (ConstantArrayClass.videoAlbumArrayList != null) {
                ConstantArrayClass.videoAlbumArrayList.clear();
            }
            if (ConstantArrayClass.videoAlbumArrayList2 != null) {
                ConstantArrayClass.videoAlbumArrayList2.clear();
            }
            String[] strArr = {"_id", "bucket_id", "bucket_display_name", Utils.mediaPath, "date_added"};
            Cursor query = activity.getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, strArr, null, null, "date_added DESC");
            if (query != null) {
                query.getCount();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE, MMM dd yyyy", Locale.US);
                new SimpleDateFormat("dd MMM yyyy", Locale.US);
                while (query.moveToNext()) {
                    long j = query.getLong(query.getColumnIndex(strArr[0]));
                    String string = query.getString(query.getColumnIndex(strArr[1]));
                    String string2 = query.getString(query.getColumnIndex(strArr[2]));
                    String string3 = query.getString(query.getColumnIndex(strArr[3]));
                    String string4 = query.getString(query.getColumnIndex(strArr[4]));
                    if (!TextUtils.isEmpty(string4)) {
                        long parseLong = Long.parseLong(string4) * 1000;
                        if (!isTodayVF(parseLong) && !isYesterdayVF(parseLong)) {
                            simpleDateFormat.format(Long.valueOf(parseLong));
                        }
                        DataFileModel gMDataFileModel = new DataFileModel();
                        gMDataFileModel.path = string3;
                        gMDataFileModel.folderName = string2;
                        gMDataFileModel.modifiedDate = string4;
                        gMDataFileModel.duration = 0L;
                        gMDataFileModel.id = j;
                        gMDataFileModel.mediaType = ExifInterface.GPS_MEASUREMENT_3D;
                        gMDataFileModel.isSelected = false;
                        gMDataFileModel.bucket_id = string;
                        if (videoFolderHashMap.containsKey(string2)) {
                            arrayList = videoFolderHashMap.get(string2);
                        } else {
                            arrayList = new ArrayList<>();
                        }
                        arrayList.add(gMDataFileModel);
                        videoFolderHashMap.put(string2, arrayList);
                    }
                }
                query.close();
            }
            Set<String> keySet = videoFolderHashMap.keySet();
            ArrayList arrayList2 = new ArrayList();
            arrayList2.addAll(keySet);
            ArrayList arrayList3 = new ArrayList();
            for (int i = 0; i < arrayList2.size(); i++) {
                try {
                    FolderModel gMFolderModel = new FolderModel();
                    gMFolderModel.foldername = arrayList2.get(i).toString();
                    gMFolderModel.bucket_id = videoFolderHashMap.get(arrayList2.get(i)).get(0).bucket_id;
                    gMFolderModel.coverThumb = videoFolderHashMap.get(arrayList2.get(i)).get(0).path;
                    gMFolderModel.pathlist = videoFolderHashMap.get(arrayList2.get(i));
                    arrayList3.add(gMFolderModel);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (ConstantArrayClass.videoAlbumsList == null) {
                ConstantArrayClass.videoAlbumsList = new ArrayList<>();
            }
            if (ConstantArrayClass.videoAlbumsList != null) {
                ConstantArrayClass.videoAlbumsList.clear();
            }
            ConstantArrayClass.videoAlbumsList.addAll(arrayList3);
            if (VideoFragment.videoFolderAdapter != null) {
                VideoFragment.videoFolderAdapter.refreshData(ConstantArrayClass.videoAlbumsList);
            }
            try {
                new SortingFolderDialog(activity, ConstantArrayClass.videoAlbumsList, null).Sorting(LoginPreferenceUtilsFolder.GetStringData(activity, SortingDataDialog.SortingName), LoginPreferenceUtilsFolder.GetStringData(activity, SortingDataDialog.SortingType));
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        } catch (Exception e3) {
            e3.printStackTrace();
        }
    }

    public static ArrayList<String> GetVideoPathList(String str, Activity activity) {
        ArrayList<String> arrayList = new ArrayList<>();
        Cursor query = activity.getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, new String[]{Utils.mediaPath, "bucket_display_name"}, "bucket_id = ?", new String[]{str}, "date_modified DESC");
        if (query.moveToFirst()) {
            int columnIndexOrThrow = query.getColumnIndexOrThrow(Utils.mediaPath);
            do {
                arrayList.add(query.getString(columnIndexOrThrow));
            } while (query.moveToNext());
            query.close();
            return arrayList;
        }
        query.close();
        return arrayList;
    }

    public static void GetVideoList(Activity activity) {
        File file;
        if (ConstantArrayClass.videoList != null) {
            ConstantArrayClass.videoList.clear();
        }
        String[] strArr = {"_id", "_display_name", Utils.mediaPath, "date_added", "date_modified"};
        try {
            char c = 1;
            char c2 = 0;
            Cursor query = activity.getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, strArr, "bucket_id =?", new String[]{bucketId}, null);
            while (query.moveToNext()) {
                long j = query.getLong(query.getColumnIndex(strArr[c2]));
                String string = query.getString(query.getColumnIndex(strArr[c]));
                String string2 = query.getString(query.getColumnIndex(strArr[2]));
                String string3 = query.getString(query.getColumnIndex(strArr[3]));
                String string4 = query.getString(query.getColumnIndex(strArr[4]));
                try {
                    file = new File(string2);
                } catch (Exception e) {
                    e.printStackTrace();
                    file = null;
                }
                final DataFileModel gMDataFileModel = new DataFileModel(j, string, string2, BaseFragment.getDurationFile(file.getPath()), null, null, false, ExifInterface.GPS_MEASUREMENT_3D, string3, string4);
                activity.runOnUiThread(new Runnable() {
                    @Override 
                    public void run() {
                        ConstantArrayClass.videoList.add(DataFileModel.class);
                    }
                });
                new SortingDataDialog(activity, ConstantArrayClass.videoList, null).Sorting(LoginPreferenceUtilsData.GetStringData(activity, SortingDataDialog.SortingName), LoginPreferenceUtilsData.GetStringData(activity, SortingDataDialog.SortingType));
                c = 1;
                c2 = 0;
            }
            query.close();
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    @SuppressLint("Range")
    public static void GetFolderAllImageSelectedList(Activity activity, String str) {
        if (ConstantArrayClass.folderAllImageList != null) {
            ConstantArrayClass.folderAllImageList.clear();
        }
        String[] strArr = {"_id", Utils.mediaPath, "date_added", "date_modified"};
        try {
            Cursor query = activity.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, strArr, "bucket_id =?", new String[]{str}, null);
            ArrayList arrayList = new ArrayList(query.getCount());
            while (query.moveToNext()) {
                arrayList.add(new DataFileModel(query.getLong(query.getColumnIndex(strArr[0])), query.getString(query.getColumnIndex(strArr[1])), query.getString(query.getColumnIndex(strArr[1])), 0L, null, null, false, "1", query.getString(query.getColumnIndex(strArr[2])), query.getString(query.getColumnIndex(strArr[3]))));
            }
            query.close();
            if (ConstantArrayClass.folderAllImageList == null) {
                ConstantArrayClass.folderAllImageList = new ArrayList<>();
            }
            ConstantArrayClass.folderAllImageList.clear();
            ConstantArrayClass.folderAllImageList.addAll(arrayList);
            new Handler().postDelayed(() -> AppUtilsClass.RefreshImageAlbum(activity), 500L);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void GetFolderAllVideoSelectedList(Activity activity, String str) {
        if (ConstantArrayClass.folderAllVideoList != null) {
            ConstantArrayClass.folderAllVideoList.clear();
        }
        String[] strArr = {"_id", "_display_name", Utils.mediaPath, "date_added", "date_modified"};
        try {
            Cursor query = activity.getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, strArr, "bucket_id =?", new String[]{str}, null);
            while (query.moveToNext()) {
                @SuppressLint("Range") long j = query.getLong(query.getColumnIndex(strArr[0]));
                String string = query.getString(query.getColumnIndex(strArr[1]));
                String string2 = query.getString(query.getColumnIndex(strArr[2]));
                String string3 = query.getString(query.getColumnIndex(strArr[3]));
                String string4 = query.getString(query.getColumnIndex(strArr[4]));
                File file = null;
                try {
                    file = new File(string2);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                final DataFileModel gMDataFileModel = new DataFileModel(j, string, string2, BaseFragment.getDurationFile(file.getPath()), null, null, false, ExifInterface.GPS_MEASUREMENT_3D, string3, string4);
                activity.runOnUiThread(new Runnable() {
                    @Override 
                    public void run() {
                        ConstantArrayClass.folderAllVideoList.add(gMDataFileModel);
                    }
                });
            }
            query.close();
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    public static long getFilePathToMediaID(String str, Context context) {
        Cursor query = context.getContentResolver().query(MediaStore.Files.getContentUri("external"), new String[]{"_id"}, "_data=?", new String[]{str}, null);
        long j = 0;
        if (query != null) {
            while (query.moveToNext()) {
                j = Long.parseLong(query.getString(query.getColumnIndex("_id")));
            }
        }
        return j;
    }

    public static void requestDeletePermission(List<Uri> list, Activity activity) {
        if (Build.VERSION.SDK_INT >= 30) {
            try {
                activity.startIntentSenderForResult(MediaStore.createDeleteRequest(activity.getContentResolver(), list).getIntentSender(), REQUEST_PERM_DELETE, null, 0, 0, 0);
            } catch (IntentSender.SendIntentException e) {
                e.printStackTrace();
            }
        }
    }

    public static void requestIntent(List<Uri> list, Activity activity) {
        if (Build.VERSION.SDK_INT < 30 || list.size() <= 0) {
            return;
        }
        requestDeletePermission(list, activity);
    }

    public static boolean deleteFile(Context context, DataFileModel gMDataFileModel) {
        File file = new File(gMDataFileModel.path);
        if (file.exists()) {
            file.delete();
            ContentResolver contentResolver = context.getContentResolver();
            Uri contentUri = MediaStore.Files.getContentUri("external");
            contentResolver.delete(contentUri, "_id=" + gMDataFileModel.id, null);
            MediaScannerConnection.scanFile(context, new String[]{file.getAbsolutePath()}, null, new MediaScannerConnection.OnScanCompletedListener() {
                @Override
                public void onScanCompleted(String str, Uri uri) {
                }
            });
            if (file.exists()) {
                try {
                    file.getCanonicalFile().delete();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (file.exists()) {
                    context.deleteFile(file.getName());
                    MediaScannerConnection.scanFile(context, new String[]{file.toString()}, null, new MediaScannerConnection.OnScanCompletedListener() {
                        @Override
                        public void onScanCompleted(String str, Uri uri) {
                        }
                    });
                }
            }
            return true;
        }
        return false;
    }

    private boolean checkPermission(Activity activity) {
        return ContextCompat.checkSelfPermission(activity, "android.permission.WRITE_EXTERNAL_STORAGE") == 0 && ContextCompat.checkSelfPermission(activity, "android.permission.READ_EXTERNAL_STORAGE") == 0 && ContextCompat.checkSelfPermission(activity, "android.permission.CAMERA") == 0;
    }

    private void requestPermission(Activity activity) {
        ActivityCompat.requestPermissions(activity, new String[]{"android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.READ_EXTERNAL_STORAGE", "android.permission.CAMERA"}, 200);
    }

    public static void getAccessFolder11() {
        accessfolderListFor11.clear();
        File file = new File(String.valueOf(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)));
        File file2 = new File(String.valueOf(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)));
        File file3 = new File(String.valueOf(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)));
        File file4 = new File(String.valueOf(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)));
        if (file.isDirectory()) {
            File[] listFiles = file.listFiles();
            listFile = listFiles;
            for (File file5 : listFiles) {
                if (file5.isDirectory() && !file5.getAbsolutePath().contains(".")) {
                    FolderModel gMFolderModel = new FolderModel();
                    gMFolderModel.folderPath = file5.getPath();
                    gMFolderModel.foldername = file5.getName();
                    accessfolderListFor11.add(gMFolderModel);
                }
            }
        }
        if (file2.isDirectory()) {
            File[] listFiles2 = file2.listFiles();
            listFile = listFiles2;
            for (File file6 : listFiles2) {
                if (file6.isDirectory() && !file6.getAbsolutePath().contains(".")) {
                    FolderModel gMFolderModel2 = new FolderModel();
                    gMFolderModel2.folderPath = file6.getPath();
                    gMFolderModel2.foldername = file6.getName();
                    accessfolderListFor11.add(gMFolderModel2);
                }
            }
        }
        if (file3.isDirectory()) {
            File[] listFiles3 = file3.listFiles();
            listFile = listFiles3;
            for (File file7 : listFiles3) {
                if (file7.isDirectory() && !file7.getAbsolutePath().contains(".")) {
                    FolderModel gMFolderModel3 = new FolderModel();
                    gMFolderModel3.folderPath = file7.getPath();
                    gMFolderModel3.foldername = file7.getName();
                    accessfolderListFor11.add(gMFolderModel3);
                }
            }
        }
        if (file4.isDirectory()) {
            File[] listFiles4 = file4.listFiles();
            listFile = listFiles4;
            for (File file8 : listFiles4) {
                if (file8.isDirectory() && !file8.getAbsolutePath().contains(".")) {
                    FolderModel gMFolderModel4 = new FolderModel();
                    gMFolderModel4.folderPath = file8.getPath();
                    gMFolderModel4.foldername = file8.getName();
                    accessfolderListFor11.add(gMFolderModel4);
                }
            }
        }
    }

    public static ArrayList<Object> GetDeletedImageFiles() {
        ArrayList<Object> arrayList = new ArrayList<>();
        File[] listFiles = new File(FolderPath.SDCARD_PATH_DELETE_IMAGE).listFiles();
        String str = rootMainDir + File.separator + ".PhotoGallery1/TrashImage";
        if (listFiles != null && listFiles.length > 0) {
            for (File file : listFiles) {
                DataFileModel gMDataFileModel = new DataFileModel();
                gMDataFileModel.id = 0L;
                gMDataFileModel.name = file.getName();
                gMDataFileModel.path = file.getPath();
                gMDataFileModel.folderName = "TrashImage";
                gMDataFileModel.folderPath = str;
                gMDataFileModel.oldpath = "";
                gMDataFileModel.directory = "";
                gMDataFileModel.isDirectory = false;
                gMDataFileModel.isSelected = false;
                gMDataFileModel.mediaType = "1";
                arrayList.add(gMDataFileModel);
            }
        }
        return arrayList;
    }

    public static ArrayList<Object> GetDeletedVideoFiles() {
        ArrayList<Object> arrayList = new ArrayList<>();
        File[] listFiles = new File(FolderPath.SDCARD_PATH_DELETE_VIDEO).listFiles();
        String str = rootMainDir + File.separator + ".PhotoGallery1/TrashVideo";
        if (listFiles != null && listFiles.length > 0) {
            for (File file : listFiles) {
                DataFileModel gMDataFileModel = new DataFileModel();
                gMDataFileModel.id = 0L;
                gMDataFileModel.name = file.getName();
                gMDataFileModel.path = file.getPath();
                gMDataFileModel.folderName = "TrashVideo";
                gMDataFileModel.folderPath = str;
                gMDataFileModel.oldpath = "";
                gMDataFileModel.directory = "";
                gMDataFileModel.isDirectory = false;
                gMDataFileModel.isSelected = false;
                gMDataFileModel.mediaType = ExifInterface.GPS_MEASUREMENT_3D;
                arrayList.add(gMDataFileModel);
            }
        }
        return arrayList;
    }

    public static boolean isYesterday(long j) {
        return DateUtils.isToday(j + 86400000);
    }

    public static boolean isToday(long j) {
        return DateUtils.isToday(j);
    }

    public static void ScanMomentAllDataNew(Activity activity) {
        String[] strArr;
        Cursor query;
        ArrayList<DataFileModel> arrayList;
        photosDataHashMap = new LinkedHashMap<>();
        if (ConstantArrayClass.arrayVideoSize != null) {
            ConstantArrayClass.arrayVideoSize.clear();
        }
        if (ConstantArrayClass.arrayImageSize != null) {
            ConstantArrayClass.arrayImageSize.clear();
        }
        Uri contentUri = MediaStore.Files.getContentUri("external");
        if (Build.VERSION.SDK_INT >= 30) {
            strArr = new String[]{"_id", Utils.mediaPath, "date_modified", "media_type", "bucket_id", "bucket_display_name", "duration"};
            query = activity.getContentResolver().query(contentUri, strArr, "media_type=1 OR media_type=3", null, "date_modified DESC");
        } else {
            strArr = new String[]{"_id", Utils.mediaPath, "date_modified", "media_type", "bucket_id", "bucket_display_name"};
            query = activity.getContentResolver().query(contentUri, strArr, "media_type=1 OR media_type=3", null, "date_modified DESC");
        }
        if (query != null) {
            query.getCount();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE, MMM dd yyyy", Locale.US);
            SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("dd MMM yyyy", Locale.US);
            while (query.moveToNext()) {
                String string = query.getString(query.getColumnIndexOrThrow(strArr[0]));
                String string2 = query.getString(query.getColumnIndexOrThrow(strArr[1]));
                if (string2 == null) {
                    string2 = "";
                }
                String string3 = query.getString(query.getColumnIndexOrThrow(strArr[2]));
                String string4 = query.getString(query.getColumnIndexOrThrow(strArr[3]));
                int i = query.getInt(query.getColumnIndexOrThrow(strArr[4]));
                String string5 = query.getString(query.getColumnIndexOrThrow(strArr[5]));
                if (!TextUtils.isEmpty(string3)) {
                    new DataFileModel().setPath(string);
                    long parseLong = Long.parseLong(string3) * 1000;
                    if (!isToday(parseLong) && !isYesterday(parseLong)) {
                        simpleDateFormat.format(Long.valueOf(parseLong));
                    }
                    DataFileModel gMDataFileModel = new DataFileModel();
                    gMDataFileModel.id1 = string;
                    gMDataFileModel.bucket_id = String.valueOf(i);
                    gMDataFileModel.path = string2;
                    gMDataFileModel.folderName = string5;
                    gMDataFileModel.modifiedDate = String.valueOf(parseLong);
                    gMDataFileModel.mediaType = string4;
                    gMDataFileModel.takenDate = string3;
                    if (string4.equals("1")) {
                        ConstantArrayClass.arrayImageSize.add(string2);
                    } else if (string4.equals(ExifInterface.GPS_MEASUREMENT_3D)) {
                        ConstantArrayClass.arrayVideoSize.add(string2);
                    }
                    Calendar.getInstance().setTimeInMillis(parseLong);
                    String format = simpleDateFormat2.format(Long.valueOf(parseLong));
                    if (TextUtils.isEmpty(string4)) {
                        String str = string.split("/")[2];
                    }
                    if (photosDataHashMap.containsKey(format)) {
                        arrayList = photosDataHashMap.get(format);
                    } else {
                        arrayList = new ArrayList<>();
                    }
                    arrayList.add(gMDataFileModel);
                    photosDataHashMap.put(format, arrayList);
                }
            }
            query.close();
        }
        loadImageWithDate();
    }

    public static void loadImageWithDate() {
        ArrayList arrayList = new ArrayList();
        Set<String> keySet = photosDataHashMap.keySet();
        ArrayList arrayList2 = new ArrayList();
        arrayList2.addAll(keySet);
        for (int i = 0; i < arrayList2.size(); i++) {
            arrayList.add((String) arrayList2.get(i));
            arrayList.addAll(photosDataHashMap.get(arrayList2.get(i)));
        }
        if (ConstantArrayClass.albumsList != null) {
            ConstantArrayClass.albumsList.clear();
        }
        ConstantArrayClass.albumsList.addAll(arrayList);
    }
}
