package hdphoto.galleryimages.gelleryalbum.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.view.ActionMode;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;
import androidx.exifinterface.media.ExifInterface;
import androidx.recyclerview.widget.RecyclerView;


import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.iten.tenoku.ad.AdShow;
import com.iten.tenoku.ad.HandleClick.HandleClick;
import com.iten.tenoku.utils.AdUtils;

import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;

import hdphoto.galleryimages.gelleryalbum.fragment.MomentFragment;
import hdphoto.galleryimages.gelleryalbum.adapter.BothAlbumSelectAdapter;
import hdphoto.galleryimages.gelleryalbum.adapter.VideoDialogAdapter;
import hdphoto.galleryimages.gelleryalbum.adapter.VideoFolderAdapter;
import hdphoto.galleryimages.gelleryalbum.listeners.OnClickListener;
import hdphoto.galleryimages.gelleryalbum.listeners.SortingListener;
import hdphoto.galleryimages.gelleryalbum.sorting.LoginPreferenceUtilsData;
import hdphoto.galleryimages.gelleryalbum.sorting.SortingDataDialog;
import hdphoto.galleryimages.gelleryalbum.model.DataFileModel;
import hdphoto.galleryimages.gelleryalbum.model.DialogAlbumModel;
import hdphoto.galleryimages.gelleryalbum.model.FolderModel;
import hdphoto.galleryimages.gelleryalbum.constant.Common;
import hdphoto.galleryimages.gelleryalbum.constant.MediaScanners;
import hdphoto.galleryimages.gelleryalbum.appclass.GalleryAppClass;
import hdphoto.galleryimages.gelleryalbum.utils.Utils;
import hdphoto.galleryimages.gelleryalbum.utils.AppUtilsClass;
import hdphoto.galleryimages.gelleryalbum.utils.ConstantArrayClass;
import hdphoto.galleryimages.gelleryalbum.utils.FileUtils;
import hdphoto.galleryimages.gelleryalbum.utils.FolderPath;
import hdphoto.galleryimages.gelleryalbum.utils.PrefClass;
import hdphoto.galleryimages.gelleryalbum.R;

@SuppressWarnings("all")


