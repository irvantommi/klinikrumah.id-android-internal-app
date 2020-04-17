package id.klinikrumah.internal.base;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import de.greenrobot.event.EventBus;
import id.klinikrumah.internal.App;
import id.klinikrumah.internal.R;
import id.klinikrumah.internal.rest.ApiClient;
import id.klinikrumah.internal.rest.ApiInterface;

public abstract class BaseActivity extends AppCompatActivity {
    // other class
    protected App app = App.getInstance();
    protected ApiInterface api;
    protected EventBus eventBus;
    // from xml
    private FrameLayout flContainer;
    private LinearLayout llError;
    private ImageView ivBaseIcon;
    private TextView tvBaseTitle;
    private TextView tvBaseContent;
    private Button btnBase;
    private ProgressBar pbBase;
    // member var

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        api = ApiClient.getRetrofit().create(ApiInterface.class);
        eventBus = app.getEventBus();
    }

    @SuppressLint("InflateParams")
    @Override
    public void setContentView(int layoutResID) {
        RelativeLayout rlBase = (RelativeLayout) getLayoutInflater().inflate(R.layout.activity_base,
                null);
        flContainer = rlBase.findViewById(R.id.fl_container);
        llError = rlBase.findViewById(R.id.ll_error);
        ivBaseIcon = rlBase.findViewById(R.id.iv_base_icon);
        tvBaseTitle = rlBase.findViewById(R.id.tv_base_title);
        tvBaseContent = rlBase.findViewById(R.id.tv_base_content);
        btnBase = rlBase.findViewById(R.id.btn_base);
        pbBase = rlBase.findViewById(R.id.pb_base);

        getLayoutInflater().inflate(layoutResID, flContainer, true);
        super.setContentView(layoutResID);
    }

    protected void showHideProgressBar() {
        pbBase.setVisibility(pbBase.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
    }

    protected void setError(String title, String content, String btnText) {
        flContainer.setVisibility(View.GONE);
        llError.setVisibility(View.VISIBLE);
        ivBaseIcon.setVisibility(View.VISIBLE);
        tvBaseTitle.setText(title);
        tvBaseContent.setText(content);
        btnBase.setText(btnText);
    }

    protected void hideError() {
        flContainer.setVisibility(View.VISIBLE);
        llError.setVisibility(View.GONE);
    }
}