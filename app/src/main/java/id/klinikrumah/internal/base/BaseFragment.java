package id.klinikrumah.internal.base;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import de.greenrobot.event.EventBus;
import id.klinikrumah.internal.App;

public abstract class BaseFragment extends Fragment {
    protected App app = App.getInstance();
    protected EventBus eventBus;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        eventBus = app.getEventBus();
    }
}