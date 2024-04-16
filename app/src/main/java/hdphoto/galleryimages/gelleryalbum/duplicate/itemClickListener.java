package hdphoto.galleryimages.gelleryalbum.duplicate;

import java.util.ArrayList;

import hdphoto.galleryimages.gelleryalbum.activity.ImageFavAdapter;
import hdphoto.galleryimages.gelleryalbum.adapter.VideoListAdapter;
import hdphoto.galleryimages.gelleryalbum.model.mVideo;

public interface itemClickListener {
    void onPicClicked(ImageFavAdapter.PicHolder picHolder, int i, ArrayList<pictureFacer> arrayList);

    void onVideoClicked(VideoListAdapter.VideoHolder videoHolder, int i, ArrayList<mVideo> arrayList);
}