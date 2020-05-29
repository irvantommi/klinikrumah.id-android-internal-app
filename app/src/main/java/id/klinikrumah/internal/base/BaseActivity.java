package id.klinikrumah.internal.base;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.Window;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.jetbrains.annotations.NotNull;

import de.greenrobot.event.EventBus;
import id.klinikrumah.internal.App;
import id.klinikrumah.internal.R;
import id.klinikrumah.internal.constant.S;
import id.klinikrumah.internal.rest.ApiClient;
import id.klinikrumah.internal.rest.ApiInterface;
import id.klinikrumah.internal.util.customview.EmptySubmitSearchView;
import id.klinikrumah.internal.util.customview.StatusBarUtil;
import id.klinikrumah.internal.util.enum_.ErrorType;
import id.klinikrumah.internal.util.static_.CommonFunc;
import retrofit2.Response;

public abstract class BaseActivity extends AppCompatActivity {
    // other class
    protected App app = App.getInstance();
    protected ApiInterface api;
    protected ApiInterface apiGoogle;
    protected Gson gson;
    protected EventBus eventBus;
    // from xml
    protected AppBarLayout ablBase;
    protected ImageButton ibBack;
    protected TextView tvTitleToolbar;
    protected ImageView ivSearch;
    protected EmptySubmitSearchView svBase;
    // member var
    protected ErrorType errorType;
    private FrameLayout flContainer;
    private LinearLayout llError;
    private ImageView ivBaseIcon;
    private TextView tvBaseTitle;
    private TextView tvBaseContent;
    private Button btnBase;
    private ProgressBar pbBase;
    private SearchListener listener;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        api = ApiClient.getRetrofit().create(ApiInterface.class);
        apiGoogle = ApiClient.getRGoogle().create(ApiInterface.class);
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
        ibBack = rlBase.findViewById(R.id.ib_back);
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

        ibBack.setOnClickListener(new View.OnClickListener() {
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
                if (listener != null) {
                    listener.onSearchChange(s);
                }
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

    protected void setStatusBar(int color) {
        StatusBarUtil.setStatusBarColor(this, color);
    }

    public void setLayoutFullScreen() {
        final Window window = getWindow();
        if (isNavigationBarAvailable() || hasSoftKeys()) {
            /*if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
                window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
            } else */
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                if (window != null) {
                    window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

                    window.getDecorView().setOnApplyWindowInsetsListener(new View.OnApplyWindowInsetsListener() {
                        @Override
                        public WindowInsets onApplyWindowInsets(View v, WindowInsets insets) {
                            int navBarHeight = insets.getSystemWindowInsetBottom();
                            if (navBarHeight != 0) {
                                window.getDecorView().setPadding(0, 0, 0, navBarHeight);
                            }
                            return insets;
                        }
                    });
                }
            } else {
                window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_IMMERSIVE |
                            View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_FULLSCREEN);
                }
            }
        } else {
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
                window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            }
            setStatusBar(getResources().getColor(android.R.color.transparent));
        }
    }

    public boolean isNavigationBarAvailable() {
        boolean hasBackKey = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK);
        boolean hasHomeKey = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_HOME);

        return (!(hasBackKey && hasHomeKey));
    }

    private boolean hasSoftKeys() {
        boolean hasSoftwareKeys;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            Display d = this.getWindowManager().getDefaultDisplay();

            DisplayMetrics realDisplayMetrics = new DisplayMetrics();
            d.getRealMetrics(realDisplayMetrics);

            int realHeight = realDisplayMetrics.heightPixels;
            int realWidth = realDisplayMetrics.widthPixels;

            DisplayMetrics displayMetrics = new DisplayMetrics();
            d.getMetrics(displayMetrics);

            int displayHeight = displayMetrics.heightPixels;
            int displayWidth = displayMetrics.widthPixels;

            hasSoftwareKeys = (realWidth - displayWidth) > 0 ||
                    (realHeight - displayHeight) > 0;
        } else {
            boolean hasMenuKey = ViewConfiguration.get(this).hasPermanentMenuKey();
            boolean hasBackKey = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK);
            hasSoftwareKeys = !hasMenuKey && !hasBackKey;
        }
        return hasSoftwareKeys;
    }

    protected void setBackgroundStatusBar(int backgroundColor, boolean isDarkColor) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(this, android.R.color.transparent));
            window.setBackgroundDrawableResource(backgroundColor);
            getWindow().getDecorView().setSystemUiVisibility(isDarkColor ? View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR : 0);
        } else {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
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

    protected JsonObject processResponse(@NotNull Response<JsonObject> response) {
        JsonObject obj = response.body();
        if (obj != null && obj.has(S.RSPNS_STATUS)) {
            if (S.RSPNS_SUCCESS.equals(obj.get(S.RSPNS_STATUS).getAsString())) {
                return obj.has(S.RSPNS_DATA) ? obj.getAsJsonObject(S.RSPNS_DATA) : new JsonObject();
            }
        }
        return null;
    }

    public void showError(@NotNull ErrorType errorType) {
        this.errorType = errorType;
        switch (this.errorType) {
            case GENERAL:
                showError(0, getString(R.string.error_general),
                        getString(R.string.error_general_content), getString(R.string.try_again));
                break;
            case NOT_FOUND:
                showError(0, getString(R.string.data_not_found),
                        getString(R.string.data_not_found_content), getString(R.string.create));
                break;
        }
    }

    protected void showError(int resId, String title, String content, String btnText) {
        flContainer.setVisibility(View.GONE);
        llError.setVisibility(View.VISIBLE);
        if (resId != 0) {
            ivBaseIcon.setImageResource(resId);
        }
        tvBaseTitle.setText(title);
        tvBaseContent.setText(content);
        btnBase.setText(btnText);
    }

    protected void onRetrofitFailure(String errorMsg) {
        Log.e("Retrofit Get", errorMsg);
        errorType = ErrorType.GENERAL;
        showError(errorType);
        showHideProgressBar();
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

    protected void showSnackBar(String message) {
        Snackbar.make(flContainer, message, BaseTransientBottomBar.LENGTH_LONG).show();
    }

    protected interface SearchListener {
        void onSearchSubmit(String s);

        void onSearchChange(String s);

        void onSearchClear();
    }
}