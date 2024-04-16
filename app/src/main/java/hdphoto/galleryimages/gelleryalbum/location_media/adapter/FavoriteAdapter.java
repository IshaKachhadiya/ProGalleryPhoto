package hdphoto.galleryimages.gelleryalbum.location_media.adapter;

import android.app.Activity;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.BaseRequestOptions;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.imageview.ShapeableImageView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import hdphoto.galleryimages.gelleryalbum.R;
import hdphoto.galleryimages.gelleryalbum.location_media.model.AlbumData;
import hdphoto.galleryimages.gelleryalbum.location_media.model.PictureData;


public class FavoriteAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Activity activity;
    OnSelectPicture onSelectPicture;
    List<Object> photoList;

    
    public interface OnSelectPicture {
        void onLongClickPicture(int i);

        void onSelectPicture(int i);
    }

    public FavoriteAdapter(Activity activity, List<Object> list, OnSelectPicture onSelectPicture) {
        new ArrayList();
        this.activity = activity;
        this.photoList = list;
        this.onSelectPicture = onSelectPicture;
    }

    @Override
    public int getItemViewType(int i) {
        if (this.photoList.get(i) instanceof PictureData) {
            return ((PictureData) this.photoList.get(i)).isVideo() ? 3 : 2;
        }
        return 1;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        RecyclerView.ViewHolder headerViewHolder;
        if (i == 1) {
            headerViewHolder = new HeaderViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_header, viewGroup, false));
        } else if (i == 2) {
            headerViewHolder = new ImageViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_picture, viewGroup, false));
        } else if (i != 3) {
            return null;
        } else {
            headerViewHolder = new VideoViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_video, viewGroup, false));
        }
        return headerViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int i) {
        int itemViewType = getItemViewType(i);
        if (itemViewType == 1) {
            ((HeaderViewHolder) viewHolder).headerText.setText(((AlbumData) this.photoList.get(i)).getTitle());
        } else if (itemViewType == 2) {
            ImageViewHolder imageViewHolder = (ImageViewHolder) viewHolder;
            PictureData pictureData = (PictureData) this.photoList.get(i);
            new File(pictureData.getFilePath());
            Glide.with(this.activity).load(pictureData.getFilePath()).placeholder((int) R.drawable.ic_unselect_gallery).apply((BaseRequestOptions<?>) new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL)).into(imageViewHolder.image);
            if (pictureData.isSelected()) {
                imageViewHolder.frame_layout.setCardBackgroundColor(this.activity.getResources().getColor(R.color.black));
                FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(imageViewHolder.ll_main.getLayoutParams());
                int dimensionPixelSize = this.activity.getResources().getDimensionPixelSize(R.dimen._8sdp);
                layoutParams.setMargins(dimensionPixelSize, dimensionPixelSize, dimensionPixelSize, dimensionPixelSize);
                imageViewHolder.ll_main.setLayoutParams(layoutParams);
                imageViewHolder.lout_select.setVisibility(0);
            } else {
                imageViewHolder.lout_select.setVisibility(8);
                FrameLayout.LayoutParams layoutParams2 = new FrameLayout.LayoutParams(imageViewHolder.ll_main.getLayoutParams());
                layoutParams2.setMargins(0, 0, 0, 0);
                imageViewHolder.ll_main.setLayoutParams(layoutParams2);
                imageViewHolder.frame_layout.setCardBackgroundColor(this.activity.getResources().getColor(R.color.white));
            }
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override 
                public void onClick(View view) {
                    FavoriteAdapter.this.onSelectPicture.onSelectPicture(i);
                }
            });
            viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override 
                public boolean onLongClick(View view) {
                    FavoriteAdapter.this.onSelectPicture.onLongClickPicture(i);
                    return true;
                }
            });
        } else if (itemViewType != 3) {
        } else {
            VideoViewHolder videoViewHolder = (VideoViewHolder) viewHolder;
            PictureData pictureData2 = (PictureData) this.photoList.get(i);
            videoViewHolder.videoDuration.setText(pictureData2.getDuration());
            Glide.with(this.activity).load(pictureData2.getFilePath()).placeholder(ContextCompat.getDrawable(this.activity, R.drawable.ic_unselect_gallery)).into(videoViewHolder.video);
            if (pictureData2.isSelected()) {
                videoViewHolder.frame_layout.setCardBackgroundColor(this.activity.getResources().getColor(R.color.black));
                FrameLayout.LayoutParams layoutParams3 = new FrameLayout.LayoutParams(videoViewHolder.ll_main.getLayoutParams());
                int dimensionPixelSize2 = this.activity.getResources().getDimensionPixelSize(R.dimen._8sdp);
                layoutParams3.setMargins(dimensionPixelSize2, dimensionPixelSize2, dimensionPixelSize2, dimensionPixelSize2);
                videoViewHolder.ll_main.setLayoutParams(layoutParams3);
                videoViewHolder.lout_select.setVisibility(0);
            } else {
                videoViewHolder.lout_select.setVisibility(8);
                FrameLayout.LayoutParams layoutParams4 = new FrameLayout.LayoutParams(videoViewHolder.ll_main.getLayoutParams());
                layoutParams4.setMargins(0, 0, 0, 0);
                videoViewHolder.ll_main.setLayoutParams(layoutParams4);
                videoViewHolder.frame_layout.setCardBackgroundColor(this.activity.getResources().getColor(R.color.white));
            }
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override 
                public void onClick(View view) {
                    FavoriteAdapter.this.onSelectPicture.onSelectPicture(i);
                }
            });
            viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override 
                public boolean onLongClick(View view) {
                    FavoriteAdapter.this.onSelectPicture.onLongClickPicture(i);
                    return true;
                }
            });
        }
    }

    @Override 
    public int getItemCount() {
        return this.photoList.size();
    }

    
    public class HeaderViewHolder extends RecyclerView.ViewHolder {
        TextView headerText;

        public HeaderViewHolder(View view) {
            super(view);
            this.headerText = (TextView) view.findViewById(R.id.headerText);
        }
    }

    
    public class ImageViewHolder extends RecyclerView.ViewHolder {
        CardView frame_layout;
        ShapeableImageView image;
        AppCompatImageView iv_select;
        FrameLayout ll_main;
        RelativeLayout lout_select;

        public ImageViewHolder(View view) {
            super(view);
            this.image = (ShapeableImageView) view.findViewById(R.id.image);
            this.iv_select = (AppCompatImageView) view.findViewById(R.id.iv_select);
            DisplayMetrics displayMetrics = new DisplayMetrics();
            this.frame_layout = (CardView) view.findViewById(R.id.frame_layout);
            this.ll_main = (FrameLayout) view.findViewById(R.id.ll_main);
            this.lout_select = (RelativeLayout) view.findViewById(R.id.lout_select);
            FavoriteAdapter.this.activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            this.image.setShapeAppearanceModel(this.image.getShapeAppearanceModel().toBuilder().setAllCornerSizes(FavoriteAdapter.this.activity.getResources().getDimension(R.dimen._8sdp)).build());
        }
    }

    
    class VideoViewHolder extends RecyclerView.ViewHolder {
        CardView frame_layout;
        AppCompatImageView iv_select;
        FrameLayout ll_main;
        RelativeLayout lout_select;
        ShapeableImageView video;
        TextView videoDuration;

        public VideoViewHolder(View view) {
            super(view);
            this.video = (ShapeableImageView) view.findViewById(R.id.video);
            this.iv_select = (AppCompatImageView) view.findViewById(R.id.iv_select);
            this.videoDuration = (TextView) view.findViewById(R.id.videoDuration);
            this.frame_layout = (CardView) view.findViewById(R.id.frame_layout);
            this.ll_main = (FrameLayout) view.findViewById(R.id.ll_main);
            this.lout_select = (RelativeLayout) view.findViewById(R.id.lout_select);
            this.video.setShapeAppearanceModel(this.video.getShapeAppearanceModel().toBuilder().setAllCornerSizes(FavoriteAdapter.this.activity.getResources().getDimension(R.dimen._10sdp)).build());
        }
    }
}
