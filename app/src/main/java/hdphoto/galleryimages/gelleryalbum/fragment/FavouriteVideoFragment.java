package hdphoto.galleryimages.gelleryalbum.fragment;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Rect;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import hdphoto.galleryimages.gelleryalbum.R;
import hdphoto.galleryimages.gelleryalbum.activity.ImageFavAdapter;
import hdphoto.galleryimages.gelleryalbum.activity.VideoViewActivity;
import hdphoto.galleryimages.gelleryalbum.adapter.VideoListAdapter;
import hdphoto.galleryimages.gelleryalbum.duplicate.LoginPreferenceManager;
import hdphoto.galleryimages.gelleryalbum.duplicate.itemClickListener;
import hdphoto.galleryimages.gelleryalbum.duplicate.pictureFacer;
import hdphoto.galleryimages.gelleryalbum.model.mVideo;

public class FavouriteVideoFragment extends Fragment implements itemClickListener {
    public static ArrayList<mVideo> getFavVideoList = new ArrayList<>();
    private int clDur;
    private int clName;
    private int clResolution;
    private int clSize;
    int column_id;
    private int column_index_data;
    int column_index_folder_name;
    private Cursor cursor;
    private RecyclerView.ItemDecoration itemDecoration;
    private ImageView noData;
    private RecyclerView rvFavVideo;
    private int thum;
    private Uri uri;
    private String duration, name, resolution, size;
    private VideoListAdapter videoAdapter;
    ArrayList<mVideo> favVideoData = new ArrayList<>();
    ArrayList<mVideo> allVideo = new ArrayList<>();

    @Override
    public void onPicClicked(ImageFavAdapter.PicHolder picHolder, int i, ArrayList<pictureFacer> arrayList) {
    }

    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.favourite_image, viewGroup, false);
        this.rvFavVideo = (RecyclerView) inflate.findViewById(R.id.rvFavimage);
        this.noData = (ImageView) inflate.findViewById(R.id.img_nomedia);
        this.swipeRefreshLayout = inflate.findViewById(R.id.swipe);
        this.itemDecoration = new SpacesItemDecoration(2);
        getVideoList();
        this.rvFavVideo.setLayoutManager(new GridLayoutManager(getContext(), 3, RecyclerView.VERTICAL, false));
        this.rvFavVideo.addItemDecoration(this.itemDecoration);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        return inflate;
    }

    @Override
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
    }

    public class SpacesItemDecoration extends RecyclerView.ItemDecoration {
        private int space;

        public SpacesItemDecoration(int i) {
            this.space = i;
        }

        @Override
        public void getItemOffsets(Rect rect, View view, RecyclerView recyclerView, RecyclerView.State state) {
            rect.left = this.space;
            rect.top = this.space;
            rect.right = this.space;
            rect.bottom = this.space;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        new DownloadWebPageTask().execute(new String[0]);
    }

    @Override
    public void onVideoClicked(VideoListAdapter.VideoHolder videoHolder, int i, ArrayList<mVideo> arrayList) {
        getFavVideoList = arrayList;
        Intent intent = new Intent(getContext(), VideoViewActivity.class);
        intent.putExtra("From", "fav");
        intent.putExtra("posV", String.valueOf(i));
        startActivityForResult(intent, 0);
    }

    private class DownloadWebPageTask extends AsyncTask<String, Void, String> {
        private DownloadWebPageTask() {
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        public String doInBackground(String... strArr) {
            FavouriteVideoFragment.this.favVideoData.clear();
            for (int i = 0; i < FavouriteVideoFragment.this.allVideo.size(); i++) {
                try {
                    try {
                        if (LoginPreferenceManager.getFavData(FavouriteVideoFragment.this.getContext(), FavouriteVideoFragment.this.allVideo.get(i).getStr_path())) {
                            FavouriteVideoFragment.this.favVideoData.add(FavouriteVideoFragment.this.allVideo.get(i));
                        }
                    } catch (Exception unused) {
                    }
                } catch (Exception unused2) {
                    return null;
                }
            }
            return null;
        }

        @Override
        public void onPostExecute(String str) {
            if (favVideoData.size() == 0) {
                noData.setVisibility(View.VISIBLE);
                rvFavVideo.setVisibility(View.GONE);
                return;
            }
            noData.setVisibility(View.GONE);
            rvFavVideo.setVisibility(View.VISIBLE);
            videoAdapter = new VideoListAdapter(FavouriteVideoFragment.this.getContext(), FavouriteVideoFragment.this.favVideoData, FavouriteVideoFragment.this);
            rvFavVideo.setAdapter(FavouriteVideoFragment.this.videoAdapter);
        }
    }

    private void getVideoList() {
        try {
            this.allVideo.clear();
            this.uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
            String[] strArr = {"_data", "bucket_display_name", "_id", "duration", "_size", "resolution", "title", "_data"};
            String.valueOf(TimeUnit.MILLISECONDS.convert(5L, TimeUnit.SECONDS));
            Cursor query = getActivity().getContentResolver().query(this.uri, strArr, null, null, "datetaken DESC");
            cursor = query;
            column_index_data = query.getColumnIndexOrThrow("_data");
            column_index_folder_name = this.cursor.getColumnIndexOrThrow("bucket_display_name");
            clSize = this.cursor.getColumnIndexOrThrow("_size");
            clDur = this.cursor.getColumnIndexOrThrow("duration");
            clName = this.cursor.getColumnIndexOrThrow("title");
            column_id = this.cursor.getColumnIndexOrThrow("_id");
            clResolution = this.cursor.getColumnIndexOrThrow("resolution");
            thum = this.cursor.getColumnIndexOrThrow("_data");
            while (this.cursor.moveToNext()) {
                String string = this.cursor.getString(this.column_index_data);
                size = this.cursor.getString(this.clSize);
                duration = timeConversion(Long.parseLong(this.cursor.getString(this.clDur)));
                name = this.cursor.getString(this.clName);
                resolution = this.cursor.getString(this.clResolution);
                float parseFloat = (Float.parseFloat(this.size) / 1024.0f) / 1024.0f;
                File file = new File(string);
                mVideo mvideo = new mVideo();
                if (file.exists()) {
                    mvideo.setBoolean_selected(false);
                    mvideo.setStr_path(string);
                    mvideo.setStr_thumb(this.cursor.getString(this.thum));
                    mvideo.setStr_name(this.name);
                    mvideo.setStr_duration(this.duration);
                    mvideo.setStr_size(String.valueOf(parseFloat));
                    mvideo.setStr_resolution(this.resolution);
                    this.allVideo.add(mvideo);
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
        if (i == 3) {
            this.videoAdapter.notifyDataSetChanged();
        }
    }
}
