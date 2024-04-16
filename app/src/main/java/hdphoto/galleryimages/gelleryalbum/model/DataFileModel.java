package hdphoto.galleryimages.gelleryalbum.model;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.ArrayList;


public class DataFileModel implements Parcelable {
    public static final Parcelable.Creator<DataFileModel> CREATOR = new Parcelable.Creator<DataFileModel>() {

        @Override
        public DataFileModel createFromParcel(Parcel parcel) {
            return new DataFileModel(parcel);
        }


        @Override
        public DataFileModel[] newArray(int i) {
            return new DataFileModel[i];
        }
    };
    ArrayList<Object> SubFile;
    String albumName;
    public String bucket_id;
    public String directory;
    public long duration;
    public String filedate;
    public String filesize;
    public String folderName;
    public String folderPath;
    public long id;
    public String id1;
    public boolean isDirectory;
    public boolean isSelected;
    long lastModified;
    public String mediaType;
    public String modifiedDate;
    public String name;
    public String oldpath;
    public String path;
    public ArrayList<String> pathlist;
    public String takenDate;
    boolean isVideo = false;
    private Boolean selected = false;

    public Boolean getSelected() {
        return selected;
    }

    public void setSelected(Boolean selected) {
        this.selected = selected;
    }





    @Override 
    public int describeContents() {
        return 0;
    }
    public boolean isVideo() {
        return this.isVideo;
    }

    public void setVideo(boolean z) {
        this.isVideo = z;
    }

    public String getFilesize() {
        return this.filesize;
    }

    public void setFilesize(String str) {
        this.filesize = str;
    }

    public String getFiledate() {
        return this.filedate;
    }

    public void setFiledate(String str) {
        this.filedate = str;
    }

    public DataFileModel(long j, String str, String str2, String str3, String str4, boolean z) {
        this.id = j;
        this.name = str;
        this.path = str2;
        this.oldpath = str3;
        this.directory = str4;
        this.isSelected = z;
    }

    public DataFileModel(long j, String str, String str2, long j2, String str3, String str4, boolean z) {
        this.id = j;
        this.name = str;
        this.path = str2;
        this.duration = j2;
        this.oldpath = str3;
        this.directory = str4;
        this.isSelected = z;
    }

    public DataFileModel(long j, String str, String str2, long j2, String str3, String str4, boolean z, String str5) {
        this.id = j;
        this.name = str;
        this.path = str2;
        this.duration = j2;
        this.oldpath = str3;
        this.directory = str4;
        this.isSelected = z;
        this.mediaType = str5;
    }

    public DataFileModel(long j, String str, String str2, long j2, String str3, String str4, boolean z, String str5, String str6, String str7) {
        this.id = j;
        this.name = str;
        this.path = str2;
        this.duration = j2;
        this.oldpath = str3;
        this.directory = str4;
        this.isSelected = z;
        this.mediaType = str5;
        this.takenDate = str6;
        this.modifiedDate = str7;
    }

    public DataFileModel() {
    }

    public String getAlbumName() {
        return this.albumName;
    }

    public void setAlbumName(String str) {
        this.albumName = str;
    }

    public String getFolderName() {
        return this.folderName;
    }

    public void setFolderName(String str) {
        this.folderName = str;
    }

    public String getBucket_id() {
        return this.bucket_id;
    }

    public void setBucket_id(String str) {
        this.bucket_id = str;
    }

    public String getFolderPath() {
        return this.folderPath;
    }

    public void setFolderPath(String str) {
        this.folderPath = str;
    }

    public boolean isDirectory() {
        return this.isDirectory;
    }

    public void setDirectory(boolean z) {
        this.isDirectory = z;
    }

    public ArrayList<Object> getSubFile() {
        return this.SubFile;
    }

    public void setSubFile(ArrayList<Object> arrayList) {
        this.SubFile = arrayList;
    }

    @Override 
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(this.id);
        parcel.writeString(this.name);
        parcel.writeString(this.path);
        parcel.writeLong(this.duration);
        parcel.writeLong(Long.parseLong(this.filesize));
        parcel.writeLong(Long.parseLong(this.filedate));
    }

    private DataFileModel(Parcel parcel) {
        this.id = parcel.readLong();
        this.name = parcel.readString();
        this.path = parcel.readString();
        this.duration = parcel.readLong();
        this.filesize = parcel.readString();
        this.filedate = parcel.readString();
    }

    public DataFileModel(String str) {
        this.path = str;
    }

    public String getTakenDate() {
        return this.takenDate;
    }

    public void setTakenDate(String str) {
        this.takenDate = str;
    }

    public String getModifiedDate() {
        return this.modifiedDate;
    }

    public void setModifiedDate(String str) {
        this.modifiedDate = str;
    }

    public String getMediaType() {
        return this.mediaType;
    }

    public void setMediaType(String str) {
        this.mediaType = str;
    }

    public long getLastModified() {
        return this.lastModified;
    }

    public void setLastModified(long j) {
        this.lastModified = j;
    }

    public ArrayList<String> getPathlist() {
        return this.pathlist;
    }

    public void setPathlist(ArrayList<String> arrayList) {
        this.pathlist = arrayList;
    }

    public String getId1() {
        return this.id1;
    }

    public void setId1(String str) {
        this.id1 = str;
    }

    public String getPath() {
        return this.path;
    }

    public void setPath(String str) {
        this.path = str;
    }
}
