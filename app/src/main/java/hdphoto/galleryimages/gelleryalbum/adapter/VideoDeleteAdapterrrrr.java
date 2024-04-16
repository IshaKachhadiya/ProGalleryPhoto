package hdphoto.galleryimages.gelleryalbum.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.BaseRequestOptions;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

import hdphoto.galleryimages.gelleryalbum.R;
import hdphoto.galleryimages.gelleryalbum.listeners.OnClickHideListener;
import hdphoto.galleryimages.gelleryalbum.model.DataFileModel;


public class VideoDeleteAdapterrrrr extends BaseAdapter {
    Activity activity;
    ArrayList<Object> arrayList;
    Context context;
    LayoutInflater layoutInflater;
    OnClickHideListener<ArrayList<Object>, Integer, Boolean, View> onClickHideListener;
    int size;

    @Override 
    public long getItemId(int i) {
        return i;
    }

    public VideoDeleteAdapterrrrr(Activity activity, Context context, ArrayList<Object> arrayList) {
        this.arrayList = arrayList;
        this.context = context;
        this.activity = activity;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override 
    public int getCount() {
        return arrayList.size();
    }

    @Override 
    public Object getItem(int i) {
        return arrayList.get(i);
    }

    public void setLayoutParams(int i) {
        size = i;
    }

    public void releaseResources() {
        arrayList = null;
        context = null;
        activity = null;
    }

    @Override 
    public View getView(final int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (view == null) {
            view = layoutInflater.inflate(R.layout.layout_image_private_delete_adapter, (ViewGroup) null);
            viewHolder = new ViewHolder();
            viewHolder.img_hide_photo_row = (ImageView) view.findViewById(R.id.hide_photo_row_image);
            viewHolder.rl_hide_photo_row_image_view = (RelativeLayout) view.findViewById(R.id.view_alpha);
            viewHolder.rl_albumView = (RelativeLayout) view.findViewById(R.id.albumViewLayout);
            viewHolder.txt_view_album_name = (TextView) view.findViewById(R.id.text_view_album_name);
            viewHolder.txt_view_album_size = (TextView) view.findViewById(R.id.text_view_album_size);
            viewHolder.imgMoreDetail = (ImageView) view.findViewById(R.id.btnMoreDetails);
            viewHolder.rl_play = (RelativeLayout) view.findViewById(R.id.rel_play);
            viewHolder.tv_size = (TextView) view.findViewById(R.id.txt_size);
            viewHolder.ll_size = (LinearLayout) view.findViewById(R.id.lin_size);
            viewHolder.txt_size = (TextView) view.findViewById(R.id.txt_size);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.rl_play.setVisibility(View.VISIBLE);
        viewHolder.ll_size.setVisibility(View.VISIBLE);
        viewHolder.img_hide_photo_row.getLayoutParams().width = size;
        viewHolder.img_hide_photo_row.getLayoutParams().height = size;
        viewHolder.rl_hide_photo_row_image_view.getLayoutParams().width = size;
        viewHolder.rl_hide_photo_row_image_view.getLayoutParams().height = size;
        if (((DataFileModel) arrayList.get(i)).isSelected) {
            viewHolder.rl_hide_photo_row_image_view.setVisibility(View.VISIBLE);
        } else {
            viewHolder.rl_hide_photo_row_image_view.setVisibility(View.GONE);
        }
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.centerCrop();
        Glide.with(context).load(((DataFileModel) arrayList.get(i)).path).apply((BaseRequestOptions<?>) requestOptions).into(viewHolder.img_hide_photo_row);
        if (((DataFileModel) arrayList.get(i)).isDirectory) {
            viewHolder.rl_albumView.setVisibility(View.VISIBLE);
            TextView textView = viewHolder.txt_view_album_size;
            textView.setText("(" + ((DataFileModel) arrayList.get(i)).getSubFile().size() + ")");
            viewHolder.txt_view_album_name.setText(((DataFileModel) arrayList.get(i)).name);
        } else {
            viewHolder.rl_albumView.setVisibility(View.GONE);
        }
        viewHolder.img_hide_photo_row.setOnClickListener(view2 -> {
            try {
                if (((DataFileModel) arrayList.get(i)).isDirectory) {
                    onClickHideListener.onClickListener(((DataFileModel) arrayList.get(i)).getSubFile(), Integer.valueOf(i), true, view2);
                } else {
                    onClickHideListener.onClickListener(arrayList, Integer.valueOf(i), false, view2);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        viewHolder.img_hide_photo_row.setOnLongClickListener(view2 -> {
            try {
                if (((DataFileModel) arrayList.get(i)).isDirectory) {
                    onClickHideListener.onLongClickListener(arrayList, Integer.valueOf(i), true, view2);
                } else {
                    onClickHideListener.onLongClickListener(arrayList, Integer.valueOf(i), false, view2);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        });
        viewHolder.imgMoreDetail.setOnClickListener(view2 -> onClickHideListener.onClickMoreListener(arrayList, Integer.valueOf(i), view2, false));
        return view;
    }

    public void setItemClickCallback(OnClickHideListener onClickHideListener) {
        this.onClickHideListener = onClickHideListener;
    }


    private static class ViewHolder {
        ImageView imgMoreDetail;
        ImageView img_hide_photo_row;
        LinearLayout ll_size;
        RelativeLayout rl_albumView,rl_play,rl_hide_photo_row_image_view;
        TextView tv_size,txt_size,txt_view_album_name,txt_view_album_size;

        private ViewHolder() {
        }
    }
}
