package hdphoto.galleryimages.gelleryalbum.location_media.model;

import java.io.Serializable;
import java.util.ArrayList;


public class AlbumData implements Serializable {
    int dateCounter;
    String folderPath;
    String title = "";
    ArrayList<PictureData> pictureData = new ArrayList<>();
    boolean isSelect = false;

    public int getDateCounter() {
        return this.dateCounter;
    }

    public void setDateCounter(int i) {
        this.dateCounter = i;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String str) {
        this.title = str;
    }

    public String getFolderPath() {
        return this.folderPath;
    }

    public void setFolderPath(String str) {
        this.folderPath = str;
    }

    public ArrayList<PictureData> getPictureData() {
        return this.pictureData;
    }

    public void setPictureData(ArrayList<PictureData> arrayList) {
        this.pictureData = arrayList;
    }

    public boolean isSelect() {
        return this.isSelect;
    }

    public void setSelect(boolean z) {
        this.isSelect = z;
    }
}
