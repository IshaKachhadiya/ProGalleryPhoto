package hdphoto.galleryimages.gelleryalbum.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ClipData;
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
import android.widget.Button;
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
import androidx.core.content.FileProvider;
import androidx.exifinterface.media.ExifInterface;
import androidx.recyclerview.widget.RecyclerView;


import com.google.android.material.bottomsheet.BottomSheetDialog;

import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;

import hdphoto.galleryimages.gelleryalbum.adapter.AlbumAdapter;
import hdphoto.galleryimages.gelleryalbum.fragment.MomentFragment;
import hdphoto.galleryimages.gelleryalbum.adapter.BothAlbumSelectAdapter;
import hdphoto.galleryimages.gelleryalbum.listeners.OnClickListener;
import hdphoto.galleryimages.gelleryalbum.listeners.SortingListener;
import hdphoto.galleryimages.gelleryalbum.sorting.LoginPreferenceUtilsData;
import hdphoto.galleryimages.gelleryalbum.sorting.SortingDataDialog;
import hdphoto.galleryimages.gelleryalbum.model.DataFileModel;
import hdphoto.galleryimages.gelleryalbum.model.DialogAlbumModel;
import hdphoto.galleryimages.gelleryalbum.model.FolderModel;
import hdphoto.galleryimages.gelleryalbum.constant.Common;
import hdphoto.galleryimages.gelleryalbum.constant.ExternalStorageHelper;
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
public class ImageSelectActivity extends BseActivity {
    private static String TAG = "ImageSelectActivity";
    public static BothAlbumSelectAdapter innerBothAlbumSelectAdapter;
    public static ActionMode mActionMode;
    public static ArrayList<Object> myImageinsideList = new ArrayList<>();
    public static ArrayList<Object> sendImgList;
    boolean Lock_AllowMultiple;
    String album;
    Dialog albumDialog;
    GridView albumGridView;
    GridView albumImageGridView;
    BottomSheetDialog btmsDialog;
    Button btnDone;
    ImageView iv_search,iv_search_close;
//    AlbumAdapter dialogAlbumAdapter;
//    ImageFolderAdapter dialogAlbumImageAdapter;
    GridView gridViewImage;
    int i2;
    ImageView ic_back,ivCopy,ivDelete, iv_edit,imgMove,imgRename,imgShare,imgSort;
    Dialog insideFileDialog;
    ProgressBar loader;
    private Menu mMenu;
    PrefClass preferenceClass;
    ProgressDialog progressDelete,progressLock,progressPermanentDelete;
    String progressTag;
    int pv;
    RelativeLayout rl_NoDataLayout,rl_Search,rl_searchBox;
    SortingListener sortingListener;
    TextView txtTitle;
    int ARRAY_REFRESH_CODE = 1101;
    int countSelected = 0;
    int dialog_count = 0;
    boolean isSelectAll = false;
    boolean isSingleSelect = false;
    int lastPosition = 0;
    String[] dataParameter = {"_id", "_display_name", Utils.mediaPath, "date_added"};
    SearchView searchView = null;
    int position = 0;
    boolean searchFlag = false;
    boolean renameTag = false;
    String toastTag = "";

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_image_select);

        Intent intent = getIntent();
        if (intent == null) {
            finish();
        }
        Lock_AllowMultiple = getIntent().getBooleanExtra("android.intent.extra.ALLOW_MULTIPLE", false);
        album = intent.getStringExtra(AppUtilsClass.INTENT_EXTRA_ALBUM);
        position = intent.getIntExtra("position", 0);
        AppUtilsClass.bucketId = intent.getStringExtra(Common.gBucketID);
        preferenceClass = new PrefClass(getApplicationContext());

        initView();
        clickListener();
        SortingCallBack();

        new GetImageAsync().execute(new Void[0]);
    }

    private void initView() {
        rl_searchBox = findViewById(R.id.rl_searchBox);
        searchView = findViewById(R.id.searchView);
        iv_search = findViewById(R.id.btnSearch);
        iv_search_close = findViewById(R.id.btnSearchClose);
        rl_Search = findViewById(R.id.rl_Search);
        ic_back = findViewById(R.id.btnBack);
        txtTitle = findViewById(R.id.toolbarTitle);
        imgSort = findViewById(R.id.btnSortImage);
        iv_edit = findViewById(R.id.edit_ic);
        ivCopy = findViewById(R.id.copy_ic);
        imgMove = findViewById(R.id.move_ic);
        imgRename = findViewById(R.id.rename_ic);
        ivDelete = findViewById(R.id.delete_ic);
        imgShare = findViewById(R.id.share_ic);
        btnDone = findViewById(R.id.btn_done);
        rl_NoDataLayout = findViewById(R.id.hintNoHideImageLayout);
        loader = findViewById(R.id.loader);
        gridViewImage = findViewById(R.id.grid_view_image_select);
    }

    private void clickListener() {
        searchView.setFocusable(false);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
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
                iv_search.setVisibility(View.VISIBLE);
                iv_search_close.setVisibility(View.GONE);
                searchFlag = false;
                return;
            }

            slideUp(rl_searchBox);
            rl_searchBox.setVisibility(View.VISIBLE);
            iv_search.setVisibility(View.GONE);
            iv_search_close.setVisibility(View.VISIBLE);
            searchFlag = true;
        });

        txtTitle.setText(album);
        txtTitle.setVisibility(View.VISIBLE);

        ic_back.setOnClickListener(view -> finish());

        imgSort.setOnClickListener(view -> {
            if (ConstantArrayClass.imageList.size() >= 1) {
                new SortingDataDialog(ImageSelectActivity.this, ConstantArrayClass.imageList, sortingListener).ShowSortingDialogue();
            } else {
                Toast.makeText(ImageSelectActivity.this, "No Image.", Toast.LENGTH_SHORT).show();
            }
        });

        iv_edit.setOnClickListener(view -> {
            if (GetSelectedImageList().size() == 1) {
                Intent intent = new Intent(ImageSelectActivity.this, EditImageActivity.class);
                intent.putExtra("imagepath", GetSelectedImageList().get(0).path + "");
                startActivity(intent);
                Common.IdentifyActivity = "";
                return;
            }
            Toast.makeText(ImageSelectActivity.this, "Select single image.", Toast.LENGTH_SHORT).show();
        });

        ivCopy.setOnClickListener(view -> {
            if (GetSelectedImageList().size() >= 1) {
                ImageAlbumDialog(false);
            } else {
                Toast.makeText(ImageSelectActivity.this, getString(R.string.select_image), Toast.LENGTH_SHORT).show();
            }
        });

        imgMove.setOnClickListener(view -> {
            if (GetSelectedImageList().size() >= 1) {
                ImageAlbumDialog(true);
            } else {
                Toast.makeText(ImageSelectActivity.this, getString(R.string.select_image), Toast.LENGTH_SHORT).show();
            }
        });

        imgRename.setOnClickListener(view -> {
            if (GetSelectedImageList().size() == 1) {
                RenameDialog(GetSelectedImageList().get(0).path);
                return;
            }
            Toast.makeText(ImageSelectActivity.this, "Select only one image", Toast.LENGTH_SHORT).show();
        });

        ivDelete.setOnClickListener(view -> {
            if (GetSelectedImageList().size() >= 1) {
                DeleteDialog();
            } else {
                Toast.makeText(ImageSelectActivity.this, getString(R.string.select_image), Toast.LENGTH_SHORT).show();
            }
        });

        imgShare.setOnClickListener(view -> {
            if (GetSelectedImageList().size() >= 1) {
                ShareMultipleImage(GetSelectedImageList());
                return;
            }
            Toast.makeText(ImageSelectActivity.this, getString(R.string.select_image), Toast.LENGTH_SHORT).show();
        });

        btnDone.setOnClickListener(view -> {
            Intent intent = getIntent();
            intent.setClipData(CreateClipData(GetSelectedImageList()));
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            setResult(-1, intent);
            finish();
        });
    }

    private ActionMode.Callback mActionModeCallback = new ActionMode.Callback() {
        @Override
        public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
            mMenu = menu;
            actionMode.getMenuInflater().inflate(R.menu.select_image_menu, menu);
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
            if(menuItem.getItemId() == R.id.Copy){
                if (GetSelectedImageList().size() >= 1) {
                    ImageAlbumDialog(false);
                } else {
                    Toast.makeText(BseActivity.activity, getString(R.string.select_image), Toast.LENGTH_SHORT).show();
                }
            } else if (menuItem.getItemId() == R.id.Delete) {
                if (GetSelectedImageList().size() >= 1) {
                    DeleteDialog();
                } else {
                    Toast.makeText(BseActivity.activity, getString(R.string.select_image), Toast.LENGTH_SHORT).show();
                }
            }else if (menuItem.getItemId() == R.id.Edit) {
                if (GetSelectedImageList().size() == 1) {
                    Intent intent = new Intent(ImageSelectActivity.this, EditImageActivity.class);
                    intent.putExtra("imagepath", GetSelectedImageList().get(0).path + "");
                    startActivity(intent);
                    Common.IdentifyActivity = "";
                } else {
                    Toast.makeText(BseActivity.activity, "Select single image.", Toast.LENGTH_SHORT).show();
                }
                actionMode.finish();
            }else if (menuItem.getItemId() == R.id.Lock) {
                new ArrayList().clear();
                ArrayList<DataFileModel> GetSelectedImageList = GetSelectedImageList();
                if (preferenceClass.getInt(Common.gIsLockStatus, 0) == 0) {
                    SecurityDialog(GetSelectedImageList);
                    actionMode.finish();
                } else if (GetSelectedImageList.size() >= 1) {
                    LockDialog();
                } else {
                    Toast.makeText(BseActivity.activity, "Select at least one image.", Toast.LENGTH_SHORT).show();
                }
            }else if (menuItem.getItemId() == R.id.Move) {
                if (GetSelectedImageList().size() >= 1) {
                    ImageAlbumDialog(true);
                } else {
                    Toast.makeText(BseActivity.activity, getString(R.string.select_image), Toast.LENGTH_SHORT).show();
                }
            }else if (menuItem.getItemId() == R.id.Rename) {
                if (GetSelectedImageList().size() == 1) {
                    RenameDialog(GetSelectedImageList().get(0).path);
                } else {
                    Toast.makeText(BseActivity.activity, "Select only one image", Toast.LENGTH_SHORT).show();
                }
            }else if (menuItem.getItemId() == R.id.Selector) {
                if (isSelectAll) {
                    UnSelectAll();
                    mMenu.getItem(0).setIcon(ContextCompat.getDrawable(BseActivity.activity, R.drawable.action_select));
                    actionMode.finish();
                } else {
                    SelectAll();
                    mMenu.getItem(0).setIcon(ContextCompat.getDrawable(BseActivity.activity, R.drawable.action_unselect));
                }
            }else if (menuItem.getItemId() == R.id.Share) {
                if (GetSelectedImageList().size() >= 1) {
                    ShareMultipleImage(GetSelectedImageList());
                } else {
                    Toast.makeText(BseActivity.activity, getString(R.string.select_image), Toast.LENGTH_SHORT).show();
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
    public void onDestroy() {
        super.onDestroy();
        ConstantArrayClass.imageList = null;
        BothAlbumSelectAdapter gMInnerBothAlbumSelectAdapter = innerBothAlbumSelectAdapter;
        if (gMInnerBothAlbumSelectAdapter != null) {
            gMInnerBothAlbumSelectAdapter.releaseResources();
        }
        gridViewImage.setOnItemClickListener(null);
    }

    @Override
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        DataOrientation(configuration.orientation);
    }


    @Override
    public void onResume() {
        DataOrientation(getResources().getConfiguration().orientation);
        if (innerBothAlbumSelectAdapter != null) {
            innerBothAlbumSelectAdapter.notifyDataSetChanged();
        }
        super.onResume();
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
            view.post(new Runnable() {
                @Override
                public void run() {
                    slideUpNow(view);
                }
            });
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

    public void filter(String str) {
        ArrayList<Object> arrayList = new ArrayList<>();
        Iterator<Object> it = ConstantArrayClass.imageList.iterator();
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
        isSingleSelect = true;
        ((DataFileModel) arrayList.get(i)).isSelected = !((DataFileModel) arrayList.get(i)).isSelected;
        if (((DataFileModel) arrayList.get(i)).isSelected) {
            countSelected++;
        } else {
            countSelected--;
        }
        if (countSelected <= 0) {
            isSingleSelect = false;
            isSelectAll = false;
            ActionMode actionMode = mActionMode;
            if (actionMode != null) {
                actionMode.finish();
            }
        }
        if (GetSelectedImageList().size() != 0) {
            mActionMode.setTitle(String.valueOf(GetSelectedImageList().size()));
        }
        innerBothAlbumSelectAdapter.notifyDataSetChanged();
    }

    public ArrayList<DataFileModel> GetSelectedImageList() {
        ArrayList<DataFileModel> arrayList = new ArrayList<>();
        int size = ConstantArrayClass.imageList.size();
        for (int i = 0; i < size; i++) {
            if (((DataFileModel) ConstantArrayClass.imageList.get(i)).isSelected) {
                arrayList.add((DataFileModel) ConstantArrayClass.imageList.get(i));
            }
        }
        return arrayList;
    }

    public void UnSelectAll() {
        int size = ConstantArrayClass.imageList.size();
        for (int i = 0; i < size; i++) {
            ((DataFileModel) ConstantArrayClass.imageList.get(i)).isSelected = false;
        }
        if (GetSelectedImageList().size() != 0) {
            mActionMode.setTitle(String.valueOf(GetSelectedImageList().size()));
        }
        countSelected = 0;
        isSelectAll = false;
        isSingleSelect = false;
        innerBothAlbumSelectAdapter.notifyDataSetChanged();
    }

    public void SelectAll() {
        int size = ConstantArrayClass.imageList.size();
        for (int i = 0; i < size; i++) {
            ((DataFileModel) ConstantArrayClass.imageList.get(i)).isSelected = true;
        }
        if (GetSelectedImageList().size() != 0) {
            mActionMode.setTitle(String.valueOf(GetSelectedImageList().size()));
        }
        countSelected = ConstantArrayClass.imageList.size();
        isSelectAll = true;
        isSingleSelect = true;
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

        ((TextView) dialog.findViewById(R.id.textDesc)).setText(getString(R.string.set_your_security_lock_for_hide_photos));

        ((TextView) dialog.findViewById(R.id.rl_yes)).setOnClickListener(view -> {
            FolderPath.lock_screen = 1;
            startActivity(new Intent(ImageSelectActivity.this, PrivateActivity.class));
            dialog.dismiss();
        });

        ((TextView) dialog.findViewById(R.id.rl_no)).setOnClickListener(view -> dialog.dismiss());
        dialog.show();
    }

    public void LockDialog() {
        final Dialog dialog = new Dialog(this, R.style.ThemeWithCorners1);
        dialog.requestWindowFeature(1);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.setContentView(R.layout.dialog_lock_image);
        dialog.setTitle(getString(R.string.lock_image));
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        ((TextView) dialog.findViewById(R.id.rl_yes)).setOnClickListener(view -> {
            ImageSelectActivity.this.LockImages();
            dialog.dismiss();
        });

        ((TextView) dialog.findViewById(R.id.rl_no)).setOnClickListener(view -> dialog.dismiss());
        dialog.show();
    }

    public void LockImages() {
        new ArrayList().clear();
        ArrayList<DataFileModel> GetSelectedImageList = GetSelectedImageList();
        if (GetSelectedImageList.size() <= 0) {
            Toast.makeText(this, getString(R.string.select_image), Toast.LENGTH_SHORT).show();
        } else if (ExternalStorageHelper.isExternalStorageReadableAndWritable()) {
            progressTag = "FromLock";
//            new LockImageExecute(GetSelectedImageList).execute(new Void[0]);
        } else {
            Toast.makeText(this, "not Read Write", Toast.LENGTH_SHORT).show();
        }
    }

    public class LockImageExecute extends AsyncTask<Void, Void, Void> {
        ArrayList<DataFileModel> pathList;

        public LockImageExecute(ArrayList<DataFileModel> arrayList) {
            this.pathList = new ArrayList<>();
            progressLock = new ProgressDialog(ImageSelectActivity.this);
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
            new File(FolderPath.SDCARD_PATH_IMAGE).mkdirs();
            for (int i = 0; i < pathList.size(); i++) {
                File file = new File(pathList.get(i).path);
                File file2 = new File(FolderPath.SDCARD_PATH_IMAGE + File.separator + file.getName());
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
                            Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
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
            isSingleSelect = false;
            isSelectAll = false;
            progressLock.dismiss();
            if (mActionMode != null) {
                mActionMode.finish();
            }
            AppUtilsClass.RefreshImageAlbum(ImageSelectActivity.this);
            AppUtilsClass.RefreshMoment(ImageSelectActivity.this);
            new GetImageAsync().execute(new Void[0]);
            Toast.makeText(ImageSelectActivity.this, getString(R.string.files_moved_to_vault_successfully), Toast.LENGTH_SHORT).show();
        }
    }

    public void ImageAlbumDialog(final boolean z) {
        albumDialog = new Dialog(this, R.style.MyDialog);
        albumDialog.requestWindowFeature(1);
        albumDialog.setCancelable(false);
        albumDialog.setContentView(R.layout.dg_folder);
        ImageView imageView = (ImageView) albumDialog.findViewById(R.id.btnBack);
        TextView textView = (TextView) albumDialog.findViewById(R.id.toolbarTitle);
        textView.setText("Select Image Album");
        textView.setVisibility(View.VISIBLE);
        albumGridView = (GridView) albumDialog.findViewById(R.id.albumGridView);
        Cursor query = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new String[]{"_id", "bucket_id", "bucket_display_name", Utils.mediaPath}, null, null, null);
        final ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList();
        if (query != null) {
            while (query.moveToNext()) {
                DialogAlbumModel gMDialogAlbumModel = new DialogAlbumModel();
                gMDialogAlbumModel.bucket_id = query.getString(query.getColumnIndex("bucket_id"));
                if (!arrayList2.contains(gMDialogAlbumModel.bucket_id)) {
                    gMDialogAlbumModel.foldername = query.getString(query.getColumnIndex("bucket_display_name"));
                    gMDialogAlbumModel.coverThumb = query.getString(query.getColumnIndexOrThrow(Utils.mediaPath));
                    gMDialogAlbumModel.folderPath = GetParentPath(query.getString(query.getColumnIndexOrThrow(Utils.mediaPath)));
                    gMDialogAlbumModel.id = query.getString(query.getColumnIndex("_id"));
                    gMDialogAlbumModel.pathlist = GetPathList("" + gMDialogAlbumModel.bucket_id);
                    arrayList.add(gMDialogAlbumModel);
                    arrayList2.add(gMDialogAlbumModel.bucket_id);
                }
            }
            query.close();
        }

        dialogAlbumAdapter = new AlbumAdapter(this, (ArrayList<DialogAlbumModel>) arrayList);
        albumGridView.setAdapter((ListAdapter) dialogAlbumAdapter);

        FolderOrientation(getResources().getConfiguration().orientation);

        albumGridView.setOnItemClickListener((adapterView, view, i, j) -> {
            if (arrayList == null || arrayList.isEmpty()) {
                return;
            }
            MoveImageInsideDialog(new File(((DialogAlbumModel) arrayList.get(i)).folderPath), z, ((DialogAlbumModel) arrayList.get(i)).bucket_id, ((DialogAlbumModel) arrayList.get(i)).foldername);
        });

        imageView.setOnClickListener(view -> {
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

        albumDialog.show();
    }

    public String GetParentPath(String str) {
        return new File(str).getAbsoluteFile().getParent();
    }

    public ArrayList<String> GetPathList(String str) {
        ArrayList<String> arrayList = new ArrayList<>();
        Cursor query = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new String[]{Utils.mediaPath, "bucket_display_name"}, "bucket_id = ?", new String[]{str}, null);
        if (query.moveToFirst()) {
            int columnIndexOrThrow = query.getColumnIndexOrThrow(Utils.mediaPath);
            query.getColumnIndexOrThrow("bucket_display_name");
            do {
                arrayList.add(query.getString(columnIndexOrThrow));
            } while (query.moveToNext());
            query.close();
            return arrayList;
        }
        query.close();
        return arrayList;
    }

    public void MoveImageInsideDialog(final File file, final boolean z, final String str, String str2) {
        insideFileDialog = new Dialog(this, R.style.MyDialog);
        insideFileDialog.requestWindowFeature(1);
        insideFileDialog.setCancelable(false);
        insideFileDialog.setContentView(R.layout.dialog_gm_folder_image);

        ImageView imageView = (ImageView) insideFileDialog.findViewById(R.id.btnBack);
        TextView textView = (TextView) insideFileDialog.findViewById(R.id.toolbarTitle);

        textView.setText(str2);
        textView.setVisibility(View.VISIBLE);
        albumImageGridView = (GridView) insideFileDialog.findViewById(R.id.albumGridView);
        Cursor query = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, this.dataParameter, "bucket_id =?", new String[]{str}, null);
        ArrayList arrayList = new ArrayList(query.getCount());
        while (query.moveToNext()) {
            String string = query.getString(query.getColumnIndex(Utils.mediaPath));
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

//        dialogAlbumImageAdapter = new ImageFolderAdapter(this, (ArrayList<Object>) arrayList);
//        albumImageGridView.setAdapter((ListAdapter) dialogAlbumImageAdapter);
//        orientationBasedUIAlbumImage(getResources().getConfiguration().orientation);
//        dialogAlbumImageAdapter.notifyDataSetChanged();

//        albumImageGridView.setOnItemClickListener((adapterView, view, i, j) -> {
//            sendImgList.clear();
//            sendImgList = new ArrayList<>();
//            sendImgList.addAll(ImageSelectActivity.myImageinsideList);
//            lastPosition = i;
//
//            Common.IdentifyActivity = TAG;
//            Intent intent = new Intent(ImageSelectActivity.this, PreviewActivity.class);
//            intent.putExtra(Common.gImagePath, ((DataFileModel) sendImgList.get(lastPosition)).path);
//            intent.putExtra(Common.gTotalimage, sendImgList.size());
//            intent.putExtra(Common.gCurrenrtPosition, lastPosition);
//            intent.putExtra(Common.gArrayType, "open");
//            intent.putExtra(Common.gActivityname, "InnerImageSelectActivity");
//            intent.putExtra(Common.gBucketID, AppUtilsClass.bucketId);
//            intent.putExtra(Common.gMediaType, ((DataFileModel) sendImgList.get(lastPosition)).mediaType);
//            startActivityForResult(intent, ARRAY_REFRESH_CODE);
//        });

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
            albumImageGridView.setOnItemClickListener(null);
        }

        linearLayout.setOnClickListener(view -> {
            MoveImageonAlbum(file, z, str);
            linearLayout.setVisibility(View.GONE);
            UnSelectAll();
            if (mActionMode != null) {
                mActionMode.finish();
            }
        });

        insideFileDialog.show();
    }

    public void MoveImageonAlbum(File file, boolean z, String str) {
        if (GetSelectedImageList().size() > 0) {
            new MoveToAlbum(GetSelectedImageList(), file, z, str).execute(new Void[0]);
        } else {
            Toast.makeText(this, getString(R.string.select_image), 0).show();
        }
    }


    public class MoveToAlbum extends AsyncTask<Void, Void, Void> {
        File albumDir;
        String bucketId;
        boolean operation;
        ArrayList<DataFileModel> patharraylist;
        ProgressDialog progress;
        int toast_cnt = 0;

        public MoveToAlbum(ArrayList<DataFileModel> arrayList, File file, boolean z, String str) {
            this.patharraylist = new ArrayList<>();
            this.progress = new ProgressDialog(ImageSelectActivity.this);
            this.patharraylist = arrayList;
            this.albumDir = file;
            this.operation = z;
            this.bucketId = str;
        }

        @Override
        public void onPreExecute() {
            super.onPreExecute();
            progress.setMessage(getString(R.string.please_wait_a_while));
            progress.setProgressStyle(0);
            progress.setIndeterminate(false);
            progress.setCancelable(false);
            progress.show();
        }

        @Override
        public Void doInBackground(Void... voidArr) {
            if (operation) {
                toast_cnt = 0;
                for (int i = 0; i < patharraylist.size(); i++) {
                    File file = new File(patharraylist.get(i).path);
                    File file2 = new File(albumDir + File.separator + file.getName());
                    try {
                        org.apache.commons.io.FileUtils.copyFile(file, file2);
                        file.delete();
                        if (patharraylist.size() - 1 == i) {
                            for (int i2 = 0; i2 < patharraylist.size(); i2++) {
                                ContentResolver contentResolver = getContentResolver();
                                Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                                contentResolver.delete(uri, "_data='" + patharraylist.get(i2).path + "'", null);
                            }
                        }
                        ContentValues contentValues = new ContentValues();
                        contentValues.put(Utils.mediaPath, file2.getPath());
                        contentValues.put("mime_type", "image/*");
                        contentValues.put("date_added", Long.valueOf(System.currentTimeMillis()));
                        contentValues.put("date_modified", Long.valueOf(file2.lastModified()));
                        getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                toast_cnt = 1;
                for (int i3 = 0; i3 < patharraylist.size(); i3++) {
                    File file3 = new File(patharraylist.get(i3).path);
                    File file4 = new File(albumDir + File.separator + System.currentTimeMillis() + file3.getName());
                    albumDir.mkdirs();
                    try {
                        org.apache.commons.io.FileUtils.copyFile(file3, file4);
                        ContentValues contentValues2 = new ContentValues();
                        contentValues2.put("title", file4.getName());
                        contentValues2.put("description", "Gallery");
                        contentValues2.put(Utils.mediaPath, file4.getPath());
                        contentValues2.put("mime_type", "image/*");
                        contentValues2.put("date_added", Long.valueOf(System.currentTimeMillis()));
                        contentValues2.put("date_modified", Long.valueOf(System.currentTimeMillis()));
                        contentValues2.put("bucket_id", Integer.valueOf(file4.getPath().toLowerCase(Locale.US).hashCode()));
                        contentValues2.put("bucket_display_name", file4.getName().toLowerCase(Locale.US));
                        contentValues2.put(Utils.mediaPath, file4.getPath());
                        getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues2);
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
            progress.dismiss();
            AppUtilsClass.RefreshImageAlbum(ImageSelectActivity.this);
            AppUtilsClass.RefreshMoment(ImageSelectActivity.this);
            new GetImageAsync().execute(new Void[0]);
            if (patharraylist.size() == 1) {
                if (toast_cnt == 0) {
                    Toast.makeText(ImageSelectActivity.this, getString(R.string.files_move_successfully), Toast.LENGTH_SHORT).show();
                } else if (toast_cnt == 1) {
                    Toast.makeText(ImageSelectActivity.this, getString(R.string.files_copy_successfully), Toast.LENGTH_SHORT).show();
                }
                insideFileDialog.dismiss();
                albumDialog.dismiss();
                return;
            }
            if (toast_cnt == 0) {
                Toast.makeText(ImageSelectActivity.this, "Files moved successfully.", Toast.LENGTH_SHORT).show();
            } else if (toast_cnt == 1) {
                Toast.makeText(ImageSelectActivity.this, "Files copied successfully.", Toast.LENGTH_SHORT).show();
            }
            insideFileDialog.dismiss();
            albumDialog.dismiss();
        }
    }

    public void RefreshAdapter(String str) {
        Cursor query = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, this.dataParameter, "bucket_id =?", new String[]{str}, null);
        ArrayList arrayList = new ArrayList(query.getCount());
        if (query.moveToNext()) {
            String string = query.getString(query.getColumnIndex(Utils.mediaPath));
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
        myImageinsideList = new ArrayList<>();
        myImageinsideList.addAll(arrayList);
//        dialogAlbumImageAdapter = new ImageFolderAdapter(this, myImageinsideList);
//        albumImageGridView.setAdapter((ListAdapter) dialogAlbumImageAdapter);
//        orientationBasedUIAlbumImage(getResources().getConfiguration().orientation);
//        dialogAlbumImageAdapter.notifyDataSetChanged();
    }

    public void DataOrientation(int i) {
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
        if (innerBothAlbumSelectAdapter != null) {
            innerBothAlbumSelectAdapter.setLayoutParams(displayMetrics.widthPixels / (i == 1 ? this.i2 : MainActivity.dataDivider));
        }
        if (i != 1) {
            this.i2 = MainActivity.dataDivider;
        }
        gridViewImage.setNumColumns(this.i2);
    }
    AlbumAdapter dialogAlbumAdapter;

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
        if (dialogAlbumAdapter != null) {
            dialogAlbumAdapter.setLayoutParams(displayMetrics.widthPixels / (i == 1 ? this.i2 : MainActivity.albumDivider));
        }
        if (i != 1) {
            this.i2 = MainActivity.albumDivider;
        }
        albumGridView.setNumColumns(this.i2);
    }

    private void orientationBasedUIAlbumImage(int i) {
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
//        if (dialogAlbumImageAdapter != null) {
//            dialogAlbumImageAdapter.setLayoutParams(displayMetrics.widthPixels / (i == 1 ? this.i2 : MainActivity.dataDivider));
//        }
        if (i != 1) {
            this.i2 = MainActivity.dataDivider;
        }
        albumImageGridView.setNumColumns(this.i2);
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
                try {
                    if (FileUtils.CopyMoveFile(file, file2).exists()) {
                        AppUtilsClass.insertUri(ImageSelectActivity.this, file2);
                        file.delete();
                        ContentResolver contentResolver = getContentResolver();
                        Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                        contentResolver.delete(uri, "_data='" + file + "'", null);
                        UnSelectAll();
                        new Handler().postDelayed(() -> new GetImageAsync().execute(new Void[0]), 500L);
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
        dialog.setContentView(R.layout.dg_delete_file);
        dialog.setTitle("Delete Image");
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        ((RelativeLayout) dialog.findViewById(R.id.rl_yes)).setOnClickListener(view -> {
            progressTag = "FromDelete";
            new DeleteToTrashImageExecute(GetSelectedImageList()).execute(new Void[0]);
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

    public class DeleteToTrashImageExecute extends AsyncTask<Void, Void, Void> {
        ArrayList<DataFileModel> pathList;

        public DeleteToTrashImageExecute(ArrayList<DataFileModel> arrayList) {
            this.pathList = new ArrayList<>();
            progressDelete = new ProgressDialog(ImageSelectActivity.this);
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
            new File(FolderPath.SDCARD_PATH_DELETE_IMAGE).mkdirs();
            for (int i = 0; i < pathList.size(); i++) {
                File file = new File(pathList.get(i).path);
                File file2 = new File(FolderPath.SDCARD_PATH_DELETE_IMAGE + File.separator + file.getName());
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
                            Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
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
            isSingleSelect = false;
            isSelectAll = false;
            progressDelete.dismiss();
            if (mActionMode != null) {
                mActionMode.finish();
            }
            AppUtilsClass.RefreshImageAlbum(ImageSelectActivity.this);
            new Handler().postDelayed(() -> {
                AppUtilsClass.RefreshMoment(ImageSelectActivity.this);
                MomentFragment.momentAdapter.notifyDataSetChanged();
            }, 1000L);
            new GetImageAsync().execute(new Void[0]);
            Toast.makeText(ImageSelectActivity.this, "Files moved to trash successfully.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i == ARRAY_REFRESH_CODE && i2 == -1) {
            if (ConstantArrayClass.imageList.size() > 0) {
                rl_NoDataLayout.setVisibility(View.GONE);
                gridViewImage.setVisibility(View.VISIBLE);
                return;
            }
            rl_NoDataLayout.setVisibility(View.VISIBLE);
            gridViewImage.setVisibility(View.GONE);
        }
    }

    public class GetImageAsync extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
        }

        public GetImageAsync() {
        }

        @Override
        public Void doInBackground(Void... voidArr) {
            try {
                if (ConstantArrayClass.imageList != null) {
                    ConstantArrayClass.imageList.clear();
                }
                if (ConstantArrayClass.imageList == null) {
                    ConstantArrayClass.imageList = new ArrayList<>();
                }
                ConstantArrayClass.imageList.addAll(((FolderModel)ConstantArrayClass.imageAlbumsList.get(position)).pathlist);
                return null;
            } catch (NumberFormatException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        public void onPostExecute(Void r4) {
            super.onPostExecute(r4);
            SetImageAdapter();
            try {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
//                        if (ConstantArrayClass.imageList == null) {
//                            return;
//                        }
                        if (ConstantArrayClass.imageList.size() > 0) {
                            rl_NoDataLayout.setVisibility(View.GONE);
                            gridViewImage.setVisibility(View.VISIBLE);
                            return;
                        }
                        rl_NoDataLayout.setVisibility(View.VISIBLE);
                        gridViewImage.setVisibility(View.GONE);
                    }
                }, 500L);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (renameTag) {
                AppUtilsClass.RefreshImageAlbum(ImageSelectActivity.this);
//                if (mActionMode != null) {
//                    mActionMode.finish();
//                }
                AppUtilsClass.ScanMomentAllDataNew(ImageSelectActivity.this);
                renameTag = false;
            }
        }
    }
    public static ArrayList<Object> PassDataAlbum = new ArrayList<>();

    public void SetImageAdapter() {
        new SortingDataDialog(this, ConstantArrayClass.imageList, null).Sorting(LoginPreferenceUtilsData.GetStringData(this, SortingDataDialog.SortingName), LoginPreferenceUtilsData.GetStringData(this, SortingDataDialog.SortingType));
        innerBothAlbumSelectAdapter = new BothAlbumSelectAdapter(this, getApplicationContext(), ConstantArrayClass.imageList);
        gridViewImage.setAdapter((ListAdapter) innerBothAlbumSelectAdapter);
        loader.setVisibility(View.GONE);
        gridViewImage.setVisibility(View.VISIBLE);
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
                if (isSingleSelect) {
                    SelectSingleImage(sendImgList, num.intValue());
                    return;
                }
                lastPosition = num.intValue();
                Common.IdentifyActivity = TAG;
                File file = new File(((DataFileModel) ImageSelectActivity.sendImgList.get(lastPosition)).path);
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
                PassDataAlbum = arrayList;

                Intent intent = new Intent(BseActivity.activity, PreviewActivity.class);
                intent.putExtra(Common.gImagePath, ((DataFileModel) sendImgList.get(lastPosition)).path);
                intent.putExtra(Common.gTotalimage, sendImgList.size());
                intent.putExtra(Common.gCurrenrtPosition, lastPosition);
                intent.putExtra(Common.gArrayType, "open");
                intent.putExtra(Common.gActivityname, "InnerImageSelectActivity");
                intent.putExtra(Common.gMediaType, str2);
                startActivityForResult(intent, ARRAY_REFRESH_CODE);
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
                SelectSingleImage(ImageSelectActivity.sendImgList, num.intValue());
            }
        });
    }

    private void SortingCallBack() {
        sortingListener = new ImageSorting();
    }


    public class ImageSorting implements SortingListener {
        ImageSorting() {
        }

        @Override
        public void Sorting(ArrayList<Object> arrayList) {
            sendImgList = new ArrayList<>();
            sendImgList = arrayList;
            innerBothAlbumSelectAdapter = new BothAlbumSelectAdapter(ImageSelectActivity.this, getApplicationContext(), sendImgList);
            gridViewImage.setAdapter((ListAdapter) ImageSelectActivity.innerBothAlbumSelectAdapter);
            loader.setVisibility(View.GONE);
            gridViewImage.setVisibility(View.VISIBLE);
            DataOrientation(getResources().getConfiguration().orientation);
            innerBothAlbumSelectAdapter.notifyDataSetChanged();
            innerBothAlbumSelectAdapter.setItemClickCallback(new OnClickListener<ArrayList<Object>, Integer, View>() {
                @Override
                public void onClickMoreListener(ArrayList<Object> arrayList2, Integer num, View view) {
                }

                @Override
                public void onClickListener(ArrayList<Object> arrayList2, Integer num) {
                    String str = "";
                    sendImgList = new ArrayList<>();
                    sendImgList = arrayList2;
                    if (isSingleSelect) {
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

                    Intent intent = new Intent(ImageSelectActivity.this, PreviewActivity.class);
                    intent.putExtra(Common.gImagePath, ((DataFileModel) sendImgList.get(lastPosition)).path);
                    intent.putExtra(Common.gTotalimage, sendImgList.size());
                    intent.putExtra(Common.gCurrenrtPosition, lastPosition);
                    intent.putExtra(Common.gArrayType, "open");
                    intent.putExtra(Common.gActivityname, "InnerImageSelectActivity");
                    intent.putExtra(Common.gMediaType, str2);
                    startActivityForResult(intent, ARRAY_REFRESH_CODE);
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

    public ClipData CreateClipData(ArrayList<DataFileModel> arrayList) {
        String[] strArr = new String[arrayList.size()];
        for (int i = 0; i < arrayList.size(); i++) {
            strArr[i] = getMimeType(this, FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".fileprovider", new File(arrayList.get(i).path)));
        }
        ClipData clipData = new ClipData("Images", strArr, new ClipData.Item(FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".fileprovider", new File(arrayList.get(0).path))));
        for (int i2 = 1; i2 < arrayList.size(); i2++) {
            clipData.addItem(new ClipData.Item(FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".fileprovider", new File(arrayList.get(i2).path))));
        }
        return clipData;
    }

    public static String getMimeType(Context context, Uri uri) {
        return context.getContentResolver().getType(uri);
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
        if (insideFileDialog == null || !insideFileDialog.isShowing()) {
            if (albumDialog == null || !albumDialog.isShowing()) {
                finish();
                try {
                    new SortingDataDialog(ImageSelectActivity.this, ConstantArrayClass.imageList, sortingListener).Sorting(AppUtilsClass.lastModifiedStr, AppUtilsClass.descendingStr);
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
                btmsDialog.dismiss();
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
        for (int i = 0; i < GetSelectedImageList().size(); i++) {
            File file = new File(GetSelectedImageList().get(i).path);
            File file2 = new File(str + File.separator + file.getName());
            new MediaScanners(GalleryAppClass.getInstance(), file2);
            try {
                FileUtils.CopyMoveFile(file, file2);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (!z) {
            Toast.makeText(this, getString(R.string.image_copy_successfully), Toast.LENGTH_SHORT).show();
            UnSelectAll();
            isSingleSelect = false;
            isSelectAll = false;
            ActionMode actionMode = mActionMode;
            if (actionMode != null) {
                actionMode.finish();
            }
            AppUtilsClass.RefreshImageAlbum(this);
            AppUtilsClass.RefreshMoment(this);
            MomentFragment.momentAdapter.notifyDataSetChanged();
        }
        if (z) {
            progressTag = "FromPermanentDelete";
            toastTag = "MoveData";
            new DeleteImageExecute(GetSelectedImageList()).execute(new Void[0]);
        }
    }


    public class DeleteImageExecute extends AsyncTask<Void, Void, Void> {
        ArrayList<DataFileModel> pathList;

        public DeleteImageExecute(ArrayList<DataFileModel> arrayList) {
            this.pathList = new ArrayList<>();
            progressPermanentDelete = new ProgressDialog(ImageSelectActivity.this);
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
                                arrayList.add(ContentUris.withAppendedId(MediaStore.Images.Media.getContentUri("external"), AppUtilsClass.getFilePathToMediaID(pathList.get(i).path, ImageSelectActivity.this)));
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
                AppUtilsClass.requestIntent(arrayList, ImageSelectActivity.this);
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
                isSingleSelect = false;
                isSelectAll = false;
                progressPermanentDelete.dismiss();
                if (mActionMode != null) {
                    mActionMode.finish();
                }
                AppUtilsClass.RefreshImageAlbum(ImageSelectActivity.this);
                AppUtilsClass.RefreshMoment(ImageSelectActivity.this);
                MomentFragment.momentAdapter.notifyDataSetChanged();
                new GetImageAsync().execute(new Void[0]);
                if (toastTag.equals("MoveData")) {
                    Toast.makeText(ImageSelectActivity.this, getString(R.string.files_move_successfully), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ImageSelectActivity.this, R.string.files_are_deleted_successfully, Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
