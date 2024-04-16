package hdphoto.galleryimages.gelleryalbum.location_media.event;

public class VideoEvent {
    String folderName;
    String imagePath;

    public VideoEvent(String str, String str2) {
        this.imagePath = str;
        this.folderName = str2;
    }

    public String getFolderName() {
        return this.folderName;
    }

    public void setFolderName(String str) {
        this.folderName = str;
    }

    public String getImagePath() {
        return this.imagePath;
    }

    public void setImagePath(String str) {
        this.imagePath = str;
    }
}