package id.klinikrumah.internal.base;

import android.os.Bundle;
import android.os.PersistableBundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import de.greenrobot.event.EventBus;
import id.klinikrumah.internal.App;
import id.klinikrumah.internal.rest.ApiClient;
import id.klinikrumah.internal.rest.ApiInterface;

public abstract class BaseActivity extends AppCompatActivity {
    protected App app = App.getInstance();
    protected ApiInterface api;
    protected EventBus eventBus;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        api = ApiClient.getRetrofit().create(ApiInterface.class);
        eventBus = app.getEventBus();
    }
}