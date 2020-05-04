package id.klinikrumah.internal.rest;

import com.google.gson.JsonObject;

import java.util.List;

import id.klinikrumah.internal.model.User;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Url;

public interface ApiInterface {
    // Method
    String GET_LEAD_LIST = "getLeadList";
    String SAVE_LEAD = "saveLead";
    // Header
    String CONTENT_LENGTH = "Content-Length";
    String CONTENT_RANGE = "Content-Range"; // https://developers.google.com/drive/api/v3/manage-uploads#multiple-chunks_1
    String X_UPLOAD_CONTENT_TYPE = "X-Upload-Content-Type";
    String X_UPLOAD_CONTENT_LENGTH = "X-Upload-Content-Length";
    String UPLOAD_RESUMABLE = "?uploadType=resumable";
    // Field
    String ID = "id";
    String NAME = "name";
    String LEAD = "lead";

    @GET(GET_LEAD_LIST)
    Call<JsonObject> getLeadList();

    @GET()
    Call<JsonObject> getLeadDetail(@Url String leadId);

    @FormUrlEncoded
    @POST(SAVE_LEAD)
    Call<JsonObject> saveLead(@Field(LEAD) String jsonLead);

    // google
    // https://futurestud.io/tutorials/retrofit-add-custom-request-header
//    @Headers({
//            "Content-Length: 40",
//            "X-Upload-Content-Type: image/jpeg",
//            "X-Upload-Content-Length: 2000000"
//    })
    @Multipart
    @POST(UPLOAD_RESUMABLE)
    Call<ResponseBody> upload(
            @Header(CONTENT_LENGTH) int contentLength,
            @Header(X_UPLOAD_CONTENT_TYPE) String xUploadContentType,
            @Header(X_UPLOAD_CONTENT_LENGTH) int xUploadContentLength,
            @Part(ID) RequestBody fileId,
            @Part(NAME) RequestBody fileName,
            @Part MultipartBody.Part file
    );

    @FormUrlEncoded
    @PUT()
    Call<User> resumeUpload(
            @Url String fileId, // "/fileId?uploadType=resumable" // https://futurestud.io/tutorials/retrofit-2-how-to-use-dynamic-urls-for-requests
            @Header(CONTENT_LENGTH) String contentLength,
            @Header(X_UPLOAD_CONTENT_TYPE) String xUploadContentType,
            @Header(X_UPLOAD_CONTENT_LENGTH) String xUploadContentLength,
            @Field(NAME) String fileName
    );

    // template
    @FormUrlEncoded
    @POST("auth")
    Call<JsonObject> login(
            @Field("email") String username,
            @Field("password") String password
    );

    @FormUrlEncoded
    @PUT("update")
    Call<User> updateUser(
            @Field("id") String id,
            @Field("email") String username,
            @Field("password") String password,
            @Field("name") String name
    );

    @FormUrlEncoded
    @HTTP(method = "DELETE", path = "user", hasBody = true)
    Call<User> deleteUser(@Field("id") String id);
}