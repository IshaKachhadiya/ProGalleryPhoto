package hdphoto.galleryimages.gelleryalbum.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.BaseRequestOptions;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

import hdphoto.galleryimages.gelleryalbum.R;
import hdphoto.galleryimages.gelleryalbum.duplicate.itemClickListener;
import hdphoto.galleryimages.gelleryalbum.model.mVideo;

public class VideoListAdapter extends RecyclerView.Adapter<VideoListAdapter.VideoHolder> {
    ArrayList<mVideo> al_video;
    Context context;
    private itemClickListener picListerner;

    public VideoListAdapter(Context context, ArrayList<mVideo> arrayList, itemClickListener itemclicklistener) {
        this.picListerner = itemclicklistener;
        this.al_video = arrayList;
        this.context = context;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView iv_image;
        CardView rl_select;
        TextView tvVideoTime;

        public ViewHolder(View view) {
            super(view);
            this.iv_image = (ImageView) view.findViewById(R.id.iv_image);
            this.rl_select = (CardView) view.findViewById(R.id.rl_select);
            this.tvVideoTime = (TextView) view.findViewById(R.id.tvVideoTime);
        }
    }

    @Override
    public VideoHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new VideoHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_video_view, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(final VideoHolder videoHolder, final int i) {
        RequestManager with = Glide.with(this.context);
        with.load("file://" + this.al_video.get(i).getStr_thumb()).skipMemoryCache(false).apply((BaseRequestOptions<?>) new RequestOptions().centerCrop()).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).into(videoHolder.ivVideo);
        videoHolder.tvVideoTime.setText(this.al_video.get(i).getStr_duration());
        videoHolder.cardVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                VideoListAdapter.this.picListerner.onVideoClicked(videoHolder, i, VideoListAdapter.this.al_video);
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.al_video.size();
    }

    public class VideoHolder extends RecyclerView.ViewHolder {
        public CardView cardVideo;
        public ImageView ivVideo;
        public TextView tvVideoTime;

        public VideoHolder(View view) {
            super(view);
            this.cardVideo = (CardView) view.findViewById(R.id.rl_select);
            this.ivVideo = (ImageView) view.findViewById(R.id.iv_image);
            this.tvVideoTime = (TextView) view.findViewById(R.id.tvVideoTime);
        }
    }
}
