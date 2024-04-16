package hdphoto.galleryimages.gelleryalbum.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.concurrent.TimeUnit;
import hdphoto.galleryimages.gelleryalbum.R;
import hdphoto.galleryimages.gelleryalbum.activity.VideoViewActivity;
import hdphoto.galleryimages.gelleryalbum.adapter.VideoDeleteAdapter;
import hdphoto.galleryimages.gelleryalbum.duplicate.Utills;
import hdphoto.galleryimages.gelleryalbum.listeners.itemDeleteClickListener;
import hdphoto.galleryimages.gelleryalbum.model.mVideo;


public class VideoListFragment extends Fragment implements itemDeleteClickListener {
    public static ArrayList<mVideo> PassVideo = new ArrayList<>();
    public static LinearLayout layout_select_video;
    private static TextView txt_selcted_count;
    Context Activity;
    TextView btCancel;
    TextView btDelete;
    private int clDur;
    private int clName;
    private int clResolution;
    private int clSize;
    int column_id;
    private int column_index_data;
    int column_index_folder_name;
    private Cursor cursor;
    Dialog deleteDialog;
    private RecyclerView.ItemDecoration itemDecoration;
    private ImageView ivDelete;
    private ImageView ivRefresh;
    private VideoDeleteAdapter obj_adapter;
    private RecyclerView rvVideoList;
    private int thum;
    private Uri uri;
    private String vDuration;
    private String vName;
    private String vResoluiton;
    private String vSize;
    private ArrayList<mVideo> al_video = new ArrayList<>();
    String sortType = "";
    String sortAscDec = "";
    ArrayList<mVideo> mDeleteList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.fragment_video_list, viewGroup, false);
        itemDecoration = new SpacesItemDecoration(2);
        init(inflate);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 3, RecyclerView.VERTICAL, false);
        rvVideoList.addItemDecoration(itemDecoration);
        rvVideoList.setLayoutManager(gridLayoutManager);
        getVideoList();
        sortAlbum(sortType, sortAscDec);
        if (VideoDeleteAdapter.checkVideo) {
            layout_select_video.setVisibility(View.GONE);
        } else {
            layout_select_video.setVisibility(View.VISIBLE);
        }
        ivRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                VideoDeleteAdapter.checkVideo = true;
                layout_select_video.setVisibility(View.GONE);
                obj_adapter = new VideoDeleteAdapter(getContext(), al_video, VideoListFragment.this);
                rvVideoList.setAdapter(obj_adapter);
            }
        });
        ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override 
            public void onClick(View view) {
                deleteDialog.show();
                mDeleteList = obj_adapter.getSelectedDeleteData();
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
                VideoListFragment videoListFragment = VideoListFragment.this;
                videoListFragment.callDelete(videoListFragment.mDeleteList);
            }
        });
        return inflate;
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

    public void callDelete(ArrayList<mVideo> arrayList) {
        for (int i = 0; i < arrayList.size(); i++) {
            mVideo mvideo = arrayList.get(i);
            File file = new File(mvideo.getStr_path());
            try {
                deleteDialog.dismiss();
                al_video.remove(al_video.indexOf(mvideo));
                if (file.delete()) {
                    try {
                        if (Build.VERSION.SDK_INT >= 19) {
                            Intent intent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
                            intent.setData(Uri.fromFile(file));
                            getContext().sendBroadcast(intent);
                        } else {
                            getContext().sendBroadcast(new Intent("android.intent.action.MEDIA_MOUNTED", Uri.fromFile(file)));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                scanFile(mvideo.getStr_path());
            } catch (Exception unused) {
            }
        }
        VideoDeleteAdapter.checkVideo = true;
        layout_select_video.setVisibility(View.GONE);
    }

    private void scanFile(String str) {
        MediaScannerConnection.scanFile(getContext(), new String[]{str}, null, new MediaScannerConnection.OnScanCompletedListener() { 
            @Override
            public void onScanCompleted(String str2, Uri uri) {
                File file = new File(str2);
                file.delete();
                if (file.delete()) {
                    Log.i("Delete", "Finished Delete Image--) ");
                } else {
                    Log.i("Delete", "Error Delete Image--) ");
                }
            }
        });
    }

    private void init(View view) {
        rvVideoList = (RecyclerView) view.findViewById(R.id.rvVideoList);
        layout_select_video = (LinearLayout) view.findViewById(R.id.layout_select_video);
        ivDelete = (ImageView) view.findViewById(R.id.ivDelete);
        ivRefresh = (ImageView) view.findViewById(R.id.ivRefresh);
        txt_selcted_count = (TextView) view.findViewById(R.id.txt_selcted_count);
        Dialog dialog = new Dialog(getContext(), R.style.Theme_Dialog);
        deleteDialog = dialog;
        dialog.requestWindowFeature(1);
        deleteDialog.setCancelable(true);
        deleteDialog.setContentView(R.layout.delete_dialog);
        deleteDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        deleteDialog.setCanceledOnTouchOutside(true);
        btDelete = (TextView) deleteDialog.findViewById(R.id.btDelete);
        btCancel = (TextView) deleteDialog.findViewById(R.id.btCancel);
    }

    public void sortAlbum(String str, String str2) {
        VideoDeleteAdapter videoDeleteAdapter = new VideoDeleteAdapter(getContext(), al_video, this);
        obj_adapter = videoDeleteAdapter;
        rvVideoList.setAdapter(videoDeleteAdapter);
    }

    private void getVideoList() {
        try {
            al_video.clear();
            uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
            String[] strArr = {"_data", "bucket_display_name", "_id", "duration", "_size", "resolution", "title", "_data", "date_modified"};
            String.valueOf(TimeUnit.MILLISECONDS.convert(5L, TimeUnit.SECONDS));
            Cursor query = getActivity().getContentResolver().query(uri, strArr, null, null, "datetaken DESC");
            cursor = query;
            column_index_data = query.getColumnIndexOrThrow("_data");
            column_index_folder_name = cursor.getColumnIndexOrThrow("bucket_display_name");
            clSize = cursor.getColumnIndexOrThrow("_size");
            clDur = cursor.getColumnIndexOrThrow("duration");
            clName = cursor.getColumnIndexOrThrow("title");
            column_id = cursor.getColumnIndexOrThrow("_id");
            clResolution = cursor.getColumnIndexOrThrow("resolution");
            thum = cursor.getColumnIndexOrThrow("_data");
            while (cursor.moveToNext()) {
                String string = cursor.getString(column_index_data);
                vSize = cursor.getString(clSize);
                vDuration = timeConversion(Long.parseLong(cursor.getString(clDur)));
                vName = cursor.getString(clName);
                vResoluiton = cursor.getString(clResolution);
                String string2 = cursor.getString(cursor.getColumnIndex(strArr[8]));
                float parseFloat = (Float.parseFloat(vSize) / 1024.0f) / 1024.0f;
                if (string2 == null) {
                    string2 = "1556082454";
                }
                File file = new File(string);
                mVideo mvideo = new mVideo();
                if (file.exists() && cursor.getInt(clDur) != 0) {
                    mvideo.setBoolean_selected(false);
                    mvideo.setStr_path(string);
                    mvideo.setStr_thumb(cursor.getString(thum));
                    mvideo.setStr_name(vName);
                    mvideo.setStr_duration(vDuration);
                    mvideo.setStr_size(String.valueOf(parseFloat));
                    mvideo.setStr_length(file.length());
                    mvideo.setStr_resolution(vResoluiton);
                    mvideo.setStr_date(string2);
                    al_video.add(mvideo);
                }
            }
        } catch (Exception unused) {
        }
    }

    public String timeConversion(long j) {
        int i = (int) j;
        int i2 = i / 3600000;
        int i3 = (i / 60000) % 60000;
        int i4 = (i % 60000) / 1000;
        return i2 > 0 ? String.format("%02d:%02d:%02d", Integer.valueOf(i2), Integer.valueOf(i3), Integer.valueOf(i4)) : String.format("%02d:%02d", Integer.valueOf(i3), Integer.valueOf(i4));
    }

    @Override
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i == 0) {
            obj_adapter.notifyDataSetChanged();
        }
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
    public void onDeleteClicked(int i, ArrayList<mVideo> arrayList) {
        PassVideo = al_video;
        Intent intent = new Intent(getContext(), VideoViewActivity.class);
        intent.putExtra("From", "main");
        intent.putExtra("posV", String.valueOf(i));
        startActivityForResult(intent, 0);
    }
}
