package hdphoto.galleryimages.gelleryalbum.duplicate;

import android.content.Context;
import android.database.Cursor;
import android.media.ExifInterface;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;


public class DuplicateImageFindTask extends AsyncTask<String, Integer, Long> {
    private DuplicateFindCallback mCallback;
    private Context mContext;
    private int mTotalFileCount = 0;
    private long mTotalFileSize = 0;
    private int mCurrentProgress = 0;
    private ArrayList<String> mParseFolders = new ArrayList<>();

   
    public interface DuplicateFindCallback {
        void onDuplicateFindExecute(String str, long j);

        void onDuplicateFindFinished(int i, long j);

        void onDuplicateFindProgressUpdate(double d);

        void onDuplicateFindStart(String str, int i);

        void onDuplicateSectionFind(SectionItem sectionItem);
    }

    public DuplicateImageFindTask(DuplicateFindCallback duplicateFindCallback, Context context) {
        this.mCallback = null;
        this.mCallback = duplicateFindCallback;
        this.mContext = context;
    }

    @Override
    public Long doInBackground(String... strArr) {
        if (strArr.length > 0) {
            parseDirectory(strArr[0]);
        }
        DuplicateFindCallback duplicateFindCallback = this.mCallback;
        if (duplicateFindCallback != null) {
            duplicateFindCallback.onDuplicateFindFinished(this.mTotalFileCount, this.mTotalFileSize);
        }
        return 0L;
    }
    private void parseDirectory(String str) {
        this.mParseFolders.add(str);
        while (this.mParseFolders.size() > 0) {
            _parseDirectory(new File(this.mParseFolders.remove(0)));
        }
    }

