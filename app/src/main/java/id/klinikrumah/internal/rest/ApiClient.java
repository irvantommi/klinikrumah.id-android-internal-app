package id.klinikrumah.internal.rest;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import id.klinikrumah.internal.App;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class ApiClient {
    private static final String SCHEME = "http://";
    private static final String SCHEME_SECURE = "https://";

    private static final String BASE_HOST = "api.irvandroid.com";
    private static final String BASE_VERSION = "/v1";
    private static final String BASE_PATH = "/klinikrumah" + BASE_VERSION;
    private static final String BASE_URL = SCHEME_SECURE + BASE_HOST + BASE_PATH + "/";

    private static final String G_API_HOST = "www.googleapis.com";
    private static final String G_API_VERSION = "/v3";
    private static final String GDRIVE_PATH = "/drive" + G_API_VERSION + "/files";
    private static final String GDRIVE_PATH_UPLOAD = "/upload/drive" + G_API_VERSION + "/files";
    private static final String GDRIVE_UPLOAD_API = SCHEME_SECURE + G_API_HOST + GDRIVE_PATH_UPLOAD + "/";

    private static final String AUTHORIZATION = "Authorization";
    private static final String BEARER = "Bearer ";
    private static final String CONTENT_TYPE = "Content-Type";
    private static final String CONTENT_TYPE_DEFAULT = "application/json; charset=UTF-8";
    private static final String CONTENT_TYPE_MULTIPART = "multipart/related";

    private static Retrofit retrofit, rGoogle = null;
    private static App app = App.getInstance();

    public static Retrofit getRetrofit() {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        Interceptor interceptor = new Interceptor() {
            @Override
            public Response intercept(@NotNull Chain chain) throws IOException {
                Request original = chain.request();
//                Request request = original.newBuilder()
//                        .header(AUTHORIZATION, BEARER + app.getAccountGoogle().getGoogleToken())
//                        .header(CONTENT_TYPE, CONTENT_TYPE_DEFAULT)
//                        .method(original.method(), original.body())
//                        .build();
                return chain.proceed(original);
            }
        };
        if (!httpClient.interceptors().contains(interceptor)) {
            httpClient.addInterceptor(interceptor);
            if (retrofit == null) {
                retrofit = new Retrofit.Builder()
                        .baseUrl(BASE_URL)
                        .addConverterFactory(ScalarsConverterFactory.create())
                        .addConverterFactory(GsonConverterFactory.create())
                        .client(httpClient.build())
                        .build();
            }
        }
        return retrofit;
    }

    public static Retrofit getRGoogle() {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        Interceptor interceptor = new Interceptor() {
            @Override
            public Response intercept(@NotNull Chain chain) throws IOException {
                Request original = chain.request();

                Request request = original.newBuilder()
                        .header(AUTHORIZATION, BEARER + app.getAccountGoogle().getGoogleToken())
                        .header(CONTENT_TYPE, CONTENT_TYPE_DEFAULT)
                        .method(original.method(), original.body())
                        .build();

                return chain.proceed(request);
            }
        };
        if (!httpClient.interceptors().contains(interceptor)) {
            httpClient.addInterceptor(interceptor);
            if (rGoogle == null) {
                rGoogle = new Retrofit.Builder()
                        .baseUrl(GDRIVE_UPLOAD_API)
                        .addConverterFactory(GsonConverterFactory.create(app.getGson()))
                        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                        .client(httpClient.build())
                        .build();
            }
        }
        return rGoogle;
    }
}