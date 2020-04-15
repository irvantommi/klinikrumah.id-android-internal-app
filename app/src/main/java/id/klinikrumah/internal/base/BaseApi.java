package id.klinikrumah.internal.base;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

class BaseApi {
    private static final String BASE_URL = "http://192.168.43.201/rest_ci/index.php/";
    private static Retrofit retrofit = null;

    static Retrofit getRetrofit() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}