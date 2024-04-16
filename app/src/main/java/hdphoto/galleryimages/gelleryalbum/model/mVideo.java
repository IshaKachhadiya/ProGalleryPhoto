package hdphoto.galleryimages.gelleryalbum.model;


public class mVideo {
    boolean boolean_selected;
    private Boolean selected = false;
    String str_date;
    String str_duration;
    double str_length;
    String str_name;
    String str_path;
    String str_resolution;
    String str_size;
    String str_thumb;

    public double getStr_length() {
        return this.str_length;
    }

    public void setStr_length(double d) {
        this.str_length = d;
    }

    public String getStr_date() {
        return this.str_date;
    }

    public void setStr_date(String str) {
        this.str_date = str;
    }

    public Boolean getSelected() {
        return this.selected;
    }

    public void setSelected(Boolean bool) {
        this.selected = bool;
    }

    public String getStr_size() {
        return this.str_size;
    }

    public void setStr_size(String str) {
        this.str_size = str;
    }

    public String getStr_duration() {
        return this.str_duration;
    }

    public void setStr_duration(String str) {
        this.str_duration = str;
    }

    public String getStr_resolution() {
        return this.str_resolution;
    }

    public void setStr_resolution(String str) {
        this.str_resolution = str;
    }

    public String getStr_name() {
        return this.str_name;
    }

    public void setStr_name(String str) {
        this.str_name = str;
    }

    public String getStr_path() {
        return this.str_path;
    }

    public void setStr_path(String str) {
        this.str_path = str;
    }

    public String getStr_thumb() {
        return this.str_thumb;
    }

    public void setStr_thumb(String str) {
        this.str_thumb = str;
    }

    public boolean isBoolean_selected() {
        return this.boolean_selected;
    }

    public void setBoolean_selected(boolean z) {
        this.boolean_selected = z;
    }
}
