package id.klinikrumah.internal.view.activity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import androidx.annotation.Nullable;

import com.google.android.gms.auth.api.signin.GoogleSignIn;

import id.klinikrumah.internal.R;
import id.klinikrumah.internal.base.BaseActivity;

public class SplashActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ablBase.setVisibility(View.GONE);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Check for existing Google Sign In account, if the user is already signed in,
                // the GoogleSignInAccount will be non-null.
//                app.setLogin(GoogleSignIn.getLastSignedInAccount(SplashActivity.this) != null);
                if (app.isLogin()) {
                    LeadListActivity.show(SplashActivity.this);
                } else {
                    LoginActivity.show(SplashActivity.this);
                }
            }
        }, 2000);
    }
}