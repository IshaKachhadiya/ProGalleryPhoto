package hdphoto.galleryimages.gelleryalbum.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.BaseRequestOptions;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

import hdphoto.galleryimages.gelleryalbum.R;
import hdphoto.galleryimages.gelleryalbum.duplicate.itemClickListener;
import hdphoto.galleryimages.gelleryalbum.duplicate.pictureFacer;

public class ImageFavAdapter extends RecyclerView.Adapter<ImageFavAdapter.PicHolder> {
    public static boolean checkLong = true;
    private Context Activity;
    private final ArrayList<pictureFacer> listData;
    private final itemClickListener picListerner;
    public ArrayList<pictureFacer> selectedDeleteData = new ArrayList<>();

    public ImageFavAdapter(Context context, ArrayList<pictureFacer> arrayList, itemClickListener itemclicklistener) {
        this.picListerner = itemclicklistener;
        this.Activity = context;
        this.listData = arrayList;
    }

    @NonNull
    @Override
    public PicHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new PicHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.image_private_row, viewGroup, false));
    }

    @Override 
    public void onBindViewHolder(final PicHolder picHolder, @SuppressLint("RecyclerView") final int i) {
        Glide.with(this.Activity).load(this.listData.get(i).getPicturePath()).apply((BaseRequestOptions<?>) new RequestOptions().centerCrop()).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).into(picHolder.iv_picture);
        ImageView imageView = picHolder.iv_picture;
        ViewCompat.setTransitionName(imageView, String.valueOf(i) + "_image");
        if (checkLong) {
            picHolder.iv_select.setVisibility(View.INVISIBLE);
            picHolder.rlt_select.setVisibility(View.INVISIBLE);
        } else {
            try {
                if (this.selectedDeleteData.contains(this.listData.get(i))) {
                    picHolder.iv_select.setVisibility(View.VISIBLE);
                    picHolder.rlt_select.setVisibility(View.VISIBLE);
                } else {
                    picHolder.iv_select.setVisibility(View.INVISIBLE);
                    picHolder.rlt_select.setVisibility(View.INVISIBLE);
                }
            } catch (Exception unused) {
            }
        }
        picHolder.iv_picture.setOnClickListener(new View.OnClickListener() {
            @Override 
            public void onClick(View view) {
                if (ImageFavAdapter.checkLong) {
                    picListerner.onPicClicked(picHolder, i, listData);
                    picHolder.iv_select.setVisibility(View.INVISIBLE);
                    picHolder.rlt_select.setVisibility(View.INVISIBLE);
                    return;
                }
                try {
                    if (selectedDeleteData.contains(listData.get(i))) {
                        picHolder.iv_select.setVisibility(View.INVISIBLE);
                        picHolder.rlt_select.setVisibility(View.INVISIBLE);
                        selectedDeleteData.remove(listData.get(i));
                        picHolder.imgCard.setBackgroundColor(Activity.getResources().getColor(R.color.white));
                        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(-1, -1);
                        layoutParams.setMargins(0, 0, 0, 0);
                        picHolder.iv_picture.setLayoutParams(layoutParams);
                        picHolder.iv_select.setImageResource(R.drawable.radio_unselect);
                    } else {
                        picHolder.iv_select.setVisibility(View.VISIBLE);
                        picHolder.rlt_select.setVisibility(View.VISIBLE);
                        selectedDeleteData.add((pictureFacer) listData.get(i));
                        Log.e("SelectedSize--)", "" + selectedDeleteData.size());
                        picHolder.iv_select.setVisibility(View.VISIBLE);
                        picHolder.iv_select.setImageResource(R.drawable.ic_tick_icon);
                        int dimension = (int) Activity.getResources().getDimension(R.dimen._8sdp);
                        picHolder.imgCard.setBackgroundColor(Activity.getResources().getColor(R.color.back_transparncy));
                        RelativeLayout.LayoutParams layoutParams2 = new RelativeLayout.LayoutParams(-1, -1);
                        layoutParams2.setMargins(dimension, dimension, dimension, dimension);
                        picHolder.iv_picture.setLayoutParams(layoutParams2);
                    }
                } catch (Exception e) {
                    Log.e("Exception--)", "" + e.getMessage());
                }
            }
        });
        picHolder.iv_picture.setOnLongClickListener(new View.OnLongClickListener() {
            @Override 
            public boolean onLongClick(View view) {
                ImageFavAdapter.checkLong = false;
                return false;
            }
        });
    }


    @Override 
    public int getItemCount() {
        return this.listData.size();
    }

    public class PicHolder extends RecyclerView.ViewHolder {
        public CardView imgCard;
        public ImageView iv_select, iv_picture;
        public RelativeLayout rlt_select;

        public PicHolder(View view) {
            super(view);
            iv_picture = (ImageView) view.findViewById(R.id.ivImage);
            iv_select = (ImageView) view.findViewById(R.id.img_select);
            rlt_select = (RelativeLayout) view.findViewById(R.id.rlt_select);
            imgCard = (CardView) view.findViewById(R.id.imgCard);
        }
    }
}