package hdphoto.galleryimages.gelleryalbum.activity;

import static hdphoto.galleryimages.gelleryalbum.utils.Utils.imageBitmap;
import static hdphoto.galleryimages.gelleryalbum.utils.Utils.imageUri;
import static hdphoto.galleryimages.gelleryalbum.utils.Utils.stringMutableLiveData;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnticipateOvershootInterpolator;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.ChangeBounds;
import androidx.transition.TransitionManager;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.iten.tenoku.ad.AdShow;
import com.iten.tenoku.ad.HandleClick.HandleClick;
import com.iten.tenoku.utils.AdUtils;
import com.theartofdev.edmodo.cropper.CropImageView;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.io.IOException;

import hdphoto.galleryimages.gelleryalbum.constant.BitmapProcessing;
import hdphoto.galleryimages.gelleryalbum.filter.FilterListener;
import hdphoto.galleryimages.gelleryalbum.filter.FilterViewAdapter;
import hdphoto.galleryimages.gelleryalbum.fragment.EmojiFragment;
import hdphoto.galleryimages.gelleryalbum.fragment.PropertiesFragment;
import hdphoto.galleryimages.gelleryalbum.fragment.ShapeFragment;
import hdphoto.galleryimages.gelleryalbum.fragment.StickerFragment;
import hdphoto.galleryimages.gelleryalbum.fragment.TextEditorDialogFragment;
import hdphoto.galleryimages.gelleryalbum.utils.FileSaveHelper;
import hdphoto.galleryimages.gelleryalbum.tool.EditingToolsAdapter;
import hdphoto.galleryimages.gelleryalbum.tool.ToolType;
import hdphoto.galleryimages.gelleryalbum.utils.ShapeBuilder;
import hdphoto.galleryimages.gelleryalbum.utils.AppUtilsClass;
import hdphoto.galleryimages.gelleryalbum.utils.FolderPath;
import hdphoto.galleryimages.gelleryalbum.R;
import ja.burhanrashid52.photoeditor.OnPhotoEditorListener;
import ja.burhanrashid52.photoeditor.PhotoEditor;
import ja.burhanrashid52.photoeditor.PhotoEditorView;
import ja.burhanrashid52.photoeditor.PhotoFilter;
import ja.burhanrashid52.photoeditor.SaveSettings;
import ja.burhanrashid52.photoeditor.TextStyleBuilder;
import ja.burhanrashid52.photoeditor.ViewType;

public class EditImageActivity extends BaseActivity implements OnPhotoEditorListener, PropertiesFragment.Properties, ShapeFragment.Properties, EmojiFragment.EmojiListener, StickerFragment.StickerListener, EditingToolsAdapter.OnItemSelected, FilterListener {
    public static final String ACTION_NEXTGEN_EDIT = "action_nextgen_edit";
    public static final String PINCH_TEXT_SCALABLE_INTENT_KEY = "PINCH_TEXT_SCALABLE";
    Bitmap bmmain;
    CropImageView cropImageView;
    File filePath;
    EmojiFragment gEmojiBSFragment;
    boolean gIsFilterVisible;
    PhotoEditor gPhotoEditor;
    PhotoEditorView gPhotoEditorView;
    PropertiesFragment gPropertiesBSFragment;
    ConstraintLayout gRootView;
    RecyclerView gRvFilters;
    RecyclerView gRvTools;
    FileSaveHelper gSaveFileHelper;
    Uri gSaveImageUri;
    ShapeFragment gShapeBSFragment;
    ShapeBuilder gShapeBuilder;
    StickerFragment gStickerBSFragment;
    TextView gTxtCurrentTool;
    ImageView iv_camera,iv_close,iv_crop,iv_gallery,iv_redo,iv_share,iv_undo,iv_back;
    Uri mainuri;
    TextView tv_save;
    EditingToolsAdapter gEditingToolsAdapter = new EditingToolsAdapter(this);
    FilterViewAdapter gFilterViewAdapter = new FilterViewAdapter(this);
    ConstraintSet gConstraintSet = new ConstraintSet();
    String stringExtra = null;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        CreateFullScreen();
        setContentView(R.layout.activity_single_edit_image);

        initView();
        clickListener();
        observable();

        filePath = new File(FolderPath.SAVE_IMAGE_PATH);
        if (!filePath.exists()) {
            filePath.mkdirs();
        }

        HandleIntentImage(gPhotoEditorView.getSource());
        gPropertiesBSFragment = new PropertiesFragment();
        gEmojiBSFragment = new EmojiFragment();
        gStickerBSFragment = new StickerFragment();
        gShapeBSFragment = new ShapeFragment();
        gStickerBSFragment.setStickerListener(this);
        gEmojiBSFragment.setEmojiListener(this);
        gPropertiesBSFragment.setPropertiesChangeListener(this);
        gShapeBSFragment.setPropertiesChangeListener(this);
        gRvTools.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        gRvTools.setAdapter(gEditingToolsAdapter);
        gRvFilters.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        gRvFilters.setAdapter(gFilterViewAdapter);

