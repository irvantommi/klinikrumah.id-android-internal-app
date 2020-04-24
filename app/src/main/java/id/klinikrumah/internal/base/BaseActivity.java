package id.klinikrumah.internal.base;

import android.annotation.SuppressLint;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import de.greenrobot.event.EventBus;
import id.klinikrumah.internal.App;
import id.klinikrumah.internal.R;
import id.klinikrumah.internal.constant.S;
import id.klinikrumah.internal.rest.ApiClient;
import id.klinikrumah.internal.rest.ApiInterface;
import id.klinikrumah.internal.util.CommonFunc;
import id.klinikrumah.internal.util.ErrorType;
import retrofit2.Response;

public abstract class BaseActivity extends AppCompatActivity {
    // other class
    protected App app = App.getInstance();
    protected ApiInterface api;
    protected Gson gson;
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
        gson = app.getGson();
        eventBus = app.getEventBus();
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//            getSupportActionBar().getNavigationIcon().setColorFilter(getResources().getColor(R.color.colorPrimaryDark), PorterDuff.Mode.SRC_ATOP);
            final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_material);
            upArrow.setColorFilter(getResources().getColor(R.color.colorPrimaryDark), PorterDuff.Mode.SRC_ATOP);
            getSupportActionBar().setHomeAsUpIndicator(upArrow);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        finish();
        return true;
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
        super.setContentView(rlBase);
    }

    protected void showHideProgressBar() {
        pbBase.setVisibility(pbBase.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
    }

    protected JsonObject processResponse(@NotNull Response<List<JsonObject>> response) {
        JsonObject obj = response.body().get(0);
        if (obj.has(S.RSPNS_STATUS)) {
            if (S.RSPNS_SUCCESS.equals(obj.get(S.RSPNS_STATUS).getAsString())) {
                return obj.has(S.RSPNS_DATA) ? obj.getAsJsonObject(S.RSPNS_DATA) : new JsonObject();
            }
        }
        return null;
    }

    protected void setError(@NotNull ErrorType type) {
        switch (type) {
            case GENERAL:
                setError(0, getString(R.string.error_general),
                        getString(R.string.error_general_content), getString(R.string.try_again));
                break;
            case NOT_FOUND:
                setError(0, getString(R.string.data_not_found),
                        getString(R.string.data_not_found_content), getString(R.string.create_new));
                break;
        }
    }

    protected void setError(int resId, String title, String content, String btnText) {
        flContainer.setVisibility(View.GONE);
        llError.setVisibility(View.VISIBLE);
        if (resId != 0) {
            ivBaseIcon.setBackground(getResources().getDrawable(resId));
        }
        tvBaseTitle.setText(title);
        tvBaseContent.setText(content);
        btnBase.setText(btnText);
    }

    protected void setBtnBaseClickListener(View.OnClickListener listener) {
        btnBase.setOnClickListener(listener);
    }

    protected void setContentVisibility(int i) {
        flContainer.setVisibility(i);
    }

    protected void hideError() {
        flContainer.setVisibility(View.VISIBLE);
        llError.setVisibility(View.GONE);
    }

    protected String setDefaultIfEmpty(String input) {
        return CommonFunc.setDefaultIfEmpty(input);
    }

    protected String setString(Editable input) {
        return CommonFunc.setStringFromEditable(input);
    }

    protected String setString(CharSequence input) {
        return CommonFunc.setStringFromCharSequence(input);
    }
}