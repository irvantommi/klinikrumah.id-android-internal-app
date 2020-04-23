package id.klinikrumah.internal.base;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.gson.Gson;

import de.greenrobot.event.EventBus;
import id.klinikrumah.internal.App;
import id.klinikrumah.internal.rest.ApiClient;
import id.klinikrumah.internal.rest.ApiInterface;

public abstract class BaseFragment extends Fragment {
    protected App app = App.getInstance();
    protected ApiInterface api;
    protected Gson gson;
    protected EventBus eventBus;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        api = ApiClient.getRetrofit().create(ApiInterface.class);
        gson = app.getGson();
        eventBus = app.getEventBus();
    }
}