        gPhotoEditor = new PhotoEditor.Builder(this, gPhotoEditorView).setPinchTextScalable(getIntent().getBooleanExtra(PINCH_TEXT_SCALABLE_INTENT_KEY, true)).build();
        gPhotoEditor.setOnPhotoEditorListener(this);

        stringExtra = getIntent().getStringExtra("imagepath");
        if (stringExtra != null) {
            gPhotoEditorView.getSource().setImageURI(Uri.parse(stringExtra));
        }
        gSaveFileHelper = new FileSaveHelper(this);
    }

    private void observable() {
        stringMutableLiveData.observe(this, s -> {
            if (s.equals("1")) {
                stringMutableLiveData.setValue("2");
                if (imageBitmap != null) {
                    bmmain = imageBitmap;
                    gSaveImageUri = imageUri;
                    mainuri = imageUri;
                    gPhotoEditor.clearAllViews();
                    gPhotoEditorView.getSource().setImageBitmap(imageBitmap);
                }
            }
        });
    }

    private void HandleIntentImage(ImageView imageView) {
        Uri data;
        Intent intent = getIntent();
        if (intent == null) {
            return;
        }
        if ("android.intent.action.EDIT".equals(intent.getAction()) || ACTION_NEXTGEN_EDIT.equals(intent.getAction())) {
            try {
                imageView.setImageBitmap(MediaStore.Images.Media.getBitmap(getContentResolver(), intent.getData()));
                return;
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }
        String type = intent.getType();
        if (type == null || !type.startsWith("image/") || (data = intent.getData()) == null) {
            return;
        }
        imageView.setImageURI(data);
    }

    private void initView() {
        gPhotoEditorView = findViewById(R.id.photoEditorView);
        gTxtCurrentTool = findViewById(R.id.txtCurrentTool);
        gRvTools = findViewById(R.id.rvConstraintTools);
        gRvFilters = findViewById(R.id.rvFilterView);
        gRootView = findViewById(R.id.rootView);
        iv_back = findViewById(R.id.img_back);
        iv_undo = findViewById(R.id.imgUndo);
        iv_redo = findViewById(R.id.imgRedo);
        iv_camera = findViewById(R.id.imgCamera);
        iv_gallery = findViewById(R.id.imgGallery);
        tv_save = findViewById(R.id.txtSave);
        iv_close = findViewById(R.id.imgClose);
        iv_share = findViewById(R.id.imgShare);
        iv_crop = findViewById(R.id.imgCrop);
        cropImageView = findViewById(R.id.cropImageView);
    }

    private void clickListener() {
        iv_back.setOnClickListener(view -> onBackPressed());

        iv_undo.setOnClickListener(view -> gPhotoEditor.undo());

        iv_redo.setOnClickListener(view -> gPhotoEditor.redo());

        iv_camera.setOnClickListener(view -> startActivity(new Intent(EditImageActivity.this, CameraActivity.class)));

        iv_gallery.setOnClickListener(view -> startActivityForResult(new Intent("android.intent.action.PICK", MediaStore.Images.Media.INTERNAL_CONTENT_URI), 53));

        tv_save.setOnClickListener(view -> SaveImage());

        iv_close.setOnClickListener(view -> onBackPressed());

        iv_share.setOnClickListener(view -> {
            if (gSaveImageUri == null) {
                SaveImage();
                new Handler().postDelayed(this::ShareImage, 1900L);
                return;
            }
            ShareImage();
        });

        iv_crop.setOnClickListener(view -> cropImage(Uri.fromFile(new File(stringExtra))));
    }

    @Override
    public void onEditTextChangeListener(final View view, String str, int i) {
        TextEditorDialogFragment.show(this, str, i).setOnTextEditorListener((str2, i2) -> {
            TextStyleBuilder textStyleBuilder = new TextStyleBuilder();
            textStyleBuilder.withTextColor(i);
            gPhotoEditor.editText(view, str, textStyleBuilder);
            gTxtCurrentTool.setText(R.string.label_text);
        });
    }

    @Override
    public void onAddViewListener(ViewType viewType, int i) {
    }

    @Override
    public void onRemoveViewListener(ViewType viewType, int i) {
    }

    @Override
    public void onStartViewChangeListener(ViewType viewType) {
    }

    @Override
    public void onStopViewChangeListener(ViewType viewType) {
    }

    public void ShareImage() {
        if (gSaveImageUri == null) {
            ShowSnackBar(getString(R.string.msg_save_image_to_share));
            return;
        }
        Intent intent = new Intent("android.intent.action.SEND");
        intent.setType("image/*");
        intent.putExtra("android.intent.extra.STREAM", buildFileProviderUri(gSaveImageUri));
        startActivity(Intent.createChooser(intent, getString(R.string.msg_share_image)));
    }

    private Uri buildFileProviderUri(Uri uri) {
        return FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".fileprovider", new File(uri.getPath()));
    }

    public void SaveImage() {
        if (ContextCompat.checkSelfPermission(this, "android.permission.WRITE_EXTERNAL_STORAGE") == 0 || ContextCompat.checkSelfPermission(this, "android.permission.READ_MEDIA_IMAGES") == 0) {
            showLoading("Saving...");
            File file = new File(this.filePath + File.separator + System.currentTimeMillis() + ".png");
            try {
                file.createNewFile();
                gPhotoEditor.saveAsFile(file.getAbsolutePath(), new SaveSettings.Builder().setClearViewsEnabled(true).setTransparencyEnabled(true).build(), new PhotoEditor.OnSaveListener() {
                    @Override
                    public void onSuccess(String str) {
                        HideLoading();
                        ShowSnackBar(getString(R.string.image_saved_successfully));
                        gSaveImageUri = Uri.fromFile(new File(str));
                        gPhotoEditorView.getSource().setImageURI(gSaveImageUri);
                        AppUtilsClass.insertUri(EditImageActivity.this, new File(gSaveImageUri.getPath()));
                        new Handler().postDelayed(() -> {
                            AppUtilsClass.RefreshMoment(EditImageActivity.this);
                            AppUtilsClass.RefreshImageAlbum(EditImageActivity.this);
                        }, 500L);
                    }

                    @Override
                    public void onFailure(Exception exc) {
                        HideLoading();
                        ShowSnackBar(getString(R.string.failed_to_save_image));
                    }
                });
                return;
            } catch (IOException e) {
                e.printStackTrace();
                HideLoading();
                ShowSnackBar(e.getMessage());
                return;
            }
        }
        requestPermission("android.permission.WRITE_EXTERNAL_STORAGE");
    }

    @Override
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i2 == -1) {
            if (i == 53) {
                try {
                    gPhotoEditor.clearAllViews();
                    gPhotoEditorView.getSource().setImageBitmap(MediaStore.Images.Media.getBitmap(getContentResolver(), intent.getData()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        if (i == 69) {
            if (i2 == -1) {
                handleUCropResult(intent);
            } else {
                setResultCancelled();
            }
        } else if (i != 96) {
            setResultCancelled();
        } else {
            UCrop.getError(intent);
            setResultCancelled();
        }
    }

    @Override
    public void onColorChanged(int i) {
        gTxtCurrentTool.setText(R.string.label_brush);
        gPhotoEditor.setBrushColor(i);
    }

    @Override
    public void onOpacityChanged(int i) {
        gTxtCurrentTool.setText(R.string.label_brush);
        gPhotoEditor.setOpacity(i);
    }

    @Override
    public void onShapeSizeChanged(int i) {
        gTxtCurrentTool.setText(R.string.label_brush);
        gPhotoEditor.setBrushSize(i);
    }

    @Override
    public void onEmojiClick(String str) {
        gPhotoEditor.addEmoji(str);
        gTxtCurrentTool.setText(R.string.label_emoji);
    }

    @Override
    public void onStickerClick(Bitmap bitmap) {
        gPhotoEditor.addImage(bitmap);
        gTxtCurrentTool.setText(R.string.label_sticker);
    }

    @Override
    public void isPermissionGranted(boolean z, String str) {
        if (z) {
            SaveImage();
        }
    }

    @Override
    public void onFilterSelected(PhotoFilter photoFilter) {
        gPhotoEditor.setFilterEffect(photoFilter);
    }

    @Override
    public void onTouchSourceImage(@NonNull MotionEvent motionEvent) {

    }

    static class ToolTypeMappings {
        static final int[] iArr;

        static {
            iArr = new int[ToolType.values().length];
            try {
                iArr[ToolType.SHAPE.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
                unused.printStackTrace();
            }
            try {
                iArr[ToolType.TEXT.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
                unused2.printStackTrace();
            }
            try {
                iArr[ToolType.ERASER.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
                unused3.printStackTrace();
            }
            try {
                iArr[ToolType.FILTER.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
                unused4.printStackTrace();
            }
            try {
                iArr[ToolType.EMOJI.ordinal()] = 5;
            } catch (NoSuchFieldError unused5) {
                unused5.printStackTrace();
            }
            try {
                iArr[ToolType.STICKER.ordinal()] = 6;
            } catch (NoSuchFieldError unused6) {
                unused6.printStackTrace();
            }
        }
    }

    @Override
    public void onToolSelected(ToolType eMToolType) {
        switch (ToolTypeMappings.iArr[eMToolType.ordinal()]) {
            case 1:
                gPhotoEditor.setBrushDrawingMode(true);
                gShapeBuilder = new ShapeBuilder();
                gTxtCurrentTool.setText(R.string.label_shape);
                ShowBSDialogFragment(gShapeBSFragment);
                return;
            case 2:
                TextEditorDialogFragment.show(this).setOnTextEditorListener((str, i) -> {
                    TextStyleBuilder textStyleBuilder = new TextStyleBuilder();
                    textStyleBuilder.withTextColor(i);
                    gPhotoEditor.addText(str, textStyleBuilder);
                    gTxtCurrentTool.setText(R.string.label_text);
                });
                return;
            case 3:
                if (!gPhotoEditor.isCacheEmpty()) {
                    gPhotoEditor.brushEraser();
                    gTxtCurrentTool.setText(R.string.label_eraser_mode);
                    return;
                }
                Toast.makeText(EditImageActivity.this, R.string.first_draw_something, Toast.LENGTH_SHORT).show();
                return;
            case 4:
                gTxtCurrentTool.setText(R.string.label_filter);
                ShowFilter(true);
                return;
            case 5:
                ShowBSDialogFragment(gEmojiBSFragment);
                return;
            case 6:
                ShowBSDialogFragment(gStickerBSFragment);
                return;
            default:
                return;
        }
    }

    private void ShowBSDialogFragment(BottomSheetDialogFragment bottomSheetDialogFragment) {
        if (bottomSheetDialogFragment == null || bottomSheetDialogFragment.isAdded()) {
            return;
        }
        bottomSheetDialogFragment.show(getSupportFragmentManager(), bottomSheetDialogFragment.getTag());
    }

    void ShowFilter(boolean z) {
        gIsFilterVisible = z;
        gConstraintSet.clone(gRootView);
        if (z) {
            gConstraintSet.clear(gRvFilters.getId(), 6);
            gConstraintSet.connect(gRvFilters.getId(), 6, 0, 6);
            gConstraintSet.connect(gRvFilters.getId(), 7, 0, 7);
        } else {
            gConstraintSet.connect(gRvFilters.getId(), 6, 0, 7);
            gConstraintSet.clear(gRvFilters.getId(), 7);
        }
        ChangeBounds changeBounds = new ChangeBounds();
        changeBounds.setDuration(350L);
        changeBounds.setInterpolator(new AnticipateOvershootInterpolator(1.0f));
        TransitionManager.beginDelayedTransition(gRootView, changeBounds);
        gConstraintSet.applyTo(gRootView);
    }

    @Override
    public void onBackPressed() {
        AdShow.getInstance(EditImageActivity.this).ShowAd(new HandleClick() {
            @Override
            public void Show(boolean adShow) {
                if (gIsFilterVisible) {
                    ShowFilter(false);
                    gTxtCurrentTool.setText(R.string.app_name);
                    return;
                }
                EditImageActivity.super.onBackPressed();
            }
        }, AdUtils.ClickType.BACK_CLICK);

    }

    public void cropImage(Uri uri) {
        Uri fromFile = Uri.fromFile(new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString(), new File(uri.toString()).getName()));
        UCrop.Options options = new UCrop.Options();
        options.setCompressionQuality(80);
        options.setToolbarColor(ContextCompat.getColor(this, R.color.black));
        options.setToolbarWidgetColor(ContextCompat.getColor(this, R.color.white));
        options.setToolbarTitle("Crop Image");
        options.setActiveControlsWidgetColor(ContextCompat.getColor(this, R.color.yellow_theme));
        options.setStatusBarColor(ContextCompat.getColor(this, R.color.black));
        options.setCropFrameColor(ContextCompat.getColor(this, R.color.yellow_theme));
        options.setCropGridColor(ContextCompat.getColor(this, R.color.yellow_theme));
        UCrop.of(uri, fromFile).withOptions(options).start(this);
    }

    private void handleUCropResult(Intent intent) {
        if (intent == null) {
            setResultCancelled();
            return;
        }
        mainuri = UCrop.getOutput(intent);
        try {
            bmmain = MediaStore.Images.Media.getBitmap(getContentResolver(), mainuri);
            if (bmmain == null) {
                try {
                    Toast.makeText(EditImageActivity.this, R.string.couldn_t_handle_this_image_it_has_large_size, Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                bmmain = BitmapProcessing.resizeBitmap(bmmain, bmmain.getWidth(), (bmmain.getHeight() * bmmain.getWidth()) / bmmain.getWidth());
                gPhotoEditorView.getSource().setImageBitmap(bmmain);
            }
        } catch (IOException e2) {
            e2.printStackTrace();
        }
    }

    private void setResultCancelled() {
        setResult(0, new Intent());
    }
}
