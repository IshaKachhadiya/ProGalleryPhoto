package com.iten.tenoku.networking;

import com.google.gson.annotations.SerializedName;
import com.iten.tenoku.model.Data;

public class AdsResponse {

    @SerializedName("iStatusCode")
    int iStatusCode;

    @SerializedName("isStatus")
    boolean isStatus;

    @SerializedName("data")
    public Data data;

    @SerializedName("vMessage")
    String vMessage;


    public void setIStatusCode(int iStatusCode) {
        this.iStatusCode = iStatusCode;
    }
    public int getIStatusCode() {
        return iStatusCode;
    }

    public void setIsStatus(boolean isStatus) {
        this.isStatus = isStatus;
    }
    public boolean getIsStatus() {
        return isStatus;
    }

    public void setData(Data data) {
        this.data = data;
    }
    public Data getData() {
        return data;
    }

    public void setVMessage(String vMessage) {
        this.vMessage = vMessage;
    }
    public String getVMessage() {
        return vMessage;
    }

}
