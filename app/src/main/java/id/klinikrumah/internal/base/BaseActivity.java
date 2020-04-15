package id.klinikrumah.internal.base;

import android.os.Bundle;
import android.os.PersistableBundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import de.greenrobot.event.EventBus;
import id.klinikrumah.internal.App;
import retrofit2.Retrofit;

public abstract class BaseActivity extends AppCompatActivity {
    protected App app = App.getInstance();
    protected Retrofit retrofit;
    protected EventBus eventBus;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        retrofit = BaseApi.getRetrofit();
        eventBus = app.getEventBus();
    }
}