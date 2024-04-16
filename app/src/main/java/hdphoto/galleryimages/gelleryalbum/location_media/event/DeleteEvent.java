package hdphoto.galleryimages.gelleryalbum.location_media.event;

import java.util.ArrayList;


public class DeleteEvent {
    ArrayList<String> deleteList;
    int pos;

    public DeleteEvent(int i, ArrayList<String> arrayList) {
        new ArrayList();
        this.pos = i;
        this.deleteList = arrayList;
    }

    public ArrayList<String> getDeleteList() {
        return this.deleteList;
    }

    public void setDeleteList(ArrayList<String> arrayList) {
        this.deleteList = arrayList;
    }

    public int getPos() {
        return this.pos;
    }

    public void setPos(int i) {
        this.pos = i;
    }
}
