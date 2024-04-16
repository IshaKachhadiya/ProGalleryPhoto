
package hdphoto.galleryimages.gelleryalbum.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
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

import java.util.ArrayList;

import hdphoto.galleryimages.gelleryalbum.R;
import hdphoto.galleryimages.gelleryalbum.adapter.VideoListAdapter;
import hdphoto.galleryimages.gelleryalbum.duplicate.LoginPreferenceManager;
import hdphoto.galleryimages.gelleryalbum.duplicate.itemClickListener;
import hdphoto.galleryimages.gelleryalbum.duplicate.pictureFacer;
import hdphoto.galleryimages.gelleryalbum.model.mVideo;

public class FavouriteImageFragment extends Fragment implements itemClickListener {
    public static ArrayList<pictureFacer> getFavimgList = new ArrayList<>();
    private ImageFavAdapter customAdapter;
    private ImageView iv_no_data;
    private RecyclerView rvFavimage;
    ArrayList<pictureFacer> favimagesData = new ArrayList<>();
    ArrayList<pictureFacer> allImage = new ArrayList<>();
    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public void onVideoClicked(VideoListAdapter.VideoHolder videoHolder, int i, ArrayList<mVideo> arrayList) {

    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.favourite_image, viewGroup, false);
        this.rvFavimage = (RecyclerView) inflate.findViewById(R.id.rvFavimage);
        this.iv_no_data = (ImageView) inflate.findViewById(R.id.img_nomedia);
        this.swipeRefreshLayout = inflate.findViewById(R.id.swipe);
        this.allImage = getAllImagesByFolder();
        this.rvFavimage.setLayoutManager(new GridLayoutManager(getContext(), 3, RecyclerView.VERTICAL, false));

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        return inflate;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(customAdapter != null){
            customAdapter = new ImageFavAdapter(FavouriteImageFragment.this.getContext(), FavouriteImageFragment.this.favimagesData, FavouriteImageFragment.this);
            rvFavimage.setAdapter(FavouriteImageFragment.this.customAdapter);
        }

        new DownloadWebPageTask().execute(new String[0]);
    }

    @Override
    public void onPicClicked(ImageFavAdapter.PicHolder picHolder, int i, ArrayList<pictureFacer> arrayList) {
        getFavimgList = arrayList;
        try {
            Intent intent = new Intent(getContext(), SingleImageActivity.class);
            intent.putExtra("Pos", String.valueOf(i));
            intent.putExtra("From", "FavImage");
            startActivityForResult(intent, 3);
        } catch (Exception unused) {
        }
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
            FavouriteImageFragment.this.favimagesData.clear();
            for (int i = 0; i < FavouriteImageFragment.this.allImage.size(); i++) {
                try {
                    try {
                        if (LoginPreferenceManager.getFavData(requireContext(), allImage.get(i).getPicturePath())) {
                            favimagesData.add(FavouriteImageFragment.this.allImage.get(i));
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
            if (favimagesData.size() == 0) {
                iv_no_data.setVisibility(View.VISIBLE);
                rvFavimage.setVisibility(View.GONE);
                return;
            }
            iv_no_data.setVisibility(View.GONE);
            rvFavimage.setVisibility(View.VISIBLE);
            customAdapter = new ImageFavAdapter(requireContext(), favimagesData, FavouriteImageFragment.this);
            rvFavimage.setAdapter(customAdapter);
        }
    }
    public ArrayList<pictureFacer> getAllImagesByFolder() {
        ArrayList<pictureFacer> arrayList = new ArrayList<>();
        for (String s : new String[]{"image/jpeg", "image/png", "image/jpg"}) {

        }

        Cursor query = getActivity().getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new String[]{"_data", "_display_name", "_size"}, "mime_type='image/jpeg' OR mime_type='image/png' OR mime_type='image/jpg'", null, null);
        try {
            query.moveToFirst();
            do {
                pictureFacer picturefacer = new pictureFacer();
                picturefacer.setPicturName(query.getString(query.getColumnIndexOrThrow("_display_name")));
                picturefacer.setPicturePath(query.getString(query.getColumnIndexOrThrow("_data")));
                picturefacer.setPictureSize(query.getString(query.getColumnIndexOrThrow("_size")));
                arrayList.add(picturefacer);
            } while (query.moveToNext());
            query.close();
            ArrayList<pictureFacer> arrayList2 = new ArrayList<>();
            int size = arrayList.size();
            while (true) {
                size--;
                if (size <= -1) {
                    return arrayList2;
                }
                arrayList2.add(arrayList.get(size));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return arrayList;
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i == 3) {
            customAdapter.notifyDataSetChanged();
        }
    }
}