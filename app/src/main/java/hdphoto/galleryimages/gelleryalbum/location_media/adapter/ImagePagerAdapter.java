package hdphoto.galleryimages.gelleryalbum.location_media.adapter;

import android.content.Context;
import android.graphics.Point;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import hdphoto.galleryimages.gelleryalbum.R;
import hdphoto.galleryimages.gelleryalbum.location_media.model.PictureData;

public class ImagePagerAdapter extends PagerAdapter {

    private final Context context;
    public static List<PictureData> displayImageList = null;


    public ImagePagerAdapter(Context context, List<PictureData> imageUrls) {
        this.context = context;
        this.displayImageList = imageUrls;
    }

    @Override
    public int getCount() {
        return displayImageList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.pager_item, container, false);

        ImageView imageView = view.findViewById(R.id.iv_img);
        Glide.with(this.context).setDefaultRequestOptions(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL).override(900, 900).dontTransform()).load(this.displayImageList.get(position).getFilePath()).placeholder(ContextCompat.getDrawable(this.context, R.drawable.no_photo_video)).into(imageView);


        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}