package hdphoto.galleryimages.gelleryalbum.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.BaseRequestOptions;
import com.bumptech.glide.request.RequestOptions;
import hdphoto.galleryimages.gelleryalbum.R;
import hdphoto.galleryimages.gelleryalbum.model.DataFileModel;
import java.util.ArrayList;


public class VideoFolderAdapter extends BaseAdapter {
    Context activity;
    int size;
    ArrayList<Object> videoAlbumArray;

    @Override 
    public long getItemId(int i) {
        return i;
    }

    public VideoFolderAdapter(Context context, ArrayList<Object> arrayList) {
        this.activity = context;
        this.videoAlbumArray = arrayList;
    }

    @Override 
    public int getCount() {
        return videoAlbumArray.size();
    }

    @Override 
    public Object getItem(int i) {
        return Integer.valueOf(i);
    }


    public class ViewHolder {
        ImageView ivAlbumCover;

        public ViewHolder() {
        }
    }

    public void setLayoutParams(int i) {
        size = i;
    }

    @Override 
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = new ViewHolder();
        View inflate = LayoutInflater.from(activity).inflate(R.layout.layout_folder_video_adapter, viewGroup, false);
        viewHolder.ivAlbumCover = (ImageView) inflate.findViewById(R.id.image_view_image_select);
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.centerCrop();
        Glide.with(activity).load(((DataFileModel) videoAlbumArray.get(i)).path).apply((BaseRequestOptions<?>) requestOptions).into(viewHolder.ivAlbumCover);
        viewHolder.ivAlbumCover.getLayoutParams().width = size;
        viewHolder.ivAlbumCover.getLayoutParams().height = size;
        return inflate;
    }
}
