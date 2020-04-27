package id.klinikrumah.internal.base;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
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

import com.google.android.material.appbar.AppBarLayout;
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
import id.klinikrumah.internal.util.EmptySubmitSearchView;
import id.klinikrumah.internal.util.ErrorType;
import retrofit2.Response;

public abstract class BaseActivity extends AppCompatActivity {
    // other class
    protected App app = App.getInstance();
    protected ApiInterface api;
    protected Gson gson;
    protected EventBus eventBus;
    // from xml
    protected AppBarLayout ablBase;
    protected ImageView ivBack;
    protected TextView tvTitleToolbar;
    protected ImageView ivSearch;
    protected EmptySubmitSearchView svBase;
    private FrameLayout flContainer;
    private LinearLayout llError;
    private ImageView ivBaseIcon;
    private TextView tvBaseTitle;
    private TextView tvBaseContent;
    private Button btnBase;
    private ProgressBar pbBase;
    // member var
    private SearchListener listener;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        api = ApiClient.getRetrofit().create(ApiInterface.class);
        gson = app.getGson();
        eventBus = app.getEventBus();
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
            /*getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//            getSupportActionBar().getNavigationIcon().setColorFilter(getResources().getColor(R.color.colorPrimaryDark), PorterDuff.Mode.SRC_ATOP);
            final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_material);
            upArrow.setColorFilter(getResources().getColor(R.color.colorPrimaryDark), PorterDuff.Mode.SRC_ATOP);
            getSupportActionBar().setHomeAsUpIndicator(upArrow);*/
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
        ablBase = rlBase.findViewById(R.id.abl_base);
        ivBack = rlBase.findViewById(R.id.iv_back);
        tvTitleToolbar = rlBase.findViewById(R.id.tv_title_toolbar);
        ivSearch = rlBase.findViewById(R.id.iv_search);
        svBase = rlBase.findViewById(R.id.sv_base);
        flContainer = rlBase.findViewById(R.id.fl_container);
        llError = rlBase.findViewById(R.id.ll_error);
        ivBaseIcon = rlBase.findViewById(R.id.iv_base_icon);
        tvBaseTitle = rlBase.findViewById(R.id.tv_base_title);
        tvBaseContent = rlBase.findViewById(R.id.tv_base_content);
        btnBase = rlBase.findViewById(R.id.btn_base);
        pbBase = rlBase.findViewById(R.id.pb_base);

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        ivSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setSearchIconified(false);
            }
        });
        svBase.setOnQueryTextListener(new EmptySubmitSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                listener.onSearchSubmit(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                listener.onSearchChange(s);
                return false;
            }
        });
        svBase.findViewById(R.id.search_close_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                svBase.setQuery("", false);
                listener.onSearchClear();
                hideError();
            }
        });

        getLayoutInflater().inflate(layoutResID, flContainer, true);
        super.setContentView(rlBase);
    }

    protected void setSearchListener(SearchListener listener) {
        this.listener = listener;
    }

    protected void setSearchIconified(boolean isIconified) {
        if (isIconified) {
            svBase.setVisibility(View.GONE);
            ivSearch.setVisibility(View.VISIBLE);
        } else {
            ivSearch.setVisibility(View.GONE);
            svBase.setVisibility(View.VISIBLE);
            svBase.requestFocus();
            keyboardShow(svBase);
        }
    }

    protected void showHideProgressBar() {
        pbBase.setVisibility(pbBase.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
    }

    protected void keyboardShow(@NotNull View v) {
        if (v.requestFocus()) {
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
            }
        }
    }

    protected void keyboardHide() {
        if (getCurrentFocus() != null) {
            InputMethodManager iMM = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            if (iMM != null) {
                iMM.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            }
//             this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        }
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

    public void setError(@NotNull ErrorType type) {
        switch (type) {
            case GENERAL:
                setError(0, getString(R.string.error_general),
                        getString(R.string.error_general_content), getString(R.string.try_again));
                break;
            case NOT_FOUND:
                setError(0, getString(R.string.data_not_found),
                        getString(R.string.data_not_found_content), getString(R.string.create));
                break;
        }
    }

    protected void setError(int resId, String title, String content, String btnText) {
        flContainer.setVisibility(View.GONE);
        llError.setVisibility(View.VISIBLE);
        if (resId != 0) {
            ivBaseIcon.setImageResource(resId);
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

    public void hideError() {
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

    protected interface SearchListener {
        void onSearchSubmit(String s);

        void onSearchChange(String s);

        void onSearchClear();
    }
}