public class VideoSelectActivity extends BseActivity {
    static String TAG = "VideoSelectActivity";
    public static GridView gridViewVideo;
    public static BothAlbumSelectAdapter innerBothAlbumSelectAdapter;
    public static ActionMode mActionMode;
    public static ArrayList<Object> myVideoList;
    public static ArrayList<Object> sendImgList;
    String album;
    Dialog albumDialog;
    GridView albumVideoGridView;
    ImageView btnSearch;
    ImageView btnSearchClose;
    BottomSheetDialog dialog;
    int i2;
    ImageView iv_back, imgSort;
    Dialog insideFileDialog;
    int lastPosition;
    ProgressBar loader;
    private Menu mMenu;
    PrefClass preferenceClass;
    ProgressDialog progressDelete, progressLock, progressPermanentDelete;
    String progressTag;
    int pv;
    RelativeLayout rl_NoDataLayout, rl_Search, rl_searchBox;
    SortingListener sortingListener;
    TextView tv_error_dis, txtTitle;
    VideoFolderAdapter videoAlbumAdapter;
    VideoDialogAdapter videoDialogAdapter;
    GridView videoGridView;
    int ARRAY_REFRESH_CODE = 1101;
    int countSelected = 0;
    int dialog_count = 0;
    boolean isSelectedAll = false;
    boolean isSingleSelection = false;
    SearchView videoSearchView = null;
    int position = 0;
    boolean searchFlag = false;
    boolean renameTag = false;
    String toastTag = "";

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_video_select);

        Common.strplay = "InnerVideoSelectActivity";

        Intent intent = getIntent();
        if (intent == null) {
            finish();
        }

        album = intent.getStringExtra(AppUtilsClass.INTENT_EXTRA_ALBUM);
        position = intent.getIntExtra("position", 0);
        AppUtilsClass.bucketId = intent.getStringExtra(Common.gBucketID);
        preferenceClass = new PrefClass(getApplicationContext());

        initView();
        clickListener();
        SortingCallBack();
        new GetVideoAsync().execute(new Void[0]);
    }

    private ActionMode.Callback mActionModeCallback = new ActionMode.Callback() {
        @Override
        public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
            mMenu = menu;
            actionMode.getMenuInflater().inflate(R.menu.select_video_menu, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
            if (Build.VERSION.SDK_INT < 21) {
                return true;
            }
            getWindow().clearFlags(67108864);
            return true;
        }

        @Override
        public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
            if (menuItem.getItemId() == R.id.Copy) {
                if (GetSelectedVideoList().size() >= 1) {
                    VideoAlbumDialog(false);
                } else {
                    Toast.makeText(BseActivity.activity, "Select Video.", Toast.LENGTH_SHORT).show();
                }
            } else if (menuItem.getItemId() == R.id.Delete) {
                if (GetSelectedVideoList().size() >= 1) {
                    DeleteDialog();
                } else {
                    Toast.makeText(BseActivity.activity, "Select Video.", Toast.LENGTH_SHORT).show();
                }
            } else if (menuItem.getItemId() == R.id.Lock) {
                new ArrayList().clear();
                ArrayList<DataFileModel> GetSelectedVideoList = GetSelectedVideoList();
                if (preferenceClass.getInt(Common.gIsLockStatus, 0) == 0) {
                    SecurityDialog(GetSelectedVideoList);
                    actionMode.finish();
                } else if (GetSelectedVideoList.size() >= 1) {
                    LockDialog();
                } else {
                    Toast.makeText(BseActivity.activity, "Select at least one image.", Toast.LENGTH_SHORT).show();
                }
            } else if (menuItem.getItemId() == R.id.Move) {
                if (GetSelectedVideoList().size() >= 1) {
                    VideoAlbumDialog(true);
                } else {
                    Toast.makeText(BseActivity.activity, "Select Video.", Toast.LENGTH_SHORT).show();
                }
            } else if (menuItem.getItemId() == R.id.Rename) {
                if (GetSelectedVideoList().size() == 1) {
                    RenameDialog(GetSelectedVideoList().get(0).path);
                } else {
                    Toast.makeText(BseActivity.activity, "Select at only video", Toast.LENGTH_SHORT).show();
                }
            } else if (menuItem.getItemId() == R.id.Selector) {
                if (isSelectedAll) {
                    UnSelectAll();
                    mMenu.getItem(0).setIcon(ContextCompat.getDrawable(BseActivity.activity, R.drawable.action_select));
                    actionMode.finish();
                } else {
                    SelectAll();
                    mMenu.getItem(0).setIcon(ContextCompat.getDrawable(BseActivity.activity, R.drawable.action_unselect));
                }
            } else if (menuItem.getItemId() == R.id.Share) {
                if (GetSelectedVideoList().size() >= 1) {
                    ShareMultipleVideo(GetSelectedVideoList());
                } else {
                    Toast.makeText(BseActivity.activity, "Select Video.", Toast.LENGTH_SHORT).show();
                }
                actionMode.finish();
            }
            return false;
        }

        @Override
        public void onDestroyActionMode(ActionMode actionMode) {
            mActionMode = null;
            UnSelectAll();
            if (Build.VERSION.SDK_INT >= 21) {
                getWindow().clearFlags(67108864);
            }
        }
    };

    @Override
    public void onResume() {
        DataOrientation(getResources().getConfiguration().orientation);
        if (innerBothAlbumSelectAdapter != null) {
            innerBothAlbumSelectAdapter.notifyDataSetChanged();
        }
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ConstantArrayClass.imageList = null;
        if (innerBothAlbumSelectAdapter != null) {
            innerBothAlbumSelectAdapter.releaseResources();
        }
        gridViewVideo.setOnItemClickListener(null);
    }

    @Override
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        DataOrientation(configuration.orientation);
    }

    public static void slideDown(final View view) {
        view.animate().translationY(view.getHeight()).alpha(0.0f).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animator) {
                view.setVisibility(View.GONE);
                view.setAlpha(1.0f);
                view.setTranslationY(0.0f);
            }
        });
    }

    public static void slideUp(final View view) {
        view.setVisibility(View.VISIBLE);
        view.setAlpha(0.0f);
        if (view.getHeight() > 0) {
            slideUpNow(view);
        } else {
            view.post(() -> slideUpNow(view));
        }
    }

    public static void slideUpNow(final View view) {
        view.setTranslationY(view.getHeight());
        view.animate().translationY(0.0f).alpha(1.0f).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animator) {
                view.setVisibility(View.VISIBLE);
                view.setAlpha(1.0f);
            }
        });
    }

    private void initView() {
        rl_searchBox = findViewById(R.id.rl_searchBox);
        videoSearchView = findViewById(R.id.searchView);
        btnSearch = findViewById(R.id.btnSearch);
        btnSearchClose = findViewById(R.id.btnSearchClose);
        rl_Search = findViewById(R.id.rl_Search);
        iv_back = findViewById(R.id.btnBack);
        txtTitle = findViewById(R.id.toolbarTitle);
        imgSort = findViewById(R.id.btnSortImage);
        rl_NoDataLayout = findViewById(R.id.hintNoHideImageLayout);
        tv_error_dis = findViewById(R.id.text_view_error);
        loader = findViewById(R.id.loader);
        gridViewVideo = findViewById(R.id.grid_view_image_select);

        tv_error_dis.setVisibility(View.INVISIBLE);
    }

    private void clickListener() {
        videoSearchView.setFocusable(false);
        videoSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String str) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String str) {
                filter(str);
                return false;
            }
        });

        rl_Search.setOnClickListener(view -> {
            if (searchFlag) {
                slideDown(rl_searchBox);
                rl_searchBox.setVisibility(View.GONE);
                btnSearch.setVisibility(View.VISIBLE);
                btnSearchClose.setVisibility(View.GONE);
                searchFlag = false;
                return;
            }
            slideUp(rl_searchBox);
            rl_searchBox.setVisibility(View.VISIBLE);
            btnSearch.setVisibility(View.GONE);
            btnSearchClose.setVisibility(View.VISIBLE);
            searchFlag = true;
        });

        txtTitle.setText(album);
        txtTitle.setVisibility(View.VISIBLE);

        iv_back.setOnClickListener(view -> onBackPressed());
    }

    public void filter(String str) {
        ArrayList<Object> arrayList = new ArrayList<>();
        Iterator<Object> it = ConstantArrayClass.videoList.iterator();
        while (it.hasNext()) {
            Object next = it.next();
            DataFileModel gMDataFileModel = (DataFileModel) next;
            if (gMDataFileModel.name != null && gMDataFileModel.name.toLowerCase().contains(str.toLowerCase())) {
                arrayList.add(next);
            }
        }
        if (arrayList.isEmpty()) {
            Toast.makeText(this, "No Data Found!", Toast.LENGTH_SHORT).show();
        } else {
            innerBothAlbumSelectAdapter.refreshData(arrayList);
        }
    }

    public void SelectSingleImage(ArrayList<Object> arrayList, int i) {
        isSingleSelection = true;
        ((DataFileModel) arrayList.get(i)).isSelected = !((DataFileModel) arrayList.get(i)).isSelected;
        if (((DataFileModel) arrayList.get(i)).isSelected) {
            countSelected++;
        } else {
            countSelected--;
        }
        if (countSelected <= 0) {
            isSingleSelection = false;
            isSelectedAll = false;
            if (mActionMode != null) {
                mActionMode.finish();
            }
        }
        if (GetSelectedVideoList().size() != 0) {
            mActionMode.setTitle(String.valueOf(GetSelectedVideoList().size()));
        }
        innerBothAlbumSelectAdapter.notifyDataSetChanged();
    }

    public ArrayList<DataFileModel> GetSelectedVideoList() {
        ArrayList<DataFileModel> arrayList = new ArrayList<>();
        int size = ConstantArrayClass.videoList.size();
        for (int i = 0; i < size; i++) {
            if (((DataFileModel) ConstantArrayClass.videoList.get(i)).isSelected) {
                arrayList.add((DataFileModel) ConstantArrayClass.videoList.get(i));
            }
        }
        return arrayList;
    }

    public void UnSelectAll() {
        int size = ConstantArrayClass.videoList.size();
        for (int i = 0; i < size; i++) {
            ((DataFileModel) ConstantArrayClass.videoList.get(i)).isSelected = false;
        }
        if (GetSelectedVideoList().size() != 0) {
            mActionMode.setTitle(String.valueOf(GetSelectedVideoList().size()));
        }
        countSelected = 0;
        isSelectedAll = false;
        isSingleSelection = false;
        innerBothAlbumSelectAdapter.notifyDataSetChanged();
    }

    public void SelectAll() {
        int size = ConstantArrayClass.videoList.size();
        for (int i = 0; i < size; i++) {
            ((DataFileModel) ConstantArrayClass.videoList.get(i)).isSelected = true;
        }
        if (GetSelectedVideoList().size() != 0) {
            mActionMode.setTitle(String.valueOf(GetSelectedVideoList().size()));
        }
        countSelected = ConstantArrayClass.videoList.size();
        isSelectedAll = true;
        isSingleSelection = true;
        innerBothAlbumSelectAdapter.notifyDataSetChanged();
    }

    public void SecurityDialog(ArrayList<DataFileModel> arrayList) {
        ConstantArrayClass.firstTimeLockDataArray = arrayList;
        final Dialog dialog = new Dialog(this, R.style.ThemeWithCorners1);
        dialog.requestWindowFeature(1);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.setContentView(R.layout.dialog_security);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        ((TextView) dialog.findViewById(R.id.rl_yes)).setOnClickListener(view -> {
            FolderPath.lock_screen = 1;
            startActivity(new Intent(VideoSelectActivity.this, PrivateActivity.class));
            dialog.dismiss();
        });

        ((TextView) dialog.findViewById(R.id.rl_no)).setOnClickListener(view -> dialog.dismiss());

        dialog.show();
    }

    public void LockDialog() {
        final Dialog dialog = new Dialog(this, R.style.ThemeWithCorners1);
        dialog.requestWindowFeature(1);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.setContentView(R.layout.dialog_lock_video);
        dialog.setTitle(getString(R.string.lock_video));
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        ((TextView) dialog.findViewById(R.id.rl_yes)).setOnClickListener(view -> {
            LockVideos();
            dialog.dismiss();
        });

        ((TextView) dialog.findViewById(R.id.rl_no)).setOnClickListener(view -> dialog.dismiss());

        dialog.show();
    }

    public void LockVideos() {
        new ArrayList().clear();
        ArrayList<DataFileModel> GetSelectedVideoList = GetSelectedVideoList();
        if (GetSelectedVideoList.size() > 0) {
            progressTag = "FromLock";
            new LockVideoExecute(GetSelectedVideoList).execute(new Void[0]);
            return;
        }
        Toast.makeText(this, "Select Video.", Toast.LENGTH_SHORT).show();
    }

    public class LockVideoExecute extends AsyncTask<Void, Void, Void> {
        ArrayList<DataFileModel> pathList;

        public LockVideoExecute(ArrayList<DataFileModel> arrayList) {
            this.pathList = new ArrayList<>();
            progressLock = new ProgressDialog(VideoSelectActivity.this);
            this.pathList = arrayList;
        }

        @Override
        public void onPreExecute() {
            super.onPreExecute();
            progressLock.setMessage(getString(R.string.please_wait_a_while));
            progressLock.setProgressStyle(0);
            progressLock.setIndeterminate(false);
            progressLock.setCancelable(false);
            progressLock.show();
        }

        @Override
        public Void doInBackground(Void... voidArr) {
            ArrayList arrayList = new ArrayList();
            ArrayList<String> listString = preferenceClass.getListString(Common.gOldPath);
            new File(FolderPath.SDCARD_PATH_VIDEO).mkdirs();
            for (int i = 0; i < pathList.size(); i++) {
                File file = new File(pathList.get(i).path);
                File file2 = new File(FolderPath.SDCARD_PATH_VIDEO + File.separator + file.getName());
                String parent = file2.getParent();
                try {
                    FileUtils.CopyMoveFile(file, file2);
                    long j = pathList.get(i).id;
                    String str = pathList.get(i).name;
                    String str2 = pathList.get(i).path;
                    listString.add(str2);
                    arrayList.add(new DataFileModel(j, str, str2, file2.getPath(), parent, false));
                    if (pathList.size() - 1 == i) {
                        for (int i2 = 0; i2 < pathList.size(); i2++) {
                            File file3 = new File(pathList.get(i2).path);
                            file3.delete();
                            ContentResolver contentResolver = getContentResolver();
                            Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                            contentResolver.delete(uri, "_data='" + file3.getPath() + "'", null);
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
        public void onPostExecute(Void r3) {
            super.onPostExecute(r3);
            UnSelectAll();
            isSingleSelection = false;
            isSelectedAll = false;
            progressLock.dismiss();
            if (mActionMode != null) {
                mActionMode.finish();
            }

            AppUtilsClass.RefreshVideoAlbum(VideoSelectActivity.this);
            AppUtilsClass.RefreshMoment(VideoSelectActivity.this);

            new GetVideoAsync().execute(new Void[0]);
            Toast.makeText(VideoSelectActivity.this, getString(R.string.files_moved_to_vault_successfully), Toast.LENGTH_SHORT).show();
        }
    }

    public void VideoAlbumDialog(final boolean z) {
        albumDialog = new Dialog(this, R.style.MyDialog);
        albumDialog.requestWindowFeature(1);
        albumDialog.setCancelable(false);
        albumDialog.setContentView(R.layout.dg_folder);
        TextView textView = (TextView) albumDialog.findViewById(R.id.toolbarTitle);
        textView.setText("Select Video Folder");
        textView.setVisibility(View.VISIBLE);

        videoGridView = (GridView) albumDialog.findViewById(R.id.albumGridView);
        ((ImageView) albumDialog.findViewById(R.id.btnBack)).setOnClickListener(view -> {
            UnSelectAll();
            mMenu.getItem(0).setIcon(ContextCompat.getDrawable(BseActivity.activity, R.drawable.action_select));
            if (mActionMode != null) {
                mActionMode.finish();
            }
            albumDialog.dismiss();
        });

        albumDialog.setOnKeyListener((dialogInterface, i, keyEvent) -> {
            if (i == 4 && dialog_count == 0) {
                onBackPressed();
                return true;
            }
            dialog_count = 0;
            return false;
        });

        Cursor query = getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, new String[]{"_id", "bucket_id", "bucket_display_name", Utils.mediaPath}, null, null, null);
        final ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList();
        if (query != null) {
            while (query.moveToNext()) {
                DialogAlbumModel gMDialogAlbumModel = new DialogAlbumModel();
                gMDialogAlbumModel.bucket_id = query.getString(query.getColumnIndex("bucket_id"));
                if (!arrayList2.contains(gMDialogAlbumModel.bucket_id)) {
                    gMDialogAlbumModel.foldername = query.getString(query.getColumnIndex("bucket_display_name"));
                    gMDialogAlbumModel.coverThumb = query.getString(query.getColumnIndexOrThrow(Utils.mediaPath));
                    gMDialogAlbumModel.folderPath = GetFolderPath(query.getString(query.getColumnIndexOrThrow(Utils.mediaPath)));
                    gMDialogAlbumModel.id = query.getString(query.getColumnIndex("_id"));
                    gMDialogAlbumModel.pathlist = GetVideoPathList("" + gMDialogAlbumModel.bucket_id);
                    arrayList.add(gMDialogAlbumModel);
                    arrayList2.add(gMDialogAlbumModel.bucket_id);
                }
            }
            query.close();
        }

        videoDialogAdapter = new VideoDialogAdapter(this, arrayList);
        videoGridView.setAdapter((ListAdapter) videoDialogAdapter);
        FolderOrientation(getResources().getConfiguration().orientation);
        videoDialogAdapter.notifyDataSetChanged();

        videoGridView.setOnItemClickListener((adapterView, view, i, j) -> {
            if (arrayList == null || arrayList.isEmpty()) {
                return;
            }
            MoveFileInsideDialog(new File(((DialogAlbumModel) arrayList.get(i)).folderPath), z, ((DialogAlbumModel) arrayList.get(i)).bucket_id, ((DialogAlbumModel) arrayList.get(i)).foldername);
        });

        albumDialog.show();
    }

    public String GetFolderPath(String str) {
        return new File(str).getAbsoluteFile().getParent();
    }

    public ArrayList<String> GetVideoPathList(String str) {
        ArrayList<String> arrayList = new ArrayList<>();
        Cursor query = getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, new String[]{Utils.mediaPath, "bucket_display_name"}, "bucket_id = ?", new String[]{str}, null);
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

    public void MoveFileInsideDialog(final File file, final boolean z, final String str, String str2) {
        insideFileDialog = new Dialog(this, R.style.MyDialog);
        insideFileDialog.requestWindowFeature(1);
        insideFileDialog.setCancelable(false);
        insideFileDialog.setContentView(R.layout.dialog_gm_folder_image);
        ImageView imageView = (ImageView) insideFileDialog.findViewById(R.id.btnBack);
        TextView textView = (TextView) insideFileDialog.findViewById(R.id.toolbarTitle);
        textView.setText(str2);
        textView.setVisibility(View.VISIBLE);
        albumVideoGridView = (GridView) insideFileDialog.findViewById(R.id.albumGridView);
        String[] strArr = {"_id", "_display_name", Utils.mediaPath, "date_added"};
        Cursor query = getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, strArr, "bucket_id =?", new String[]{str}, null);
        ArrayList arrayList = new ArrayList(query.getCount());
        while (query.moveToNext()) {
            String string = query.getString(query.getColumnIndex(strArr[2]));
            try {
                if (new File(string).exists()) {
                    DataFileModel gMDataFileModel = new DataFileModel();
                    gMDataFileModel.path = string;
                    arrayList.add(gMDataFileModel);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        query.close();
        videoAlbumAdapter = new VideoFolderAdapter(this, arrayList);
        albumVideoGridView.setAdapter((ListAdapter) videoAlbumAdapter);
        ChangeOrientation(getResources().getConfiguration().orientation);
        videoAlbumAdapter.notifyDataSetChanged();
        albumVideoGridView.setOnItemClickListener((adapterView, view, i, j) -> {
            sendImgList = new ArrayList<>();
            try {
                sendImgList.addAll(myVideoList);
            } catch (Exception e2) {
                e2.printStackTrace();
            }
            lastPosition = i;
            Common.IdentifyActivity = TAG;
            Intent intent = new Intent(VideoSelectActivity.this, DataFileModel.class);
            intent.putExtra(Common.gImagePath, ((DataFileModel) sendImgList.get(i)).path);
            intent.putExtra(Common.gTotalimage, sendImgList.size());
            intent.putExtra(Common.gCurrenrtPosition, i);
            intent.putExtra(Common.gArrayType, "open");
            intent.putExtra(Common.gActivityname, "InnerVideoSelectActivity");
            intent.putExtra(Common.gBucketID, AppUtilsClass.bucketId);
            intent.putExtra(Common.gMediaType, ((DataFileModel) sendImgList.get(lastPosition)).mediaType);
            BseActivity.activity.startActivityForResult(intent, ARRAY_REFRESH_CODE);
        });

        insideFileDialog.setOnKeyListener((dialogInterface, i, keyEvent) -> {
            if (i == 4) {
                dialog_count = 1;
                onBackPressed();
            }
            return true;
        });

        imageView.setOnClickListener(view -> insideFileDialog.dismiss());

        final LinearLayout linearLayout = (LinearLayout) insideFileDialog.findViewById(R.id.btnPaste);
        if (linearLayout.getVisibility() == View.VISIBLE) {
            albumVideoGridView.setOnItemClickListener(null);
        }
        linearLayout.setOnClickListener(view -> {
            MoveVideoonAlbum(file, z, str);
            linearLayout.setVisibility(View.GONE);
            UnSelectAll();
            if (mActionMode != null) {
                mActionMode.finish();
            }
        });
        insideFileDialog.show();
    }

    public void MoveVideoonAlbum(File file, boolean z, String str) {
        if (GetSelectedVideoList().size() > 0) {
            new MoveToAlbumExecute(GetSelectedVideoList(), file, z, str).execute(new Void[0]);
        } else {
            Toast.makeText(this, getString(R.string.select_image), Toast.LENGTH_SHORT).show();
        }
    }

    public class MoveToAlbumExecute extends AsyncTask<Void, Void, Void> {
        File albumDir;
        String bucketId;
        boolean operation;
        ArrayList<DataFileModel> pathaList;
        ProgressDialog progressDialog;
        int toast_cnt = 0;

        public MoveToAlbumExecute(ArrayList<DataFileModel> arrayList, File file, boolean z, String str) {
            this.pathaList = new ArrayList<>();
            progressDialog = new ProgressDialog(VideoSelectActivity.this);
            this.pathaList = arrayList;
            this.albumDir = file;
            this.operation = z;
            this.bucketId = str;
        }

        @Override
        public void onPreExecute() {
            super.onPreExecute();
            progressDialog.setMessage(getString(R.string.please_wait_a_while));
            progressDialog.setProgressStyle(0);
            progressDialog.setIndeterminate(false);
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        public Void doInBackground(Void... voidArr) {
            if (operation) {
                toast_cnt = 0;
                for (int i = 0; i < pathaList.size(); i++) {
                    File file = new File(pathaList.get(i).path);
                    File file2 = new File(albumDir + File.separator + file.getName());
                    try {
                        FileUtils.CopyMoveFile(file, file2);
                        if (pathaList.size() - 1 == i) {
                            for (int i2 = 0; i2 < pathaList.size(); i2++) {
                                ContentResolver contentResolver = getContentResolver();
                                Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                                contentResolver.delete(uri, "_data='" + pathaList.get(i2).path + "'", null);
                                StringBuilder sb = new StringBuilder();
                                sb.append("Remove MediaStore: Video ");
                                sb.append(i2);
                            }
                        }
                        file.delete();
                        ContentValues contentValues = new ContentValues();
                        contentValues.put("title", file2.getName());
                        contentValues.put("description", "Name");
                        contentValues.put(Utils.mediaPath, file2.getPath());
                        contentValues.put("mime_type", "video/*");
                        contentValues.put("date_modified", Long.valueOf(System.currentTimeMillis()));
                        contentValues.put("bucket_id", Integer.valueOf(file2.getPath().toLowerCase(Locale.US).hashCode()));
                        contentValues.put("bucket_display_name", file2.getName().toLowerCase(Locale.US));
                        getContentResolver().insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, contentValues);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } else {
                toast_cnt = 1;
                for (int i3 = 0; i3 < pathaList.size(); i3++) {
                    File file3 = new File(pathaList.get(i3).path);
                    File file4 = new File(albumDir + File.separator + System.currentTimeMillis() + file3.getName());
                    try {
                        org.apache.commons.io.FileUtils.copyFile(file3, file4);
                        ContentValues contentValues2 = new ContentValues();
                        contentValues2.put(Utils.mediaPath, file4.getPath());
                        getContentResolver().insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, contentValues2);
                    } catch (IOException e2) {
                        e2.printStackTrace();
                    }
                }
            }
            return null;
        }

        @Override
        public void onPostExecute(Void r3) {
            super.onPostExecute(r3);
            RefreshAdapter(bucketId);
            progressDialog.dismiss();
            AppUtilsClass.RefreshPhotoVideo(VideoSelectActivity.this);
            new GetVideoAsync().execute(new Void[0]);
            if (pathaList.size() == 1) {
                if (toast_cnt == 0) {
                    Toast.makeText(VideoSelectActivity.this, getString(R.string.files_move_successfully), Toast.LENGTH_SHORT).show();
                } else if (toast_cnt == 1) {
                    Toast.makeText(VideoSelectActivity.this, getString(R.string.files_copy_successfully), Toast.LENGTH_SHORT).show();
                }
                insideFileDialog.dismiss();
                albumDialog.dismiss();
                return;
            }
            if (toast_cnt == 0) {
                Toast.makeText(VideoSelectActivity.this, "Files moved successfully.", Toast.LENGTH_SHORT).show();
            } else if (toast_cnt == 1) {
                Toast.makeText(VideoSelectActivity.this, "Files copied successfully.", Toast.LENGTH_SHORT).show();
            }
            insideFileDialog.dismiss();
            albumDialog.dismiss();
        }
    }

    public void RefreshAdapter(String str) {
        String[] strArr = {"_id", "_display_name", Utils.mediaPath, "date_added"};
        Cursor query = getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, strArr, "bucket_id =?", new String[]{str}, null);
        ArrayList arrayList = new ArrayList(query.getCount());
        if (query.moveToNext()) {
            String string = query.getString(query.getColumnIndex(strArr[2]));
            try {
                if (new File(string).exists()) {
                    DataFileModel gMDataFileModel = new DataFileModel();
                    gMDataFileModel.path = string;
                    arrayList.add(gMDataFileModel);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            query.moveToPrevious();
            return;
        }
        query.close();
        myVideoList = new ArrayList<>();
        myVideoList.addAll(arrayList);
        videoAlbumAdapter = new VideoFolderAdapter(this, myVideoList);
        albumVideoGridView.setAdapter((ListAdapter) videoAlbumAdapter);
        ChangeOrientation(getResources().getConfiguration().orientation);
        videoAlbumAdapter.notifyDataSetChanged();
    }

    public void DataOrientation(int i) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(displayMetrics);
        if (MainActivity.preferenceDataUtils.getInt(Common.gDataColumns, 0) != 0) {
            int i2 = MainActivity.preferenceDataUtils.getInt(Common.gDataColumns, 0);
            this.pv = i2;
            if (i2 != 0) {
                this.i2 = i2;
            } else {
                this.i2 = 3;
                MainActivity.dataDivider = 9;
            }
        } else {
            this.i2 = 3;
            MainActivity.dataDivider = 9;
        }
        if (innerBothAlbumSelectAdapter != null) {
            innerBothAlbumSelectAdapter.setLayoutParams(displayMetrics.widthPixels / (i == 1 ? this.i2 : MainActivity.dataDivider));
        }
        if (i != 1) {
            this.i2 = MainActivity.dataDivider;
        }
        gridViewVideo.setNumColumns(this.i2);
    }

    private void FolderOrientation(int i) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(displayMetrics);
        if (MainActivity.preferenceAlbumUtils.getInt(Common.gAlbumColumns, 0) != 0) {
            int i2 = MainActivity.preferenceAlbumUtils.getInt(Common.gAlbumColumns, 0);
            this.pv = i2;
            if (i2 != 0) {
                this.i2 = i2;
            } else {
                this.i2 = 2;
                MainActivity.albumDivider = 4;
            }
        } else {
            this.i2 = 2;
            MainActivity.albumDivider = 4;
        }
        if (videoDialogAdapter != null) {
            videoDialogAdapter.setLayoutParams(displayMetrics.widthPixels / (i == 1 ? this.i2 : MainActivity.albumDivider));
        }
        if (i != 1) {
            this.i2 = MainActivity.albumDivider;
        }
        videoGridView.setNumColumns(this.i2);
    }

    private void ChangeOrientation(int i) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(displayMetrics);
        if (MainActivity.preferenceDataUtils.getInt(Common.gDataColumns, 0) != 0) {
            int i2 = MainActivity.preferenceDataUtils.getInt(Common.gDataColumns, 0);
            this.pv = i2;
            if (i2 != 0) {
                this.i2 = i2;
            } else {
                this.i2 = 3;
                MainActivity.dataDivider = 9;
            }
        } else {
            this.i2 = 3;
            MainActivity.dataDivider = 9;
        }
        if (videoAlbumAdapter != null) {
            videoAlbumAdapter.setLayoutParams(displayMetrics.widthPixels / (i == 1 ? this.i2 : MainActivity.dataDivider));
        }
        if (i != 1) {
            this.i2 = MainActivity.dataDivider;
        }
        albumVideoGridView.setNumColumns(this.i2);
    }

    public void RenameDialog(final String str) {
        final Dialog dialog = new Dialog(this, R.style.ThemeWithCorners1);
        dialog.requestWindowFeature(1);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.setContentView(R.layout.dialog_rename_file);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        ((TextView) dialog.findViewById(R.id.txtTitle)).setText("Rename File");
        final EditText editText = (EditText) dialog.findViewById(R.id.edit);
        editText.setText(FilenameUtils.getBaseName(str));

        ((TextView) dialog.findViewById(R.id.rl_yes)).setOnClickListener(view -> {
            if (editText.getText().toString().length() > 0) {
                renameTag = true;
                File file = new File(str);
                String parent = file.getParent();
                String substring = file.getAbsolutePath().substring(file.getAbsolutePath().lastIndexOf("."));
                File file2 = new File(parent + File.separator + editText.getText().toString().trim() + substring);
                new MediaScanners(VideoSelectActivity.this, file2);
                try {
                    if (FileUtils.CopyMoveFile(file, file2).exists()) {
                        AppUtilsClass.insertUri(VideoSelectActivity.this, file2);
                        file.delete();
                        ContentResolver contentResolver = getContentResolver();
                        Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                        contentResolver.delete(uri, "_data='" + file + "'", null);
                        UnSelectAll();
                        new Handler().postDelayed(() -> new GetVideoAsync().execute(new Void[0]), 500L);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                dialog.dismiss();
                return;
            }
            editText.setError("Please Enter File Name");
        });

        ((TextView) dialog.findViewById(R.id.rl_no)).setOnClickListener(view -> dialog.dismiss());

        dialog.show();
    }

    public void DeleteDialog() {
        final Dialog dialog = new Dialog(this, R.style.ThemeWithCorners1);
        dialog.requestWindowFeature(1);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.setContentView(R.layout.dg_delete_video);
        dialog.setTitle("Delete Video");
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        ((TextView) dialog.findViewById(R.id.rl_yes)).setOnClickListener(view -> {
            progressTag = "FromDelete";
            new DeleteToTrashVideoExecute(GetSelectedVideoList()).execute(new Void[0]);
            dialog.dismiss();
        });

        ((TextView) dialog.findViewById(R.id.rl_no)).setOnClickListener(view -> {
            UnSelectAll();
            mMenu.getItem(0).setIcon(ContextCompat.getDrawable(BseActivity.activity, R.drawable.action_select));
            if (mActionMode != null) {
                mActionMode.finish();
            }
            dialog.dismiss();
        });
        dialog.show();
    }

    public class DeleteToTrashVideoExecute extends AsyncTask<Void, Void, Void> {
        ArrayList<DataFileModel> pathList;

        public DeleteToTrashVideoExecute(ArrayList<DataFileModel> arrayList) {
            this.pathList = new ArrayList<>();
            progressDelete = new ProgressDialog(VideoSelectActivity.this);
            this.pathList = arrayList;
        }

        @Override
        public void onPreExecute() {
            super.onPreExecute();
            progressDelete.setMessage(getString(R.string.please_wait_a_while));
            progressDelete.setProgressStyle(0);
            progressDelete.setIndeterminate(false);
            progressDelete.setCancelable(false);
            progressDelete.show();
        }

        @Override
        public Void doInBackground(Void... voidArr) {
            ArrayList arrayList = new ArrayList();
            ArrayList<String> listString = preferenceClass.getListString(Common.gOldPath);
            new File(FolderPath.SDCARD_PATH_DELETE_VIDEO).mkdirs();
            for (int i = 0; i < pathList.size(); i++) {
                File file = new File(pathList.get(i).path);
                File file2 = new File(FolderPath.SDCARD_PATH_DELETE_VIDEO + File.separator + file.getName());
                String parent = file2.getParent();
                try {
                    FileUtils.CopyMoveFile(file, file2);
                    long j = pathList.get(i).id;
                    String str = pathList.get(i).name;
                    String str2 = pathList.get(i).path;
                    listString.add(str2);
                    arrayList.add(new DataFileModel(j, str, str2, file2.getPath(), parent, false));
                    if (pathList.size() - 1 == i) {
                        for (int i2 = 0; i2 < pathList.size(); i2++) {
                            File file3 = new File(pathList.get(i2).path);
                            file3.delete();
                            ContentResolver contentResolver = getContentResolver();
                            Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                            contentResolver.delete(uri, "_data='" + file3.getPath() + "'", null);
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
        public void onPostExecute(Void r5) {
            super.onPostExecute(r5);
            UnSelectAll();
            isSingleSelection = false;
            isSelectedAll = false;
            progressDelete.dismiss();
            if (mActionMode != null) {
                mActionMode.finish();
            }
            AppUtilsClass.RefreshVideoAlbum(VideoSelectActivity.this);
            new Handler().postDelayed(() -> {
                AppUtilsClass.RefreshMoment(VideoSelectActivity.this);
                MomentFragment.momentAdapter.notifyDataSetChanged();
            }, 1000L);
            new GetVideoAsync().execute(new Void[0]);
            Toast.makeText(VideoSelectActivity.this, "Files moved to trash successfully.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i == ARRAY_REFRESH_CODE && i2 == -1) {
            if (ConstantArrayClass.videoList.size() > 0) {
                rl_NoDataLayout.setVisibility(View.GONE);
                gridViewVideo.setVisibility(View.VISIBLE);
                return;
            }
            rl_NoDataLayout.setVisibility(View.VISIBLE);
            gridViewVideo.setVisibility(View.GONE);
        }
    }


    public class GetVideoAsync extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
        }

        public GetVideoAsync() {
        }

        @Override
        public Void doInBackground(Void... voidArr) {
            try {
                if (ConstantArrayClass.videoList != null) {
                    ConstantArrayClass.videoList.clear();
                }
                if (ConstantArrayClass.videoList == null) {
                    ConstantArrayClass.videoList = new ArrayList<>();
                }
                ConstantArrayClass.videoList.addAll(ConstantArrayClass.videoAlbumsList.get(position).pathlist);
                return null;
            } catch (NumberFormatException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        public void onPostExecute(Void r4) {
            super.onPostExecute(r4);
            SetVideoAdapter();
            try {
                new Handler().postDelayed(() -> {
                    if (ConstantArrayClass.videoList == null) {
                        return;
                    }
                    if (ConstantArrayClass.videoList.size() > 0) {
                        rl_NoDataLayout.setVisibility(View.GONE);
                        gridViewVideo.setVisibility(View.VISIBLE);
                        return;
                    }
                    rl_NoDataLayout.setVisibility(View.VISIBLE);
                    gridViewVideo.setVisibility(View.GONE);
                }, 500L);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (renameTag) {
                AppUtilsClass.RefreshVideoAlbum(VideoSelectActivity.this);
                if (mActionMode != null) {
                    mActionMode.finish();
                }
                AppUtilsClass.ScanMomentAllDataNew(VideoSelectActivity.this);
                renameTag = false;
            }
        }
    }

    public void SetVideoAdapter() {
        new SortingDataDialog(this, ConstantArrayClass.videoList, sortingListener).Sorting(LoginPreferenceUtilsData.GetStringData(getApplicationContext(), SortingDataDialog.SortingName), LoginPreferenceUtilsData.GetStringData(getApplicationContext(), SortingDataDialog.SortingType));
        innerBothAlbumSelectAdapter = new BothAlbumSelectAdapter(this, getApplicationContext(), ConstantArrayClass.videoList);
        gridViewVideo.setAdapter((ListAdapter) innerBothAlbumSelectAdapter);
        loader.setVisibility(View.GONE);
        gridViewVideo.setVisibility(View.VISIBLE);
        DataOrientation(getResources().getConfiguration().orientation);
        innerBothAlbumSelectAdapter.setItemClickCallback(new OnClickListener<ArrayList<Object>, Integer, View>() {
            @Override
            public void onClickMoreListener(ArrayList<Object> arrayList, Integer num, View view) {
            }

            @Override
            public void onClickListener(final ArrayList<Object> arrayList, final Integer num) {
                String str = "";
                sendImgList = new ArrayList<>();
                sendImgList = arrayList;
                if (isSingleSelection) {
                    SelectSingleImage(sendImgList, num.intValue());
                    return;
                }
                lastPosition = num.intValue();
                Common.IdentifyActivity = TAG;
                File file = new File(((DataFileModel) sendImgList.get(lastPosition)).path);
                try {
                    if (file.exists()) {
                        String name = file.getName();
                        str = name.substring(name.lastIndexOf("."));
                    }
                } catch (Exception unused) {
                    unused.printStackTrace();
                }
                String str2 = (str.endsWith(".jpg") || str.endsWith(".JPG") || str.endsWith(".jpeg") || str.endsWith(".JPEG") || str.endsWith(".png") || str.endsWith(".PNG") || str.endsWith(".gif") || str.endsWith(".GIF")) ? "1" : ExifInterface.GPS_MEASUREMENT_3D;
                if (((DataFileModel) sendImgList.get(lastPosition)).mediaType != null) {
                    str2 = ((DataFileModel) sendImgList.get(lastPosition)).mediaType;
                }
                Intent intent = new Intent(VideoSelectActivity.this, PreviewActivity.class);
                intent.putExtra(Common.gImagePath, ((DataFileModel) sendImgList.get(num.intValue())).path);
                intent.putExtra(Common.gTotalimage, sendImgList.size());
                intent.putExtra(Common.gCurrenrtPosition, num);
                intent.putExtra(Common.gMediaType, str2);
                intent.putExtra(Common.gArrayType, "open");
                intent.putExtra(Common.gActivityname, "InnerVideoSelectActivity");
                BseActivity.activity.startActivityForResult(intent, ARRAY_REFRESH_CODE);

            }

            @Override
            public void onLongClickListener(ArrayList<Object> arrayList, Integer num) {
                sendImgList = new ArrayList<>();
                sendImgList = arrayList;
                if (mActionMode != null) {
                    mActionMode = null;
                }
                if (mActionMode == null) {
                    mActionMode = startSupportActionMode(mActionModeCallback);
                }
                SelectSingleImage(VideoSelectActivity.sendImgList, num.intValue());
            }
        });
    }

    public void SortingCallBack() {
        sortingListener = new VideoSorting();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() != 16908332) {
            return false;
        }
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        AdShow.getInstance(VideoSelectActivity.this).ShowAd(new HandleClick() {
            @Override
            public void Show(boolean adShow) {
                if (insideFileDialog == null || !insideFileDialog.isShowing()) {
                    if (albumDialog == null || !albumDialog.isShowing()) {
                        finish();
                        try {
                            new SortingDataDialog(VideoSelectActivity.this, ConstantArrayClass.videoList, sortingListener).Sorting(AppUtilsClass.lastModifiedStr, AppUtilsClass.descendingStr);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return;
                    } else {
                        albumDialog.dismiss();
                        return;
                    }
                }
                insideFileDialog.dismiss();
            }
        }, AdUtils.ClickType.BACK_CLICK);

    }


    public class VideoSorting implements SortingListener {
        VideoSorting() {
        }

        @Override
        public void Sorting(ArrayList<Object> arrayList) {
            sendImgList = new ArrayList<>();
            sendImgList = arrayList;
            innerBothAlbumSelectAdapter = new BothAlbumSelectAdapter(VideoSelectActivity.this, getApplicationContext(), VideoSelectActivity.sendImgList);
            gridViewVideo.setAdapter((ListAdapter) VideoSelectActivity.innerBothAlbumSelectAdapter);
            loader.setVisibility(View.GONE);
            gridViewVideo.setVisibility(View.VISIBLE);
            DataOrientation(getResources().getConfiguration().orientation);
            innerBothAlbumSelectAdapter.notifyDataSetChanged();
            innerBothAlbumSelectAdapter.setItemClickCallback(new OnClickListener<ArrayList<Object>, Integer, View>() {
                @Override
                public void onClickMoreListener(ArrayList<Object> arrayList2, Integer num, View view) {

                }

                @Override
                public void onClickListener(final ArrayList<Object> arrayList2, final Integer num) {
                    String str = "";
                    sendImgList = new ArrayList<>();
                    sendImgList = arrayList2;
                    if (isSingleSelection) {
                        SelectSingleImage(VideoSelectActivity.sendImgList, num.intValue());
                        return;
                    }
                    lastPosition = num.intValue();
                    Common.IdentifyActivity = TAG;
                    File file = new File(((DataFileModel) sendImgList.get(lastPosition)).path);
                    try {
                        if (file.exists()) {
                            String name = file.getName();
                            str = name.substring(name.lastIndexOf("."));
                        }
                    } catch (Exception unused) {
                        unused.printStackTrace();
                    }
                    String str2 = (str.endsWith(".jpg") || str.endsWith(".JPG") || str.endsWith(".jpeg") || str.endsWith(".JPEG") || str.endsWith(".png") || str.endsWith(".PNG") || str.endsWith(".gif") || str.endsWith(".GIF")) ? "1" : ExifInterface.GPS_MEASUREMENT_3D;
                    if (((DataFileModel) sendImgList.get(lastPosition)).mediaType != null) {
                        str2 = ((DataFileModel) sendImgList.get(lastPosition)).mediaType;
                    }
                    Intent intent = new Intent(VideoSelectActivity.this, PreviewActivity.class);
                    intent.putExtra(Common.gImagePath, ((DataFileModel) sendImgList.get(num.intValue())).path);
                    intent.putExtra(Common.gTotalimage, sendImgList.size());
                    intent.putExtra(Common.gCurrenrtPosition, num);
                    intent.putExtra(Common.gMediaType, str2);
                    intent.putExtra(Common.gArrayType, "open");
                    intent.putExtra(Common.gActivityname, "InnerVideoSelectActivity");
                    BseActivity.activity.startActivityForResult(intent, ARRAY_REFRESH_CODE);
                }

                @Override
                public void onLongClickListener(ArrayList<Object> arrayList2, Integer num) {
                    sendImgList = new ArrayList<>();
                    sendImgList = arrayList2;
                    if (mActionMode != null) {
                        mActionMode = null;
                    }
                    if (mActionMode == null) {
                        mActionMode = startSupportActionMode(mActionModeCallback);
                    }
                    SelectSingleImage(sendImgList, num.intValue());
                }
            });
        }
    }

    public class AccessFolder11 extends RecyclerView.Adapter<AccessFolder11.MyViewHolder> {
        boolean abool;
        Context context;
        ArrayList<FolderModel> folderList;

        public AccessFolder11(Context context, ArrayList<FolderModel> arrayList, boolean z) {
            this.folderList = new ArrayList<>();
            this.context = context;
            this.folderList = arrayList;
            this.abool = z;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.layout_select_folder, viewGroup, false));
        }

        @Override
        public void onBindViewHolder(MyViewHolder myViewHolder, final int i) {
            myViewHolder.fileName.setText(folderList.get(i).foldername);
            myViewHolder.fullPath.setText(folderList.get(i).folderPath);
            myViewHolder.fullPath.setSelected(true);
            myViewHolder.itemView.setOnClickListener(view -> {
                dialog.dismiss();
                CopyMoveData(folderList.get(i).getFolderPath(), abool);
            });
        }

        @Override
        public int getItemCount() {
            if (folderList != null) {
                return folderList.size();
            }
            return 0;
        }

        public void refreshData(ArrayList<FolderModel> arrayList) {
            this.folderList = arrayList;
            notifyDataSetChanged();
        }


        public class MyViewHolder extends RecyclerView.ViewHolder {
            TextView fileName;
            TextView fullPath;

            public MyViewHolder(View view) {
                super(view);
                fileName = (TextView) view.findViewById(R.id.fileName);
                fullPath = (TextView) view.findViewById(R.id.fullPath);
            }
        }
    }

    public void CopyMoveData(String str, boolean z) {
        for (int i = 0; i < GetSelectedVideoList().size(); i++) {
            File file = new File(GetSelectedVideoList().get(i).path);
            File file2 = new File(str + File.separator + file.getName());
            new MediaScanners(GalleryAppClass.getInstance(), file2);
            try {
                FileUtils.CopyMoveFile(file, file2);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (!z) {
            Toast.makeText(this, "Video Copy Successfully", Toast.LENGTH_SHORT).show();
            UnSelectAll();
            isSingleSelection = false;
            isSelectedAll = false;
            if (mActionMode != null) {
                mActionMode.finish();
            }
            AppUtilsClass.ScanVideoAlbumListData(this);
            AppUtilsClass.RefreshMoment(this);
            MomentFragment.momentAdapter.notifyDataSetChanged();
        }
        if (z) {
            progressTag = "FromPermanentDelete";
            toastTag = "MoveData";
            new DeleteVideoExecute(GetSelectedVideoList()).execute(new Void[0]);
        }
    }

    public class DeleteVideoExecute extends AsyncTask<Void, Void, Void> {
        ArrayList<DataFileModel> pathList;

        public DeleteVideoExecute(ArrayList<DataFileModel> arrayList) {
            this.pathList = new ArrayList<>();
            progressPermanentDelete = new ProgressDialog(VideoSelectActivity.this);
            this.pathList = arrayList;
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
            ArrayList<String> listString = preferenceClass.getListString(Common.gOldPath);
            new File(FolderPath.SDCARD_PATH_DELETE_IMAGE).mkdirs();
            try {
                ArrayList arrayList = new ArrayList();
                int i = 0;
                while (i < pathList.size()) {
                    if (pathList.size() - 1 == i) {
                        while (i >= 0) {
                            if (Build.VERSION.SDK_INT >= 30) {
                                arrayList.add(ContentUris.withAppendedId(MediaStore.Video.Media.getContentUri("external"), AppUtilsClass.getFilePathToMediaID(pathList.get(i).path, VideoSelectActivity.this)));
                            }
                            pathList.remove(i);
                            i--;
                        }
                        preferenceClass.putListString(Common.gOldPath, listString);
                    }
                    i++;
                }
                if (Build.VERSION.SDK_INT < 30) {
                    return null;
                }
                AppUtilsClass.requestIntent(arrayList, VideoSelectActivity.this);
                return null;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        public void onPostExecute(Void r3) {
            super.onPostExecute(r3);
            if (Build.VERSION.SDK_INT != 30) {
                UnSelectAll();
                isSingleSelection = false;
                isSelectedAll = false;
                progressPermanentDelete.dismiss();
                if (mActionMode != null) {
                    mActionMode.finish();
                }
                AppUtilsClass.ScanVideoAlbumListData(VideoSelectActivity.this);
                AppUtilsClass.RefreshMoment(VideoSelectActivity.this);
                MomentFragment.momentAdapter.notifyDataSetChanged();
                new GetVideoAsync().execute(new Void[0]);
                if (toastTag.equals("MoveData")) {
                    Toast.makeText(VideoSelectActivity.this, R.string.files_move_successfully, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(VideoSelectActivity.this, R.string.files_are_deleted_successfully, Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
