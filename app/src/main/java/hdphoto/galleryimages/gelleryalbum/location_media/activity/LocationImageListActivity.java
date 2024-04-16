package hdphoto.galleryimages.gelleryalbum.location_media.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.RecoverableSecurityException;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.database.Cursor;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Objects;

import hdphoto.galleryimages.gelleryalbum.R;
import hdphoto.galleryimages.gelleryalbum.databinding.ActivityLocationImageListBinding;
import hdphoto.galleryimages.gelleryalbum.location_media.adapter.PictureAdapter;
import hdphoto.galleryimages.gelleryalbum.location_media.common.Constant;
import hdphoto.galleryimages.gelleryalbum.location_media.event.DeleteEvent;
import hdphoto.galleryimages.gelleryalbum.location_media.event.DisplayDeleteEvent;
import hdphoto.galleryimages.gelleryalbum.location_media.model.AlbumData;
import hdphoto.galleryimages.gelleryalbum.location_media.model.LocationImageData;
import hdphoto.galleryimages.gelleryalbum.location_media.model.PictureData;
import hdphoto.galleryimages.gelleryalbum.location_media.rx.RxBus;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class LocationImageListActivity extends AppCompatActivity {
    private ActivityLocationImageListBinding binding;
    private ProgressDialog loadingDialog;
    private LocationImageData locationImageData;
    private PictureAdapter pictureAdapter;
    public  static ArrayList<Object> pictureData = new ArrayList<>();

    public static void DeleteEvent(Throwable th) {
    }

    public static void DisplayDeleteEvent(Throwable th) {
    }

    public static void onCreate() {
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        binding = ActivityLocationImageListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        LocationImageListActivity.onCreate();

        locationImageData = (LocationImageData) getIntent().getSerializableExtra("data");
        binding.locationName.setText(((LocationImageData) Objects.requireNonNull(this.locationImageData)).getTitle());
        pictureData.addAll(this.locationImageData.getPictureData());
        initView();
        initAdapter();
        initListener();
        DisplayDeleteEvent();
        DeleteEvent();
    }

    private void initView() {
        ProgressDialog progressDialog = new ProgressDialog(this);
        this.loadingDialog = progressDialog;
        progressDialog.requestWindowFeature(1);
        loadingDialog.setCancelable(false);
        loadingDialog.setMessage(getString(R.string.Delete_images));
        loadingDialog.setCanceledOnTouchOutside(false);
    }

    private void DeleteEvent() {
        RxBus.getInstance().addSubscription(this, RxBus.getInstance().toObservable(DeleteEvent.class).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).distinctUntilChanged().subscribe(new Action1() { // from class: com.gallery.photos.pro.activity.LocationImageListActivity$$ExternalSyntheticLambda8
            @Override
            public void call(Object obj) {
                clickevent((DeleteEvent) obj);
            }
        }, new Action1() {
            @Override
            public void call(Object obj) {
                DeleteEvent((Throwable) obj);
            }
        }));
    }

    public void clickevent(DeleteEvent deleteEvent) {
        if (deleteEvent.getPos() == -1 || deleteEvent.getDeleteList() == null || deleteEvent.getDeleteList().size() == 0) {
            return;
        }
        ArrayList<String> deleteList = deleteEvent.getDeleteList();
        ArrayList<Object> arrayList = pictureData;
        if (arrayList == null || arrayList.size() == 0) {
            return;
        }
        for (int i = 0; i < deleteList.size(); i++) {
            int i2 = 0;
            while (i2 < pictureData.size()) {
                if ((pictureData.get(i2) instanceof PictureData) && ((PictureData) pictureData.get(i2)).getFilePath().equalsIgnoreCase(deleteList.get(i))) {
                    boolean z = i2 != 0 ? pictureData.get(i2 - 1) instanceof AlbumData : false;
                    boolean z2 = i2 < pictureData.size() + (-2) ? pictureData.get(i2 + 1) instanceof AlbumData : false;
                    if (z && z2) {
                        pictureData.remove(i2);
                        pictureData.remove(i2 - 1);
                    } else if (i2 == pictureData.size() - 1) {
                        pictureData.remove(i2);
                        if (z) {
                            pictureData.remove(i2 - 1);
                        }
                    } else {
                        pictureData.remove(i2);
                    }
                    if (i2 != 0) {
                        i2--;
                    }
                    if (i == deleteList.size() - 1) {
                        break;
                    }
                }
                i2++;
            }
        }
        if (pictureAdapter != null) {
            pictureAdapter.notifyDataSetChanged();
        }
        ArrayList<Object> arrayList2 = pictureData;
        if (arrayList2 != null && arrayList2.size() != 0) {
            binding.locationRecycler.setVisibility(View.VISIBLE);
            return;
        }
        binding.locationRecycler.setVisibility(View.GONE);
        onBackPressed();
    }

    private void DisplayDeleteEvent() {
        RxBus.getInstance().addSubscription(this, RxBus.getInstance().toObservable(DisplayDeleteEvent.class).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).distinctUntilChanged().subscribe(new Action1() { // from class: com.gallery.photos.pro.activity.LocationImageListActivity$$ExternalSyntheticLambda2
            @Override
            public void call(Object obj) {
                disdeleEvent((DisplayDeleteEvent) obj);
            }
        }, new Action1() {
            @Override
            public void call(Object obj) {
                DisplayDeleteEvent((Throwable) obj);
            }
        }));
    }

    public void disdeleEvent(DisplayDeleteEvent displayDeleteEvent) {
        if (displayDeleteEvent.getDeleteList() == null || displayDeleteEvent.getDeleteList().size() == 0) {
            return;
        }
        ArrayList<String> deleteList = displayDeleteEvent.getDeleteList();
        ArrayList<Object> arrayList = pictureData;
        if (arrayList == null || arrayList.size() == 0) {
            return;
        }
        for (int i = 0; i < deleteList.size(); i++) {
            int i2 = 0;
            while (i2 < this.pictureData.size()) {
                if ((this.pictureData.get(i2) instanceof PictureData) && ((PictureData) this.pictureData.get(i2)).getFilePath().equalsIgnoreCase(deleteList.get(i))) {
                    boolean z = i2 != 0 ? this.pictureData.get(i2 - 1) instanceof AlbumData : false;
                    boolean z2 = i2 < this.pictureData.size() + (-2) ? this.pictureData.get(i2 + 1) instanceof AlbumData : false;
                    if (z && z2) {
                        this.pictureData.remove(i2);
                        this.pictureData.remove(i2 - 1);
                    } else if (i2 == this.pictureData.size() - 1) {
                        this.pictureData.remove(i2);
                        if (z) {
                            this.pictureData.remove(i2 - 1);
                        }
                    } else {
                        this.pictureData.remove(i2);
                    }
                    if (i2 != 0) {
                        i2--;
                    }
                    if (i == deleteList.size() - 1) {
                        break;
                    }
                }
                i2++;
            }
        }
        PictureAdapter pictureAdapter = this.pictureAdapter;
        if (pictureAdapter != null) {
            pictureAdapter.notifyDataSetChanged();
        }

        ArrayList<Object> arrayList2 = this.pictureData;
        if (arrayList2 != null && arrayList2.size() != 0) {
            this.binding.locationRecycler.setVisibility(View.VISIBLE);
            return;
        }
        this.binding.locationRecycler.setVisibility(View.GONE);
        onBackPressed();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        RxBus.getInstance().unSubscribe(this);
    }

    private void initListener() {

        this.binding.ivCloseSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public final void onClick(View view) {
                for (int i = 0; i < pictureData.size(); i++) {
                    if (pictureData.get(i) != null && (pictureData.get(i) instanceof PictureData)) {
                        PictureData pictureDatas = (PictureData) pictureData.get(i);
                        pictureDatas.setCheckboxVisible(false);
                        pictureDatas.setSelected(false);
                    }
                }
                if (pictureAdapter != null) {
                    pictureAdapter.notifyDataSetChanged();
                }
                binding.toolbar.setVisibility(View.VISIBLE);
                binding.selectToolbar.setVisibility(View.GONE);
            }
        });
        this.binding.imageShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<Uri> arrayList = new ArrayList<>();
                Intent intent = new Intent("android.intent.action.SEND_MULTIPLE");
                for (int i = 0; i < pictureData.size(); i++) {
                    if (pictureData.get(i) != null && (pictureData.get(i) instanceof PictureData)) {
                        PictureData pictureDatadd = (PictureData) pictureData.get(i);
                        if (pictureDatadd.isSelected()) {
                            arrayList.add(FileProvider.getUriForFile(LocationImageListActivity.this, getPackageName() + ".fileprovider", new File(pictureDatadd.getFilePath())));
                        }
                    }
                }
                intent.setType("*/*");
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.putExtra("android.intent.extra.ALLOW_MULTIPLE", true);
                intent.putParcelableArrayListExtra("android.intent.extra.STREAM", arrayList);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(Intent.createChooser(intent, getString(R.string.share_with)));
            }
        });
        this.binding.imageDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= 30) {
                    deleteFileOnAboveQ();
                } else {
                    showDeleteDialog();
                }
            }
        });

        binding.icBack.setOnClickListener(v -> {
            onBackPressed();
        });
    }


    private void showDeleteDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.delete_image_msg));
        builder.setCancelable(false);
        builder.setPositiveButton("Yes", new AnonymousClass2());
        builder.setNegativeButton(getString(R.string.No), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.show();
    }


    public class AnonymousClass2 implements DialogInterface.OnClickListener {

        public void lambda$deletePhoto$0(String str, Uri uri) {
        }

        AnonymousClass2() {
        }

        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            dialogInterface.dismiss();
            if (LocationImageListActivity.this.loadingDialog != null && !LocationImageListActivity.this.loadingDialog.isShowing()) {
                LocationImageListActivity.this.loadingDialog.setMessage(LocationImageListActivity.this.getString(R.string.Delete_images));
                LocationImageListActivity.this.loadingDialog.show();
            }
            new Thread(new Runnable() {
                @Override
                public void run() {
                    AnonymousClass2.this.deletePhoto();
                }
            }).start();
        }

        public void deletePhoto() {
            final ArrayList arrayList = new ArrayList();
            for (int i = 0; i < LocationImageListActivity.this.pictureData.size(); i++) {
                if (LocationImageListActivity.this.pictureData.get(i) != null && (LocationImageListActivity.this.pictureData.get(i) instanceof PictureData)) {
                    PictureData pictureData = (PictureData) LocationImageListActivity.this.pictureData.get(i);
                    if (pictureData.isSelected()) {
                        File file = new File(pictureData.getFilePath());
                        LocationImageListActivity.this.getContentResolver().delete(FileProvider.getUriForFile(LocationImageListActivity.this, LocationImageListActivity.this.getApplicationContext().getPackageName() + ".fileprovider", file), null, null);
                        try {
                            file.delete();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        try {
                            FileUtils.deleteDirectory(file);
                        } catch (Exception e2) {
                            e2.printStackTrace();
                        }
                        MediaScannerConnection.scanFile(LocationImageListActivity.this, new String[]{file.getPath()}, null, new MediaScannerConnection.OnScanCompletedListener() { // from class: com.gallery.photos.pro.activity.LocationImageListActivity$2$$ExternalSyntheticLambda1
                            @Override
                            public void onScanCompleted(String str, Uri uri) {
                                lambda$deletePhoto$0(str, uri);
                            }
                        });
                        arrayList.add(file.getPath());
                    } else {
                        pictureData.setCheckboxVisible(false);
                    }
                }
            }
            int i2 = 0;
            while (i2 < pictureData.size()) {
                if (pictureData.get(i2) != null && (LocationImageListActivity.this.pictureData.get(i2) instanceof PictureData) && ((PictureData) LocationImageListActivity.this.pictureData.get(i2)).isSelected()) {
                    boolean z = i2 != 0 ? LocationImageListActivity.this.pictureData.get(i2 - 1) instanceof AlbumData : false;
                    boolean z2 = i2 < LocationImageListActivity.this.pictureData.size() + (-2) ? LocationImageListActivity.this.pictureData.get(i2 + 1) instanceof AlbumData : false;
                    if (z && z2) {
                        pictureData.remove(i2);
                        pictureData.remove(i2 - 1);
                    } else if (i2 == pictureData.size() - 1) {
                        pictureData.remove(i2);
                        if (z) {
                            pictureData.remove(i2 - 1);
                        }
                    } else {
                        pictureData.remove(i2);
                    }
                    if (i2 != 0) {
                        i2--;
                    }
                }
                i2++;
            }
            runOnUiThread(new Runnable() {
                @Override 
                public final void run() {
                    RxBus.getInstance().post(new DeleteEvent(1, arrayList));
                    setClose();
                    if (loadingDialog != null && loadingDialog.isShowing()) {
                        loadingDialog.dismiss();
                    }
                    pictureAdapter.notifyDataSetChanged();
                    if (pictureData == null || pictureData.size() == 0) {
                        binding.locationRecycler.setVisibility(View.GONE);
                        onBackPressed();
                    } else {
                        binding.locationRecycler.setVisibility(View.VISIBLE);
                    }
                    Toast.makeText(LocationImageListActivity.this, getString(R.string.Delete_image_successfully), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i == 2222 && i2 == -1) {
            final ArrayList arrayList = new ArrayList();
            int i3 = 0;
            while (i3 < this.pictureData.size()) {
                if (this.pictureData.get(i3) != null && (pictureData.get(i3) instanceof PictureData)) {
                    PictureData pictureDatadd = (PictureData) pictureData.get(i3);
                    pictureDatadd.setCheckboxVisible(false);
                    if (pictureDatadd.isSelected()) {
                        arrayList.add(pictureDatadd.getFilePath());
                        boolean z = i3 != 0 ? pictureData.get(i3 - 1) instanceof AlbumData : false;
                        boolean z2 = i3 < pictureData.size() + (-2) ? pictureData.get(i3 + 1) instanceof AlbumData : false;
                        if (z && z2) {
                            pictureData.remove(i3);
                            pictureData.remove(i3 - 1);
                        } else if (i3 == pictureData.size() - 1) {
                            pictureData.remove(i3);
                            if (z) {
                                pictureData.remove(i3 - 1);
                            }
                        } else {
                            pictureData.remove(i3);
                        }
                        if (i3 != 0) {
                            i3--;
                        }
                    }
                }
                i3++;
            }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    RxBus.getInstance().post(new DeleteEvent(1, arrayList));
                    setClose();
                    ProgressDialog progressDialog = loadingDialog;
                    if (progressDialog != null && progressDialog.isShowing()) {
                        loadingDialog.dismiss();
                    }
                    pictureAdapter.notifyDataSetChanged();
                    if (binding.selectToolbar.getVisibility() == View.VISIBLE) {
                        setClose();
                    }
                    Toast.makeText(LocationImageListActivity.this, getString(R.string.Delete_image_successfully), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public void deleteFileOnAboveQ() {
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < pictureData.size(); i++) {
            if (pictureData.get(i) != null && (pictureData.get(i) instanceof PictureData)) {
                PictureData pictureDatadd = (PictureData) pictureData.get(i);
                if (pictureDatadd.isSelected()) {
                    File file = new File(pictureDatadd.getFilePath());
                    if (pictureDatadd.isVideo()) {
                        arrayList.add(getVideoUriFromFile(file.getPath(), this));
                    } else {
                        arrayList.add(getImageUriFromFile(file.getPath(), this));
                    }
                }
            }
        }
        try {
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    startIntentSenderForResult(MediaStore.createDeleteRequest(getContentResolver(), arrayList).getIntentSender(), 2222, null, 0, 0, 0, null);
                }
            } catch (IntentSender.SendIntentException e) {
                e.printStackTrace();
            }
        } catch (SecurityException e2) {
            e2.printStackTrace();
            RecoverableSecurityException recoverableSecurityException = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                recoverableSecurityException = (RecoverableSecurityException) e2;
            }
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    startIntentSenderForResult(recoverableSecurityException.getUserAction().getActionIntent().getIntentSender(), 2221, null, 0, 0, 0, null);
                }
            } catch (IntentSender.SendIntentException e3) {
                e3.printStackTrace();
            }
        }
    }

    public Uri getImageUriFromFile(String str, Context context) {
        ContentResolver contentResolver = context.getContentResolver();
        Cursor query = contentResolver.query(MediaStore.Images.Media.getContentUri("external"), new String[]{"_id"}, "_data = ?", new String[]{str}, "date_added desc");
        query.moveToFirst();
        if (query.isAfterLast()) {
            query.close();
            ContentValues contentValues = new ContentValues();
            contentValues.put("_data", str);
            return contentResolver.insert(MediaStore.Images.Media.getContentUri("external"), contentValues);
        }
        @SuppressLint("Range") Uri build = MediaStore.Images.Media.getContentUri("external").buildUpon().appendPath(Integer.toString(query.getInt(query.getColumnIndex("_id")))).build();
        query.close();
        return build;
    }

    public Uri getVideoUriFromFile(String str, Context context) {
        ContentResolver contentResolver = context.getContentResolver();
        Cursor query = contentResolver.query(MediaStore.Video.Media.getContentUri("external"), new String[]{"_id"}, "_data = ?", new String[]{str}, "date_added desc");
        query.moveToFirst();
        if (query.isAfterLast()) {
            query.close();
            ContentValues contentValues = new ContentValues();
            contentValues.put("_data", str);
            return contentResolver.insert(MediaStore.Video.Media.getContentUri("external"), contentValues);
        }
        @SuppressLint("Range") Uri build = MediaStore.Video.Media.getContentUri("external").buildUpon().appendPath(Integer.toString(query.getInt(query.getColumnIndex("_id")))).build();
        query.close();
        return build;
    }

    private void initAdapter() {
        pictureAdapter = new PictureAdapter(this, pictureData, new PictureAdapter.OnSelectPicture() {
            @Override
            public void onSelectPicture(int i) {
                if (pictureData.get(i) instanceof PictureData) {
                    PictureData pictureDatad = (PictureData) pictureData.get(i);
                    if (pictureDatad.isCheckboxVisible()) {
                        pictureDatad.setSelected(!pictureDatad.isSelected());
                        pictureAdapter.notifyItemChanged(i);
                        LocationImageListActivity.this.setSelectedFile();
                        return;
                    }
                    int i2 = -1;
                    ArrayList arrayList = new ArrayList();
                    for (int i3 = 0; i3 < LocationImageListActivity.this.pictureData.size(); i3++) {
                        if (LocationImageListActivity.this.pictureData.get(i3) instanceof PictureData) {
                            arrayList.add((PictureData) LocationImageListActivity.this.pictureData.get(i3));
                            if (i == i3) {
                                i2 = arrayList.size() - 1;
                            }
                        }
                    }
                    Constant.displayImageList = new ArrayList<>();
                    Constant.displayImageList.addAll(arrayList);
                    Intent intent = new Intent(LocationImageListActivity.this, ImageShowActivity.class);
                    intent.putExtra("pos", i2);
//                    intent.putExtra("name",Constant.displayImageList.get(i2).getFileName());
                    startActivity(intent);

                }
            }

            @Override
            public void onLongClickPicture(int i) {
                if (pictureData.get(i) instanceof PictureData) {
                    PictureData pictureData = (PictureData) LocationImageListActivity.this.pictureData.get(i);
                    for (int i2 = 0; i2 < LocationImageListActivity.this.pictureData.size(); i2++) {
                        if (LocationImageListActivity.this.pictureData.get(i2) != null && (LocationImageListActivity.this.pictureData.get(i2) instanceof PictureData)) {
                            ((PictureData) LocationImageListActivity.this.pictureData.get(i2)).setCheckboxVisible(true);
                        }
                    }
                    pictureData.setCheckboxVisible(true);
                    pictureData.setSelected(true);
                    pictureAdapter.notifyDataSetChanged();
                    setSelectedFile();
                }
            }
        });
        GridLayoutManager gridLayoutManager = new GridLayoutManager((Context) this, 2, RecyclerView.VERTICAL, false);
        this.binding.locationRecycler.setLayoutManager(gridLayoutManager);
       /* gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() { // from class: com.gallery.photos.pro.activity.LocationImageListActivity.4
            @Override // androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
            public int getSpanSize(int i) {
                if (LocationImageListActivity.this.pictureAdapter.getItemViewType(i) == 1) {
                    return PreferencesManager.getImageGrid(LocationImageListActivity.this);
                }
                return 1;
            }
        });*/
        this.binding.locationRecycler.setAdapter(this.pictureAdapter);
    }

    public void setSelectedFile() {
        int i = 0;
        for (int i2 = 0; i2 < this.pictureData.size(); i2++) {
            if (this.pictureData.get(i2) != null && (this.pictureData.get(i2) instanceof PictureData) && ((PictureData) this.pictureData.get(i2)).isSelected()) {
                i++;
            }
        }
        if (i == 0) {
            this.binding.toolbar.setVisibility(View.VISIBLE);
            this.binding.selectToolbar.setVisibility(View.GONE);
            setClose();
        } else {
            this.binding.toolbar.setVisibility(View.GONE);
            this.binding.selectToolbar.setVisibility(View.VISIBLE);
        }
        TextView customTextView = this.binding.textSelect;
        customTextView.setText(i + " " + getString(R.string.Selected_toolbar));
    }

    public void setClose() {
        for (int i = 0; i < this.pictureData.size(); i++) {
            if (this.pictureData.get(i) != null && (this.pictureData.get(i) instanceof PictureData)) {
                PictureData pictureData = (PictureData) this.pictureData.get(i);
                pictureData.setCheckboxVisible(false);
                pictureData.setSelected(false);
            }
        }
        PictureAdapter pictureAdapter = this.pictureAdapter;
        if (pictureAdapter != null) {
            pictureAdapter.notifyDataSetChanged();
        }
        this.binding.toolbar.setVisibility(View.VISIBLE);
        this.binding.selectToolbar.setVisibility(View.GONE);
    }

    @Override
    public void onBackPressed() {
        if (this.binding.selectToolbar.getVisibility() == View.VISIBLE) {
            setClose();
            return;
        }
        ArrayList<PictureData> arrayList = new ArrayList<>();
        for (int i = 0; i < this.pictureData.size(); i++) {
            if (this.pictureData.get(i) instanceof PictureData) {
                arrayList.add((PictureData) this.pictureData.get(i));
            }
        }
        this.locationImageData.setPictureData(arrayList);
        Intent intent = new Intent();
        intent.putExtra("data", this.locationImageData);
        setResult(-1, intent);
        super.onBackPressed();
    }
}