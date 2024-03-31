package com.example.tracktechsopt;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiInterface {
    @FormUrlEncoded
    @POST("upload_cars_tracktechgt.php") // Asegúrate de que la ruta sea correcta
    Call<String> uploadImage(@Field("image") String imageBase64, @Field("imageFolde") String imageFolder, @Field("imageName") String imageName);
}