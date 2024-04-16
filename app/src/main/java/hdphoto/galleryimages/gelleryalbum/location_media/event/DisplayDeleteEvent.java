package hdphoto.galleryimages.gelleryalbum.location_media.event;

import java.util.ArrayList;


public class DisplayDeleteEvent {
    ArrayList<String> deleteList;

    public DisplayDeleteEvent(ArrayList<String> arrayList) {
        new ArrayList();
        this.deleteList = arrayList;
    }

    public ArrayList<String> getDeleteList() {
        return this.deleteList;
    }

    public void setDeleteList(ArrayList<String> arrayList) {
        this.deleteList = arrayList;
    }
}
