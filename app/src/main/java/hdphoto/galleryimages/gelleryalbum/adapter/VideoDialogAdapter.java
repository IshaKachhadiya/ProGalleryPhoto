package hdphoto.galleryimages.gelleryalbum.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.BaseRequestOptions;
import com.bumptech.glide.request.RequestOptions;
import hdphoto.galleryimages.gelleryalbum.R;
import hdphoto.galleryimages.gelleryalbum.model.DialogAlbumModel;
import java.util.ArrayList;


public class VideoDialogAdapter extends BaseAdapter {
    Context activity;
    ArrayList<DialogAlbumModel> arrayList;
    int size;

    @Override
    public long getItemId(int i) {
        return i;
    }

    public VideoDialogAdapter(Context context, ArrayList<DialogAlbumModel> arrayList) {
        this.activity = context;
        this.arrayList = arrayList;
    }

    @Override 
    public int getCount() {
        return arrayList.size();
    }

    @Override 
    public Object getItem(int i) {
        return Integer.valueOf(i);
    }


    public class ViewHolder {
        ImageView imgAlbumCover;
        TextView txtAlbumName;
        TextView txtCount;

        public ViewHolder() {
        }
    }

    public void setLayoutParams(int i) {
        size = i;
    }

    @Override 
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = new ViewHolder();
        View inflate = LayoutInflater.from(activity).inflate(R.layout.layout_album_adapter, viewGroup, false);
        viewHolder.txtAlbumName = (TextView) inflate.findViewById(R.id.text_view_album_name);
        viewHolder.imgAlbumCover = (ImageView) inflate.findViewById(R.id.image_view_album_image);
        viewHolder.txtCount = (TextView) inflate.findViewById(R.id.text_view_album_size);
        viewHolder.txtAlbumName.setText(arrayList.get(i).foldername);
        viewHolder.txtCount.setText("(" + arrayList.get(i).pathlist.size() + ")");
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.centerCrop();
        Glide.with(activity).load(arrayList.get(i).coverThumb).apply((BaseRequestOptions<?>) requestOptions).into(viewHolder.imgAlbumCover);
        viewHolder.imgAlbumCover.getLayoutParams().width = size;
        viewHolder.imgAlbumCover.getLayoutParams().height = size;
        return inflate;
    }
}
