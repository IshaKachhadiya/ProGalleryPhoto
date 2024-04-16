package hdphoto.galleryimages.gelleryalbum.location_media.event;

import java.util.ArrayList;

import hdphoto.galleryimages.gelleryalbum.location_media.model.LocationImageData;


public class LocationEvent {
    ArrayList<LocationImageData> deleteList;

    public LocationEvent(ArrayList<LocationImageData> arrayList) {
        new ArrayList();
        this.deleteList = arrayList;
    }

    public ArrayList<LocationImageData> getDeleteList() {
        return this.deleteList;
    }

    public void setDeleteList(ArrayList<LocationImageData> arrayList) {
        this.deleteList = arrayList;
    }
}
