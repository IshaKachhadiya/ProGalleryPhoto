package com.iten.tenoku.networking;


import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface Api {

    @FormUrlEncoded
    @POST("appSetting/singleAppDetails")
    Call<AdsResponse> addapikey(@Field("vApikey") String Apikey);

    @POST("requestChack/request")
    Call<ResponseBody> requestCheck(@Body RequestBody requestBody);


}