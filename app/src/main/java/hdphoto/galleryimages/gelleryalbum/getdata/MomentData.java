package hdphoto.galleryimages.gelleryalbum.getdata;

import android.app.Activity;
import android.os.AsyncTask;

import hdphoto.galleryimages.gelleryalbum.fragment.MomentFragment;
import hdphoto.galleryimages.gelleryalbum.utils.AppUtilsClass;
import hdphoto.galleryimages.gelleryalbum.utils.ConstantArrayClass;
import java.util.ArrayList;


public class MomentData {
    public static boolean gettingData = false;
    Activity context;
    public GetMomentAllAsync getMomentAllAsync = new GetMomentAllAsync();

    public MomentData(Activity activity) {
        this.context = activity;
    }


    public class GetMomentAllAsync extends AsyncTask<Void, Void, Void> {
        public GetMomentAllAsync() {
        }

        @Override 
        protected void onPreExecute() {
            MomentData.gettingData = true;
        }

        @Override 
        public Void doInBackground(Void... voidArr) {
            AppUtilsClass.ScanMomentAllDataNew(context);
            try {
                if (MomentFragment.momentAdapter == null) {
                    return null;
                }
                ArrayList<Object> arrayList = new ArrayList<>();
                arrayList.addAll(ConstantArrayClass.albumsList);
                MomentFragment.momentAdapter.FilterNewData(arrayList);
                return null;
            } catch (Exception e) {
                e.printStackTrace();
                return null;

            }
        }

        @Override 
        public void onPostExecute(Void r2) {
            super.onPostExecute(r2);
            if (MomentFragment.momentAdapter != null && ConstantArrayClass.albumsList.size() != 0) {
                MomentFragment.momentAdapter.RefreshData(ConstantArrayClass.albumsList);
            }
            MomentData.gettingData = false;
        }
    }
}
