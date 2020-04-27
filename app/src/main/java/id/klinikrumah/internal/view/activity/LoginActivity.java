package id.klinikrumah.internal.view.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.google.android.material.textfield.TextInputEditText;

import org.jetbrains.annotations.NotNull;

import id.klinikrumah.internal.R;
import id.klinikrumah.internal.base.BaseActivity;

public class LoginActivity extends BaseActivity implements View.OnClickListener {
    private static final String TITLE = "Masuk / Daftar";
    // from xml
    private TextInputEditText etEmail;
    private TextInputEditText etPwd;
    private TextView tvForgotPwd;
    private Button btnLogin;
    private Button btnLoginByPhone;
    private Button btnLoginByFb;
    private Button btnLoginByGoogle;
    private TextView tvTnc;
    private TextView tvRegister;
    // other class
//    private
    // member var
//    private

    public static void show(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
        ((Activity) context).overridePendingTransition(0, 0);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ablBase.setVisibility(View.GONE);

        etEmail = findViewById(R.id.et_email);
        etPwd = findViewById(R.id.et_password);
        tvForgotPwd = findViewById(R.id.tv_forgot_pwd);
        btnLogin = findViewById(R.id.btn_login);
        btnLoginByPhone = findViewById(R.id.btn_login_by_phone);
        btnLoginByFb = findViewById(R.id.btn_login_by_fb);
        btnLoginByGoogle = findViewById(R.id.btn_login_by_google);
        tvTnc = findViewById(R.id.tv_tnc);
        tvRegister = findViewById(R.id.tv_register);

        tvForgotPwd.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
        btnLoginByPhone.setOnClickListener(this);
        btnLoginByFb.setOnClickListener(this);
        btnLoginByGoogle.setOnClickListener(this);
        tvTnc.setOnClickListener(this);
        tvRegister.setOnClickListener(this);
    }

    @Override
    public void onClick(@NotNull View v) {
        switch (v.getId()) {
            case R.id.tv_forgot_pwd:

                break;
            case R.id.btn_login:
                LeadListActivity.show(this);
                break;
            case R.id.btn_login_by_phone:

                break;
            case R.id.btn_login_by_fb:

                break;
            case R.id.btn_login_by_google:

                break;
            case R.id.tv_tnc:

                break;
            case R.id.tv_register:

                break;
        }
    }
}