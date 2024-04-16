package hdphoto.galleryimages.gelleryalbum.utils;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.net.Uri;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class FileSaveHelper implements LifecycleObserver {
    ExecutorService executor;
    MutableLiveData<FileMeta> fileCreatedResult;
    ContentResolver gContentResolver;
    Observer<FileMeta> observer;
    OnFileCreateResult resultListener;


    public interface OnFileCreateResult {
        void onFileCreateResult(boolean z, String str, String str2, Uri uri);
    }

    public void onFileCreateResultCallback(FileMeta fileMeta) {
        OnFileCreateResult onFileCreateResult = this.resultListener;
        if (onFileCreateResult != null) {
            onFileCreateResult.onFileCreateResult(fileMeta.isCreated, fileMeta.filePath, fileMeta.error, fileMeta.uri);
        }
    }

    public FileSaveHelper(ContentResolver contentResolver) {
        observer = (Observer) obj -> onFileCreateResultCallback((FileMeta) obj);
        this.gContentResolver = contentResolver;
        executor = Executors.newSingleThreadExecutor();
        fileCreatedResult = new MutableLiveData<>();
    }

    public FileSaveHelper(AppCompatActivity appCompatActivity) {
        this(appCompatActivity.getContentResolver());
        addObserver(appCompatActivity);
    }

    private void addObserver(LifecycleOwner lifecycleOwner) {
        this.fileCreatedResult.observe(lifecycleOwner, this.observer);
        lifecycleOwner.getLifecycle().addObserver(this);
    }

    public static class FileMeta {
        ContentValues cvDetails;
        String error;
        String filePath;
        boolean isCreated;
        Uri uri;

        public FileMeta(boolean z, String str, Uri uri, String str2, ContentValues contentValues) {
            this.isCreated = z;
            this.filePath = str;
            this.uri = uri;
            this.error = str2;
            this.cvDetails = contentValues;
        }
    }
}
