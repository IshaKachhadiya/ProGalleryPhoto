package hdphoto.galleryimages.gelleryalbum.model;

public class LangModel {
    public int image;
    public Boolean isCheck;
    public String isoLanguage;
    public String languageName;

    public LangModel(String str, String str2, Boolean bool) {
        this.languageName = str;
        this.isoLanguage = str2;
        this.isCheck = bool;
//        this.image = i;
    }

    public String getLanguageName() {
        return this.languageName;
    }

    public void setLanguageName(String str) {
        this.languageName = str;
    }

    public String getIsoLanguage() {
        return this.isoLanguage;
    }

    public void setIsoLanguage(String str) {
        this.isoLanguage = str;
    }

    public Boolean getCheck() {
        return this.isCheck;
    }

    public void setCheck(Boolean bool) {
        this.isCheck = bool;
    }

    public int getImage() {
        return this.image;
    }

    public void setImage(int i) {
        this.image = i;
    }
}
