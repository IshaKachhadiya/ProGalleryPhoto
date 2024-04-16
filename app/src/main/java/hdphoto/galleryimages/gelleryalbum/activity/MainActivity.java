package hdphoto.galleryimages.gelleryalbum.activity;

import static android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION;
import static hdphoto.galleryimages.gelleryalbum.fragment.ImageFragment.imageFolderAdapter;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.permissionx.guolindev.PermissionX;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Set;

import hdphoto.galleryimages.gelleryalbum.databinding.ActivityGmMainBinding;
import hdphoto.galleryimages.gelleryalbum.duplicate.DuplicateImageActivity;
import hdphoto.galleryimages.gelleryalbum.fragment.FavouriteFragment;
import hdphoto.galleryimages.gelleryalbum.fragment.ImageFragment;
import hdphoto.galleryimages.gelleryalbum.fragment.ImagePrivateFragment;
import hdphoto.galleryimages.gelleryalbum.fragment.MomentFragment;
import hdphoto.galleryimages.gelleryalbum.fragment.VideoFragment;
import hdphoto.galleryimages.gelleryalbum.fragment.VideoPrivateFragment;
import hdphoto.galleryimages.gelleryalbum.getdata.ImageAlbumData;
import hdphoto.galleryimages.gelleryalbum.getdata.MomentData;
import hdphoto.galleryimages.gelleryalbum.getdata.VideoAlbumData;
import hdphoto.galleryimages.gelleryalbum.listeners.AlbumSortingListener;
import hdphoto.galleryimages.gelleryalbum.listeners.HideImageFolderDataSortingListener;
import hdphoto.galleryimages.gelleryalbum.listeners.HideVideoFolderDataSortingListener;
import hdphoto.galleryimages.gelleryalbum.location_media.activity.LocationImageActivity;
import hdphoto.galleryimages.gelleryalbum.location_media.activity.MediaColorActivity;
import hdphoto.galleryimages.gelleryalbum.location_media.common.Constant;
import hdphoto.galleryimages.gelleryalbum.location_media.event.VideoEvent;
import hdphoto.galleryimages.gelleryalbum.location_media.rx.RxBus;
import hdphoto.galleryimages.gelleryalbum.model.DataFileModel;
import hdphoto.galleryimages.gelleryalbum.model.FolderModel;
import hdphoto.galleryimages.gelleryalbum.sorting.SortingFolderDialog;
import hdphoto.galleryimages.gelleryalbum.sorting.SortingPrivateImageDialog;
import hdphoto.galleryimages.gelleryalbum.sorting.SortingVideoPrivateDialog;
import hdphoto.galleryimages.gelleryalbum.constant.Common;
import hdphoto.galleryimages.gelleryalbum.utils.AppUtilsClass;
import hdphoto.galleryimages.gelleryalbum.utils.ConstantArrayClass;
import hdphoto.galleryimages.gelleryalbum.utils.PrefClass;
import hdphoto.galleryimages.gelleryalbum.R;
import kotlin.jvm.internal.Intrinsics;

