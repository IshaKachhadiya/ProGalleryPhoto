package hdphoto.galleryimages.gelleryalbum.location_media.adapter;

import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;

import hdphoto.galleryimages.gelleryalbum.R;
import hdphoto.galleryimages.gelleryalbum.location_media.model.AlbumData;


public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.ViewHolder> {
    FragmentActivity activity;
    ArrayList<AlbumData> albums;
    OnSelectAlbum onSelectAlbum;

    
    public interface OnSelectAlbum {
        void onClickAlbum(int i);
    }

    public AlbumAdapter(FragmentActivity fragmentActivity, ArrayList<AlbumData> arrayList, OnSelectAlbum onSelectAlbum) {
        this.activity = fragmentActivity;
        this.onSelectAlbum = onSelectAlbum;
        this.albums = arrayList;
    }

    @Override 
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(this.activity).inflate(R.layout.item_album, viewGroup, false));
    }

    @Override 
    public void onBindViewHolder(ViewHolder viewHolder, final int i) {
        try {
            if (this.albums.get(i).getPictureData() != null && this.albums.get(i).getPictureData().size() != 0) {
                Glide.with(this.activity).load(this.albums.get(i).getPictureData().get(0).getFilePath()).placeholder(ContextCompat.getDrawable(this.activity, R.drawable.ic_unselect_gallery)).into(viewHolder.albumImage);
            } else {
                Glide.with(this.activity).load(ContextCompat.getDrawable(this.activity, R.drawable.ic_unselect_gallery)).placeholder(ContextCompat.getDrawable(this.activity, R.drawable.ic_unselect_gallery)).into(viewHolder.albumImage);
            }
            TextView customTextView = viewHolder.albumName;
            customTextView.setText(this.albums.get(i).getTitle() + "\n(" + this.albums.get(i).getPictureData().size() + ")");
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() { // from class: com.gallery.photos.pro.adapter.AlbumAdapter$$ExternalSyntheticLambda0
                @Override 
                public final void onClick(View view) {
                    AlbumAdapter.this.m346xb2ec9615(i, view);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    
    /* renamed from: lambda$onBindViewHolder$0$com-gallery-photos-pro-adapter-AlbumAdapter  reason: not valid java name */
    public  void m346xb2ec9615(int i, View view) {
        this.onSelectAlbum.onClickAlbum(i);
    }

    @Override 
    public int getItemCount() {
        return this.albums.size();
    }

    public void addData(ArrayList<AlbumData> arrayList) {
        this.albums = arrayList;
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
            AlbumAdapter.this.activity.getWindowManager().getDefaultDisplay().getMetrics(new DisplayMetrics());
            this.albumImage.setShapeAppearanceModel(this.albumImage.getShapeAppearanceModel().toBuilder().setAllCornerSizes(AlbumAdapter.this.activity.getResources().getDimension(R.dimen._6sdp)).build());
        }
    }
}
