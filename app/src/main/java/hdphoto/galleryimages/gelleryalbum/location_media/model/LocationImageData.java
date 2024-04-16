package hdphoto.galleryimages.gelleryalbum.location_media.model;

import java.io.Serializable;
import java.util.ArrayList;


public class LocationImageData implements Serializable {
    String lat;
    String longs;
    String title;
    ArrayList<PictureData> pictureData = new ArrayList<>();
    boolean isSelect = false;

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String str) {
        this.title = str;
    }

    public ArrayList<PictureData> getPictureData() {
        return this.pictureData;
    }

    public void setPictureData(ArrayList<PictureData> arrayList) {
        this.pictureData = arrayList;
    }

    public String getLat() {
        return this.lat;
    }

    public void setLat(String str) {
        this.lat = str;
    }

    public String getLongs() {
        return this.longs;
    }

    public void setLongs(String str) {
        this.longs = str;
    }

    public boolean isSelect() {
        return this.isSelect;
    }

    public void setSelect(boolean z) {
        this.isSelect = z;
    }
}
