package com.joung.vienna.retrofit;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface FCMAPI {

    @Headers({"Content-Type: application/json"})
    @POST(BaseUrl.PATH_SEND_FCM)
    Call<JsonObject> sendFCM(@Header("Authorization") String token, @Body JsonObject device);
}
