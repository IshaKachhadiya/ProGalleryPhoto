package hdphoto.galleryimages.gelleryalbum.activity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.iten.tenoku.ad.AdShow;
import com.iten.tenoku.ad.HandleClick.HandleClick;
import com.iten.tenoku.utils.AdUtils;

import java.io.File;
import java.util.ArrayList;

import hdphoto.galleryimages.gelleryalbum.adapter.AllDataAdapter;
import hdphoto.galleryimages.gelleryalbum.model.DataFileModel;
import hdphoto.galleryimages.gelleryalbum.constant.Common;
import hdphoto.galleryimages.gelleryalbum.utils.Utils;
import hdphoto.galleryimages.gelleryalbum.utils.AppUtilsClass;
import hdphoto.galleryimages.gelleryalbum.utils.FileUtils;
import hdphoto.galleryimages.gelleryalbum.R;

@SuppressWarnings("all")
public class VideosActivity extends PermissionHelperActivity {
    String album;
    AllDataAdapter allGalleryVideoAdapter;
    GridView gridViewVideo;
    ImageView iv_back;
    ProgressBar progressBar;
    ProgressDialog progressFolder;
    TextView tv_error_dis,tv_more_op,tv_toolbar_title;
    ArrayList<DataFileModel> videosList;
    int albumType = 0;
    int countSelected = 0;
    boolean isSingleSelection = false;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_videos);
        setView(findViewById(R.id.layout_image_select));

        Intent intent = getIntent();
        if (intent == null) {
            finish();
        }

        album = intent.getStringExtra(Common.gAlbumPath);
        albumType = intent.getIntExtra(Common.gAlbumType, 0);

        initView();
        clickListener();

        new GetAllVideoExecute().execute(new Void[0]);
    }

    private void initView() {
        iv_back = findViewById(R.id.btnBack);
        tv_toolbar_title = findViewById(R.id.toolbarTitle);
        tv_more_op = findViewById(R.id.btnMoveToAlbum);
        tv_error_dis = findViewById(R.id.text_view_error);
        progressBar = findViewById(R.id.progress_bar_image_select);
        gridViewVideo = findViewById(R.id.grid_view_image_select);

        tv_toolbar_title.setText("Select Video");
        tv_more_op.setVisibility(View.VISIBLE);
        tv_error_dis.setVisibility(View.INVISIBLE);
    }

    private void clickListener() {
        iv_back.setOnClickListener(view -> onBackPressed());

        tv_more_op.setOnClickListener(view -> MoveToAlbum());

        gridViewVideo.setOnItemClickListener((adapterView, view, i, j) -> SelectSingleVideo(i));

        gridViewVideo.setOnItemLongClickListener((adapterView, view, i, j) -> {
            SelectSingleVideo(i);
            return true;
        });
    }

    private ArrayList<DataFileModel> GetSelectedVideoList() {
        ArrayList<DataFileModel> arrayList = new ArrayList<>();
        int size = videosList.size();
        for (int i = 0; i < size; i++) {
            if (videosList.get(i).isSelected) {
                arrayList.add(videosList.get(i));
            }
        }
        return arrayList;
    }

    public void MoveToAlbum() {
        if (GetSelectedVideoList().size() > 0) {
            new VideoMoveToFolderExecute(GetSelectedVideoList(), album).execute(new Void[0]);
        } else {
            Toast.makeText(this, "Select Video.", Toast.LENGTH_SHORT).show();
        }
    }

    public void SelectSingleVideo(int i) {
        isSingleSelection = true;
        videosList.get(i).isSelected = !videosList.get(i).isSelected;
        if (videosList.get(i).isSelected) {
            countSelected++;
        } else {
            countSelected--;
        }
        if (countSelected <= 0) {
            isSingleSelection = false;
        }
        allGalleryVideoAdapter.notifyDataSetChanged();
    }

    @Override
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        orientationBasedUI(configuration.orientation);
    }

    public void orientationBasedUI(int i) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(displayMetrics);
        if (allGalleryVideoAdapter != null) {
            int screenWidthPixels = displayMetrics.widthPixels;
            allGalleryVideoAdapter.setLayoutParams(i == 1 ? screenWidthPixels / 4 : screenWidthPixels / 8);
        }
        gridViewVideo.setNumColumns(i != 1 ? 8 : 4);
    }

    @Override
    public void hideViews() {
        progressBar.setVisibility(View.INVISIBLE);
        gridViewVideo.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        videosList = null;

        if (allGalleryVideoAdapter != null) {
            allGalleryVideoAdapter.releaseResources();
        }

        gridViewVideo.setOnItemClickListener(null);
    }

    public class GetAllVideoExecute extends AsyncTask<Void, Void, Void> {
        ProgressDialog progressPermanentDelete;

        public GetAllVideoExecute() {
            progressPermanentDelete = new ProgressDialog(VideosActivity.this);
        }

        @Override
        public void onPreExecute() {
            super.onPreExecute();
            progressPermanentDelete.setMessage(getString(R.string.please_wait_a_while));
            progressPermanentDelete.setProgressStyle(0);
            progressPermanentDelete.setIndeterminate(false);
            progressPermanentDelete.setCancelable(false);
            progressPermanentDelete.show();
        }

        @Override
        public Void doInBackground(Void... voidArr) {
            ScanVideoFiles();
            return null;
        }

        @Override
        public void onPostExecute(Void r4) {
            super.onPostExecute(r4);
            progressPermanentDelete.dismiss();
            allGalleryVideoAdapter = new AllDataAdapter(VideosActivity.this, getApplicationContext(), videosList);
            gridViewVideo.setAdapter((ListAdapter) allGalleryVideoAdapter);
            progressBar.setVisibility(View.INVISIBLE);
            gridViewVideo.setVisibility(View.VISIBLE);
            orientationBasedUI(getResources().getConfiguration().orientation);
        }
    }

    public void ScanVideoFiles() {
        try {
            ArrayList arrayList = new ArrayList();
            Cursor query = getContentResolver().query(MediaStore.Files.getContentUri("external"), new String[]{"_id", Utils.mediaPath, "media_type", "datetaken", "bucket_id", "bucket_display_name"}, "media_type=3", null, "datetaken DESC");
            try {
                if (query.moveToFirst()) {
                    while (!query.isAfterLast()) {
                        if (new File(query.getString(query.getColumnIndex(Utils.mediaPath))).exists()) {
                            query.getLong(query.getColumnIndex("datetaken"));
                            DataFileModel gMDataFileModel = new DataFileModel();
                            gMDataFileModel.id1 = query.getString(query.getColumnIndex("_id"));
                            gMDataFileModel.bucket_id = query.getString(query.getColumnIndex("bucket_id"));
                            gMDataFileModel.path = query.getString(query.getColumnIndexOrThrow(Utils.mediaPath));
                            gMDataFileModel.folderName = query.getString(query.getColumnIndex("bucket_display_name"));
                            gMDataFileModel.takenDate = query.getString(query.getColumnIndexOrThrow("datetaken"));
                            gMDataFileModel.mediaType = query.getString(query.getColumnIndexOrThrow("media_type"));
                            arrayList.add(gMDataFileModel);
                        }
                        query.moveToNext();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            query.close();
            if (videosList == null) {
                videosList = new ArrayList<>();
            }
            if (videosList != null) {
                videosList.clear();
            }
            videosList.addAll(arrayList);
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }


    public class VideoMoveToFolderExecute extends AsyncTask<Void, Void, Void> {
        String albumName;
        ArrayList<DataFileModel> pathList;

        public VideoMoveToFolderExecute(ArrayList<DataFileModel> arrayList, String str) {
            this.pathList = new ArrayList<>();
            progressFolder = new ProgressDialog(VideosActivity.this);
            this.pathList = arrayList;
            this.albumName = str;
        }

        @Override
        public void onPreExecute() {
            super.onPreExecute();
            progressFolder.setMessage(getString(R.string.please_wait_a_while));
            progressFolder.setProgressStyle(0);
            progressFolder.setIndeterminate(false);
            progressFolder.setCancelable(false);
            progressFolder.show();
        }

        @Override
        public Void doInBackground(Void... voidArr) {
            ArrayList arrayList = new ArrayList();
            ArrayList<String> listString = preferenceClass.getListString(Common.gOldPath);
            File file = new File(albumName);
            if (!file.exists()) {
                file.mkdirs();
            }
            for (int i = 0; i < pathList.size(); i++) {
                File file2 = new File(pathList.get(i).path);
                File file3 = new File(albumName + file2.getName());
                String parent = file3.getParent();
                try {
                    FileUtils.CopyMoveFile(file2, file3);
                    AppUtilsClass.insertUri(VideosActivity.this, file3);
                    long j = pathList.get(i).id;
                    String str = pathList.get(i).name;
                    String str2 = pathList.get(i).path;
                    listString.add(str2);
                    arrayList.add(new DataFileModel(j, str, str2, file3.getPath(), parent, false));
                    if (pathList.size() - 1 == i) {
                        for (int i2 = 0; i2 < pathList.size(); i2++) {
                            File file4 = new File(pathList.get(i2).path);
                            file4.delete();
                            ContentResolver contentResolver = getContentResolver();
                            Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                            contentResolver.delete(uri, "_data='" + file4.getPath() + "'", null);
                        }
                        preferenceClass.putListString(Common.gOldPath, listString);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        public void onPostExecute(Void r4) {
            super.onPostExecute(r4);
            if (progressFolder.isShowing() || progressFolder != null) {
                progressFolder.dismiss();
            }
            allGalleryVideoAdapter.notifyDataSetChanged();
            setResult(-1, new Intent());
            AppUtilsClass.RefreshVideoAlbum(VideosActivity.this);
            new Handler().postDelayed(() -> AppUtilsClass.RefreshMoment(VideosActivity.this), 500L);
            Toast.makeText(VideosActivity.this, "Folder Created Successfully.", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(() -> finish(), 1000L);
        }
    }


    @Override
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
    }

    @Override
    public void onBackPressed() {
        AdShow.getInstance(VideosActivity.this).ShowAd(new HandleClick() {
            @Override
            public void Show(boolean adShow) {
                VideosActivity.super.onBackPressed();
            }
        }, AdUtils.ClickType.BACK_CLICK);
    }
}
