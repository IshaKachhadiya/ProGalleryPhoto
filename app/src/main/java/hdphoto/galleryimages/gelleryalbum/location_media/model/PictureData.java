package hdphoto.galleryimages.gelleryalbum.location_media.model;

import android.net.Uri;

import java.io.Serializable;
import java.util.ArrayList;


public class PictureData implements Serializable {
    long date;
    String duration;
    String fileName;
    String filePath;
    Long fileSize;
    boolean isSelected;
    String lat;
    String longs;
    String thing;
    String thumbnails;
    Uri uri;
    public String folderName = "";
    ArrayList<Integer> colorList = new ArrayList<>();
    boolean isVideo = false;
    boolean isCheckboxVisible = false;
    boolean isFavorite = false;

    public String getMediaType() {
        return mediaType;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    public String mediaType;


    public ArrayList<Integer> getColorList() {
        return this.colorList;
    }

    public void setColorList(ArrayList<Integer> arrayList) {
        this.colorList = arrayList;
    }

    public long getDate() {
        return this.date;
    }

    public void setDate(long j) {
        this.date = j;
    }

    public boolean isFavorite() {
        return this.isFavorite;
    }

    public void setFavorite(boolean z) {
        this.isFavorite = z;
    }

    public boolean isSelected() {
        return this.isSelected;
    }

    public void setSelected(boolean z) {
        this.isSelected = z;
    }

    public boolean isCheckboxVisible() {
        return this.isCheckboxVisible;
    }

    public void setCheckboxVisible(boolean z) {
        this.isCheckboxVisible = z;
    }

    public String getFileName() {
        return this.fileName;
    }

    public void setFileName(String str) {
        this.fileName = str;
    }

    public String getFilePath() {
        return this.filePath;
    }

    public void setFilePath(String str) {
        this.filePath = str;
    }

    public Long getFileSize() {
        return this.fileSize;
    }

    public void setFileSize(Long l) {
        this.fileSize = l;
    }

    public String getFolderName() {
        return this.folderName;
    }

    public void setFolderName(String str) {
        this.folderName = str;
    }

    public String getThumbnails() {
        return this.thumbnails;
    }

    public void setThumbnails(String str) {
        this.thumbnails = str;
    }

    public String getDuration() {
        return this.duration;
    }

    public void setDuration(String str) {
        this.duration = str;
    }

    public boolean isVideo() {
        return this.isVideo;
    }

    public void setVideo(boolean z) {
        this.isVideo = z;
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

    public Uri getUri() {
        return this.uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }

    public String getThing() {
        return this.thing;
    }

    public void setThing(String str) {
        this.thing = this.thing;
    }
}
