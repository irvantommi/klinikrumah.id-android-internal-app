package id.klinikrumah.internal.view.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;

import id.klinikrumah.internal.R;
import id.klinikrumah.internal.base.BaseActivity;

public class SplashActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        LeadListActivity.show(this);
    }
}