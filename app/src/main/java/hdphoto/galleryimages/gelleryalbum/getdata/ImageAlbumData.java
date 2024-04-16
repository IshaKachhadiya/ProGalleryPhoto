package hdphoto.galleryimages.gelleryalbum.getdata;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;

import hdphoto.galleryimages.gelleryalbum.fragment.ImageFragment;
import hdphoto.galleryimages.gelleryalbum.fragment.MomentFragment;
import hdphoto.galleryimages.gelleryalbum.model.FolderModel;
import hdphoto.galleryimages.gelleryalbum.utils.AppUtilsClass;
import hdphoto.galleryimages.gelleryalbum.utils.ConstantArrayClass;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class ImageAlbumData {
    public static boolean gettingData = false;
    Activity context;
    public GetImageAlbumAsync getImageAlbumAsync = new GetImageAlbumAsync();

    public ImageAlbumData(Activity activity) {
        this.context = activity;
    }

    public class GetImageAlbumAsync extends AsyncTask<Void, Void, Void> {
        public GetImageAlbumAsync() {
        }

        @Override 
        protected void onPreExecute() {
            gettingData = true;
        }

        @Override 
        public Void doInBackground(Void... voidArr) {
//            try {
//                AppUtilsClass.ScanImageAlbumListData(context);
//                new ArrayList().addAll(ConstantArrayClass.imageAlbumsList);
////                ImageFragment.imageFolderAdapter.FilterNewData(ConstantArrayClass.imageAlbumsList);
//            }catch (Exception e){
//                e.printStackTrace();
//            }
            return null;
        }

        @SuppressLint("NotifyDataSetChanged")
        @Override
        public void onPostExecute(Void r2) {
            super.onPostExecute(r2);
//            if (ImageFragment.imageFolderAdapter != null && ConstantArrayClass.imageAlbumsList.size() != 0) {
//                ImageFragment.imageFolderAdapter.refreshData(ConstantArrayClass.imageAlbumsList);
//            }
//            gettingData = false;
        }
    }

}
