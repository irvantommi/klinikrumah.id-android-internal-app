package id.klinikrumah.internal.rest;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    private static final String SCHEME = "http://";
    private static final String SCHEME_SECURE = "https://";

    private static final String BASE_HOST = "54.169.99.237";
    private static final String BASE_VERSION = "/v1";
    private static final String BASE_PATH = "/klinikrumah/index.php" + BASE_VERSION;
    private static final String BASE_URL = SCHEME + BASE_HOST + BASE_PATH + "/";

    private static final String GOOGLE_HOST = "www.googleapis.com";
    private static final String GOOGLE_VERSION = "/v3";
    private static final String GOOGLE_PATH_UPLOAD = "/upload/drive" + GOOGLE_VERSION + "/files";
    private static final String GOOGLE_API = SCHEME_SECURE + GOOGLE_HOST + GOOGLE_PATH_UPLOAD + "/";

    private static final String AUTHORIZATION = "Authorization";
    private static final String BEARER = "Bearer ";
    private static final String CONTENT_TYPE = "Content-Type";
    private static final String CONTENT_TYPE_DEFAULT = "application/json; charset=UTF-8";
    private static final String CONTENT_TYPE_IMG = "image/jpeg";

    private static Retrofit retrofit, rGoogle = null;

    public static Retrofit getRetrofit() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public static Retrofit getRGoogle() {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Interceptor.Chain chain) throws IOException {
                Request original = chain.request();

                Request request = original.newBuilder()
                        .header(AUTHORIZATION, BEARER)
                        .header(CONTENT_TYPE, CONTENT_TYPE_DEFAULT)
                        .method(original.method(), original.body())
                        .build();

                return chain.proceed(request);
            }
        });
        OkHttpClient client = httpClient.build();
        if (rGoogle == null) {
            rGoogle = new Retrofit.Builder()
                    .baseUrl(GOOGLE_API)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();
        }
        return rGoogle;
    }
}