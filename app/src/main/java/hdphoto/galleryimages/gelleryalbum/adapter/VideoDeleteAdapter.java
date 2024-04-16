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

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.BaseRequestOptions;
import com.bumptech.glide.request.RequestOptions;
import hdphoto.galleryimages.gelleryalbum.R;
import hdphoto.galleryimages.gelleryalbum.fragment.VideoListFragment;
import hdphoto.galleryimages.gelleryalbum.listeners.OnClickHideListener;
import hdphoto.galleryimages.gelleryalbum.listeners.itemDeleteClickListener;
import hdphoto.galleryimages.gelleryalbum.model.DataFileModel;
import hdphoto.galleryimages.gelleryalbum.model.mVideo;

import java.util.ArrayList;


public class VideoDeleteAdapter extends RecyclerView.Adapter<VideoDeleteAdapter.viewHolder> {
    public static boolean checkVideo = true;
    private Context Activity;
    ArrayList<mVideo> al_video;
    private itemDeleteClickListener picListerner;
    private ArrayList<mVideo> selectedDeleteData = new ArrayList<>();

    public VideoDeleteAdapter(Context context, ArrayList<mVideo> arrayList, itemDeleteClickListener itemdeleteclicklistener) {
        this.picListerner = itemdeleteclicklistener;
        this.al_video = arrayList;
        this.Activity = context;
    }

    @Override 
    public viewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new viewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.vault_video_view, viewGroup, false));
    }

    @Override 
    public void onBindViewHolder(viewHolder viewholder, int i) {
        mVideo mvideo = this.al_video.get(i);
        RequestManager with = Glide.with(this.Activity);
        with.load("file://" + this.al_video.get(i).getStr_thumb()).skipMemoryCache(false).apply((BaseRequestOptions<?>) new RequestOptions().centerCrop()).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).into(viewholder.iv_image);
        viewholder.tvVideoTime.setText(mvideo.getStr_duration());
        if (this.selectedDeleteData.contains(mvideo)) {
            int dimension = (int) this.Activity.getResources().getDimension(R.dimen._8sdp);
            viewholder.rl_select.setBackgroundColor(this.Activity.getResources().getColor(R.color.back_transparncy));
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(-1, -1);
            layoutParams.setMargins(dimension, dimension, dimension, dimension);
            viewholder.iv_image.setLayoutParams(layoutParams);
            viewholder.img_select.setImageResource(R.drawable.ic_tick_icon);
            return;
        }
        viewholder.rl_select.setBackgroundColor(this.Activity.getResources().getColor(R.color.white));
        RelativeLayout.LayoutParams layoutParams2 = new RelativeLayout.LayoutParams(-1, -1);
        layoutParams2.setMargins(0, 0, 0, 0);
        viewholder.iv_image.setLayoutParams(layoutParams2);
        viewholder.img_select.setImageResource(R.drawable.radio_unselect);
    }

    public ArrayList<mVideo> getSelectedDeleteData() {
        return this.selectedDeleteData;
    }

    @Override 
    public int getItemCount() {
        return this.al_video.size();
    }

    
    public class viewHolder extends RecyclerView.ViewHolder {
        public ImageView img_select;
        public ImageView iv_image;
        CardView rl_select;
        public RelativeLayout rlt_select;
        TextView tvVideoTime;

        public viewHolder(View view) {
            super(view);
            this.iv_image = (ImageView) view.findViewById(R.id.iv_image);
            this.rl_select = (CardView) view.findViewById(R.id.rl_select);
            this.tvVideoTime = (TextView) view.findViewById(R.id.tvVideoTime);
            this.img_select = (ImageView) view.findViewById(R.id.img_select);
            this.rlt_select = (RelativeLayout) view.findViewById(R.id.rlt_select);
            this.iv_image.setOnLongClickListener(new View.OnLongClickListener() { 
                @Override 
                public boolean onLongClick(View view2) {
                    VideoListFragment.layout_select_video.setVisibility(0);
                    VideoDeleteAdapter.checkVideo = false;
                    return false;
                }
            });
            this.iv_image.setOnClickListener(new View.OnClickListener() { 
                @Override 
                public void onClick(View view2) {
                    if (VideoDeleteAdapter.checkVideo) {
                        picListerner.onDeleteClicked(getAdapterPosition(), VideoDeleteAdapter.this.al_video);
                        viewHolder.this.img_select.setVisibility(4);
                        viewHolder.this.rlt_select.setVisibility(4);
                        return;
                    }
                    try {
                        if (selectedDeleteData.contains(al_video.get(viewHolder.this.getAdapterPosition()))) {
                            img_select.setVisibility(4);
                            rlt_select.setVisibility(4);
                            selectedDeleteData.remove(al_video.get(getAdapterPosition()));
                            VideoListFragment.setSelectedData(selectedDeleteData.size());
                            rl_select.setBackgroundColor(Activity.getResources().getColor(R.color.white));
                            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(-1, -1);
                            layoutParams.setMargins(0, 0, 0, 0);
                            iv_image.setLayoutParams(layoutParams);
                            img_select.setImageResource(R.drawable.radio_unselect);
                        } else {
                            img_select.setVisibility(0);
                            rlt_select.setVisibility(0);
                            selectedDeleteData.add(al_video.get(getAdapterPosition()));
                            VideoListFragment.setSelectedData(selectedDeleteData.size());
                            int dimension = (int) Activity.getResources().getDimension(R.dimen._8sdp);
                            rl_select.setBackgroundColor(Activity.getResources().getColor(R.color.back_transparncy));
                            RelativeLayout.LayoutParams layoutParams2 = new RelativeLayout.LayoutParams(-1, -1);
                            layoutParams2.setMargins(dimension, dimension, dimension, dimension);
                            iv_image.setLayoutParams(layoutParams2);
                            img_select.setImageResource(R.drawable.ic_tick_icon);
                        }
                    } catch (Exception unused) {
                    }
                }
            });
        }
    }
}
