package hdphoto.galleryimages.gelleryalbum.model;

import android.graphics.Bitmap;
import java.util.ArrayList;


public class FolderModel {
    public Bitmap bitmap;
    public String bucket_id;
    public String coverThumb;
    public String folderPath;
    public String foldername;
    public String id;
    public boolean isSelected;
    public long lastModified;
    public ArrayList<DataFileModel> pathlist;
    int type;

    public long getLastModified() {
        return this.lastModified;
    }

    public void setLastModified(long j) {
        this.lastModified = j;
    }

    public String getCoverThumb() {
        return this.coverThumb;
    }

    public void setCoverThumb(String str) {
        this.coverThumb = str;
    }

    public Bitmap getBitmap() {
        return this.bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public String getBucket_id() {
        return this.bucket_id;
    }

    public void setBucket_id(String str) {
        this.bucket_id = str;
    }

    public String getFoldername() {
        return this.foldername;
    }

    public void setFoldername(String str) {
        this.foldername = str;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String str) {
        this.id = str;
    }

    public ArrayList<DataFileModel> getPathlist() {
        return this.pathlist;
    }

    public void setPathlist(ArrayList<DataFileModel> arrayList) {
        this.pathlist = arrayList;
    }

    public int getType() {
        return this.type;
    }

    public void setType(int i) {
        this.type = i;
    }

    public String getFolderPath() {
        return this.folderPath;
    }

    public void setFolderPath(String str) {
        this.folderPath = str;
    }

    public boolean isSelected() {
        return this.isSelected;
    }

    public void setSelected(boolean z) {
        this.isSelected = z;
    }
}