    private void _parseDirectory(File file) {
        File[] listFiles;
        File[] fileArr;
        if (file != null && file.isDirectory() && file.isHidden()) {
            Log.d("Hidden directory:", "");
            return;
        }
        DuplicateFindCallback duplicateFindCallback = this.mCallback;
        if (duplicateFindCallback != null) {
            duplicateFindCallback.onDuplicateFindStart(file.getAbsolutePath(), 0);
        }
        ArrayList arrayList = new ArrayList();
        int i = 20;
        int i2 = 1;
        if (file != null && file.isDirectory() && (listFiles = file.listFiles()) != null) {
            int length = listFiles.length;
            int i3 = 0;
            int i4 = 0;
            while (i3 < length) {
                File file2 = listFiles[i3];
                if (file2.isDirectory()) {
                    this.mParseFolders.add(file2.getAbsolutePath());
                } else if (ImageUtils.isImage(file2.getName())) {
                    String dateTime = getDateTime(file2);
                    long length2 = file2.length();
                    Log.e("ImageDateTime--)", "" + dateTime);
                    this.mTotalFileSize = this.mTotalFileSize + length2;
                    this.mTotalFileCount = this.mTotalFileCount + i2;
                    i4++;
                    if (i4 == i) {
                        this.mCurrentProgress += i2;
                        DuplicateFindCallback duplicateFindCallback2 = this.mCallback;
                        if (duplicateFindCallback2 != null) {
                            duplicateFindCallback2.onDuplicateFindExecute(file2.getAbsolutePath(), file2.length());
                            fileArr = listFiles;
                            this.mCallback.onDuplicateFindProgressUpdate(calcProgress(this.mCurrentProgress));
                        } else {
                            fileArr = listFiles;
                        }
                        i4 = 0;
                    } else {
                        fileArr = listFiles;
                    }
                    if (dateTime != null) {
                        arrayList.add(new ImageHolder(file2.getAbsolutePath(), dateTime, length2));
                    }
                    i3++;
                    listFiles = fileArr;
                    i = 20;
                    i2 = 1;
                }
                fileArr = listFiles;
                i3++;
                listFiles = fileArr;
                i = 20;
                i2 = 1;
            }
        }
        Object[] array = arrayList.toArray();
        Arrays.sort(array, new MyComparator());
        Log.d("Finish sort:", "" + array.length);
        if (array.length > 1) {
            ImageHolder imageHolder = (ImageHolder) array[0];
            ArrayList arrayList2 = new ArrayList();
            arrayList2.add(imageHolder);
            Log.e("Length Gallery--)", "" + array.length);
            ArrayList arrayList3 = arrayList2;
            ImageHolder imageHolder2 = imageHolder;
            int i5 = 0;
            for (int i6 = 1; i6 < array.length; i6++) {
                ImageHolder imageHolder3 = (ImageHolder) array[i6];
                if (this.mCallback != null) {
                    Log.e("Length Image Holder--)", "" + imageHolder3.getImageTS());
                    this.mCallback.onDuplicateFindExecute(imageHolder3.getImagePath(), imageHolder3.getImageSize());
                }
                i5++;
                if (i5 == 5) {
                    int i7 = this.mCurrentProgress + 5;
                    this.mCurrentProgress = i7;
                    this.mCallback.onDuplicateFindProgressUpdate(calcProgress(i7));
                    try {
                        Thread.sleep(1L);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    i5 = 0;
                }
                if (imageHolder3.imageTS.longValue() != 0) {
                    if (imageHolder3.imageTS.longValue() - imageHolder2.imageTS.longValue() < 5100) {
                        Log.e("HolderSize0--)", "" + (imageHolder3.imageTS.longValue() - imageHolder2.imageTS.longValue()));
                        arrayList3.add(imageHolder3);
                    } else {
                        Log.e("HolderSize--)", "" + arrayList3.size() + " " + this.mCallback);
                        if (this.mCallback != null && arrayList3.size() > 1) {
                            Log.e("HolderSize1--)", "" + arrayList3.size() + " " + this.mCallback);
                            this.mCallback.onDuplicateSectionFind(new SectionItem(imageHolder2.imageDate, arrayList3));
                        }
                        arrayList3 = new ArrayList();
                        arrayList3.add(imageHolder3);
                    }
                    imageHolder2 = imageHolder3;
                }
            }
            Log.e("HolderSize2--)", "" + arrayList3.size() + " " + this.mCallback);
            if (this.mCallback == null || arrayList3.size() <= 1) {
                return;
            }
            Log.e("HolderSize3--)", "" + arrayList3.size() + " " + this.mCallback);
            this.mCallback.onDuplicateSectionFind(new SectionItem(((ImageHolder) arrayList3.get(0)).getImageDate(), arrayList3));
        }
    }

    private double calcProgress(int i) {
        double atan;
        if (i <= 100) {
            double d = i;
            Double.isNaN(d);
            Double.isNaN(d);
            Double.isNaN(d);
            atan = Math.atan(((-1.0E-4d) * d * d) + (d * 0.02d));
        } else {
            double d2 = i;
            Double.isNaN(d2);
            atan = Math.atan(d2 * 0.01d);
        }
        return (atan * 2.0d) / 3.141592653589793d;
    }

   
    public static class SectionItem {
        private String header;
        private ArrayList<ImageHolder> images;

        public SectionItem(String str, ArrayList<ImageHolder> arrayList) {
            this.header = str;
            this.images = arrayList;
        }

        public String getHeader() {
            return this.header;
        }

        public ArrayList<ImageHolder> getImages() {
            return this.images;
        }
    }

    public class MyComparator implements Comparator<Object> {
        MyComparator() {
        }

        @Override // java.util.Comparator
        public int compare(Object obj, Object obj2) {
            return ((ImageHolder) obj).imageTS.compareTo(((ImageHolder) obj2).imageTS);
        }
    }

    private String getDateTime(File file) {
        try {
            return new ExifInterface(file.getAbsolutePath()).getAttribute(androidx.exifinterface.media.ExifInterface.TAG_DATETIME);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static class ImageHolder {
        String imageDate;
        String imagePath;
        long imageSize;
        Long imageTS;

        public ImageHolder(String str, String str2, long j) {
            this.imageSize = 0L;
            this.imageTS = 0L;
            this.imageDate = str2;
            this.imagePath = str;
            this.imageSize = j;
            Log.d("ImDate", "" + this.imageDate);
            String[] strArr = {"yyyy:MM:dd HH:mm:ss", "yyyy:MM:dd HH:mm::ss"};
            for (int i = 0; i < 2; i++) {
                try {
                    this.imageTS = Long.valueOf(new SimpleDateFormat(strArr[i]).parse(str2).getTime());
                    return;
                } catch (Exception e) {
                    Log.d("", e.getMessage());
                }
            }
        }

        public long getImageSize() {
            return this.imageSize;
        }

        public String getImagePath() {
            return this.imagePath;
        }

        public String getImageDate() {
            return this.imageDate;
        }

        public Long getImageTS() {
            return this.imageTS;
        }
    }
}
