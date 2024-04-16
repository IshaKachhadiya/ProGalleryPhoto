package hdphoto.galleryimages.gelleryalbum.duplicate;

public class pictureFacer {
    private String date;
    private String imageUri;
    private double length;
    private String picturName;
    private String picturePath;
    private String pictureSize;
    private Boolean selected = false;

    public String getDate() {
        return this.date;
    }

    public void setDate(String str) {
        this.date = str;
    }

    public double getLength() {
        return this.length;
    }

    public void setLength(double d) {
        this.length = d;
    }

    public pictureFacer() {
    }

    public pictureFacer(String str, String str2, String str3, String str4) {
        this.picturName = str;
        this.picturePath = str2;
        this.pictureSize = str3;
        this.imageUri = str4;
    }

    public String getPicturName() {
        return this.picturName;
    }

    public void setPicturName(String str) {
        this.picturName = str;
    }

    public String getPicturePath() {
        return this.picturePath;
    }

    public void setPicturePath(String str) {
        this.picturePath = str;
    }

    public String getPictureSize() {
        return this.pictureSize;
    }

    public void setPictureSize(String str) {
        this.pictureSize = str;
    }

    public String getImageUri() {
        return this.imageUri;
    }

    public void setImageUri(String str) {
        this.imageUri = str;
    }

    public Boolean getSelected() {
        return this.selected;
    }

    public void setSelected(Boolean bool) {
        this.selected = bool;
    }
}