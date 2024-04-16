package com.iten.tenoku.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Data{

    @SerializedName("objAPP_SETTINGS")
    ObjAPPSETTINGS objAPPSETTINGS;

    @SerializedName("objPLACEMENT")
    ObjPLACEMENT objPLACEMENT;

    @SerializedName("isSTATUS")
    boolean isSTATUS;

    @SerializedName("vMSG")
    String vMSG;


    public void setObjAPPSETTINGS(ObjAPPSETTINGS objAPPSETTINGS) {
        this.objAPPSETTINGS = objAPPSETTINGS;
    }
    public ObjAPPSETTINGS getObjAPPSETTINGS() {
        return objAPPSETTINGS;
    }

    public void setObjPLACEMENT(ObjPLACEMENT objPLACEMENT) {
        this.objPLACEMENT = objPLACEMENT;
    }
    public ObjPLACEMENT getObjPLACEMENT() {
        return objPLACEMENT;
    }

    public void setIsSTATUS(boolean isSTATUS) {
        this.isSTATUS = isSTATUS;
    }
    public boolean getIsSTATUS() {
        return isSTATUS;
    }

    public void setVMSG(String vMSG) {
        this.vMSG = vMSG;
    }
    public String getVMSG() {
        return vMSG;
    }
}