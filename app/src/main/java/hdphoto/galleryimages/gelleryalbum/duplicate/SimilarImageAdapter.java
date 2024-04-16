package hdphoto.galleryimages.gelleryalbum.duplicate;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.BaseRequestOptions;
import com.bumptech.glide.request.RequestOptions;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import hdphoto.galleryimages.gelleryalbum.R;


public class SimilarImageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static boolean checkLong = true;
    private Activity activity;
    SimilarClickListener clickListener;
    List<Object> imagesDataList;
    ArrayList<pictureFacer> passData;
    public ArrayList<pictureFacer> selectedDeleteData = new ArrayList<>();

    public SimilarImageAdapter(Activity activity, List<Object> list, ArrayList<pictureFacer> arrayList, SimilarClickListener similarClickListener) {
        this.activity = activity;
        this.imagesDataList = list;
        this.clickListener = similarClickListener;
        this.passData = arrayList;
    }

    @Override
    public int getItemViewType(int i) {
        return this.imagesDataList.get(i) instanceof pictureFacer ? 2 : 1;
    }

    public ArrayList<pictureFacer> getDeletedData() {
        return this.selectedDeleteData;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        if (i != 1) {
            if (i != 2) {
                return null;
            }
            return new PhotosViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.image_private_row, viewGroup, false));
        }
        return new HeaderViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_header, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        if (this.imagesDataList.get(i) instanceof pictureFacer) {
            photosBindViewHolder((PhotosViewHolder) viewHolder, i);
        } else {
            headerBindViewHolder((HeaderViewHolder) viewHolder, i);
        }
    }

    @Override
    public int getItemCount() {
        List<Object> list = this.imagesDataList;
        if (list == null || list.size() <= 0) {
            return 0;
        }
        return this.imagesDataList.size();
    }

    public void photosBindViewHolder(PhotosViewHolder photosViewHolder, int i) {
        pictureFacer picturefacer = (pictureFacer) this.imagesDataList.get(i);
        Glide.with(this.activity).load(picturefacer.getPicturePath()).apply((BaseRequestOptions<?>) new RequestOptions().centerCrop()).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).into(photosViewHolder.picture);
        if (this.selectedDeleteData.contains(picturefacer)) {
            photosViewHolder.img_select.setVisibility(0);
            photosViewHolder.rlt_select.setVisibility(0);
            int dimension = (int) this.activity.getResources().getDimension(R.dimen._8sdp);
            photosViewHolder.imgCard.setBackgroundColor(this.activity.getResources().getColor(R.color.black_tras));
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(-1, -1);
            layoutParams.setMargins(dimension, dimension, dimension, dimension);
            photosViewHolder.picture.setLayoutParams(layoutParams);
            photosViewHolder.img_select.setImageResource(R.drawable.ic_tick_icon);
            return;
        }
        photosViewHolder.img_select.setVisibility(4);
        photosViewHolder.rlt_select.setVisibility(4);
        photosViewHolder.imgCard.setBackgroundColor(this.activity.getResources().getColor(R.color.white));
        RelativeLayout.LayoutParams layoutParams2 = new RelativeLayout.LayoutParams(-1, -1);
        layoutParams2.setMargins(0, 0, 0, 0);
        photosViewHolder.picture.setLayoutParams(layoutParams2);
        photosViewHolder.img_select.setImageResource(R.drawable.radio_unselect);
    }

    public void headerBindViewHolder(HeaderViewHolder headerViewHolder, int i) {
        headerViewHolder.txt_date.setText(((PhotosHeader) this.imagesDataList.get(i)).getHeader());
    }

   
    public class PhotosViewHolder extends RecyclerView.ViewHolder {
        public CardView imgCard;
        public ImageView img_select;
        public ImageView picture;
        public RelativeLayout rlt_select;

        public PhotosViewHolder(View view) {
            super(view);
            this.picture = (ImageView) view.findViewById(R.id.ivImage);
            this.img_select = (ImageView) view.findViewById(R.id.img_select);
            this.rlt_select = (RelativeLayout) view.findViewById(R.id.rlt_select);
            this.imgCard = (CardView) view.findViewById(R.id.imgCard);
            this.picture.setOnLongClickListener(new View.OnLongClickListener() { 
                @Override 
                public boolean onLongClick(View view2) {
                    DuplicateImageActivity.layout_delete.setVisibility(0);
                    SimilarImageAdapter.checkLong = false;
                    return false;
                }
            });
            this.picture.setOnClickListener(new View.OnClickListener() { 
                @Override 
                public void onClick(View view2) {
                    if (SimilarImageAdapter.checkLong) {
                        pictureFacer picturefacer = (pictureFacer) SimilarImageAdapter.this.imagesDataList.get(PhotosViewHolder.this.getAdapterPosition());
                        String picturePath = picturefacer.getPicturePath();
                        boolean contains = SimilarImageAdapter.this.passData.contains(picturefacer);
                        Log.e("Postiotn--)", "" + contains + " " + picturePath);
                        SimilarImageAdapter.this.clickListener.onPicClicked(PhotosViewHolder.this.getAdapterPosition(), SimilarImageAdapter.this.passData, picturePath);
                        PhotosViewHolder.this.img_select.setVisibility(4);
                        PhotosViewHolder.this.img_select.setImageResource(0);
                        PhotosViewHolder.this.rlt_select.setVisibility(4);
                        return;
                    }
                    pictureFacer picturefacer2 = (pictureFacer) SimilarImageAdapter.this.imagesDataList.get(PhotosViewHolder.this.getAdapterPosition());
                    try {
                        if (SimilarImageAdapter.this.selectedDeleteData.contains(picturefacer2)) {
                            PhotosViewHolder.this.img_select.setVisibility(4);
                            PhotosViewHolder.this.rlt_select.setVisibility(4);
                            SimilarImageAdapter.this.selectedDeleteData.remove(picturefacer2);
                            DuplicateImageActivity.setSelectedData(SimilarImageAdapter.this.selectedDeleteData.size());
                            PhotosViewHolder.this.imgCard.setBackgroundColor(SimilarImageAdapter.this.activity.getResources().getColor(R.color.white));
                            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(-1, -1);
                            layoutParams.setMargins(0, 0, 0, 0);
                            PhotosViewHolder.this.picture.setLayoutParams(layoutParams);
                            PhotosViewHolder.this.img_select.setImageResource(R.drawable.radio_unselect);
                        } else {
                            PhotosViewHolder.this.img_select.setVisibility(0);
                            PhotosViewHolder.this.rlt_select.setVisibility(0);
                            SimilarImageAdapter.this.selectedDeleteData.add(picturefacer2);
                            DuplicateImageActivity.setSelectedData(SimilarImageAdapter.this.selectedDeleteData.size());
                            PhotosViewHolder.this.img_select.setVisibility(0);
                            PhotosViewHolder.this.img_select.setImageResource(R.drawable.ic_tick_icon);
                            int dimension = (int) SimilarImageAdapter.this.activity.getResources().getDimension(R.dimen._8sdp);
                            PhotosViewHolder.this.imgCard.setBackgroundColor(SimilarImageAdapter.this.activity.getResources().getColor(R.color.back_transparncy));
                            RelativeLayout.LayoutParams layoutParams2 = new RelativeLayout.LayoutParams(-1, -1);
                            layoutParams2.setMargins(dimension, dimension, dimension, dimension);
                            PhotosViewHolder.this.picture.setLayoutParams(layoutParams2);
                        }
                    } catch (Exception unused) {
                    }
                }
            });
        }
    }

   
    public class HeaderViewHolder extends RecyclerView.ViewHolder {
        private TextView txt_date;

        public HeaderViewHolder(View view) {
            super(view);
            this.txt_date = (TextView) view.findViewById(R.id.txtHeader);
        }
    }
}
