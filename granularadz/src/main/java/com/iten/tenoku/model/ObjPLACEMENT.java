package com.iten.tenoku.model;

import com.google.gson.annotations.SerializedName;

   
public class ObjPLACEMENT {

    @SerializedName("objFestum")
    ObjFestum objFestum;
   @SerializedName("objUnity")
   ObjUnity objUnity;

   @SerializedName("objFacebook")
   ObjFacebook objFacebook;

   @SerializedName("objAdmob")
   ObjAdmob objAdmob;

   @SerializedName("objQureka")
   ObjQureka objQureka;

   @SerializedName("objCommonSettings")
   public ObjCommonSettings objCommonSettings;

    public ObjFestum getObjFestum() {
        return objFestum;
    }

    public void setObjUnity(ObjUnity objUnity) {
        this.objUnity = objUnity;
    }
    public ObjUnity getObjUnity() {
        return objUnity;
    }
    
    public void setObjFacebook(ObjFacebook objFacebook) {
        this.objFacebook = objFacebook;
    }
    public ObjFacebook getObjFacebook() {
        return objFacebook;
    }
    
    public void setObjAdmob(ObjAdmob objAdmob) {
        this.objAdmob = objAdmob;
    }
    public ObjAdmob getObjAdmob() {
        return objAdmob;
    }
    
    public void setObjQureka(ObjQureka objQureka) {
        this.objQureka = objQureka;
    }
    public ObjQureka getObjQureka() {
        return objQureka;
    }
    
    public void setObjCommonSettings(ObjCommonSettings objCommonSettings) {
        this.objCommonSettings = objCommonSettings;
    }
    public ObjCommonSettings getObjCommonSettings() {
        return objCommonSettings;
    }
    
}