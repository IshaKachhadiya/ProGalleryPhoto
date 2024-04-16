package hdphoto.galleryimages.gelleryalbum.location_media.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;

import hdphoto.galleryimages.gelleryalbum.R;
import hdphoto.galleryimages.gelleryalbum.location_media.model.LocationImageData;


public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.ViewHolder> {
    Activity activity;
    ArrayList<LocationImageData> locationImageDatas;
    OnSelectLocation onSelectLocation;

    
    public interface OnSelectLocation {
        void onClickLocation(int i);
    }

    public LocationAdapter(Activity activity, ArrayList<LocationImageData> arrayList, OnSelectLocation onSelectLocation) {
        this.activity = activity;
        this.onSelectLocation = onSelectLocation;
        this.locationImageDatas = arrayList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(this.activity).inflate(R.layout.item_album, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int i) {
        Glide.with(this.activity).load(this.locationImageDatas.get(i).getPictureData().get(0).getFilePath()).placeholder(ContextCompat.getDrawable(this.activity, R.drawable.no_photo_video)).into(viewHolder.albumImage);
        TextView customTextView = viewHolder.albumCount;
        customTextView.setText("(" + this.locationImageDatas.get(i).getPictureData().size() + ")");
        viewHolder.albumName.setText(this.locationImageDatas.get(i).getTitle());
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override 
            public void onClick(View view) {
                LocationAdapter.this.onSelectLocation.onClickLocation(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.locationImageDatas.size();
    }

    public void addData(ArrayList<LocationImageData> arrayList) {
        this.locationImageDatas = arrayList;
        notifyDataSetChanged();
    }

    
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView albumCount;
        ShapeableImageView albumImage;
        TextView albumName;

        public ViewHolder(View view) {
            super(view);
            this.albumImage = (ShapeableImageView) view.findViewById(R.id.albumImage);
            this.albumName = (TextView) view.findViewById(R.id.albumName);
            this.albumCount = (TextView) view.findViewById(R.id.albumCount);
            this.albumImage.setShapeAppearanceModel(this.albumImage.getShapeAppearanceModel().toBuilder().setAllCornerSizes(LocationAdapter.this.activity.getResources().getDimension(R.dimen._6sdp)).build());
        }
    }
}
