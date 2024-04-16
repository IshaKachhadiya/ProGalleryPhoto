package hdphoto.galleryimages.gelleryalbum;


import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class GalleryFolder implements Parcelable {

    public static final Creator<GalleryFolder> CREATOR = new Creator<GalleryFolder>() {
        @Override
        public GalleryFolder createFromParcel(Parcel in) {
            return new GalleryFolder(in);
        }

        @Override
        public GalleryFolder[] newArray(int size) {
            return new GalleryFolder[size];
        }
    };

    private String dateAdded;
    private String displayName;
    private String duration;
    private String id;
    private String path;
    private String size;
    private String title;

    public GalleryFolder(String id, String title, String displayName, String size, String duration, String path, String dateAdded) {
        this.id = id;
        this.title = title;
        this.displayName = displayName;
        this.size = size;
        this.duration = duration;
        this.path = path;
        this.dateAdded = dateAdded;
    }

    protected GalleryFolder(Parcel in) {
        this.id = in.readString();
        this.title = in.readString();
        this.displayName = in.readString();
        this.size = in.readString();
        this.duration = in.readString();
        this.path = in.readString();
        this.dateAdded = in.readString();
    }


    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getSize() {
        return this.size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getDuration() {
        return this.duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getPath() {
        return this.path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getDateAdded() {
        return this.dateAdded;
    }

    public void setDateAdded(String dateAdded) {
        this.dateAdded = dateAdded;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(this.id);
        parcel.writeString(this.title);
        parcel.writeString(this.displayName);
        parcel.writeString(this.size);
        parcel.writeString(this.duration);
        parcel.writeString(this.path);
        parcel.writeString(this.dateAdded);

    }
}