@SuppressWarnings("all")
public class MainActivity extends BseActivity {
    public static ArrayList<Object> ArrayImage11 = null;
    public static ArrayList<Object> ArrayVideo11 = null;
    public static ImageView addImageAlbum = null;
    public static ImageView addVideoAlbum = null;
    public static int albumDivider = 0;
    public static File androidpath = null;
    public static boolean checkLockForFirstFile = false;
    public static int dataDivider = 0;
    public static int fragclick = 0;
    public static ImageAlbumData getImageAlbumData = null;
    public static MomentData getMomentData = null;
    public static VideoAlbumData getVideoAlbumData = null;
    public static ImageView ivAddAlbum = null;
    public static ImageView ivDelete = null;
    public static ImageView ivSelectAll = null;
    public static ImageView ivShare = null;
    public static ImageView ivSort = null;
    public static ImageView ivUnLock = null;
    private static SharedPreferences mPreferences = null;
    public static String oriTag = "";
    public static int parameter;
    public static PrefClass preferenceAlbumUtils, preferenceDataUtils, preferenceForTrash11;
    public static int privateclick;
    public static RelativeLayout rl_imageBtn, rl_videoBtn, rl_camera;
    public static ImageView iv_sort_imglist, iv_sort_videolist;
    public static Toolbar txt_toolbar;
    public static TextView txtTitle;
    Fragment activeFragment;
    private AlbumSortingListener albumSortingListener;
    ArrayList<Object> deleteImageList;
    ArrayList<Object> deleteVideoList;
    FragmentManager fragmentManager;
    private HideImageFolderDataSortingListener hideImageFolderDataSortingListener;
    ImageView imgOpenDrawer;
    LinearLayout ll_moment, ll_other, ll_photos, ll_videos;
    ProgressDialog progressDialog;
    private HideVideoFolderDataSortingListener sortingHideVideoCallBack;
    TextView txtMoments, txtOther, txtPhotos, txtVideos;
    Typeface typeface;
    DrawerLayout drawerLayout;
    View view;
    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context2, Intent intent) {
            unregisterReceiver(broadcastReceiver);
            if (Common.IdentifyActivity.equals("PasswordChangeActivity")) {
                if (preferenceClass.getInt(Common.gIsLockStatus, 0) == 0) {
                    startActivity(new Intent(MainActivity.this, SecurityQAActivity.class));
                } else {
                    Intent intent2 = new Intent(MainActivity.this, PasswordChangeActivity.class);
                    intent2.putExtra(Common.gActivityname, "MainActivity");
                    startActivity(intent2);
                }
                Common.IdentifyActivity = "";
            }
        }
    };
    private String folder_path = "";
    int REQUEST_ACTION_OPEN_DOCUMENT_TREE = 101;
    LinkedHashMap<String, ArrayList<DataFileModel>> photosDataHashMap = new LinkedHashMap<>();
    TextView tv_photo, tv_video, tv_album;
    RelativeLayout rl_Moment, rl_Image, rl_Video, rl_fav, rlCamera;
    ActivityGmMainBinding binding;
    String str, str_video;


    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        binding = ActivityGmMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        str = getIntent().getStringExtra("secure");

        getPermission();
        initView();

        androidpath = new File(getExternalFilesDir(getString(R.string.app_name)).getAbsolutePath());
        if (!androidpath.exists()) {
            androidpath.mkdirs();
        }

        preferenceAlbumUtils = new PrefClass(this);
        preferenceDataUtils = new PrefClass(this);
        preferenceForTrash11 = new PrefClass(this);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Recover File..");
        progressDialog.setCancelable(false);

        typeface = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Medium.ttf");
        if (preferenceClass.getInt(Common.gIsLockStatus, 0) != 0) {
            preferenceClass.putInt(Common.gIsLockStatus, 1);
        }
        initGetData();

        clickListener();

        momentFragment = new MomentFragment();
        imageFragment = new ImageFragment();
        videoFragment = new VideoFragment();
        favouriteFragment = new FavouriteFragment();

        fragmentManager = this.getSupportFragmentManager();
        fragmentManager.beginTransaction().add(R.id.flContent, videoFragment, "3").hide(videoFragment).commitAllowingStateLoss();
        fragmentManager.beginTransaction().add(R.id.flContent, imageFragment, "2").hide(imageFragment).commitAllowingStateLoss();
        fragmentManager.beginTransaction().add(R.id.flContent, favouriteFragment, "4").hide(favouriteFragment).commitAllowingStateLoss();
        fragmentManager.beginTransaction().add(R.id.flContent, momentFragment, "1").commitAllowingStateLoss();
        activeFragment = momentFragment;
        fragmentManager.beginTransaction().hide(activeFragment).show(momentFragment).commitAllowingStateLoss();

        if (fragmentManager != null && !fragmentManager.isStateSaved()) {
            fragmentManager.popBackStackImmediate();
        }

        DrawerListener();
        SetToolBar();

        binding.mainBottom.ivGallery.setImageResource(R.drawable.ic_select_gallery);
        binding.mainBottom.ivAlbum.setImageResource(R.drawable.ic_unselect_album);
        binding.mainBottom.ivVideo.setImageResource(R.drawable.ic_unselect_video);
        binding.mainBottom.ivFav.setImageResource(R.drawable.ic_unselect_favourite);

        sortingBothDataCallBack();
        sortingPrivateImageCallBack();
        sortingPrivateVideoCallBack();

        GetAutoDeleteData();
    }


    VideoFragment videoFragment;
    FavouriteFragment favouriteFragment;
    MomentFragment momentFragment;
    ImageFragment imageFragment;

    private void clickListener() {
        rl_fav = view.findViewById(R.id.rl_fav);
        rlCamera = view.findViewById(R.id.rl_camera);

        rlCamera.setOnClickListener(v -> {
            openCamera();
        });

        binding.mainBottom.rlMoment.setOnClickListener(view -> {
            MainActivity.DeleteCache(this);
            MainActivity.rl_imageBtn.setVisibility(View.GONE);
            MainActivity.rl_videoBtn.setVisibility(View.GONE);
            MainActivity.fragclick = 0;

            fragmentManager.beginTransaction().hide(activeFragment).show(momentFragment).commitAllowingStateLoss();
            fragmentManager.popBackStackImmediate();
            activeFragment = momentFragment;

            binding.mainBottom.ivGallery.setImageResource(R.drawable.ic_select_gallery);
            binding.mainBottom.ivAlbum.setImageResource(R.drawable.ic_unselect_album);
            binding.mainBottom.ivVideo.setImageResource(R.drawable.ic_unselect_video);
            binding.mainBottom.ivFav.setImageResource(R.drawable.ic_unselect_favourite);
        });
        binding.mainBottom.rlImage.setOnClickListener(view -> {
            MainActivity.DeleteCache(this);
            MainActivity.rl_imageBtn.setVisibility(View.GONE);
            MainActivity.rl_videoBtn.setVisibility(View.GONE);
            MainActivity.fragclick = 2;

            fragmentManager.beginTransaction().hide(activeFragment).show(imageFragment).commitAllowingStateLoss();
            fragmentManager.popBackStackImmediate();
            activeFragment = imageFragment;

            binding.mainBottom.ivGallery.setImageResource(R.drawable.ic_unselect_gallery);
            binding.mainBottom.ivAlbum.setImageResource(R.drawable.ic_album_select);
            binding.mainBottom.ivVideo.setImageResource(R.drawable.ic_unselect_video);
            binding.mainBottom.ivFav.setImageResource(R.drawable.ic_unselect_favourite);
        });
        binding.mainBottom.rlVideo.setOnClickListener(view -> {
            MainActivity.DeleteCache(this);
            MainActivity.rl_imageBtn.setVisibility(View.GONE);
            MainActivity.rl_videoBtn.setVisibility(View.GONE);
            MainActivity.fragclick = 3;

            fragmentManager.beginTransaction().hide(activeFragment).show(videoFragment).commitAllowingStateLoss();
            fragmentManager.popBackStackImmediate();
            activeFragment = videoFragment;

            binding.mainBottom.ivGallery.setImageResource(R.drawable.ic_unselect_gallery);
            binding.mainBottom.ivAlbum.setImageResource(R.drawable.ic_unselect_album);
            binding.mainBottom.ivVideo.setImageResource(R.drawable.ic_video_select);
            binding.mainBottom.ivFav.setImageResource(R.drawable.ic_unselect_favourite);
        });

        rl_fav.setOnClickListener(v -> {
            MainActivity.fragclick = 4;

            MainActivity.rl_imageBtn.setVisibility(View.GONE);
            MainActivity.rl_videoBtn.setVisibility(View.GONE);

            binding.mainBottom.ivGallery.setImageResource(R.drawable.ic_unselect_gallery);
            binding.mainBottom.ivAlbum.setImageResource(R.drawable.ic_unselect_album);
            binding.mainBottom.ivVideo.setImageResource(R.drawable.ic_unselect_video);
            binding.mainBottom.ivFav.setImageResource(R.drawable.ic_favourite_select);

            fragmentManager.beginTransaction().hide(activeFragment).show(favouriteFragment).commitAllowingStateLoss();
            fragmentManager.popBackStackImmediate();
            activeFragment = favouriteFragment;
        });
    }

    private final void initGetData() {
        if (Constant.photoList == null || Constant.photoList.size() == 0 || Constant.folderList == null || Constant.folderList.size() == 0 || Constant.storyList == null || Constant.storyList.size() == 0) {
            new Thread(new Runnable() {
                @Override
                public final void run() {
                    GetImageData(MainActivity.this);
                }
            }).start();
        }
        if (Constant.LOCATION_GET.booleanValue()) {
            return;
        }
        new Thread(new Runnable() {
            @Override
            public final void run() {
                Constant.LOCATION_GET = false;
                Constant.getLocationImages(MainActivity.this);
            }
        }).start();
    }

    public final void GetImageData(final MainActivity this$0) {
        Constant.getImages(this$0);
        this$0.runOnUiThread(new Runnable() {
            @Override
            public final void run() {
                this$0.imageColorGet();
            }
        });
    }

    private final void imageColorGet() {
        new Thread(new Runnable() {
            @Override
            public final void run() {
                Constant.getColorImages(MainActivity.this);
                runOnUiThread(new Runnable() {
                    @Override
                    public final void run() {
                        Log.e("imageColorGet", "is stop");
                    }
                });
            }
        }).start();
    }

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private String currentPhotoPath = "";

    private void initView() {
        setActivityResultLauncher();

        view = findViewById(R.id.layout_album_select);
        drawerLayout = findViewById(R.id.drawer_layout);
        rl_imageBtn = findViewById(R.id.rl_imageBtn);
        rl_videoBtn = findViewById(R.id.rl_videoBtn);
        addImageAlbum = findViewById(R.id.addImageAlbum);
        iv_sort_imglist = findViewById(R.id.sortingImageList);
        addVideoAlbum = findViewById(R.id.addVideoAlbum);
        iv_sort_videolist = findViewById(R.id.sortingVideoList);
        ll_photos = findViewById(R.id.layPhotos);
        ll_videos = findViewById(R.id.layVideos);
        txtPhotos = findViewById(R.id.txtPhotos);
        txtVideos = findViewById(R.id.txtVideos);
        txtTitle = findViewById(R.id.tootbatTitle);
        ivSelectAll = findViewById(R.id.btnSelectAll);
        ivUnLock = findViewById(R.id.btnUnLock);
        ivDelete = findViewById(R.id.btnDelete);
        ivShare = findViewById(R.id.btnShare);
        imgOpenDrawer = findViewById(R.id.opendrawer);
        ivAddAlbum = findViewById(R.id.btnAddAlbum);
        ivSort = findViewById(R.id.btnSortAlbum);
        rl_camera = findViewById(R.id.rl_camera);

        txtTitle.setText(getString(R.string.app_name));
    }

    private String getFileExt(Uri contentUri) {
        ContentResolver c = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(c.getType(contentUri));
    }

    private static String imageFilePath;
    private ActivityResultLauncher<Intent> captureVideoActivityResultLauncher;
    private ActivityResultLauncher<Intent> captureImageActivityResultLauncher;

    public final ActivityResultLauncher<Intent> getCaptureImageActivityResultLauncher() {
        return this.captureImageActivityResultLauncher;
    }

    private final void setActivityResultLauncher() {
        this.captureImageActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback() {
            @Override
            public final void onActivityResult(Object obj) {
                imagesetActivityResultLauncher(MainActivity.this, (ActivityResult) obj);
            }
        });
        this.captureVideoActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback() {
            @Override
            public final void onActivityResult(Object obj) {
                videosetActivityResultLauncher(MainActivity.this, (ActivityResult) obj);
            }
        });
    }

    public final void videosetActivityResultLauncher(MainActivity this$0, ActivityResult activityResult) {
        if (activityResult.getResultCode() == -1) {
            MediaScannerConnection.scanFile(this$0, new String[]{this$0.imageFilePath}, null, new MediaScannerConnection.OnScanCompletedListener() {
                @Override
                public final void onScanCompleted(String str, Uri uri) {
                    m201setActivityResultLauncher$lambda7$lambda6(str, uri);
                }
            });
            Log.e("", "VideoFilePath: " + this$0.imageFilePath);
            RxBus.getInstance().post(new VideoEvent(this$0.imageFilePath, "Camera"));
        }
    }

    public static final void m201setActivityResultLauncher$lambda7$lambda6(String str, Uri uri) {
    }

    public static final void m199setActivityResultLauncher$lambda5$lambda4(String str, Uri uri) {
    }

    public final void imagesetActivityResultLauncher(MainActivity this$0, ActivityResult result) {
        if (result.getResultCode() == -1) {
            MediaScannerConnection.scanFile(this$0, new String[]{imageFilePath}, null, new MediaScannerConnection.OnScanCompletedListener() {
                @Override
                public final void onScanCompleted(String str, Uri uri) {
                    m199setActivityResultLauncher$lambda5$lambda4(str, uri);
                }
            });
            Log.e("", "imageFilePath: " + imageFilePath);
//            RxBus.getInstance().post(new EditImageEvent(this$0.imageFilePath, "Camera"));
        }
    }

    public final void setCaptureImageActivityResultLauncher(ActivityResultLauncher<Intent> activityResultLauncher) {
        this.captureImageActivityResultLauncher = activityResultLauncher;
    }

    public final ActivityResultLauncher<Intent> getCaptureVideoActivityResultLauncher() {
        return this.captureVideoActivityResultLauncher;
    }

    public final void setCaptureVideoActivityResultLauncher(ActivityResultLauncher<Intent> activityResultLauncher) {
        this.captureVideoActivityResultLauncher = activityResultLauncher;
    }

    public final void openCamera() {
        String str = MainActivity.fragclick == 2 ? ".mp4" : ".png";
//        String str = ".png";
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).toString() + File.separator + "Camera");
        String format = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss").format(Long.valueOf(System.currentTimeMillis()));
        imageFilePath = file.getPath() + File.separator + format + str;
        if (!file.exists()) {
            file.mkdirs();
        }
        Uri uriForFile = FileProvider.getUriForFile(this, getPackageName() + ".fileprovider", new File(this.imageFilePath));

        if (MainActivity.fragclick == 2) {
            try {
                Intent intent = new Intent("android.media.action.VIDEO_CAPTURE");
                intent.addFlags(1);
                intent.putExtra("output", uriForFile);
                ActivityResultLauncher<Intent> activityResultLauncher = this.captureVideoActivityResultLauncher;
                Intrinsics.checkNotNull(activityResultLauncher);
                activityResultLauncher.launch(intent);
                return;
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(MainActivity.this, getString(R.string.not_found), 0).show();
                return;
            }
        }
        try {
            Intent intent2 = new Intent("android.media.action.IMAGE_CAPTURE");
            intent2.putExtra("output", uriForFile);
            intent2.addFlags(1);
            ActivityResultLauncher<Intent> activityResultLauncher2 = this.captureImageActivityResultLauncher;
            activityResultLauncher2.launch(intent2);
        } catch (Exception e2) {
            e2.printStackTrace();
            Toast.makeText(MainActivity.this, getString(R.string.not_found), 0).show();
        }
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    private int getTotalPhotoCount(Context context) {
        int count = 0;

        Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {MediaStore.Images.Media._ID};

        ContentResolver contentResolver = context.getContentResolver();
        Cursor cursor = contentResolver.query(uri, projection, null, null, null);

        if (cursor != null) {
            count = cursor.getCount();

            cursor.close();
        }
        return count;
    }

    private int getTotalVideoCount(Context context) {
        int count = 0;

        Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {MediaStore.Video.Media._ID};

        ContentResolver contentResolver = context.getContentResolver();
        Cursor cursor = contentResolver.query(uri, projection, null, null, null);

        if (cursor != null) {
            count = cursor.getCount();
            cursor.close();
        }
        return count;
    }

    public static int getAlbumCount(Context context) {
        ContentResolver contentResolver = context.getContentResolver();
        String[] projection = {
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME
        };

        Cursor cursor = contentResolver.query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                projection,
                null,
                null,
                null
        );

        Set<String> albumSet = new HashSet<>();

        if (cursor != null) {
            try {
                int bucketNameColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);

                while (cursor.moveToNext()) {
                    String albumName = cursor.getString(bucketNameColumn);
                    albumSet.add(albumName);
                }

                Log.d("GalleryHelper", "Total Albums: " + albumSet.size());
                return albumSet.size();
            } finally {
                cursor.close();
            }
        }

        return 0;
    }

    private void getPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            PermissionX.init(MainActivity.this)
                    .permissions(Manifest.permission.READ_MEDIA_IMAGES, Manifest.permission.READ_MEDIA_VIDEO, Manifest.permission.CAMERA, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.MANAGE_EXTERNAL_STORAGE)
                    .explainReasonBeforeRequest()
                    .onExplainRequestReason((scope, deniedList, beforeRequest) -> {
                        scope.showRequestReasonDialog(deniedList, "PermissionX needs following permissions to continue", "Allow");
                    })
                    .onForwardToSettings((scope, deniedList) -> {
                        scope.showForwardToSettingsDialog(deniedList, "Please allow following permissions in settings", "Allow");
                    })
                    .request((allGranted, grantedList, deniedList) -> {
                        if (!allGranted) {
                            Toast.makeText(MainActivity.this, "The following permissions are denied：" + deniedList, Toast.LENGTH_SHORT).show();
                        } else {
                            getData(this);
                        }
                    });
        } else {
            PermissionX.init((FragmentActivity) MainActivity.this)
                    .permissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.MANAGE_EXTERNAL_STORAGE)
                    .explainReasonBeforeRequest()
                    .onExplainRequestReason((scope, deniedList, beforeRequest) -> {
                        scope.showRequestReasonDialog(deniedList, "PermissionX needs following permissions to continue", "Allow");
                    })
                    .onForwardToSettings((scope, deniedList) -> {
                        scope.showForwardToSettingsDialog(deniedList, "Please allow following permissions in settings", "Allow");
                    })
                    .request((allGranted, grantedList, deniedList) -> {
                        if (!allGranted) {
                            Toast.makeText(MainActivity.this, "The following permissions are denied：" + deniedList, Toast.LENGTH_SHORT).show();
                        } else {
                            getData(this);
                        }
                    });
        }
    }

    public static void getData(Activity activity) {
        getMomentData = new MomentData(activity);
        getMomentData.getMomentAllAsync.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[0]);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void SetToolBar() {
        imgOpenDrawer.setOnClickListener(view -> {
            openDrawer(drawerLayout);
        });

        ivAddAlbum.setOnClickListener(view -> {
            if (fragclick == 0) {
                AddImageFolderDialog();
            } else if (fragclick == 1) {
                AddVideoFolderDialog();
            }
        });

        ivSort.setOnClickListener(view -> SortingData());
    }

    private void DrawerListener() {
        findViewById(R.id.ll_Home).setOnClickListener(view -> {
            closeDrawer(drawerLayout);
            MainActivity.DeleteCache(this);
            MainActivity.rl_imageBtn.setVisibility(View.GONE);
            MainActivity.rl_videoBtn.setVisibility(View.GONE);
            MainActivity.fragclick = 0;

            fragmentManager.beginTransaction().hide(activeFragment).show(momentFragment).commitAllowingStateLoss();
            fragmentManager.popBackStackImmediate();
            activeFragment = momentFragment;

            binding.mainBottom.ivGallery.setImageResource(R.drawable.ic_select_gallery);
            binding.mainBottom.ivAlbum.setImageResource(R.drawable.ic_unselect_album);
            binding.mainBottom.ivVideo.setImageResource(R.drawable.ic_unselect_video);
            binding.mainBottom.ivFav.setImageResource(R.drawable.ic_unselect_favourite);
        });

        findViewById(R.id.ll_Media).setOnClickListener(v -> {
            closeDrawer(drawerLayout);
            startActivity(new Intent(MainActivity.this, MediaColorActivity.class));
        });

        findViewById(R.id.ll_loaction).setOnClickListener(v -> {
            closeDrawer(drawerLayout);
            startActivity(new Intent(MainActivity.this, LocationImageActivity.class));
        });

        findViewById(R.id.ll_Private).setOnClickListener(view -> {
            startActivity(new Intent(MainActivity.this, PrivateActivity.class));
        });
        findViewById(R.id.ll_duplicate).setOnClickListener(v -> {
            closeDrawer(drawerLayout);
            startActivity(new Intent(MainActivity.this, DuplicateImageActivity.class));
        });

        findViewById(R.id.ll_lang).setOnClickListener(v -> {
            closeDrawer(drawerLayout);
            startActivity(new Intent(MainActivity.this, LanguageDashboardActivity.class));
        });

        findViewById(R.id.ll_share).setOnClickListener(v -> {
            closeDrawer(drawerLayout);
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, "Hey check out my app at: https://play.google.com/store/apps/details?id=" + getPackageName());
            sendIntent.setType("text/plain");
            startActivity(sendIntent);
        });

        findViewById(R.id.ll_rate_us).setOnClickListener(v -> {
            closeDrawer(drawerLayout);
            try {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + getPackageName())));
            } catch (ActivityNotFoundException e) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName())));
            }
        });

        findViewById(R.id.ll_rate_us).setOnClickListener(v -> {
            closeDrawer(drawerLayout);
            try {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + getPackageName())));
            } catch (ActivityNotFoundException e) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName())));
            }
        });

        findViewById(R.id.ll_pp).setOnClickListener(v -> {
            closeDrawer(drawerLayout);

        });

        findViewById(R.id.ll_fav).setOnClickListener(v -> {
            closeDrawer(drawerLayout);
            MainActivity.fragclick = 4;

            MainActivity.rl_imageBtn.setVisibility(View.GONE);
            MainActivity.rl_videoBtn.setVisibility(View.GONE);
            binding.mainBottom.ivGallery.setImageResource(R.drawable.ic_unselect_gallery);
            binding.mainBottom.ivAlbum.setImageResource(R.drawable.ic_unselect_album);
            binding.mainBottom.ivVideo.setImageResource(R.drawable.ic_unselect_video);
            binding.mainBottom.ivFav.setImageResource(R.drawable.ic_favourite_select);
            fragmentManager.beginTransaction().hide(activeFragment).show(favouriteFragment).commitAllowingStateLoss();
            fragmentManager.popBackStackImmediate();
            activeFragment = favouriteFragment;
        });

        findViewById(R.id.ll_about_us).setOnClickListener(v -> {
            closeDrawer(drawerLayout);

        });

        tv_photo = findViewById(R.id.photosCount);
        tv_video = findViewById(R.id.videoCount);
        tv_album = findViewById(R.id.albumCount);
        int photoCount = getTotalPhotoCount(this);
        int vCount = getTotalVideoCount(this);
        int albumCount = getAlbumCount(this);
        tv_photo.setText(Integer.toString(photoCount));
        tv_video.setText(Integer.toString(vCount));
        tv_album.setText(Integer.toString(albumCount));
    }

    public static void openDrawer(DrawerLayout drawerLayout) {
        drawerLayout.openDrawer(GravityCompat.START);
    }

    public static void closeDrawer(DrawerLayout drawerLayout) {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        closeDrawer(drawerLayout);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            exitDialog();
        }
    }

    private void exitDialog() {
        final Dialog dialog = new Dialog(this, R.style.ThemeWithCorners1);
        dialog.requestWindowFeature(1);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.setContentView(R.layout.dialog_exit);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        ((TextView) dialog.findViewById(R.id.textDesc)).setText(getString(R.string.set_your_security_lock_for_hide_photos));

        ((TextView) dialog.findViewById(R.id.rl_yes)).setOnClickListener(view -> {
            finishAffinity();
            dialog.dismiss();
        });

        ((TextView) dialog.findViewById(R.id.rl_no)).setOnClickListener(view -> dialog.dismiss());
        dialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == R.id.action_sort) {
            return true;
        }
        return super.onOptionsItemSelected(menuItem);
    }

    public void AddImageFolderDialog() {
        final Dialog dialog = new Dialog(this, R.style.ThemeWithCorners1);
        dialog.requestWindowFeature(1);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.setContentView(R.layout.dg_add_folder);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        ((TextView) dialog.findViewById(R.id.txtTitle)).setText("Add Image Album");

        final EditText editText = (EditText) dialog.findViewById(R.id.edit);

        dialog.findViewById(R.id.rl_yes).setOnClickListener(view -> {
            if (editText.getText().toString().length() > 0) {
                folder_path = AppUtilsClass.rootMainDCIMDir + editText.getText().toString() + File.separator;
                Intent intent = new Intent(MainActivity.this, ImagesActivity.class);
                intent.putExtra(Common.gAlbumPath, folder_path);
                startActivityForResult(intent, 100);
                dialog.dismiss();
            }
        });
        dialog.findViewById(R.id.rl_no).setOnClickListener(view -> dialog.dismiss());

        dialog.show();
    }

    @Override
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i == REQUEST_ACTION_OPEN_DOCUMENT_TREE && i2 == -1) {
            Uri data = intent.getData();
            try {
                if (Build.VERSION.SDK_INT >= 19) {
                    getContentResolver().takePersistableUriPermission(data, FLAG_GRANT_READ_URI_PERMISSION);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            fragment.onActivityResult(i, i2, intent);
        }
    }


    public void AddVideoFolderDialog() {
        final Dialog dialog = new Dialog(this, R.style.ThemeWithCorners1);
        dialog.requestWindowFeature(1);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.setContentView(R.layout.dg_add_folder);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        ((TextView) dialog.findViewById(R.id.txtTitle)).setText("Add Video Album");

        final EditText editText = (EditText) dialog.findViewById(R.id.edit);

        dialog.findViewById(R.id.rl_yes).setOnClickListener(view -> {
            if (editText.getText().toString().length() > 0) {
                folder_path = AppUtilsClass.rootMainDCIMDir + editText.getText().toString() + File.separator;
                Intent intent = new Intent(MainActivity.this, VideosActivity.class);
                intent.putExtra(Common.gAlbumPath, folder_path);
                startActivityForResult(intent, 100);
                dialog.dismiss();
            }
        });
        dialog.findViewById(R.id.rl_no).setOnClickListener(view -> dialog.dismiss());

        dialog.show();
    }

    private void sortingBothDataCallBack() {
        albumSortingListener = new onCallbackReceive();
    }

    public void SortingData() {
        if (fragclick == 0) {
            new SortingFolderDialog(activity, ConstantArrayClass.imageAlbumsList, albumSortingListener).ShowSortingDialogue();
        } else if (fragclick == 1) {
            new SortingFolderDialog(activity, ConstantArrayClass.videoAlbumsList, albumSortingListener).ShowSortingDialogue();
        } else if (fragclick == 3) {
            if (privateclick == 0) {
                int lockStatus = preferenceClass.getInt(Common.gIsLockStatus, 0);
                if (lockStatus == 0) {
                    Toast.makeText(this, "Please Set Security!!", Toast.LENGTH_SHORT).show();
                } else if (lockStatus == 1) {
                    Toast.makeText(this, "Please Enter Passcode!!", Toast.LENGTH_SHORT).show();
                } else if (ImagePrivateFragment.privateImageList.size() >= 1) {
                    new SortingPrivateImageDialog(activity, ImagePrivateFragment.privateImageList, hideImageFolderDataSortingListener).ShowSortingDialogue();
                } else {
                    Toast.makeText(this, "No Photos Found!!", Toast.LENGTH_SHORT).show();
                }
            } else if (privateclick == 1) {
                int lockStatus = preferenceClass.getInt(Common.gIsLockStatus, 0);
                if (lockStatus == 0) {
                    Toast.makeText(this, "Please Set Security!!", Toast.LENGTH_SHORT).show();
                } else if (lockStatus == 1) {
                    Toast.makeText(this, "Please Enter Passcode!!", Toast.LENGTH_SHORT).show();
                } else if (VideoPrivateFragment.privateVideoList.size() >= 1) {
                    new SortingVideoPrivateDialog(activity, VideoPrivateFragment.privateVideoList, sortingHideVideoCallBack).ShowSortingDialogue();
                } else {
                    Toast.makeText(this, "No Videos Found!!", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    public class onCallbackReceive implements AlbumSortingListener {
        onCallbackReceive() {
        }

        @Override
        public void Sorting(ArrayList<FolderModel> arrayList) {
            if (fragclick == 0) {
                imageFolderAdapter.notifyDataSetChanged();
                Toast.makeText(MainActivity.this, "Image Sorting Done", Toast.LENGTH_SHORT).show();
            } else if (fragclick == 1) {
                VideoFragment.videoFolderAdapter.notifyDataSetChanged();
                Toast.makeText(MainActivity.this, "Video Sorting Done", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void sortingPrivateImageCallBack() {
        hideImageFolderDataSortingListener = new sortListenerHideImageFolderData();
    }

    public class sortListenerHideImageFolderData implements HideImageFolderDataSortingListener {
        sortListenerHideImageFolderData() {
        }

        @Override
        public void Sorting(ArrayList<Object> arrayList) {
            ImagePrivateFragment.privateImageAdapter.notifyDataSetChanged();
            Toast.makeText(MainActivity.this, "Hide Image Sorting Done", Toast.LENGTH_SHORT).show();
        }
    }

    private void sortingPrivateVideoCallBack() {
        sortingHideVideoCallBack = new sortCallBack();
    }

    public class sortCallBack implements HideVideoFolderDataSortingListener {
        sortCallBack() {
        }

        @Override
        public void Sorting(ArrayList<Object> arrayList) {
            VideoPrivateFragment.privateVideoAdapter.notifyDataSetChanged();
            Toast.makeText(MainActivity.this, "Private Video Sorting Done", Toast.LENGTH_SHORT).show();
        }
    }

    private static SharedPreferences getInstance(Context context2) {
        if (mPreferences == null) {
            mPreferences = context2.getApplicationContext().getSharedPreferences("wa_data", 0);
        }
        return mPreferences;
    }

    public static void DeleteCache(Context context2) {
        try {
            DeleteDir(context2.getCacheDir());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean DeleteDir(File file) {
        if (file != null && file.isDirectory()) {
            for (String str : file.list()) {
                if (!DeleteDir(new File(file, str))) {
                    return false;
                }
            }
            return file.delete();
        } else if (file == null || !file.isFile()) {
            return false;
        } else {
            return file.delete();
        }
    }

    public void GetAutoDeleteData() {
        deleteImageList = new ArrayList<>();
        deleteVideoList = new ArrayList<>();
        try {
            deleteImageList = AppUtilsClass.GetDeletedImageFiles();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            deleteVideoList = AppUtilsClass.GetDeletedVideoFiles();
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        if (deleteImageList != null && deleteImageList.size() != 0) {
            OldImageDeleteAfter60Days();
        }
        if (deleteVideoList == null || deleteVideoList.size() == 0) {
            return;
        }
        OldVideoDeleteAfter60Days();
    }

    private void OldImageDeleteAfter60Days() {
        long timeInMillis = Calendar.getInstance().getTimeInMillis();
        for (int i = 0; i < deleteImageList.size(); i++) {
            File file = new File(((DataFileModel) deleteImageList.get(i)).path);
            if (timeInMillis - file.lastModified() > 5184000000L) {
                file.delete();
            }
        }
    }

    private void OldVideoDeleteAfter60Days() {
        long timeInMillis = Calendar.getInstance().getTimeInMillis();
        for (int i = 0; i < deleteVideoList.size(); i++) {
            File file = new File(((DataFileModel) deleteVideoList.get(i)).path);
            if (timeInMillis - file.lastModified() > 5184000000L) {
                file.delete();
            }
        }
    }

}