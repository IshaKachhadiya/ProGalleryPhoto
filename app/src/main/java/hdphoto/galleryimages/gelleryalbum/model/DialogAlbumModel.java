package hdphoto.galleryimages.gelleryalbum.model;

import java.util.ArrayList;


public class DialogAlbumModel {
    public String bucket_id;
    public String coverThumb;
    public String folderPath;
    public String foldername;
    public String id;
    public boolean isSelected;
    public ArrayList<String> pathlist;
    int type;

    public String getCoverThumb() {
        return this.coverThumb;
    }

    public void setCoverThumb(String str) {
        this.coverThumb = str;
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

    public ArrayList<String> getPathlist() {
        return this.pathlist;
    }

    public void setPathlist(ArrayList<String> arrayList) {
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
