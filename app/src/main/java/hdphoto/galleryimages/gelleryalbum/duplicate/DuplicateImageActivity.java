package hdphoto.galleryimages.gelleryalbum.duplicate;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.iten.tenoku.ad.AdShow;
import com.iten.tenoku.ad.HandleClick.HandleClick;
import com.iten.tenoku.utils.AdUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import hdphoto.galleryimages.gelleryalbum.R;
import hdphoto.galleryimages.gelleryalbum.activity.SingleImageActivity;

public class DuplicateImageActivity extends AppCompatActivity implements SimilarClickListener {
    public static ArrayList<pictureFacer> getimgList = new ArrayList<>();
    public static LinearLayout layout_delete;
    private static TextView txt_selcted_count;
    TextView btCancel;
    TextView btDelete;
    Button btnScan;
    Dialog deleteDialog;
    ProgressDialog progressDialog;
    GridLayoutManager gridLayoutManager;
    RecyclerView.ItemDecoration itemDecoration;
    ImageView ivBack;
    private ImageView iv_delete, iv_refresh;
    DuplicateImageFindTask mDuplicateImageFindTask;
    protected ProgressBar mScanProgressBar;
    RecyclerView rvSimilar;
    SimilarImageAdapter similarImageAdapter;
    private File mCameraDir = Environment.getExternalStorageDirectory();
    private int mCurrentProgress = 0;
    private boolean mDuplicateImageScanFinished = false;
    public LinkedHashMap<String, ArrayList<pictureFacer>> DataHashMap = new LinkedHashMap<>();
    ArrayList<pictureFacer> mList = new ArrayList<>();
    public List<Object> photosWith = new ArrayList();
    public ArrayList<pictureFacer> PassData = new ArrayList<>();
    ArrayList<pictureFacer> mDeleteList = new ArrayList<>();
    private Handler mUpdateHandler = new Handler();
    private Runnable mUpdateRunnable = new Runnable() {
        @Override
        public void run() {
            if (mScanProgressBar != null) {
                mScanProgressBar.setProgress(mCurrentProgress);
            }
            if (mDuplicateImageScanFinished) {
                return;
            }
            mUpdateHandler.postDelayed(mUpdateRunnable, 33L);
        }
    };
    private DuplicateImageFindTask.DuplicateFindCallback mDuplicateFindCallback = new DuplicateImageFindTask.DuplicateFindCallback() {
        @Override
        public void onDuplicateFindProgressUpdate(double d) {
        }

        @Override
        public void onDuplicateFindStart(String str, int i) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    progressDialog.show();
                }
            });
        }

        @Override
        public void onDuplicateFindExecute(String str, long j) {
            Log.e("Duplicate--)", "Exceute");
        }

        @Override
        public void onDuplicateFindFinished(int i, long j) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                   progressDialog.dismiss();
                   setData();
                }
            });
        }

        @Override
        public void onDuplicateSectionFind(DuplicateImageFindTask.SectionItem sectionItem) {
            for (int i = 0; i < sectionItem.getImages().size(); i++) {
                pictureFacer picturefacer = new pictureFacer();
                picturefacer.setPicturePath(sectionItem.getImages().get(i).getImagePath());
                picturefacer.setLength((double) sectionItem.getImages().get(i).getImageSize());
                if (DataHashMap.containsKey(sectionItem.getHeader())) {
                    ArrayList<pictureFacer> arrayList = DataHashMap.get(sectionItem.getHeader());
                    arrayList.add(picturefacer);
                    DataHashMap.put(sectionItem.getHeader(), arrayList);
                } else {
                    ArrayList<pictureFacer> arrayList2 = new ArrayList<>();
                    arrayList2.add(picturefacer);
                    DataHashMap.put(sectionItem.getHeader(), arrayList2);
                }
                mList.add(picturefacer);
            }
        }
    };

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_duplicate_image);
        init();
        itemDecoration = new SpacesItemDecoration(2);
        gridLayoutManager = new GridLayoutManager(getApplicationContext(), 3, RecyclerView.VERTICAL, false);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int i) {
                return similarImageAdapter.getItemViewType(i) != 2 ? 3 : 1;
            }
        });
        rvSimilar.setLayoutManager(gridLayoutManager);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        mDuplicateImageFindTask = new DuplicateImageFindTask(mDuplicateFindCallback, getApplicationContext());
        File file = mCameraDir;
        if (file != null) {
            String absolutePath = file.getAbsolutePath();
            Log.d("Start scan ", "" + absolutePath);
            mDuplicateImageFindTask.execute(absolutePath);
        }
        mUpdateHandler.post(mUpdateRunnable);
        if (SimilarImageAdapter.checkLong) {
            layout_delete.setVisibility(View.GONE);
        } else {
            layout_delete.setVisibility(View.VISIBLE);
        }
        iv_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SimilarImageAdapter.checkLong = true;
                layout_delete.setVisibility(View.GONE);
                similarImageAdapter = new SimilarImageAdapter(DuplicateImageActivity.this, photosWith, PassData, DuplicateImageActivity.this);
                rvSimilar.setLayoutManager(gridLayoutManager);
                rvSimilar.setAdapter(similarImageAdapter);
            }
        });
        iv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteDialog.show();
                mDeleteList = similarImageAdapter.getDeletedData();
            }
        });
        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteDialog.dismiss();
            }
        });
        btDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callDelete(mDeleteList);
            }
        });
    }

    public void callDelete(ArrayList<pictureFacer> arrayList) {
        for (int i = 0; i < arrayList.size(); i++) {
            pictureFacer picturefacer = arrayList.get(i);
            Log.e("DeletePath", "" + picturefacer.getPicturePath());
            File file = new File(picturefacer.getPicturePath());
            try {
                deleteDialog.dismiss();
                if (file.delete()) {
                    try {
                        if (Build.VERSION.SDK_INT >= 19) {
                            Intent intent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
                            intent.setData(Uri.fromFile(file));
                            getApplicationContext().sendBroadcast(intent);
                        } else {
                            getApplicationContext().sendBroadcast(new Intent("android.intent.action.MEDIA_MOUNTED", Uri.fromFile(file)));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e("Error Files--)", "" + e.getMessage());
                    }
                }
            } catch (Exception e2) {
                Log.e("tag", e2.getMessage());
            }
        }
        SimilarImageAdapter.checkLong = true;
        DataHashMap = new LinkedHashMap<>();
        layout_delete.setVisibility(View.GONE);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mDuplicateImageFindTask = new DuplicateImageFindTask(mDuplicateFindCallback, getApplicationContext());
                mDuplicateImageFindTask.execute(mCameraDir.getAbsolutePath());
            }
        });
    }

    @Override
    public void onPicClicked(int i, ArrayList<pictureFacer> arrayList, String str) {
        int i2 = 0;
        for (int i3 = 0; i3 < arrayList.size(); i3++) {
            if (str.equals(arrayList.get(i3).getPicturePath())) {
                Log.e("Index--)", "" + i3);
                i2 = i3;
            }
        }
        getimgList = arrayList;
        try {
            Intent intent = new Intent(this, SingleImageActivity.class);
            intent.putExtra("Pos", String.valueOf(i2));
            intent.putExtra("From", "Duplicate");
            startActivityForResult(intent, 2);
        } catch (Exception unused) {
        }
    }

    public class SpacesItemDecoration extends RecyclerView.ItemDecoration {
        private int space;

        public SpacesItemDecoration(int i) {
            space = i;
        }

        @Override
        public void getItemOffsets(Rect rect, View view, RecyclerView recyclerView, RecyclerView.State state) {
            rect.left = space;
            rect.top = space;
            rect.right = space;
            rect.bottom = space;
        }
    }

    private void init() {
        mScanProgressBar = (ProgressBar) findViewById(R.id.scanProgress);
        rvSimilar = (RecyclerView) findViewById(R.id.rvSimilar);
        ivBack = (ImageView) findViewById(R.id.ivBack);
        btnScan = (Button) findViewById(R.id.btnScan);
        layout_delete = (LinearLayout) findViewById(R.id.layout_select);
        txt_selcted_count = (TextView) findViewById(R.id.txt_selcted_count);
        iv_delete = (ImageView) findViewById(R.id.ivDelete);
        iv_refresh = (ImageView) findViewById(R.id.ivRefresh);
        Dialog dialog = new Dialog(this, R.style.Theme_Dialog);
        deleteDialog = dialog;
        dialog.requestWindowFeature(1);
        deleteDialog.setCancelable(true);
        deleteDialog.setContentView(R.layout.delete_dialog);
        deleteDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        deleteDialog.setCanceledOnTouchOutside(true);
        btDelete = (TextView) deleteDialog.findViewById(R.id.btDelete);
        btCancel = (TextView) deleteDialog.findViewById(R.id.btCancel);
        progressDialog = ProgressDialog.show(this, "", "Please wait...", true);
    }


    @Override
    public void onPause() {
        super.onPause();
        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    public void setData() {
        PassData.clear();
        Set<String> keySet = DataHashMap.keySet();
        ArrayList arrayList = new ArrayList();
        arrayList.addAll(keySet);
        ArrayList arrayList2 = new ArrayList();
        for (int i = 0; i < arrayList.size(); i++) {
            ArrayList<pictureFacer> arrayList3 = DataHashMap.get(arrayList.get(i));
            arrayList2.add(new PhotosHeader((String) arrayList.get(i)));
            arrayList2.addAll(arrayList3);
            for (int i2 = 0; i2 < arrayList3.size(); i2++) {
                pictureFacer picturefacer = new pictureFacer();
                picturefacer.setPicturePath(arrayList3.get(i2).getPicturePath());
                picturefacer.setPictureSize(arrayList3.get(i2).getPictureSize());
                Log.e("imagesDataSIze--)", "" + arrayList3.get(i2).getPicturePath());
                PassData.add(picturefacer);
            }
        }
        photosWith = arrayList2;
        SimilarImageAdapter similarImageAdapter = new SimilarImageAdapter(this, arrayList2, PassData, this);
        similarImageAdapter = similarImageAdapter;
        rvSimilar.setAdapter(similarImageAdapter);
    }

    public static void setSelectedData(int i) {
        if (i == 0) {
            txt_selcted_count.setText("SELECTED ITEMS");
        } else if (i == 1 && Utills.isAlbumLongPress) {
            TextView textView = txt_selcted_count;
            textView.setText("" + i + " items");
        } else if (i > 1) {
            TextView textView2 = txt_selcted_count;
            textView2.setText("" + i + " items");
        } else {
            TextView textView3 = txt_selcted_count;
            textView3.setText("" + i + " item");
        }
    }

    @Override
    public void onBackPressed() {
        AdShow.getInstance(DuplicateImageActivity.this).ShowAd(new HandleClick() {
            @Override
            public void Show(boolean adShow) {
                DuplicateImageActivity.super.onBackPressed();
            }
        }, AdUtils.ClickType.BACK_CLICK);
    }
}
