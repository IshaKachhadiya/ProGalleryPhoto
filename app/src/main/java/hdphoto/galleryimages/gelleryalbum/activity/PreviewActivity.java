package hdphoto.galleryimages.gelleryalbum.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.FileProvider;
import androidx.documentfile.provider.DocumentFile;
import androidx.exifinterface.media.ExifInterface;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.alexvasilkov.gestures.views.GestureImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.iten.tenoku.ad.AdShow;
import com.iten.tenoku.ad.HandleClick.HandleClick;
import com.iten.tenoku.utils.AdUtils;

import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import hdphoto.galleryimages.gelleryalbum.duplicate.LoginPreferenceManager;
import hdphoto.galleryimages.gelleryalbum.fragment.ImagePrivateFragment;
import hdphoto.galleryimages.gelleryalbum.fragment.MomentFragment;
import hdphoto.galleryimages.gelleryalbum.fragment.VideoDeleteFragment;
import hdphoto.galleryimages.gelleryalbum.fragment.VideoPrivateFragment;
import hdphoto.galleryimages.gelleryalbum.adapter.AlbumAdapter;
import hdphoto.galleryimages.gelleryalbum.adapter.ImageFolderAdapter;
import hdphoto.galleryimages.gelleryalbum.adapter.SlidShowEffectAdapter;
import hdphoto.galleryimages.gelleryalbum.adapter.VideoDialogAdapter;
import hdphoto.galleryimages.gelleryalbum.adapter.VideoFolderAdapter;
import hdphoto.galleryimages.gelleryalbum.listeners.SingleListener;
import hdphoto.galleryimages.gelleryalbum.location_media.activity.LocationImageListActivity;
import hdphoto.galleryimages.gelleryalbum.slidechange.InTransformer;
import hdphoto.galleryimages.gelleryalbum.slidechange.BackgroundToForegroundTransformer;
import hdphoto.galleryimages.gelleryalbum.slidechange.FlipHorizontalTransformer;
import hdphoto.galleryimages.gelleryalbum.slidechange.OutTransformer;
import hdphoto.galleryimages.gelleryalbum.slidechange.DefaultTransformer;
import hdphoto.galleryimages.gelleryalbum.slidechange.DepthPageTransformer;
import hdphoto.galleryimages.gelleryalbum.slidechange.DrawerTransformer;
import hdphoto.galleryimages.gelleryalbum.slidechange.FlipVerticalTransformer;
import hdphoto.galleryimages.gelleryalbum.slidechange.AccordionTransformer;
import hdphoto.galleryimages.gelleryalbum.slidechange.ForegroundToBackgroundTransformer;
import hdphoto.galleryimages.gelleryalbum.slidechange.RotateDownTransformer;
import hdphoto.galleryimages.gelleryalbum.slidechange.RotateUpTransformer;
import hdphoto.galleryimages.gelleryalbum.slidechange.ScaleInOutTransformer;
import hdphoto.galleryimages.gelleryalbum.slidechange.StackTransformer;
import hdphoto.galleryimages.gelleryalbum.slidechange.TabletTransformer;
import hdphoto.galleryimages.gelleryalbum.slidechange.ZoomInTransformer;
import hdphoto.galleryimages.gelleryalbum.slidechange.ZoomOutSlideTransformer;
import hdphoto.galleryimages.gelleryalbum.slidechange.ZoomOutTransformer;
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
import hdphoto.galleryimages.gelleryalbum.R;

@SuppressWarnings("all")

