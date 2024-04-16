package hdphoto.galleryimages.gelleryalbum.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import androidx.exifinterface.media.ExifInterface;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.BaseRequestOptions;
import com.bumptech.glide.request.RequestOptions;

import hdphoto.galleryimages.gelleryalbum.R;
import hdphoto.galleryimages.gelleryalbum.model.DataFileModel;
import java.util.ArrayList;


public class AllDataAdapter extends AllGalleryImageGenericAdapter<DataFileModel> {
    public AllDataAdapter(Activity activity, Context context, ArrayList<DataFileModel> arrayList) {
        super(activity, context, arrayList);
    }

    @Override 
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (view == null) {
            view = LayoutInflater.from(activity).inflate(R.layout.layout_all_data_adapter, viewGroup, false);
            viewHolder = new ViewHolder();
            viewHolder.imageView = (ImageView) view.findViewById(R.id.image_view_image_select);
            viewHolder.rl_bg = (RelativeLayout) view.findViewById(R.id.relativelayout_bg);
            viewHolder.rel_play = (RelativeLayout) view.findViewById(R.id.rel_play);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        try {
            if (((DataFileModel) arrayList.get(i)).isSelected) {
                viewHolder.rl_bg.setVisibility(View.VISIBLE);
            } else {
                viewHolder.rl_bg.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            if (((DataFileModel) arrayList.get(i)).getMediaType().equals("3")) {
                viewHolder.rel_play.setVisibility(View.VISIBLE);
            } else if (((DataFileModel) arrayList.get(i)).getMediaType().equals("1")) {
                viewHolder.rel_play.setVisibility(View.GONE);
            }
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        try {
            RequestOptions requestOptions = new RequestOptions();
            requestOptions.centerCrop();
            Glide.with(activity).load(((DataFileModel) arrayList.get(i)).path).apply((BaseRequestOptions<?>) requestOptions).into(viewHolder.imageView);
        } catch (Exception e3) {
            e3.printStackTrace();
        }
        viewHolder.imageView.getLayoutParams().width = size;
        viewHolder.imageView.getLayoutParams().height = size;
        viewHolder.rl_bg.getLayoutParams().width = size;
        viewHolder.rl_bg.getLayoutParams().height = size;
        return view;
    }

    private static class ViewHolder {
        public ImageView imageView;
        public RelativeLayout rel_play;
        public RelativeLayout rl_bg;

        private ViewHolder() {
        }
    }
}
