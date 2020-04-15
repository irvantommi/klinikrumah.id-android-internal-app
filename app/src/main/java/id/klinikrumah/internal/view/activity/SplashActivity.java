package id.klinikrumah.internal.view.activity;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.List;

import id.klinikrumah.internal.base.BaseActivity;
import id.klinikrumah.internal.controller.SplashController;
import id.klinikrumah.internal.model.Lead;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashActivity extends BaseActivity {
    SplashController controller;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        controller = retrofit.create(SplashController.class);
        Call<List<Lead>> leadListCall = controller.getLeadList();
        leadListCall.enqueue(new Callback<List<Lead>>() {
            @Override
            public void onResponse(Call<List<Lead>> call, Response<List<Lead>> response) {
                response.body();
            }

            @Override
            public void onFailure(Call<List<Lead>> call, Throwable t) {
                Log.e("Retrofit Get", t.toString());
            }
        });
    }
}