public class PreviewActivity extends BseActivity {
    public static String ImgPath = null;
    private static final ArrayList<TransformerItem> TRANSFORM_CLASSES;
    public static ArrayList<Object> allImagesList;
    public static LinearLayout ll_bottomControl;
    public static RelativeLayout rl_videoEditing;
    public static ViewPager viewPager;
    ImageFolderAdapter imageFolderAdapter;
    ImageView iv_back, iv_delete, iv_edit, iv_effect, iv_more_op, iv_wp, iv_share, iv_slideshow;
    //    ImageView img_brightness;
//    ImageView img_brightness_select;
    Dialog insideFileDialog;
    AlbumAdapter albumAdapter;
    Dialog albumDialog;
    GridView gr_albumGridView;
    GridView gr_albumImageGridView;
    ClipData clipData;
    ClipboardManager clipboardManager;
    Cursor cursor;
    BottomSheetDialog dialog;
    Handler handler;
    TextView ll_delete, txt_favo, tv_edit, txt_secure, txt_unsecure;
    LinearLayout ll_trash_layout, ll_delete_trash, ll_details, ll_imgvdo_layout, ll_restore;
    String mediaFileType;
    int posForFileDetail11;
    ProgressDialog progressDeleteImage, progressDeleteVideo, progressLockImage, progressLockVideo;
    ProgressDialog progressPermanentImageDelete;
    ProgressDialog progressPermanentVideoDelete;
    Cursor query;
    RadioGroup radioGroup;
    RelativeLayout rl_op_main;
    View rl_top;
    Timer swipeTimer;
    TextView txt_CopyPath, txt_CopyTo, txt_CurrentImageNo, txt_Delete, txt_Detail, txt_Edit, txt_ImageEffect;
    //    LinearLayout txt_MaxBrightness;
    TextView txt_MoveTo, txt_Share, txt_Rename, txt_Secure;
    //    TextView txt_SetAsWallpaper;
    TextView txt_SlideShow, txt_UnSecure;
    VideoFolderAdapter videoAlbumAdapter;
    VideoDialogAdapter videoDialogAdapter;
    VideoView videoView;
    ViewPagerAdapter viewPagerAdapter;
    public static ArrayList<Object> myImageList = new ArrayList<>();
    public static String FName = "";
    int REQUEST_ID_SET_AS_WALLPAPER = 111;
    String activityName = "null";
    AlertDialog alertDialog1 = null;
    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            unregisterReceiver(broadcastReceiver);
            if (Common.IdentifyActivity.equals("EditImageActivity")) {
                Intent intent2 = new Intent(PreviewActivity.this, EditImageActivity.class);
                intent2.putExtra("imagepath", PreviewActivity.ImgPath + "");
                startActivity(intent2);
                Common.IdentifyActivity = "";
            }
        }
    };
    String bucketId = "null";
    int cur_pos = 0;
    int currentPage = 0;
    int currentPosition = 1;
    int delpos = 0;
    int dialog_count = 0;
    boolean isShowView = false;
    String[] dataParameter = {"_id", "_display_name", Utils.mediaPath, "date_added"};
    int selectedPos = 0;
    public int slideShowDuration = 1000;
    boolean slideShowRunning = false;
    int totalImage = 0;
    String progressTag = "";
    String toastTag = "";
    ArrayList<Object> arrayList = null;
    String substring = "";
    RelativeLayout preview_main;

    static {
        ArrayList<TransformerItem> arrayList = new ArrayList<>();
        TRANSFORM_CLASSES = arrayList;
        arrayList.add(new TransformerItem(DefaultTransformer.class));
        arrayList.add(new TransformerItem(AccordionTransformer.class));
        arrayList.add(new TransformerItem(BackgroundToForegroundTransformer.class));
        arrayList.add(new TransformerItem(InTransformer.class));
        arrayList.add(new TransformerItem(OutTransformer.class));
        arrayList.add(new TransformerItem(DepthPageTransformer.class));
        arrayList.add(new TransformerItem(FlipHorizontalTransformer.class));
        arrayList.add(new TransformerItem(FlipVerticalTransformer.class));
        arrayList.add(new TransformerItem(ForegroundToBackgroundTransformer.class));
        arrayList.add(new TransformerItem(RotateDownTransformer.class));
        arrayList.add(new TransformerItem(RotateUpTransformer.class));
        arrayList.add(new TransformerItem(ScaleInOutTransformer.class));
        arrayList.add(new TransformerItem(StackTransformer.class));
        arrayList.add(new TransformerItem(TabletTransformer.class));
        arrayList.add(new TransformerItem(ZoomInTransformer.class));
        arrayList.add(new TransformerItem(ZoomOutSlideTransformer.class));
        arrayList.add(new TransformerItem(ZoomOutTransformer.class));
        arrayList.add(new TransformerItem(DrawerTransformer.class));
    }

    private int previousSelected;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_preview);

        arrayList = new ArrayList<>();
        MainActivity.oriTag = "";

        if (getIntent() == null) {
            finish();
        }

        clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ImgPath = getIntent().getStringExtra(Common.gImagePath);
        totalImage = getIntent().getIntExtra(Common.gTotalimage, 0);
        currentPosition = getIntent().getIntExtra(Common.gCurrenrtPosition, 0);
        activityName = getIntent().getStringExtra(Common.gActivityname);
        mediaFileType = getIntent().getStringExtra(Common.gMediaType);
        bucketId = getIntent().getStringExtra(Common.gBucketID);
        posForFileDetail11 = currentPosition;

        try {
            substring = ImgPath.substring(ImgPath.lastIndexOf("."));
        } catch (Exception e) {
            e.printStackTrace();
        }

        initView();
        clickListener();

        if (allImagesList == null || allImagesList.isEmpty()) {
            return;
        }


        viewPagerAdapter = new ViewPagerAdapter(this, allImagesList);
        viewPager = findViewById(R.id.container);
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.setCurrentItem(currentPosition);
        currentPosition++;
        txt_CurrentImageNo.setText(currentPosition + "/" + totalImage);

        if (LoginPreferenceManager.getFavData(getApplicationContext(), ((DataFileModel) this.allImagesList.get(this.viewPager.getCurrentItem())).path)) {
            txt_favo.setText(getString(R.string.favourite));
        } else {
            txt_favo.setText(getString(R.string.unfavorite));
        }

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrollStateChanged(int i2) {
            }

            @Override
            public void onPageScrolled(int i2, float f, int i3) {
                String str2 = "";
                selectedPos = i2;
                currentPosition = i2;
                cur_pos = i2 + 1;
                posForFileDetail11 = currentPosition;
                try {
                    ImgPath = ((DataFileModel) allImagesList.get(currentPosition)).path;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                File file = new File(((DataFileModel) allImagesList.get(currentPosition)).path);
                try {
                    if (file.exists()) {
                        String name = file.getName();
                        str2 = name.substring(name.lastIndexOf("."));
                    }
                } catch (Exception unused) {
                    unused.printStackTrace();
                }

                String str3 = (str2.endsWith(".jpg") || str2.endsWith(".JPG") || str2.endsWith(".jpeg") || str2.endsWith(".JPEG") || str2.endsWith(".png") || str2.endsWith(".PNG") || str2.endsWith(".gif") || str2.endsWith(".GIF")) ? "1" : ExifInterface.GPS_MEASUREMENT_3D;
                if (((DataFileModel) PreviewActivity.allImagesList.get(currentPosition)).mediaType != null) {
                    str3 = ((DataFileModel) PreviewActivity.allImagesList.get(currentPosition)).getMediaType();
                }
                mediaFileType = str3;
            }

            @Override
            public void onPageSelected(int i2) {
                String str2 = "";
                selectedPos = i2;
                int i3 = i2 + 1;
                currentPosition = i2;
                cur_pos = i3;
                posForFileDetail11 = currentPosition;
                ImgPath = ((DataFileModel) allImagesList.get(currentPosition)).path;
                File file = new File(((DataFileModel) allImagesList.get(currentPosition)).path);
                try {
                    if (file.exists()) {
                        String name = file.getName();
                        str2 = name.substring(name.lastIndexOf("."));
                    }
                } catch (Exception unused) {
                    unused.printStackTrace();
                }
                String str3 = (str2.endsWith(".jpg") || str2.endsWith(".JPG") || str2.endsWith(".jpeg") || str2.endsWith(".JPEG") || str2.endsWith(".png") || str2.endsWith(".PNG") || str2.endsWith(".gif") || str2.endsWith(".GIF")) ? "1" : ExifInterface.GPS_MEASUREMENT_3D;
                if (((DataFileModel) allImagesList.get(currentPosition)).mediaType != null) {
                    str3 = ((DataFileModel) allImagesList.get(currentPosition)).getMediaType();
                }
                mediaFileType = str3;
                txt_CurrentImageNo.setText(i3 + "/" + PreviewActivity.allImagesList.size());
                if (mediaFileType.equals("1")) {
                    tv_edit.setVisibility(View.VISIBLE);
                    rl_videoEditing.setVisibility(View.GONE);
                    txt_SlideShow.setVisibility(View.VISIBLE);
                    txt_ImageEffect.setVisibility(View.VISIBLE);
                    if (activityName.equals("ImagePrivateFragment")) {
//                        txt_SetAsWallpaper.setVisibility(View.GONE);
                    } else {
//                        txt_SetAsWallpaper.setVisibility(View.VISIBLE);
                    }
                } else if (mediaFileType.equals(ExifInterface.GPS_MEASUREMENT_3D)) {
                    rl_videoEditing.setVisibility(View.VISIBLE);
                    tv_edit.setVisibility(View.GONE);
                    txt_SlideShow.setVisibility(View.GONE);
                    txt_ImageEffect.setVisibility(View.GONE);
//                    txt_SetAsWallpaper.setVisibility(View.GONE);
                }

                try {
                    if (PreviewActivity.this.previousSelected != -1) {
                        ((DataFileModel) PreviewActivity.this.allImagesList.get(PreviewActivity.this.previousSelected)).setSelected(false);
                        PreviewActivity.this.previousSelected = i2;
                        ((DataFileModel) PreviewActivity.this.allImagesList.get(i2)).setSelected(true);
                        if (LoginPreferenceManager.getFavData(PreviewActivity.this.getApplicationContext(), ((DataFileModel) PreviewActivity.this.allImagesList.get(PreviewActivity.this.viewPager.getCurrentItem())).getPath())) {
                            txt_favo.setText(getString(R.string.favourite));
                        } else {
                            txt_favo.setText(getString(R.string.unfavorite));
                        }
                    } else {
                        PreviewActivity.this.previousSelected = i2;
                        ((DataFileModel) PreviewActivity.this.allImagesList.get(i2)).setSelected(true);
                        if (LoginPreferenceManager.getFavData(PreviewActivity.this.getApplicationContext(), ((DataFileModel) PreviewActivity.this.allImagesList.get(PreviewActivity.this.viewPager.getCurrentItem())).getPath())) {
                            txt_favo.setText(getString(R.string.favourite));
                        } else {
                            txt_favo.setText(getString(R.string.unfavorite));

                        }
                    }
                } catch (Exception unused) {

                }
            }
        });

        findViewById(R.id.rel_play).setOnClickListener(view -> {
            if (activityName.equals("VideoPrivateFragment")) {
                Intent intent = new Intent(PreviewActivity.this, VideoPlayerActivity.class);
                intent.putExtra("medium", ImgPath);
                intent.putExtra("videoPos", currentPosition);
                intent.putExtra("fromActivity", "VideoPrivateFragment");
                intent.addCategory("android.intent.category.DEFAULT");
                startActivity(intent);
                return;
            }
            Intent intent = new Intent(PreviewActivity.this, VideoPlayerActivity.class);
            intent.putExtra("medium", ImgPath);
            intent.putExtra("videoPos", currentPosition);
            intent.putExtra("fromActivity", "null");
            intent.addCategory("android.intent.category.DEFAULT");
            startActivity(intent);
        });

        viewPagerAdapter.setItemClickCallback((obj, obj2, obj3, view) -> {
            if (slideShowRunning) {
                isShowView = false;
                slideShowRunning = false;
                ShowControl();
                if (swipeTimer != null) {
                    swipeTimer.cancel();
                    swipeTimer.purge();
                    swipeTimer = null;
                    return;
                }
                return;
            }
            hideShowController();
        });
    }

    public void hideShowController() {
        if (ll_bottomControl.getVisibility() == View.VISIBLE) {
            ll_bottomControl.setVisibility(View.INVISIBLE);
            rl_top.setVisibility(View.INVISIBLE);
            return;
        }
        ll_bottomControl.setVisibility(View.VISIBLE);
        rl_top.setVisibility(View.VISIBLE);
    }


    public static final class TransformerItem {
        final Class<? extends ViewPager.PageTransformer> clazz;
        final String title;

        public TransformerItem(Class<? extends ViewPager.PageTransformer> cls) {
            this.clazz = cls;
            this.title = cls.getSimpleName();
        }

        public String toString() {
            return title;
        }
    }

    private void initView() {
        iv_back = findViewById(R.id.btnBack);
        ll_trash_layout = findViewById(R.id.ll_trash_layout);
        ll_imgvdo_layout = findViewById(R.id.ll_imgvdo_layout);
        ll_restore = findViewById(R.id.ll_restore);
        ll_delete_trash = findViewById(R.id.ll_delete_trash);
        txt_CurrentImageNo = findViewById(R.id.txtCurrentImageNo);
        iv_share = findViewById(R.id.btnShareImage);
        videoView = findViewById(R.id.videoview);
        iv_delete = findViewById(R.id.btnDeleteImage);
        iv_effect = findViewById(R.id.btnImageEffect);
        iv_wp = findViewById(R.id.btnSetWallpaper);
        iv_slideshow = findViewById(R.id.btnSlideShow);
        iv_edit = findViewById(R.id.btnEditImnage);
        iv_more_op = findViewById(R.id.btnmoreoption);
        ll_bottomControl = findViewById(R.id.linearLayout2);
        rl_top = findViewById(R.id.rl_top);
        rl_videoEditing = findViewById(R.id.rl_videoEditing);
        rl_op_main = findViewById(R.id.rel_op_main);
        txt_SlideShow = findViewById(R.id.txt_SlideShow);
        txt_ImageEffect = findViewById(R.id.txt_ImageEffect);
        txt_Detail = findViewById(R.id.txt_detail);
        txt_Delete = findViewById(R.id.txt_del);
        txt_Edit = findViewById(R.id.txt_edit);
        txt_Rename = findViewById(R.id.txt_rename);
        txt_Share = findViewById(R.id.txt_share);
        txt_CopyTo = findViewById(R.id.txt_copy_to);
        txt_MoveTo = findViewById(R.id.txt_move_to);
        txt_Secure = findViewById(R.id.txt_secure);
        txt_UnSecure = findViewById(R.id.txt_unsecure);
        txt_CopyPath = findViewById(R.id.txt_Copy_Path);
//        txt_SetAsWallpaper = findViewById(R.id.txt_Set_as_wallpaper);
//        txt_MaxBrightness = findViewById(R.id.txt_max_brightness);
//        img_brightness = findViewById(R.id.img_brightness);
//        img_brightness_select = findViewById(R.id.img_brightness_select);
        txt_secure = findViewById(R.id.ll_secure);
        tv_edit = findViewById(R.id.ll_edit);
        ll_delete = findViewById(R.id.ll_delete);
        ll_details = findViewById(R.id.ll_details);
        txt_unsecure = findViewById(R.id.ll_unsecure);
        preview_main = findViewById(R.id.preview_main);
        txt_favo = findViewById(R.id.txt_favorit);

        if (substring.endsWith(".jpg") || substring.endsWith(".JPG") || substring.endsWith(".jpeg") || substring.endsWith(".JPEG") || substring.endsWith(".png") || substring.endsWith(".PNG") || substring.endsWith(".gif") || substring.endsWith(".GIF")) {
            tv_edit.setVisibility(View.VISIBLE);
            txt_SlideShow.setVisibility(View.VISIBLE);
            txt_ImageEffect.setVisibility(View.VISIBLE);
            if (activityName.equals("ImagePrivateFragment")) {
//                txt_SetAsWallpaper.setVisibility(View.GONE);
            } else {
//                txt_SetAsWallpaper.setVisibility(View.VISIBLE);
            }
            if (activityName.equals("ImageDeleteFragment")) {
                ll_trash_layout.setVisibility(View.VISIBLE);
                ll_imgvdo_layout.setVisibility(View.GONE);
            } else {
                ll_trash_layout.setVisibility(View.GONE);
                ll_imgvdo_layout.setVisibility(View.VISIBLE);
            }
        } else {
            tv_edit.setVisibility(View.GONE);
            rl_videoEditing.setVisibility(View.VISIBLE);
            txt_SlideShow.setVisibility(View.GONE);
            txt_ImageEffect.setVisibility(View.GONE);
//            txt_SetAsWallpaper.setVisibility(View.GONE);
            if (activityName.equals("VideoDeleteFragment")) {
                ll_trash_layout.setVisibility(View.VISIBLE);
                ll_imgvdo_layout.setVisibility(View.GONE);
            } else {
                ll_trash_layout.setVisibility(View.GONE);
                ll_imgvdo_layout.setVisibility(View.VISIBLE);
            }
        }
        if (activityName.equals("InnerImageSelectActivity")) {
            allImagesList = ImageSelectActivity.sendImgList;
            this.allImagesList = ImageSelectActivity.PassDataAlbum;
        } else if (activityName.equals("MomentFragment")) {
            allImagesList = new ArrayList<>();
            for (int i = 0; i < ConstantArrayClass.albumsList.size(); i++) {
                if (ConstantArrayClass.albumsList.get(i) instanceof DataFileModel) {
                    allImagesList.add((DataFileModel) ConstantArrayClass.albumsList.get(i));
                    arrayList.add((DataFileModel) ConstantArrayClass.albumsList.get(i));

                    if (((DataFileModel) allImagesList.get(arrayList.size() - 1)).path.equals(ImgPath)) {
                        currentPosition = allImagesList.size() - 1;
                    }
                }
            }
        } else if (activityName.equals("LocationImageListActivity")) {
            allImagesList = LocationImageListActivity.pictureData;

        } else if (activityName.equals("InnerVideoSelectActivity")) {
            allImagesList = VideoSelectActivity.sendImgList;
        } else if (activityName.equals("ImagePrivateFragment")) {
            txt_CopyTo.setVisibility(View.GONE);
            txt_MoveTo.setVisibility(View.GONE);
            txt_Secure.setVisibility(View.GONE);
            txt_secure.setVisibility(View.GONE);
            txt_Rename.setVisibility(View.GONE);
            txt_UnSecure.setVisibility(View.GONE);
            txt_unsecure.setVisibility(View.VISIBLE);
            allImagesList = ImagePrivateFragment.sendPrivateImageList;
        } else if (activityName.equals("VideoPrivateFragment")) {
            txt_CopyTo.setVisibility(View.GONE);
            txt_MoveTo.setVisibility(View.GONE);
            txt_Secure.setVisibility(View.GONE);
            txt_secure.setVisibility(View.GONE);
            txt_Rename.setVisibility(View.GONE);
            txt_UnSecure.setVisibility(View.GONE);
            txt_unsecure.setVisibility(View.VISIBLE);
            allImagesList = VideoPrivateFragment.sendPrivateVideoList;
        } else if (activityName.equals("ImageDeleteFragment")) {
            txt_CopyTo.setVisibility(View.GONE);
            txt_MoveTo.setVisibility(View.GONE);
            txt_Secure.setVisibility(View.GONE);
            txt_secure.setVisibility(View.GONE);
            txt_Rename.setVisibility(View.GONE);
            txt_UnSecure.setVisibility(View.GONE);
            txt_unsecure.setVisibility(View.VISIBLE);
//            allImagesList = ImageDeleteFragment.sendDeleteImageList;
        } else if (activityName.equals("VideoDeleteFragment")) {
            txt_CopyTo.setVisibility(View.GONE);
            txt_MoveTo.setVisibility(View.GONE);
            txt_Secure.setVisibility(View.GONE);
            txt_secure.setVisibility(View.GONE);
            txt_Rename.setVisibility(View.GONE);
            txt_UnSecure.setVisibility(View.GONE);
            txt_unsecure.setVisibility(View.VISIBLE);
            allImagesList = VideoDeleteFragment.sendDeleteVideoList;
        }
    }

    private void clickListener() {
        iv_back.setOnClickListener(view -> onBackPressed());

        ll_restore.setOnClickListener(view -> {
            rl_op_main.setVisibility(View.GONE);
            RestoreDialog();
        });

        ll_delete_trash.setOnClickListener(view -> {
            rl_op_main.setVisibility(View.GONE);
            DeleteDialog(ImgPath);
        });

        txt_secure.setOnClickListener(view -> {
            rl_op_main.setVisibility(View.GONE);
            if (mediaFileType.equals("1")) {
                if (preferenceClass.getInt(Common.gIsLockStatus, 0) == 0) {
                    Common.checkPreview = true;
                    ConstantArrayClass.firstTimeLockDataArray.add((DataFileModel) allImagesList.get(currentPosition));
                    SecurityDialog();
                    return;
                }
                LockImagesDialog(ImgPath);
            } else if (mediaFileType.equals(ExifInterface.GPS_MEASUREMENT_3D)) {
                if (preferenceClass.getInt(Common.gIsLockStatus, 0) == 0) {
                    Common.checkPreview = true;
                    ConstantArrayClass.firstTimeLockDataArray.add((DataFileModel) allImagesList.get(currentPosition));
                    SecurityDialog();
                    return;
                }
                LockVideosDialog(ImgPath);
            }
        });

        tv_edit.setOnClickListener(view -> {
            rl_op_main.setVisibility(View.GONE);
            Intent intent = new Intent(PreviewActivity.this, EditImageActivity.class);
            StringBuilder sb = new StringBuilder();
            sb.append(ImgPath);
            sb.append("");
            intent.putExtra("imagepath", sb.toString());
            startActivity(intent);
            Common.IdentifyActivity = "";

        });

        txt_favo.setOnClickListener(v -> {
            rl_op_main.setVisibility(View.GONE);
            try {
                if (LoginPreferenceManager.getFavData(getApplicationContext(), ((DataFileModel) allImagesList.get(viewPager.getCurrentItem())).getPath())) {
                    LoginPreferenceManager.saveFavData(getApplicationContext(), ((DataFileModel) allImagesList.get(viewPager.getCurrentItem())).getPath(), false);

                    Toast.makeText(this, R.string.unfavourite_successfully, 0).show();
                    txt_favo.setText(getString(R.string.unfavorite));

                } else {
                    LoginPreferenceManager.saveFavData(getApplicationContext(), ((DataFileModel) allImagesList.get(viewPager.getCurrentItem())).getPath(), true);
                    Toast.makeText(PreviewActivity.this, R.string.favourite_successfully, 0).show();
                    txt_favo.setText(getString(R.string.favourite));

                }
                this.imageFolderAdapter.notifyDataSetChanged();

            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        ll_delete.setOnClickListener(view -> {
            rl_op_main.setVisibility(View.GONE);
            DeleteDialog(ImgPath);
        });

        ll_details.setOnClickListener(view -> {
            rl_op_main.setVisibility(View.GONE);
            DetailDialog(ImgPath);
        });

        txt_unsecure.setOnClickListener(view -> {
            rl_op_main.setVisibility(View.GONE);
            UnlockDialog();
        });

        txt_SlideShow.setOnClickListener(view -> {
            rl_op_main.setVisibility(View.GONE);
            SlidShowTimerDialog();
        });

        txt_ImageEffect.setOnClickListener(view -> {
            rl_op_main.setVisibility(View.GONE);
            SlidEffectDialog();
        });

        iv_share.setOnClickListener(view -> SingleShare());

        iv_delete.setOnClickListener(view -> deleteImage(ImgPath));

        iv_effect.setOnClickListener(view -> {
            rl_op_main.setVisibility(View.GONE);
            SlidEffectDialog();
        });

        iv_wp.setOnClickListener(view -> {
            Intent intent = new Intent("android.intent.action.ATTACH_DATA");
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
            intent.setDataAndType(FileProvider.getUriForFile(activity, getPackageName() + ".fileprovider", new File(ImgPath)), getMimeType(ImgPath));
            intent.putExtra("SET_LOCKSCREEN_WALLPAPER", true);
            activity.startActivityForResult(Intent.createChooser(intent, getString(R.string.set_as)), REQUEST_ID_SET_AS_WALLPAPER);
        });

        iv_slideshow.setOnClickListener(view -> {
            rl_op_main.setVisibility(View.GONE);
            SlidShowTimerDialog();
        });

        iv_more_op.setOnClickListener(view -> rl_op_main.setVisibility(View.VISIBLE));

        rl_op_main.setOnClickListener(view -> rl_op_main.setVisibility(View.GONE));

        txt_Detail.setOnClickListener(view -> {
            rl_op_main.setVisibility(View.GONE);
            DetailDialog(ImgPath);
        });

        txt_Delete.setOnClickListener(view -> {
            rl_op_main.setVisibility(View.GONE);
            DeleteDialog(ImgPath);
        });

        txt_Edit.setOnClickListener(view -> {
            rl_op_main.setVisibility(View.GONE);
            Intent intent = new Intent(PreviewActivity.this, EditImageActivity.class);
            intent.putExtra("imagepath", ImgPath + "");
            startActivity(intent);
            Common.IdentifyActivity = "";
        });

        txt_Rename.setVisibility(View.VISIBLE);

        txt_Rename.setOnClickListener(view -> {
            rl_op_main.setVisibility(View.GONE);
            if (activityName.equals("ImagePrivateFragment")) {
                return;
            }
            RenameFileDialog();
        });

        txt_Share.setOnClickListener(view -> {
            rl_op_main.setVisibility(View.GONE);
            SingleShare();
        });

        txt_CopyTo.setOnClickListener(view -> {
            rl_op_main.setVisibility(View.GONE);
            if (mediaFileType.equals("1")) {
                GetImageAlbumDialog(false);
            } else if (mediaFileType.equals(ExifInterface.GPS_MEASUREMENT_3D)) {
                GetVideoAlbumDialog(false);
            }

        });

        txt_MoveTo.setOnClickListener(view -> {
            rl_op_main.setVisibility(View.GONE);
            if (mediaFileType.equals("1")) {
                GetImageAlbumDialog(true);
            } else if (mediaFileType.equals(ExifInterface.GPS_MEASUREMENT_3D)) {
                GetVideoAlbumDialog(true);
            }

        });

        txt_Secure.setOnClickListener(view -> {
            rl_op_main.setVisibility(View.GONE);
            if (mediaFileType.equals("1")) {
                if (preferenceClass.getInt(Common.gIsLockStatus, 0) == 0) {
                    SecurityDialog();
                } else {
                    LockImagesDialog(ImgPath);
                }
            } else if (mediaFileType.equals(ExifInterface.GPS_MEASUREMENT_3D)) {
                if (preferenceClass.getInt(Common.gIsLockStatus, 0) == 0) {
                    SecurityDialog();
                } else {
                    LockVideosDialog(ImgPath);
                }
            }
        });

        txt_UnSecure.setOnClickListener(view -> {
            rl_op_main.setVisibility(View.GONE);
            UnlockDialog();
        });

        txt_CopyPath.setOnClickListener(view -> {
            rl_op_main.setVisibility(View.GONE);
            ClipData newPlainText = ClipData.newPlainText("text", ImgPath);
            clipData = newPlainText;
            clipboardManager.setPrimaryClip(newPlainText);
            rl_op_main.setVisibility(View.GONE);
            Toast.makeText(PreviewActivity.this, getString(R.string.path_copied_successfully), Toast.LENGTH_SHORT).show();
        });

//        txt_SetAsWallpaper.setOnClickListener(view -> {
//            rl_op_main.setVisibility(View.GONE);
//            Intent intent = new Intent("android.intent.action.ATTACH_DATA");
//            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
//            intent.setDataAndType(FileProvider.getUriForFile(activity, getPackageName() + ".fileprovider", new File(ImgPath)), getMimeType(ImgPath));
//            intent.putExtra("SET_LOCKSCREEN_WALLPAPER", true);
//            activity.startActivityForResult(Intent.createChooser(intent, getString(R.string.set_as)), REQUEST_ID_SET_AS_WALLPAPER);
//        });

        /*txt_MaxBrightness.setOnClickListener(view -> {
            if (img_brightness.getVisibility() == View.VISIBLE) {
                getWindow().addFlags(128);
                WindowManager.LayoutParams attributes = getWindow().getAttributes();
                attributes.screenBrightness = 1.0f;
                getWindow().setAttributes(attributes);
                img_brightness.setVisibility(View.GONE);
                img_brightness_select.setVisibility(View.VISIBLE);
                rl_op_main.setVisibility(View.GONE);
                return;
            }
            getWindow().addFlags(128);
            WindowManager.LayoutParams attributes2 = getWindow().getAttributes();
            attributes2.screenBrightness = -1.0f;
            getWindow().setAttributes(attributes2);
            img_brightness_select.setVisibility(View.GONE);
            img_brightness.setVisibility(View.VISIBLE);
            rl_op_main.setVisibility(View.GONE);
        });*/
    }

    public static String getMimeType(String str) {
        String fileExtensionFromUrl = MimeTypeMap.getFileExtensionFromUrl(str);
        if (fileExtensionFromUrl != null) {
            return MimeTypeMap.getSingleton().getMimeTypeFromExtension(fileExtensionFromUrl);
        }
        return null;
    }

    private void AlbumOrientation(int i) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(displayMetrics);
        if (albumAdapter != null) {
            int screenWidthPixels = displayMetrics.widthPixels;
            albumAdapter.setLayoutParams(i == 1 ? screenWidthPixels / 3 : screenWidthPixels / 9);
        }
        if (videoDialogAdapter != null) {
            int screenWidthPixels = displayMetrics.widthPixels;
            videoDialogAdapter.setLayoutParams(i == 1 ? screenWidthPixels / 3 : screenWidthPixels / 9);
        }
        gr_albumGridView.setNumColumns(i != 1 ? 9 : 3);
    }

    private void ImageOrientation(int i) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(displayMetrics);
        if (imageFolderAdapter != null) {
            int screenWidthPixels = displayMetrics.widthPixels;
            imageFolderAdapter.setLayoutParams(i == 1 ? screenWidthPixels / 4 : screenWidthPixels / 16);
        }
        if (videoAlbumAdapter != null) {
            int screenWidthPixels = displayMetrics.widthPixels;
            videoAlbumAdapter.setLayoutParams(i == 1 ? screenWidthPixels / 4 : screenWidthPixels / 16);
        }
        gr_albumImageGridView.setNumColumns(i != 1 ? 16 : 4);
    }

    public void UnlockDialog() {
        final Dialog dialog = new Dialog(this, R.style.ThemeWithCorners1);
        dialog.requestWindowFeature(1);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.setContentView(R.layout.dialog_unlock_image);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        if (mediaFileType.equals("1")) {
            ((TextView) dialog.findViewById(R.id.txtTitle)).setText(getString(R.string.unlock_image));
            ((TextView) dialog.findViewById(R.id.textDesc)).setText(getString(R.string.are_you_sure_you_want_to_unlock_image));
        } else if (mediaFileType.equals(ExifInterface.GPS_MEASUREMENT_3D)) {
            ((TextView) dialog.findViewById(R.id.txtTitle)).setText(getString(R.string.unlock_video));
            ((TextView) dialog.findViewById(R.id.textDesc)).setText(getString(R.string.are_you_sure_you_want_to_unlock_video));
        }
        ((TextView) dialog.findViewById(R.id.rl_yes)).setOnClickListener(view -> {
            if (mediaFileType.equals("1")) {
                new UnLockImageExecute(ImgPath).execute(new Void[0]);
            } else if (mediaFileType.equals(ExifInterface.GPS_MEASUREMENT_3D)) {
                new UnLockVideoExecute(ImgPath).execute(new Void[0]);
            }
            dialog.dismiss();
        });
        ((TextView) dialog.findViewById(R.id.rl_no)).setOnClickListener(view -> dialog.dismiss());
        dialog.show();
    }

    public class UnLockImageExecute extends AsyncTask<Void, Void, Void> {
        String imagePath;
        ProgressDialog progressDialog;
        boolean isRecover = true;
        ArrayList<Object> imageArray = new ArrayList<>();

        public UnLockImageExecute(String str) {
            progressDialog = new ProgressDialog(PreviewActivity.this);
            this.imagePath = str;
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
            ArrayList arrayList = new ArrayList();
            ArrayList<String> listString = preferenceClass.getListString(Common.gOldPath);
            File file = new File(imagePath);
            String name = file.getName();
            File file2 = new File(FolderPath.SDCARD_PATH_IMAGE_LOCK_BACKUP);
            if (!file2.exists()) {
                file2.mkdirs();
            }
            File file3 = new File(file2 + File.separator + name);
            String parent = file3.getParent();
            new MediaScanners(GalleryAppClass.getInstance(), file3);
            try {
                FileUtils.CopyMoveFile(file, file3);
                AppUtilsClass.insertUri(PreviewActivity.this, file3);
                long j = ((DataFileModel) allImagesList.get(currentPosition)).id;
                String str = ((DataFileModel) allImagesList.get(currentPosition)).name;
                String str2 = ((DataFileModel) allImagesList.get(currentPosition)).path;
                listString.add(str2);
                arrayList.add(new DataFileModel(j, str, str2, file3.getPath(), parent, false));
                File file4 = new File(imagePath);
                file4.delete();
                ContentResolver contentResolver = getContentResolver();
                Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                contentResolver.delete(uri, "_data='" + file4.getPath() + "'", null);
                preferenceClass.putListString(Common.gOldPath, listString);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        public void onPostExecute(Void r6) {
            super.onPostExecute(r6);
            int i = currentPosition;
            progressDialog.dismiss();
            if (i == allImagesList.size()) {
                currentPosition--;
                allImagesList.remove(currentPosition);
                i = 0;
            } else {
                allImagesList.remove(currentPosition);
            }
            viewPagerAdapter = new ViewPagerAdapter(PreviewActivity.this, allImagesList);
            viewPager.setAdapter(viewPagerAdapter);
            viewPager.setCurrentItem(i);
            new Handler().postDelayed(() -> AppUtilsClass.RefreshMoment(PreviewActivity.this), 500L);
            if ((activityName.equals("ImagePrivateFragment") || activityName.equals("VideoPrivateFragment") || activityName.equals("ImageDeleteFragment") || activityName.equals("VideoDeleteFragment")) && allImagesList.size() == 0) {
                setResult(-1, getIntent());
                finish();
            }
            Toast.makeText(PreviewActivity.this, getString(R.string.files_are_unlocked_successfully), Toast.LENGTH_SHORT).show();
        }
    }


    public class UnLockVideoExecute extends AsyncTask<Void, Void, Void> {
        ProgressDialog progressDialog;
        String videoPath;
        boolean isRecover = true;
        ArrayList<Object> imageArray = new ArrayList<>();

        public UnLockVideoExecute(String str) {
            progressDialog = new ProgressDialog(PreviewActivity.this);
            this.videoPath = str;
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
            ArrayList arrayList = new ArrayList();
            ArrayList<String> listString = preferenceClass.getListString(Common.gOldPath);
            File file = new File(videoPath);
            String name = file.getName();
            File file2 = new File(FolderPath.SDCARD_PATH_VIDEO_LOCK_BACKUP);
            if (!file2.exists()) {
                file2.mkdirs();
            }
            File file3 = new File(file2 + File.separator + name);
            String parent = file3.getParent();
            new MediaScanners(GalleryAppClass.getInstance(), file3);
            try {
                FileUtils.CopyMoveFile(file, file3);
                AppUtilsClass.insertUri(PreviewActivity.this, file3);
                long j = ((DataFileModel) allImagesList.get(currentPosition)).id;
                String str = ((DataFileModel) allImagesList.get(currentPosition)).name;
                String str2 = ((DataFileModel) allImagesList.get(currentPosition)).path;
                listString.add(str2);
                arrayList.add(new DataFileModel(j, str, str2, file3.getPath(), parent, false));
                File file4 = new File(videoPath);
                file4.delete();
                ContentResolver contentResolver = getContentResolver();
                Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                contentResolver.delete(uri, "_data='" + file4.getPath() + "'", null);
                preferenceClass.putListString(Common.gOldPath, listString);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        public void onPostExecute(Void r6) {
            super.onPostExecute(r6);
            progressDialog.dismiss();
            int i = currentPosition;
            if (i == allImagesList.size()) {
                currentPosition--;
                allImagesList.remove(currentPosition);
                i = 0;
            } else {
                allImagesList.remove(currentPosition);
            }
            viewPagerAdapter = new ViewPagerAdapter(PreviewActivity.this, allImagesList);
            viewPager.setAdapter(viewPagerAdapter);
            viewPager.setCurrentItem(i);
            new Handler().postDelayed(() -> AppUtilsClass.RefreshMoment(PreviewActivity.this), 500L);
            if ((activityName.equals("ImagePrivateFragment") || activityName.equals("VideoPrivateFragment") || activityName.equals("ImageDeleteFragment") || activityName.equals("VideoDeleteFragment")) && allImagesList.size() == 0) {
                setResult(-1, getIntent());
                finish();
            }
            Toast.makeText(PreviewActivity.this, getString(R.string.files_are_restored_successfully), Toast.LENGTH_SHORT).show();
        }
    }

    public void DeleteDialog(final String str) {
        final Dialog dialog = new Dialog(this, R.style.ThemeWithCorners1);
        dialog.requestWindowFeature(1);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.setContentView(R.layout.dg_delete_file);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        if (mediaFileType.equals("1")) {
            ((TextView) dialog.findViewById(R.id.txtTitle)).setText(getString(R.string.delete_image));
            ((TextView) dialog.findViewById(R.id.textDesc)).setText(getString(R.string.are_you_sure_you_want_to_restore_image));
        } else if (mediaFileType.equals(ExifInterface.GPS_MEASUREMENT_3D)) {
            ((TextView) dialog.findViewById(R.id.txtTitle)).setText(getString(R.string.delete_video));
            ((TextView) dialog.findViewById(R.id.textDesc)).setText(getString(R.string.are_you_sure_you_want_to_restore_video));
        }
        ((TextView) dialog.findViewById(R.id.rl_yes)).setOnClickListener(view -> {
            if (activityName.equals("ImageDeleteFragment") || activityName.equals("VideoDeleteFragment") || activityName.equals("ImagePrivateFragment") || activityName.equals("VideoPrivateFragment")) {
                new DeleteBGExecute(str).execute(new Void[0]);
            } else if (mediaFileType.equals("1")) {
                progressTag = "FromDeleteImage";
                new DeleteTrashImageExecute(str).execute(new Void[0]);
            } else if (mediaFileType.equals(ExifInterface.GPS_MEASUREMENT_3D)) {
                progressTag = "FromDeleteVideo";
                new DeleteTrashVideoExecute(str).execute(new Void[0]);
            }
            dialog.dismiss();
        });
        ((TextView) dialog.findViewById(R.id.rl_no)).setOnClickListener(view -> dialog.dismiss());
        dialog.show();
    }


    public class DeleteBGExecute extends AsyncTask<Void, Void, Void> {
        String delImagePath;
        ProgressDialog progressDialog;

        public DeleteBGExecute(String str) {
            this.delImagePath = "";
            progressDialog = new ProgressDialog(PreviewActivity.this);
            this.delImagePath = str;
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
            File file = new File(delImagePath);
            try {
                file.delete();
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (mediaFileType.equals("1")) {
                ContentResolver contentResolver = getContentResolver();
                Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                try {
                    contentResolver.delete(uri, "_data='" + file.getPath() + "'", null);
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            } else if (mediaFileType.equals(ExifInterface.GPS_MEASUREMENT_3D)) {
                ContentResolver contentResolver2 = getContentResolver();
                Uri uri2 = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                try {
                    contentResolver2.delete(uri2, "_data='" + file.getPath() + "'", null);
                } catch (Exception e3) {
                    e3.printStackTrace();
                }
            }
            return null;
        }

        @Override
        public void onPostExecute(Void r6) {
            super.onPostExecute(r6);
            int i = currentPosition;
            if (i == allImagesList.size()) {
                currentPosition--;
                allImagesList.remove(currentPosition);
                i = 0;
            } else {
                allImagesList.remove(currentPosition);
            }
            viewPagerAdapter = new ViewPagerAdapter(activity, allImagesList);
            viewPager.setAdapter(viewPagerAdapter);
            viewPager.setCurrentItem(i);
            progressDialog.dismiss();
            if ((activityName.equals("ImagePrivateFragment") || activityName.equals("VideoPrivateFragment") || activityName.equals("ImageDeleteFragment") || activityName.equals("VideoDeleteFragment")) && allImagesList.size() == 0) {
                setResult(-1, getIntent());
                finish();
            }
            Toast.makeText(PreviewActivity.this, getString(R.string.files_are_deleted_successfully), Toast.LENGTH_SHORT).show();
        }
    }

    public class DeleteTrashImageExecute extends AsyncTask<Void, Void, Void> {
        String pathList;

        public DeleteTrashImageExecute(String str) {
            progressDeleteImage = new ProgressDialog(PreviewActivity.this);
            this.pathList = str;
        }

        @Override
        public void onPreExecute() {
            super.onPreExecute();
            progressDeleteImage.setMessage(getString(R.string.please_wait_a_while));
            progressDeleteImage.setProgressStyle(0);
            progressDeleteImage.setIndeterminate(false);
            progressDeleteImage.setCancelable(false);
            progressDeleteImage.show();
        }

        @Override
        public Void doInBackground(Void... voidArr) {
            File file = null;
            ArrayList arrayList = new ArrayList();
            ArrayList<String> listString = preferenceClass.getListString(Common.gOldPath);
            new File(FolderPath.SDCARD_PATH_DELETE_IMAGE).mkdirs();
            File file2 = new File(pathList);
            File file3 = new File(FolderPath.SDCARD_PATH_DELETE_IMAGE + File.separator + file2.getName());
            String parent = file3.getParent();
            if (allImagesList.size() == 1) {
                currentPosition = 0;
            } else if (currentPosition == allImagesList.size()) {
                currentPosition--;
            }
            if (activityName.equals("MomentFragment")) {
                MomentFragment.momentAdapter.RemoveItemSingle(pathList);
                allImagesList.remove(selectedPos);
            }
            try {
                FileUtils.CopyMoveFile(file2, file3);
                long j = ((DataFileModel) allImagesList.get(currentPosition)).id;
                String str = ((DataFileModel) allImagesList.get(currentPosition)).name;
                String str2 = ((DataFileModel) allImagesList.get(currentPosition)).path;
                listString.add(pathList);
                arrayList.add(new DataFileModel(j, str, str2, file3.getPath(), parent, false));
                new File(pathList).delete();
                try {
                    getContentResolver().delete(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "_data='" + file2.getPath() + "'", null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                preferenceClass.putListString(Common.gOldPath, listString);
            } catch (Exception e2) {
                e2.printStackTrace();
            }
            return null;
        }

        @Override
        public void onPostExecute(Void r4) {
            super.onPostExecute(r4);
            delpos = currentPosition;
            if (activityName.equals("InnerImageSelectActivity")) {
                if (currentPosition == allImagesList.size()) {
                    currentPosition--;
                    allImagesList.remove(currentPosition);
                } else {
                    allImagesList.remove(currentPosition);
                }
            }
            if (viewPagerAdapter != null) {
                viewPagerAdapter.notifyDataSetChanged();
            }
            new Handler().postDelayed(() -> AppUtilsClass.RefreshImageAlbum(PreviewActivity.this), 500L);
            AppUtilsClass.RefreshMoment(PreviewActivity.this);
            progressDeleteImage.dismiss();
            Toast.makeText(PreviewActivity.this, getString(R.string.files_are_deleted_successfully), Toast.LENGTH_SHORT).show();
            if (PreviewActivity.allImagesList.size() == 0) {
                setResult(-1, getIntent());
                finish();
            }
        }
    }

    public class DeleteTrashVideoExecute extends AsyncTask<Void, Void, Void> {
        String pathList;

        public DeleteTrashVideoExecute(String str) {
            progressDeleteVideo = new ProgressDialog(PreviewActivity.this);
            this.pathList = str;
        }

        @Override
        public void onPreExecute() {
            super.onPreExecute();
            progressDeleteVideo.setMessage(getString(R.string.please_wait_a_while));
            progressDeleteVideo.setProgressStyle(0);
            progressDeleteVideo.setIndeterminate(false);
            progressDeleteVideo.setCancelable(false);
            progressDeleteVideo.show();
        }

        @Override
        public Void doInBackground(Void... voidArr) {
            File file = null;
            ArrayList arrayList = new ArrayList();
            ArrayList<String> listString = preferenceClass.getListString(Common.gOldPath);
            new File(FolderPath.SDCARD_PATH_DELETE_VIDEO).mkdirs();
            File file2 = new File(pathList);
            File file3 = new File(FolderPath.SDCARD_PATH_DELETE_VIDEO + File.separator + file2.getName());
            String parent = file3.getParent();
            if (allImagesList.size() == 1) {
                currentPosition = 0;
            } else if (currentPosition == allImagesList.size()) {
                currentPosition--;
            }
            if (activityName.equals("MomentFragment")) {
                MomentFragment.momentAdapter.RemoveItemSingle(pathList);
                allImagesList.remove(selectedPos);
            }
            try {
                FileUtils.CopyMoveFile(file2, file3);
                long j = ((DataFileModel) allImagesList.get(currentPosition)).id;
                String str = ((DataFileModel) allImagesList.get(currentPosition)).name;
                String str2 = ((DataFileModel) allImagesList.get(currentPosition)).path;
                listString.add(str2);
                arrayList.add(new DataFileModel(j, str, str2, file3.getPath(), parent, false));
                new File(str2).delete();
                try {
                    getContentResolver().delete(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, "_data='" + file2.getPath() + "'", null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                preferenceClass.putListString(Common.gOldPath, listString);
            } catch (Exception e2) {
                e2.printStackTrace();
            }
            return null;
        }

        @Override
        public void onPostExecute(Void r4) {
            super.onPostExecute(r4);
            delpos = currentPosition;
            if (activityName.equals("InnerVideoSelectActivity")) {
                if (currentPosition == allImagesList.size()) {
                    currentPosition--;
                    allImagesList.remove(currentPosition);
                } else {
                    allImagesList.remove(currentPosition);
                }
            }
            if (viewPagerAdapter != null) {
                viewPagerAdapter.notifyDataSetChanged();
            }
            new Handler().postDelayed(() -> AppUtilsClass.RefreshVideoAlbum(PreviewActivity.this), 500L);
            AppUtilsClass.RefreshMoment(PreviewActivity.this);
            progressDeleteVideo.dismiss();
            Toast.makeText(PreviewActivity.this, getString(R.string.files_are_deleted_successfully), Toast.LENGTH_SHORT).show();
            if (allImagesList.size() == 0) {
                setResult(-1, getIntent());
                finish();
            }
        }
    }

    public void RenameFileDialog() {
        final Dialog dialog = new Dialog(this, R.style.ThemeWithCorners1);
        dialog.requestWindowFeature(1);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.setContentView(R.layout.dialog_rename_file);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        ((TextView) dialog.findViewById(R.id.txtTitle)).setText(getString(R.string.rename_file));
        final EditText editText = (EditText) dialog.findViewById(R.id.edit);
        editText.setText(FilenameUtils.getBaseName(ImgPath));
        ((TextView) dialog.findViewById(R.id.rl_yes)).setOnClickListener(view -> {
            if (editText.getText().toString().length() > 0) {
                ArrayList arrayList = new ArrayList();
                arrayList.add(ImgPath);
                File file = new File(ImgPath);
                String parent = file.getParent();
                String substring = file.getAbsolutePath().substring(file.getAbsolutePath().lastIndexOf("."));
                File file2 = new File(parent + File.separator + editText.getText().toString().trim() + substring);
                if (mediaFileType.equals("1")) {
                    try {
                        String obj = editText.getText().toString();
                        if (!obj.isEmpty()) {
                            String renameFile = renameFile(this, file, obj + file2.getName().substring(file2.getName().lastIndexOf("."), file2.getName().length()));
                            if (renameFile != null) {
                                ((DataFileModel) allImagesList.get(selectedPos)).setPath(renameFile);
                            } else {
                                Toast.makeText(PreviewActivity.this, getString(R.string.try_different_name), Toast.LENGTH_SHORT).show();
                            }
                        }
                        editText.requestFocus();
                        editText.setError(getString(R.string.image_name_can_not_be_empty));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    viewPagerAdapter = new ViewPagerAdapter(PreviewActivity.this, allImagesList);
                    viewPager.setAdapter(viewPagerAdapter);
                    viewPager.setCurrentItem(selectedPos);
                    dialog.dismiss();
                    viewPagerAdapter = new ViewPagerAdapter(PreviewActivity.this, allImagesList);
                    viewPager.setAdapter(viewPagerAdapter);
                    viewPager.setCurrentItem(selectedPos);
                    return;
                }
                if (mediaFileType.equals(ExifInterface.GPS_MEASUREMENT_3D)) {
                    try {
                        String obj2 = editText.getText().toString();
                        if (!obj2.isEmpty()) {
                            String renameFile2 = renameFile(this, file, obj2 + file2.getName().substring(file2.getName().lastIndexOf("."), file2.getName().length()));
                            if (renameFile2 != null) {
                                ((DataFileModel) allImagesList.get(selectedPos)).setPath(renameFile2);
                            } else {
                                Toast.makeText(PreviewActivity.this, getString(R.string.try_different_name), Toast.LENGTH_SHORT).show();
                            }
                        }
                        editText.requestFocus();
                        editText.setError(getString(R.string.video_name_can_not_be_empty));
                    } catch (Exception e2) {
                        e2.printStackTrace();
                    }
                    viewPagerAdapter = new ViewPagerAdapter(PreviewActivity.this, allImagesList);
                    viewPager.setAdapter(viewPagerAdapter);
                    viewPager.setCurrentItem(selectedPos);
                }
                dialog.dismiss();
                viewPagerAdapter = new ViewPagerAdapter(PreviewActivity.this, allImagesList);
                viewPager.setAdapter(viewPagerAdapter);
                viewPager.setCurrentItem(selectedPos);
                return;
            }
            editText.setError(getString(R.string.enter_file_name));
        });
        ((TextView) dialog.findViewById(R.id.rl_no)).setOnClickListener(view -> dialog.dismiss());
        dialog.show();
    }

    public static String renameFile(Context context, File file, String str) {
        File file2 = new File(file.getAbsolutePath());
        File file3 = new File(file2.getParent() + File.separator + str);
        if (file2.exists() && !file3.exists() && file2.renameTo(file3) && file3.exists()) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(Utils.mediaPath, file3.getAbsolutePath());
            ContentResolver contentResolver = context.getContentResolver();
            Uri contentUri = MediaStore.Files.getContentUri("external");
            contentResolver.update(contentUri, contentValues, "_data='" + file3 + "'", null);
            Toast.makeText(context, R.string.rename_successfully, Toast.LENGTH_SHORT).show();
            return file3.getAbsolutePath();
        }
        return null;
    }

    public void RestoreDialog() {
        final Dialog dialog = new Dialog(this, R.style.ThemeWithCorners1);
        dialog.requestWindowFeature(1);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.setContentView(R.layout.dialog_restore_image);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        if (mediaFileType.equals("1")) {
            ((TextView) dialog.findViewById(R.id.txtTitle)).setText(getString(R.string.restore_image));
            ((TextView) dialog.findViewById(R.id.textDesc)).setText(getString(R.string.are_you_sure_you_want_to_restore_image));
        } else if (mediaFileType.equals(ExifInterface.GPS_MEASUREMENT_3D)) {
            ((TextView) dialog.findViewById(R.id.txtTitle)).setText(getString(R.string.restore_video));
            ((TextView) dialog.findViewById(R.id.textDesc)).setText(getString(R.string.are_you_sure_you_want_to_restore_image));
        }
        ((TextView) dialog.findViewById(R.id.rl_yes)).setOnClickListener(view -> {
            if (mediaFileType.equals("1")) {
                new RestoreImageExecute(ImgPath).execute(new Void[0]);
            } else if (mediaFileType.equals(ExifInterface.GPS_MEASUREMENT_3D)) {
                new RestoreVideoExecute(ImgPath).execute(new Void[0]);
            }
            dialog.dismiss();
        });
        ((TextView) dialog.findViewById(R.id.rl_no)).setOnClickListener(view -> dialog.dismiss());
        dialog.show();
    }


    public class RestoreImageExecute extends AsyncTask<Void, Void, Void> {
        String imagePath;
        ProgressDialog progressDialog;
        boolean isRecover = true;
        ArrayList<Object> imageArray = new ArrayList<>();

        public RestoreImageExecute(String str) {
            progressDialog = new ProgressDialog(PreviewActivity.this);
            this.imagePath = str;
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
            ArrayList arrayList = new ArrayList();
            ArrayList<String> listString = preferenceClass.getListString(Common.gOldPath);
            File file = new File(imagePath);
            String name = file.getName();
            File file2 = new File(FolderPath.SDCARD_PATH_IMAGE_TRASH_BACKUP);
            if (!file2.exists()) {
                file2.mkdirs();
            }
            File file3 = new File(file2 + File.separator + name);
            String parent = file3.getParent();
            new MediaScanners(GalleryAppClass.getInstance(), file3);
            try {
                FileUtils.CopyMoveFile(file, file3);
                long j = ((DataFileModel) allImagesList.get(currentPosition)).id;
                String str = ((DataFileModel) allImagesList.get(currentPosition)).name;
                String str2 = ((DataFileModel) allImagesList.get(currentPosition)).path;
                listString.add(str2);
                arrayList.add(new DataFileModel(j, str, str2, file3.getPath(), parent, false));
                File file4 = new File(imagePath);
                file4.delete();
                ContentResolver contentResolver = getContentResolver();
                Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                contentResolver.delete(uri, "_data='" + file4.getPath() + "'", null);
                preferenceClass.putListString(Common.gOldPath, listString);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        public void onPostExecute(Void r6) {
            super.onPostExecute(r6);
            int i = currentPosition;
            progressDialog.dismiss();
            if (i == allImagesList.size()) {
                currentPosition--;
                allImagesList.remove(currentPosition);
                i = 0;
            } else {
                allImagesList.remove(currentPosition);
            }
            viewPagerAdapter = new ViewPagerAdapter(PreviewActivity.this, allImagesList);
            viewPager.setAdapter(viewPagerAdapter);
            viewPager.setCurrentItem(i);
            new Handler().postDelayed(() -> AppUtilsClass.RefreshPhotoVideo(PreviewActivity.this), 500L);
            if ((activityName.equals("ImagePrivateFragment") || activityName.equals("VideoPrivateFragment") || activityName.equals("ImageDeleteFragment") || activityName.equals("VideoDeleteFragment")) && PreviewActivity.allImagesList.size() == 0) {
                setResult(-1, getIntent());
                finish();
            }
            Toast.makeText(PreviewActivity.this, getString(R.string.files_are_unlocked_successfully), Toast.LENGTH_SHORT).show();
        }
    }

    public class RestoreVideoExecute extends AsyncTask<Void, Void, Void> {
        ProgressDialog progressDialog;
        String videoPath;
        boolean isRecover = true;
        ArrayList<Object> imageArray = new ArrayList<>();

        public RestoreVideoExecute(String str) {
            progressDialog = new ProgressDialog(PreviewActivity.this);
            this.videoPath = str;
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
            ArrayList arrayList = new ArrayList();
            ArrayList<String> listString = preferenceClass.getListString(Common.gOldPath);
            File file = new File(videoPath);
            String name = file.getName();
            File file2 = new File(FolderPath.SDCARD_PATH_VIDEO_TRASH_BACKUP);
            if (!file2.exists()) {
                file2.mkdirs();
            }
            File file3 = new File(file2 + File.separator + name);
            String parent = file3.getParent();
            new MediaScanners(GalleryAppClass.getInstance(), file3);
            try {
                FileUtils.CopyMoveFile(file, file3);
                long j = ((DataFileModel) allImagesList.get(currentPosition)).id;
                String str = ((DataFileModel) allImagesList.get(currentPosition)).name;
                String str2 = ((DataFileModel) allImagesList.get(currentPosition)).path;
                listString.add(str2);
                arrayList.add(new DataFileModel(j, str, str2, file3.getPath(), parent, false));
                File file4 = new File(videoPath);
                file4.delete();
                ContentResolver contentResolver = getContentResolver();
                Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                contentResolver.delete(uri, "_data='" + file4.getPath() + "'", null);
                preferenceClass.putListString(Common.gOldPath, listString);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        public void onPostExecute(Void r6) {
            super.onPostExecute(r6);
            progressDialog.dismiss();
            int i = currentPosition;
            if (i == allImagesList.size()) {
                currentPosition--;
                allImagesList.remove(currentPosition);
                i = 0;
            } else {
                allImagesList.remove(currentPosition);
            }
            viewPagerAdapter = new ViewPagerAdapter(PreviewActivity.this, PreviewActivity.allImagesList);
            viewPager.setAdapter(viewPagerAdapter);
            viewPager.setCurrentItem(i);
            new Handler().postDelayed(() -> AppUtilsClass.RefreshPhotoVideo(PreviewActivity.this), 500L);
            if ((activityName.equals("ImagePrivateFragment") || activityName.equals("VideoPrivateFragment") || activityName.equals("ImageDeleteFragment") || activityName.equals("VideoDeleteFragment")) && PreviewActivity.allImagesList.size() == 0) {
                setResult(-1, getIntent());
                finish();
            }
            Toast.makeText(PreviewActivity.this, getString(R.string.files_are_restored_successfully), Toast.LENGTH_SHORT).show();
        }
    }

    public void SecurityDialog() {
        final Dialog dialog = new Dialog(this, R.style.ThemeWithCorners1);
        dialog.requestWindowFeature(1);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.setContentView(R.layout.dialog_security);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        TextView textView = dialog.findViewById(R.id.textDesc);
        if (mediaFileType.equals("1")) {
            textView.setText(getString(R.string.set_your_security_lock_for_hide_photos));
        } else if (mediaFileType.equals(ExifInterface.GPS_MEASUREMENT_3D)) {
            textView.setText(getString(R.string.set_your_security_lock_for_hide_videos));
        }
        ((TextView) dialog.findViewById(R.id.rl_yes)).setOnClickListener(view -> {
            FolderPath.lock_screen = 1;
            startActivity(new Intent(PreviewActivity.this, PrivateActivity.class));
            dialog.dismiss();
        });
        ((TextView) dialog.findViewById(R.id.rl_no)).setOnClickListener(view -> dialog.dismiss());
        dialog.show();
    }

    public void LockImagesDialog(final String str) {
        final Dialog dialog = new Dialog(this, R.style.ThemeWithCorners1);
        dialog.requestWindowFeature(1);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.setContentView(R.layout.dialog_lock_image);
        dialog.setTitle(getString(R.string.lock_image));
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        ((TextView) dialog.findViewById(R.id.rl_yes)).setOnClickListener(view -> {
            LockImages(str);
            dialog.dismiss();
        });
        ((TextView) dialog.findViewById(R.id.rl_no)).setOnClickListener(view -> dialog.dismiss());
        dialog.show();
    }

    public ArrayList<DataFileModel> GetSelectedList() {
        ArrayList<DataFileModel> arrayList = new ArrayList<>();
        int size = ConstantArrayClass.folderAllImageList.size();
        for (int i = 0; i < size; i++) {
            if (((DataFileModel) ConstantArrayClass.folderAllImageList.get(i)).isSelected) {
                arrayList.add((DataFileModel) ConstantArrayClass.folderAllImageList.get(i));
            }
        }
        return arrayList;
    }

    public void LockImages(String str) {
//        ArrayList<DataFileModel> GetSelectedList = GetSelectedList();

        if (ExternalStorageHelper.isExternalStorageReadableAndWritable()) {
            try {
                progressTag = "FromLockImage";
                new LockImageExecute(str).execute(new Void[0]);
//                new LockImageExecute(GetSelectedList).execute(new Void[0]);

                return;
            } catch (Exception e) {
                e.getMessage();
                viewPagerAdapter.notifyDataSetChanged();
                return;
            }
        }
        Toast.makeText(this, "not Read Write", Toast.LENGTH_SHORT).show();
    }


    public class LockImageExecute extends AsyncTask<Void, Void, Void> {
        String pathList;

        public LockImageExecute(String str) {
            progressLockImage = new ProgressDialog(PreviewActivity.this);
            this.pathList = str;
        }

        @Override
        public void onPreExecute() {
            super.onPreExecute();
            progressLockImage.setMessage(getString(R.string.please_wait_a_while));
            progressLockImage.setProgressStyle(0);
            progressLockImage.setIndeterminate(false);
            progressLockImage.setCancelable(false);
            progressLockImage.show();
        }

        @Override
        public Void doInBackground(Void... voidArr) {
            File file = null;
            ArrayList arrayList = new ArrayList();
            ArrayList<String> listString = preferenceClass.getListString(Common.gOldPath);
            new File(FolderPath.SDCARD_PATH_IMAGE).mkdirs();
            File file2 = new File(pathList);
            File file3 = new File(FolderPath.SDCARD_PATH_IMAGE + File.separator + file2.getName());
            String parent = file3.getParent();
            if (allImagesList.size() == 1) {
                currentPosition = 0;
            } else if (currentPosition == allImagesList.size()) {
                currentPosition--;
            }
            if (activityName.equals("MomentFragment")) {
                MomentFragment.momentAdapter.RemoveItemSingle(pathList);
                allImagesList.remove(selectedPos);
            }
            try {
                FileUtils.CopyMoveFile(file2, file3);
                long j = ((DataFileModel) allImagesList.get(currentPosition)).id;
                String str = ((DataFileModel) allImagesList.get(currentPosition)).name;
                String str2 = ((DataFileModel) allImagesList.get(currentPosition)).path;
                listString.add(str2);
                arrayList.add(new DataFileModel(j, str, str2, file3.getPath(), parent, false));
                new File(pathList).delete();
                getContentResolver().delete(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "_data='" + file2.getPath() + "'", null);
                preferenceClass.putListString(Common.gOldPath, listString);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        public void onPostExecute(Void r6) {
            super.onPostExecute(r6);
            delpos = currentPosition;
            if (activityName.equals("MomentFragment")) {
                delpos = currentPosition;
                if (ImageSelectActivity.innerBothAlbumSelectAdapter != null) {
                    ImageSelectActivity.innerBothAlbumSelectAdapter.notifyDataSetChanged();
                }
                if (viewPagerAdapter != null) {
                    viewPagerAdapter.notifyDataSetChanged();
                }
            } else {
                int i = currentPosition;
                if (i == allImagesList.size()) {
                    currentPosition--;
                    allImagesList.remove(currentPosition);
                    i = 0;
                } else {
                    allImagesList.remove(currentPosition);
                }
                viewPagerAdapter = new ViewPagerAdapter(PreviewActivity.this, PreviewActivity.allImagesList);
                viewPager.setAdapter(viewPagerAdapter);
                viewPager.setCurrentItem(i);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        AppUtilsClass.RefreshMoment(PreviewActivity.this);
                    }
                }, 500L);
            }
            progressLockImage.dismiss();
            Toast.makeText(PreviewActivity.this, getString(R.string.files_moved_to_vault_successfully), Toast.LENGTH_SHORT).show();
            if (allImagesList.size() == 0) {
                setResult(-1, getIntent());
                finish();
            }
        }
    }

    public void LockVideosDialog(final String str) {
        final Dialog dialog = new Dialog(this, R.style.ThemeWithCorners1);
        dialog.requestWindowFeature(1);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.setContentView(R.layout.dialog_lock_video);
        dialog.setTitle(getString(R.string.lock_video));
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        ((TextView) dialog.findViewById(R.id.rl_yes)).setOnClickListener(view -> {
            LockVideos(str);
            dialog.dismiss();
        });
        ((TextView) dialog.findViewById(R.id.rl_no)).setOnClickListener(view -> dialog.dismiss());
        dialog.show();
    }

    public void LockVideos(String str) {
        progressTag = "FromLockVideo";
        new LockVideoExecute(str).execute(new Void[0]);
    }


    public class LockVideoExecute extends AsyncTask<Void, Void, Void> {
        String pathList;

        public LockVideoExecute(String str) {
            progressLockVideo = new ProgressDialog(PreviewActivity.this);
            this.pathList = str;
        }

        @Override
        public void onPreExecute() {
            super.onPreExecute();
            progressLockVideo.setMessage(getString(R.string.please_wait_a_while));
            progressLockVideo.setProgressStyle(0);
            progressLockVideo.setIndeterminate(false);
            progressLockVideo.setCancelable(false);
            progressLockVideo.show();
        }

        @Override
        public Void doInBackground(Void... voidArr) {
            File file = null;
            ArrayList arrayList = new ArrayList();
            ArrayList<String> listString = preferenceClass.getListString(Common.gOldPath);
            new File(FolderPath.SDCARD_PATH_VIDEO).mkdirs();
            File file2 = new File(pathList);
            File file3 = new File(FolderPath.SDCARD_PATH_VIDEO + File.separator + file2.getName());
            String parent = file3.getParent();
            if (allImagesList.size() == 1) {
                currentPosition = 0;
            } else if (currentPosition == allImagesList.size()) {
                currentPosition--;
            }
            if (activityName.equals("MomentFragment")) {
                MomentFragment.momentAdapter.RemoveItemSingle(pathList);
                allImagesList.remove(selectedPos);
            }
            try {
                FileUtils.CopyMoveFile(file2, file3);
                long j = ((DataFileModel) allImagesList.get(currentPosition)).id;
                String str = ((DataFileModel) allImagesList.get(currentPosition)).name;
                listString.add(((DataFileModel) allImagesList.get(currentPosition)).path);
                arrayList.add(new DataFileModel(j, str, pathList, file3.getPath(), parent, false));
                new File(pathList).delete();
                getContentResolver().delete(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, "_data='" + file2.getPath() + "'", null);
                preferenceClass.putListString(Common.gOldPath, listString);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        public void onPostExecute(Void r6) {
            super.onPostExecute(r6);
            if (activityName.equals("MomentFragment")) {
                delpos = currentPosition;
                if (VideoSelectActivity.innerBothAlbumSelectAdapter != null) {
                    VideoSelectActivity.innerBothAlbumSelectAdapter.notifyDataSetChanged();
                }
                if (viewPagerAdapter != null) {
                    viewPagerAdapter.notifyDataSetChanged();
                }
            } else {
                int i = currentPosition;
                if (i == allImagesList.size()) {
                    currentPosition--;
                    allImagesList.remove(currentPosition);
                    i = 0;
                } else {
                    allImagesList.remove(currentPosition);
                }
                viewPagerAdapter = new ViewPagerAdapter(PreviewActivity.this, allImagesList);
                viewPager.setAdapter(viewPagerAdapter);
                viewPager.setCurrentItem(i);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        AppUtilsClass.RefreshMoment(PreviewActivity.this);
                        AppUtilsClass.RefreshVideoAlbum(PreviewActivity.this);
                    }
                }, 500L);
            }
            progressLockVideo.dismiss();
            Toast.makeText(PreviewActivity.this, getString(R.string.files_moved_to_vault_successfully), Toast.LENGTH_SHORT).show();
            if (allImagesList.size() == 0) {
                setResult(-1, getIntent());
                finish();
            }
        }
    }

    public void GetImageAlbumDialog(final boolean z) {
        albumDialog = new Dialog(this, R.style.MyDialog);
        albumDialog.requestWindowFeature(1);
        albumDialog.setCancelable(false);
        albumDialog.setContentView(R.layout.dg_folder);
        ImageView imageView = albumDialog.findViewById(R.id.btnBack);
        TextView textView = albumDialog.findViewById(R.id.toolbarTitle);
        textView.setText("Select Image Folder");
        textView.setVisibility(View.VISIBLE);
        gr_albumGridView = (GridView) albumDialog.findViewById(R.id.albumGridView);

        query = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new String[]{"_id", "bucket_id", "bucket_display_name", Utils.mediaPath}, null, null, null);
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
                    gMDialogAlbumModel.pathlist = GetImagePathList("" + gMDialogAlbumModel.bucket_id);
                    arrayList.add(gMDialogAlbumModel);
                    arrayList2.add(gMDialogAlbumModel.bucket_id);
                }
            }
            query.close();
        }
        albumAdapter = new AlbumAdapter(this, arrayList);
        gr_albumGridView.setAdapter((ListAdapter) albumAdapter);
        AlbumOrientation(getResources().getConfiguration().orientation);
        gr_albumGridView.setOnItemClickListener((adapterView, view, i, j) -> {
            if (arrayList == null || arrayList.isEmpty()) {
                return;
            }
            MoveFileInsideDialog(new File(((DialogAlbumModel) arrayList.get(i)).folderPath), z, ((DialogAlbumModel) arrayList.get(i)).bucket_id, ((DialogAlbumModel) arrayList.get(i)).foldername);
        });
        imageView.setOnClickListener(view -> albumDialog.dismiss());

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

    public ArrayList<String> GetImagePathList(String str) {
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

    public void GetVideoAlbumDialog(final boolean z) {
        albumDialog = new Dialog(this, R.style.MyDialog);
        albumDialog.requestWindowFeature(1);
        albumDialog.setCancelable(false);
        albumDialog.setContentView(R.layout.dg_folder);
        TextView textView = albumDialog.findViewById(R.id.toolbarTitle);
        textView.setText("Select Video Folder");
        textView.setVisibility(View.VISIBLE);
        gr_albumGridView = (GridView) albumDialog.findViewById(R.id.albumGridView);
        (albumDialog.findViewById(R.id.btnBack)).setOnClickListener(view -> albumDialog.dismiss());
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
                    gMDialogAlbumModel.folderPath = GetParentPath(query.getString(query.getColumnIndexOrThrow(Utils.mediaPath)));
                    gMDialogAlbumModel.id = query.getString(query.getColumnIndex("_id"));
                    gMDialogAlbumModel.pathlist = GetVideoPathList("" + gMDialogAlbumModel.bucket_id);
                    arrayList.add(gMDialogAlbumModel);
                    arrayList2.add(gMDialogAlbumModel.bucket_id);
                }
            }
            query.close();
        }
        videoDialogAdapter = new VideoDialogAdapter(this, arrayList);
        gr_albumGridView.setAdapter((ListAdapter) videoDialogAdapter);
        AlbumOrientation(getResources().getConfiguration().orientation);
        videoDialogAdapter.notifyDataSetChanged();
        gr_albumGridView.setOnItemClickListener((adapterView, view, i, j) -> {
            if (arrayList == null || arrayList.isEmpty()) {
                return;
            }
            MoveFileInsideDialog(new File(((DialogAlbumModel) arrayList.get(i)).folderPath), z, ((DialogAlbumModel) arrayList.get(i)).bucket_id, ((DialogAlbumModel) arrayList.get(i)).foldername);
        });
        albumDialog.show();
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
        ImageView imageView = insideFileDialog.findViewById(R.id.btnBack);
        TextView textView = insideFileDialog.findViewById(R.id.toolbarTitle);
        textView.setText(str2);
        textView.setVisibility(View.VISIBLE);
        gr_albumImageGridView = (GridView) insideFileDialog.findViewById(R.id.albumGridView);
        String[] strArr = {"_id", "_display_name", Utils.mediaPath, "date_added"};
        if (mediaFileType.equals("1")) {
            cursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, strArr, "bucket_id =?", new String[]{str}, null);
        } else if (mediaFileType.equals(ExifInterface.GPS_MEASUREMENT_3D)) {
            cursor = getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, strArr, "bucket_id =?", new String[]{str}, null);
        }
        ArrayList arrayList = new ArrayList(cursor.getCount());
        while (cursor.moveToNext()) {
            String string = cursor.getString(cursor.getColumnIndex(strArr[2]));
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
        cursor.close();
        if (mediaFileType.equals("1")) {
            imageFolderAdapter = new ImageFolderAdapter(this, arrayList);
            gr_albumImageGridView.setAdapter((ListAdapter) imageFolderAdapter);
            ImageOrientation(getResources().getConfiguration().orientation);
            imageFolderAdapter.notifyDataSetChanged();
        } else if (mediaFileType.equals(ExifInterface.GPS_MEASUREMENT_3D)) {
            videoAlbumAdapter = new VideoFolderAdapter(this, arrayList);
            gr_albumImageGridView.setAdapter((ListAdapter) videoAlbumAdapter);
            ImageOrientation(getResources().getConfiguration().orientation);
            videoAlbumAdapter.notifyDataSetChanged();
        }

        insideFileDialog.setOnKeyListener((dialogInterface, i, keyEvent) -> {
            if (i == 4) {
                dialog_count = 1;
                onBackPressed();
            }
            return true;
        });

        imageView.setOnClickListener(view -> insideFileDialog.dismiss());

        final LinearLayout linearLayout = insideFileDialog.findViewById(R.id.btnPaste);
        if (linearLayout.getVisibility() == View.VISIBLE) {
            gr_albumImageGridView.setOnItemClickListener(null);
        }
        linearLayout.setOnClickListener(view -> {
            MoveImageonAlbum(file, z, str);
            linearLayout.setVisibility(View.GONE);
        });
        insideFileDialog.show();
    }

    public void MoveImageonAlbum(File file, boolean z, String str) {
        new MoveToAlbumExecute(ImgPath, file, z, str).execute(new Void[0]);
    }

    public class MoveToAlbumExecute extends AsyncTask<Void, Void, Void> {
        File albumDir;
        String bucketId;
        String imagepath;
        boolean operation;
        ProgressDialog progressDialog;
        int toast_cnt = 0;

        public MoveToAlbumExecute(String str, File file, boolean z, String str2) {
            progressDialog = new ProgressDialog(PreviewActivity.this);
            this.imagepath = str;
            this.albumDir = file;
            this.operation = z;
            this.bucketId = str2;
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
                File file = new File(imagepath);
                File file2 = new File(albumDir + File.separator + file.getName());
                if (mediaFileType.equals("1")) {
                    try {
                        FileUtils.CopyMoveFile(file, file2);
                        file.delete();
                        ContentResolver contentResolver = getContentResolver();
                        Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                        contentResolver.delete(uri, "_data='" + imagepath + "'", null);
                        ContentValues contentValues = new ContentValues();
                        contentValues.put(Utils.mediaPath, file2.getPath());
                        contentValues.put("mime_type", "image/*");
                        contentValues.put("date_added", Long.valueOf(System.currentTimeMillis()));
                        contentValues.put("date_modified", Long.valueOf(System.currentTimeMillis()));
                        getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
                        return null;
                    } catch (Exception e) {
                        e.printStackTrace();
                        return null;
                    }
                } else if (mediaFileType.equals(ExifInterface.GPS_MEASUREMENT_3D)) {
                    try {
                        FileUtils.CopyMoveFile(file, file2);
                        file.delete();
                        ContentResolver contentResolver2 = getContentResolver();
                        Uri uri2 = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                        contentResolver2.delete(uri2, "_data='" + imagepath + "'", null);
                        ContentValues contentValues2 = new ContentValues();
                        contentValues2.put("title", file2.getName());
                        contentValues2.put("description", "Name");
                        contentValues2.put(Utils.mediaPath, file2.getPath());
                        contentValues2.put("mime_type", "video/*");
                        contentValues2.put("date_added", Long.valueOf(System.currentTimeMillis()));
                        contentValues2.put("date_modified", Long.valueOf(System.currentTimeMillis()));
                        contentValues2.put("bucket_id", Integer.valueOf(file2.getPath().toLowerCase(Locale.US).hashCode()));
                        contentValues2.put("bucket_display_name", file2.getName().toLowerCase(Locale.US));
                        getContentResolver().insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, contentValues2);
                        return null;
                    } catch (Exception e2) {
                        e2.printStackTrace();
                        return null;
                    }
                } else {
                    return null;
                }
            }
            toast_cnt = 1;
            File file3 = new File(PreviewActivity.ImgPath);
            File file4 = new File(albumDir + File.separator + System.currentTimeMillis() + file3.getName());
            albumDir.mkdirs();
            if (mediaFileType.equals("1")) {
                try {
                    org.apache.commons.io.FileUtils.copyFile(file3, file4);
                    ContentValues contentValues3 = new ContentValues();
                    contentValues3.put("title", file4.getName());
                    contentValues3.put("description", "Gallery");
                    contentValues3.put(Utils.mediaPath, file4.getPath());
                    contentValues3.put("mime_type", "image/*");
                    contentValues3.put("date_added", Long.valueOf(System.currentTimeMillis()));
                    contentValues3.put("date_modified", Long.valueOf(System.currentTimeMillis()));
                    contentValues3.put("bucket_id", Integer.valueOf(file4.getPath().toLowerCase(Locale.US).hashCode()));
                    contentValues3.put("bucket_display_name", file4.getName().toLowerCase(Locale.US));
                    getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues3);
                    return null;
                } catch (IOException e3) {
                    e3.printStackTrace();
                    return null;
                }
            } else if (mediaFileType.equals(ExifInterface.GPS_MEASUREMENT_3D)) {
                try {
                    org.apache.commons.io.FileUtils.copyFile(file3, file4);
                    ContentValues contentValues4 = new ContentValues();
                    contentValues4.put(Utils.mediaPath, file4.getPath());
                    getContentResolver().insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, contentValues4);
                    return null;
                } catch (IOException e4) {
                    e4.printStackTrace();
                    return null;
                }
            } else {
                return null;
            }
        }

        @Override
        public void onPostExecute(Void r3) {
            super.onPostExecute(r3);
            AppUtilsClass.RefreshPhotoVideo(PreviewActivity.this);
            if (mediaFileType.equals("1")) {
                RefreshImageAdapter(bucketId);
            } else if (mediaFileType.equals(ExifInterface.GPS_MEASUREMENT_3D)) {
                RefreshVideoAdapter(bucketId);
            }
            if (toast_cnt == 0) {
                Toast.makeText(PreviewActivity.this, getString(R.string.file_moved_successfully), Toast.LENGTH_SHORT).show();
            } else if (toast_cnt == 1) {
                Toast.makeText(PreviewActivity.this, getString(R.string.file_copied_successfully), Toast.LENGTH_SHORT).show();
            }
            progressDialog.dismiss();
            insideFileDialog.dismiss();
            albumDialog.dismiss();
        }
    }

    public void RefreshImageAdapter(String str) {
        Cursor query = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, dataParameter, "bucket_id =?", new String[]{str}, "date_added");
        ArrayList arrayList = new ArrayList(query.getCount());
        while (query.moveToNext()) {
            String string = query.getString(query.getColumnIndex(dataParameter[2]));
            try {
                if (new File(string).exists()) {
                    DataFileModel gMDataFileModel = new DataFileModel();
                    gMDataFileModel.path = string;
                    arrayList.add(gMDataFileModel);
                }
            } catch (Exception e) {
                Log.e("Exception : ", e.toString());
            }
        }
        query.close();
        myImageList = new ArrayList<>();
        myImageList.addAll(arrayList);
        imageFolderAdapter = new ImageFolderAdapter(this, myImageList);
        gr_albumImageGridView.setAdapter((ListAdapter) imageFolderAdapter);
        ImageOrientation(getResources().getConfiguration().orientation);
        imageFolderAdapter.notifyDataSetChanged();
    }

    public void RefreshVideoAdapter(String str) {
        Cursor query = getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, dataParameter, "bucket_id =?", new String[]{str}, "date_added");
        ArrayList arrayList = new ArrayList(query.getCount());
        while (query.moveToNext()) {
            String string = query.getString(query.getColumnIndex(dataParameter[2]));
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
        myImageList = new ArrayList<>();
        myImageList.addAll(arrayList);
        videoAlbumAdapter = new VideoFolderAdapter(this, myImageList);
        gr_albumImageGridView.setAdapter((ListAdapter) videoAlbumAdapter);
        ImageOrientation(getResources().getConfiguration().orientation);
        videoAlbumAdapter.notifyDataSetChanged();
    }

    public void SlidShowTimerDialog() {
        final Dialog dialog = new Dialog(this, R.style.alert_dialog);
        dialog.requestWindowFeature(1);
        dialog.setContentView(R.layout.dialog_slide_show);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        radioGroup = (RadioGroup) dialog.findViewById(R.id.radioGroup);
        ((TextView) dialog.findViewById(R.id.rl_yes)).setOnClickListener(view -> {
            RadioButton radioButton = (RadioButton) radioGroup.findViewById(radioGroup.getCheckedRadioButtonId());
            if (radioButton.getText().equals("1 Sec.")) {
                slideShowDuration = 1000;
            } else if (radioButton.getText().equals("2 Sec.")) {
                slideShowDuration = 2000;
            } else if (radioButton.getText().equals("3 Sec.")) {
                slideShowDuration = 3000;
            } else if (radioButton.getText().equals("4 Sec.")) {
                slideShowDuration = 4000;
            } else if (radioButton.getText().equals("5 Sec.")) {
                slideShowDuration = 5000;
            }
            dialog.dismiss();
            TimerStart();
        });
        ((TextView) dialog.findViewById(R.id.rl_no)).setOnClickListener(view -> dialog.dismiss());
        dialog.show();
    }

    public void TimerStart() {
        try {
            slideShowRunning = true;
            if (viewPagerAdapter != null) {
                currentPage = viewPager.getCurrentItem() + 1;
            }
            HideControl();
            handler = new Handler();
            final SlideTimer slideTimer = new SlideTimer();
            Timer timer = new Timer();
            swipeTimer = timer;
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    handler.post(slideTimer);
                }
            }, 0L, slideShowDuration);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public class SlideTimer implements Runnable {
        SlideTimer() {
        }

        @Override
        public void run() {
            if (currentPage == allImagesList.size()) {
                currentPage = 0;
            } else {
                currentPage++;
            }
            viewPager.setCurrentItem(currentPage, true);
        }
    }

    public void SlidEffectDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.alert_dialog);
        View inflate = getLayoutInflater().inflate(R.layout.dg_effect_list, (ViewGroup) null);
        builder.setView(inflate);
        (inflate.findViewById(R.id.lin_main)).setBackgroundColor(0);
        ListView listView = (ListView) inflate.findViewById(R.id.listView1);
        final SlidShowEffectAdapter gMSlidShowEffectAdapter = new SlidShowEffectAdapter(this, new String[]{"Default", "Accordion", "BackgroundToForeground", "CubeIn", "CubeOut", "DepthPage", "FlipHorizontal", "FlipVertical", "ForegroundToBackground", "RotateDown", "RotateUp", "ScaleInOut", "Stack", "Tablet", "ZoomIn", "ZoomOutSlide", "ZoomOut", "Drawer"});
        listView.setAdapter((ListAdapter) gMSlidShowEffectAdapter);
        listView.setOnItemClickListener((adapterView, view, i, j) -> {
            Common.gEff_Pos = i;
            SetEffect(i);
            gMSlidShowEffectAdapter.notifyDataSetChanged();
        });
        ((TextView) inflate.findViewById(R.id.rl_yes)).setOnClickListener(view -> alertDialog1.dismiss());
        ((TextView) inflate.findViewById(R.id.rl_no)).setOnClickListener(view -> alertDialog1.dismiss());
        alertDialog1 = builder.show();
    }

    public void SetEffect(int i) {
        try {
            viewPager.setPageTransformer(true, TRANSFORM_CLASSES.get(i).clazz.newInstance());
        } catch (IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        }
    }

    public static String getFilePathFromStringPath(String str) {
        return str.substring(str.lastIndexOf("/") + 1, str.length());
    }

    public void DetailDialog(String str) {
        final Dialog dialog = new Dialog(this, R.style.ThemeWithCorners1);
        dialog.requestWindowFeature(1);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.setContentView(R.layout.dg_data_detail);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        TextView textView = dialog.findViewById(R.id.txtImageName);
        TextView textView2 = dialog.findViewById(R.id.txtImagePath);
        TextView textView3 = dialog.findViewById(R.id.txtImageSize);
        TextView textView4 = dialog.findViewById(R.id.txtImageDate);
        try {
            File file = new File(str);
            if (file.exists()) {
                Date date = new Date(file.lastModified());
                textView.setText(getFilePathFromStringPath(str));
                textView2.setText(file.getParent());
                textView3.setText(GetFileSize((int) (file.length() / 1024)) + "");
                if (activityName.equals("MomentFragment")) {
                    String GetDateFromTimeStampBase = AppUtilsClass.GetDateFromTimeStampBase(Long.parseLong(((DataFileModel) allImagesList.get(currentPosition)).takenDate) * 1000);
                    textView4.setText(GetDateFromTimeStampBase);
                } else {
                    String GetDateFromTimeStampBase2 = AppUtilsClass.GetDateFromTimeStampBase(file.lastModified());
                    textView4.setText(GetDateFromTimeStampBase2);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        ((RelativeLayout) dialog.findViewById(R.id.rl_no)).setOnClickListener(view -> dialog.dismiss());
        dialog.show();
    }

    public String GetFileSize(int i) {
        double d = i;
        Double.isNaN(d);
        double d2 = d / 1024.0d;
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        if (d2 > 1.0d) {
            return decimalFormat.format(d2).concat(" MB");
        }
        return decimalFormat.format(i).concat(" KB");
    }

    public void SingleShare() {
        try {
            Uri uriForFile = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".fileprovider", new File(ImgPath));
            Intent intent = new Intent("android.intent.action.SEND");
            intent.setType("image/*");
            intent.putExtra("android.intent.extra.STREAM", uriForFile);
            startActivity(Intent.createChooser(intent, "Share Image"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteImage(String str) {
        File file = new File(str);
        ContentResolver contentResolver = getContentResolver();
        Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        contentResolver.delete(uri, "_data='" + file.getPath() + "'", null);
        currentPosition = currentPosition + (-1);
        viewPagerAdapter.notifyDataSetChanged();
    }

    public void ShowControl() {
        ll_bottomControl.setVisibility(View.VISIBLE);
        rl_top.setVisibility(View.VISIBLE);
        if (rl_op_main.getVisibility() == View.VISIBLE) {
            rl_op_main.setVisibility(View.GONE);
        }
    }

    public void HideControl() {
        ll_bottomControl.setVisibility(View.INVISIBLE);
        rl_top.setVisibility(View.INVISIBLE);
        rl_op_main.setVisibility(View.GONE);
    }

    @Override

    public void onActivityResult(int i, int i2, Intent intent) {
        if (i == REQUEST_ID_SET_AS_WALLPAPER && i2 == 1) {
            Toast.makeText(this, "Wallpaper set Successfully !!", Toast.LENGTH_SHORT).show();
        }
        super.onActivityResult(i, i2, intent);
    }

    @Override
    public void onBackPressed() {
        AdShow.getInstance(PreviewActivity.this).ShowAd(new HandleClick() {
            @Override
            public void Show(boolean adShow) {
                if (swipeTimer != null) {
                    swipeTimer.cancel();
                    swipeTimer.purge();
                    swipeTimer = null;
                }
                if (insideFileDialog == null || !insideFileDialog.isShowing()) {
                    if (albumDialog == null || !albumDialog.isShowing()) {
                        finish();
                    } else {
                        albumDialog.dismiss();
                    }
                    return;
                }
                insideFileDialog.dismiss();
            }
        }, AdUtils.ClickType.BACK_CLICK);

    }

    @Override
    public void onPause() {
        super.onPause();
        videoView = new VideoView(this);
        if (videoView != null) {
            videoView.pause();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        videoView = new VideoView(this);
        if (videoView != null) {
            videoView.start();
        }

        if (viewPagerAdapter != null) {
            viewPagerAdapter.notifyDataSetChanged();
        }
        AdShow.getInstance(this).ShowNativeAd(findViewById(R.id.nativeSmall).findViewById(R.id.native_ad_layout), AdUtils.NativeType.NATIVE_BANNER);
    }

    public class AccessFolder11 extends RecyclerView.Adapter<AccessFolder11.MyViewHolder> {
        boolean abool;
        Context context;
        ArrayList<FolderModel> folderList;
        boolean frombool;

        public AccessFolder11(Context context, ArrayList<FolderModel> arrayList, boolean z, boolean z2) {
            this.folderList = new ArrayList<>();
            this.context = context;
            this.folderList = arrayList;
            this.abool = z;
            this.frombool = z2;
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
                if (frombool) {
                    String folderPath = folderList.get(i).getFolderPath();
                    folderPath.substring(folderPath.lastIndexOf("/") + 1);
                    FName = folderPath;
                    UnLockImage();
                    return;
                }
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
                fileName = view.findViewById(R.id.fileName);
                fullPath = view.findViewById(R.id.fullPath);
            }
        }
    }

    public void CopyMoveData(String str, boolean z) {
        File file = new File(ImgPath);
        File file2 = new File(str + File.separator + file.getName());
        new MediaScanners(GalleryAppClass.getInstance(), file2);
        try {
            FileUtils.CopyMoveFile(file, file2);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!z) {
            Toast.makeText(this, getString(R.string.files_copy_successfully), Toast.LENGTH_SHORT).show();
            if (mediaFileType.equals("1")) {
                AppUtilsClass.ScanImageAlbumListData(this);
            } else if (mediaFileType.equals(ExifInterface.GPS_MEASUREMENT_3D)) {
                AppUtilsClass.ScanVideoAlbumListData(this);
            }
            AppUtilsClass.RefreshMoment(this);
        }
        if (z) {
            if (mediaFileType.equals("1")) {
                progressTag = "FromPermanentImageDelete";
                toastTag = "MoveImageData";
                new DeleteImageExecute(ImgPath).execute(new Void[0]);
            } else if (mediaFileType.equals(ExifInterface.GPS_MEASUREMENT_3D)) {
                progressTag = "FromPermanentVideoDelete";
                toastTag = "MoveVideoData";
                new DeleteVideoExecute(ImgPath).execute(new Void[0]);
            }
        }
    }

    public class DeleteImageExecute extends AsyncTask<Void, Void, Void> {
        String pathList;

        public DeleteImageExecute(String str) {
            progressPermanentImageDelete = new ProgressDialog(PreviewActivity.this);
            this.pathList = str;
        }

        @Override
        public void onPreExecute() {
            super.onPreExecute();
            progressPermanentImageDelete.setMessage(getString(R.string.please_wait_a_while));
            progressPermanentImageDelete.setProgressStyle(0);
            progressPermanentImageDelete.setIndeterminate(false);
            progressPermanentImageDelete.setCancelable(false);
            progressPermanentImageDelete.show();
        }

        @Override
        public Void doInBackground(Void... voidArr) {
            if (allImagesList.size() == 1) {
                currentPosition = 0;
            } else if (currentPosition == allImagesList.size()) {
                currentPosition--;
            }
            if (activityName.equals("MomentFragment")) {
                MomentFragment.momentAdapter.RemoveItemSingle(pathList);
                allImagesList.remove(selectedPos);
            }
            if (Build.VERSION.SDK_INT < 30) {
                return null;
            }
            ArrayList arrayList = new ArrayList();
            if (Build.VERSION.SDK_INT < 30) {
                return null;
            }
            arrayList.add(ContentUris.withAppendedId(MediaStore.Images.Media.getContentUri("external"), AppUtilsClass.getFilePathToMediaID(pathList, PreviewActivity.this)));
            AppUtilsClass.requestIntent(arrayList, PreviewActivity.this);
            return null;
        }

        @Override
        public void onPostExecute(Void r4) {
            super.onPostExecute(r4);
            if (Build.VERSION.SDK_INT != 30) {
                delpos = currentPosition;
                if (viewPagerAdapter != null) {
                    viewPagerAdapter.notifyDataSetChanged();
                }
                new Handler().postDelayed(() -> AppUtilsClass.RefreshImageAlbum(PreviewActivity.this), 500L);
                AppUtilsClass.RefreshMoment(PreviewActivity.this);
                progressPermanentImageDelete.dismiss();
                if (toastTag.equals("MoveImageData")) {
                    Toast.makeText(PreviewActivity.this, getString(R.string.files_move_successfully), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(PreviewActivity.this, getString(R.string.files_are_deleted_successfully), Toast.LENGTH_SHORT).show();
                }
                if (allImagesList.size() == 0) {
                    setResult(-1, getIntent());
                    finish();
                }
            }
        }
    }

    public class DeleteVideoExecute extends AsyncTask<Void, Void, Void> {
        String pathList;

        public DeleteVideoExecute(String str) {
            progressPermanentVideoDelete = new ProgressDialog(PreviewActivity.this);
            this.pathList = str;
        }

        @Override
        public void onPreExecute() {
            super.onPreExecute();
            progressPermanentVideoDelete.setMessage(getString(R.string.please_wait_a_while));
            progressPermanentVideoDelete.setProgressStyle(0);
            progressPermanentVideoDelete.setIndeterminate(false);
            progressPermanentVideoDelete.setCancelable(false);
            progressPermanentVideoDelete.show();
        }

        @Override
        public Void doInBackground(Void... voidArr) {
            if (allImagesList.size() == 1) {
                currentPosition = 0;
            } else if (currentPosition == allImagesList.size()) {
                currentPosition--;
            }
            if (activityName.equals("MomentFragment")) {
                MomentFragment.momentAdapter.RemoveItemSingle(pathList);
                allImagesList.remove(selectedPos);
            }
            if (Build.VERSION.SDK_INT < 30) {
                return null;
            }
            ArrayList arrayList = new ArrayList();
            if (Build.VERSION.SDK_INT < 30) {
                return null;
            }
            arrayList.add(ContentUris.withAppendedId(MediaStore.Video.Media.getContentUri("external"), AppUtilsClass.getFilePathToMediaID(pathList, PreviewActivity.this)));
            AppUtilsClass.requestIntent(arrayList, PreviewActivity.this);
            return null;
        }

        @Override
        public void onPostExecute(Void r4) {
            super.onPostExecute(r4);
            if (Build.VERSION.SDK_INT != 30) {
                delpos = currentPosition;
                if (viewPagerAdapter != null) {
                    viewPagerAdapter.notifyDataSetChanged();
                }
                new Handler().postDelayed(() -> AppUtilsClass.RefreshVideoAlbum(PreviewActivity.this), 500L);

                AppUtilsClass.RefreshMoment(PreviewActivity.this);
                progressPermanentVideoDelete.dismiss();
                if (toastTag.equals("MoveVideoData")) {
                    Toast.makeText(PreviewActivity.this, getString(R.string.files_move_successfully), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(PreviewActivity.this, getString(R.string.files_are_deleted_successfully), Toast.LENGTH_SHORT).show();
                }
                if (PreviewActivity.allImagesList.size() == 0) {
                    setResult(-1, getIntent());
                    finish();
                }
            }
        }
    }

    public void UnLockImage() {
        if (ImgPath.isEmpty()) {
            return;
        }
        ArrayList arrayList = new ArrayList();
        if (DocumentFile.fromSingleUri(this, Uri.parse(ImgPath)).exists() && DownloadForUnlock(this, ImgPath)) {
            arrayList.add(ImgPath);
        }
        runOnUiThread(() -> {
            try {
                TempDeleteImage();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        viewPagerAdapter.notifyDataSetChanged();
    }

    public boolean DownloadForUnlock(Context context, String str) {
        return copyFileInSavedDirForUnlock(context, str);
    }

    static boolean copyFileInSavedDirForUnlock(Context context, String str) {
        Uri fromFile = Uri.fromFile(new File(FName + File.separator + new File(str).getName()));
        try {
            InputStream openInputStream = context.getContentResolver().openInputStream(Uri.parse(str));
            OutputStream openOutputStream = context.getContentResolver().openOutputStream(fromFile, "w");
            byte[] bArr = new byte[1024];
            while (true) {
                int read = openInputStream.read(bArr);
                if (read > 0) {
                    openOutputStream.write(bArr, 0, read);
                } else {
                    openInputStream.close();
                    openOutputStream.flush();
                    openOutputStream.close();
                    Intent intent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
                    intent.setData(fromFile);
                    context.sendBroadcast(intent);
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void TempDeleteImage() {
        boolean z;
        ArrayList arrayList = new ArrayList();
        DocumentFile fromSingleUri = DocumentFile.fromSingleUri(this, Uri.parse(ImgPath));
        if (fromSingleUri.exists() && fromSingleUri.delete()) {
            arrayList.add(ImgPath);
            z = true;
        } else {
            z = false;
        }
        Iterator it = arrayList.iterator();
        while (it.hasNext()) {
            allImagesList.remove(it.next());
        }
        if (currentPosition == allImagesList.size()) {
            currentPosition = currentPosition - 1;
            allImagesList.remove(currentPosition);
            currentPosition = 0;
        } else {
            allImagesList.remove(currentPosition);
        }
        viewPagerAdapter = new ViewPagerAdapter(PreviewActivity.this, allImagesList);
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.setCurrentItem(currentPosition);
        if (!z) {
            Toast.makeText(this, "Couldn't delete some files.", Toast.LENGTH_SHORT).show();
        } else if (z) {
            Toast.makeText(this, getString(R.string.files_are_deleted_successfully), Toast.LENGTH_SHORT).show();
        }
        if (Build.VERSION.SDK_INT >= 30) {
            AppUtilsClass.RefreshMoment(this);
        }
    }

    public class ViewPagerAdapter extends PagerAdapter {
        ArrayList<Object> allDataList;
        Context context;
        private SingleListener<Boolean, Integer, Integer> singleListener;

        @Override
        public int getItemPosition(Object obj) {
            return -2;
        }

        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == obj;
        }

        public ViewPagerAdapter(Context context, ArrayList<Object> arrayList) {
            this.context = context;
            this.allDataList = arrayList;
        }

        @Override
        public int getCount() {
            return allDataList.size();
        }

        @Override
        public Object instantiateItem(ViewGroup viewGroup, final int i) {
            View inflate = LayoutInflater.from(context).inflate(R.layout.layout_viewpager_adapter, viewGroup, false);
            final GestureImageView gestureImageView = (GestureImageView) inflate.findViewById(R.id.imageView);
            final ImageView imageView = (ImageView) inflate.findViewById(R.id.dummyImg);
            String extension = FilenameUtils.getExtension(new File(((DataFileModel) allDataList.get(i)).path).getName());
            if (!extension.equals("gif") && !extension.equals("GIF")) {
                new RequestOptions().override(300, 300);
                Glide.with(context).asBitmap().load(((DataFileModel) allDataList.get(i)).path).into(new SimpleTarget<Bitmap>() {

                    public void onResourceReady(Bitmap bitmap, Transition<? super Bitmap> transition) {
                        gestureImageView.setImageBitmap(bitmap);
                        imageView.setVisibility(View.GONE);
                    }
                });
                gestureImageView.setOnClickListener(view -> {
                    try {
                        singleListener.onSingleCallback(true, Integer.valueOf(i), Integer.valueOf(i), view);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
                viewGroup.addView(inflate);
                return inflate;
            }
            new RequestOptions().override(300, 300);
            Glide.with(context).asBitmap().load(((DataFileModel) allDataList.get(i)).path).into(new SimpleTarget<Bitmap>() {
                public void onResourceReady(Bitmap bitmap, Transition<? super Bitmap> transition) {
                    gestureImageView.setImageBitmap(bitmap);
                    imageView.setVisibility(View.GONE);
                }
            });

            gestureImageView.setOnClickListener(view -> {
                try {
                    singleListener.onSingleCallback(true, Integer.valueOf(i), Integer.valueOf(i), view);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            viewGroup.addView(inflate);
            return inflate;
        }

        public void setItemClickCallback(SingleListener singleListener) {
            this.singleListener = singleListener;
        }

        @Override
        public void destroyItem(ViewGroup viewGroup, int i, Object obj) {
            viewGroup.removeView((LinearLayout) obj);
        }
    }


}
