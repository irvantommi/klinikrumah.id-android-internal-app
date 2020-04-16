package id.klinikrumah.internal.rest;

import com.google.gson.JsonObject;

import java.util.List;

import id.klinikrumah.internal.model.Lead;
import id.klinikrumah.internal.model.User;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.POST;
import retrofit2.http.PUT;

public interface ApiInterface {
    @GET("lead_list")
    Call<List<Lead>> getLeadList();

    // template
    @GET("login")
    Call<User> getUser();

    @FormUrlEncoded
    @POST("auth")
    Call<JsonObject> login(@Field("email") String username,
                           @Field("password") String password);

    @FormUrlEncoded
    @PUT("update")
    Call<User> updateUser(@Field("id") String id,
                          @Field("email") String username,
                          @Field("password") String password,
                          @Field("name") String name);

    @FormUrlEncoded
    @HTTP(method = "DELETE", path = "user", hasBody = true)
    Call<User> deleteUser(@Field("id") String id);
}