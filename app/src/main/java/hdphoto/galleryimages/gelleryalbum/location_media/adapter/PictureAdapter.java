package hdphoto.galleryimages.gelleryalbum.location_media.adapter;

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
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;

import hdphoto.galleryimages.gelleryalbum.R;
import hdphoto.galleryimages.gelleryalbum.location_media.model.AlbumData;
import hdphoto.galleryimages.gelleryalbum.location_media.model.PictureData;


public class PictureAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    FragmentActivity activity;
    OnSelectPicture onSelectPicture;
    ArrayList<Object> pictures;

    
    public interface OnSelectPicture {
        void onLongClickPicture(int i);

        void onSelectPicture(int i);
    }

    public PictureAdapter(FragmentActivity fragmentActivity, ArrayList<Object> arrayList, OnSelectPicture onSelectPicture) {
        this.activity = fragmentActivity;
        this.pictures = arrayList;
        this.onSelectPicture = onSelectPicture;
    }

    @Override
    public int getItemViewType(int i) {
        return this.pictures.get(i) instanceof AlbumData ? 1 : 2;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        RecyclerView.ViewHolder headerViewHolder;
        if (i == 1) {
            headerViewHolder = new HeaderViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_header, viewGroup, false));
        } else if (i != 2) {
            return null;
        } else {
            headerViewHolder = new PictureViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_picture, viewGroup, false));
        }
        return headerViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int i) {
        int itemViewType = getItemViewType(i);
        if (itemViewType == 1) {
            ((HeaderViewHolder) viewHolder).headerText.setText(((AlbumData) this.pictures.get(i)).getTitle());
        } else if (itemViewType == 2) {
            PictureViewHolder pictureViewHolder = (PictureViewHolder) viewHolder;
            PictureData pictureData = (PictureData) this.pictures.get(i);
            Glide.with(this.activity).load(pictureData.getFilePath()).placeholder(ContextCompat.getDrawable(this.activity, R.drawable.no_photo_video)).into(pictureViewHolder.image);
            if (pictureData.isSelected()) {
                pictureViewHolder.frame_layout.setCardBackgroundColor(this.activity.getResources().getColor(R.color.black_tras));
                FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(pictureViewHolder.ll_main.getLayoutParams());
                int dimensionPixelSize = this.activity.getResources().getDimensionPixelSize(R.dimen._8sdp);
                layoutParams.setMargins(dimensionPixelSize, dimensionPixelSize, dimensionPixelSize, dimensionPixelSize);
                pictureViewHolder.ll_main.setLayoutParams(layoutParams);
                pictureViewHolder.lout_select.setVisibility(0);
            } else {
                pictureViewHolder.lout_select.setVisibility(8);
                FrameLayout.LayoutParams layoutParams2 = new FrameLayout.LayoutParams(pictureViewHolder.ll_main.getLayoutParams());
                layoutParams2.setMargins(0, 0, 0, 0);
                pictureViewHolder.ll_main.setLayoutParams(layoutParams2);
                pictureViewHolder.frame_layout.setCardBackgroundColor(this.activity.getResources().getColor(R.color.white));
            }
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override 
                public void onClick(View view) {
                    PictureAdapter.this.onSelectPicture.onSelectPicture(i);
                }
            });
            viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    PictureAdapter.this.onSelectPicture.onLongClickPicture(i);
                    return true;
                }
            });
        } else {
            throw new IllegalStateException("Unexpected value: " + getItemViewType(i));
        }
    }

    @Override
    public int getItemCount() {
        return this.pictures.size();
    }
    
    class HeaderViewHolder extends RecyclerView.ViewHolder {
        TextView headerText;

        public HeaderViewHolder(View view) {
            super(view);
            this.headerText = (TextView) view.findViewById(R.id.headerText);
        }
    }

    
    class PictureViewHolder extends RecyclerView.ViewHolder {
        CardView frame_layout;
        ShapeableImageView image;
        AppCompatImageView iv_select;
        FrameLayout ll_main;
        RelativeLayout lout_select;

        public PictureViewHolder(View view) {
            super(view);
            this.image = (ShapeableImageView) view.findViewById(R.id.image);
            this.iv_select = (AppCompatImageView) view.findViewById(R.id.iv_select);
            this.frame_layout = (CardView) view.findViewById(R.id.frame_layout);
            this.ll_main = (FrameLayout) view.findViewById(R.id.ll_main);
            this.lout_select = (RelativeLayout) view.findViewById(R.id.lout_select);
            PictureAdapter.this.activity.getWindowManager().getDefaultDisplay().getMetrics(new DisplayMetrics());
            this.image.setShapeAppearanceModel(this.image.getShapeAppearanceModel().toBuilder().setAllCornerSizes(PictureAdapter.this.activity.getResources().getDimension(R.dimen._8sdp)).build());
        }
    }
}
