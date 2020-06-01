package id.klinikrumah.internal.view.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.JsonObject;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import id.klinikrumah.internal.R;
import id.klinikrumah.internal.base.BaseActivity;
import id.klinikrumah.internal.constant.S;
import id.klinikrumah.internal.model.GoogleUserData;
import id.klinikrumah.internal.model.KRUser;
import id.klinikrumah.internal.util.enum_.ErrorType;
import id.klinikrumah.internal.util.static_.CommonFunc;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends BaseActivity implements View.OnClickListener {
    private static final int SIGN_IN_GOOGLE = 69;
    private final String TAG = this.getClass().getSimpleName();
    // from xml
    private TextInputLayout tilEmail;
    private TextInputEditText etEmail;
    private TextInputLayout tilPwd;
    private TextInputEditText etPwd;
    private ImageView ivVisibility;
    private TextView tvForgotPwd;
    private Button btnLogin;
    private Button btnLoginByPhone;
    private Button btnLoginByFb;
    private LoginButton lbFb;
    private Button btnLoginByGoogle;
    private TextView tvTnc;
    private TextView tvRegister;
    // other class
//    private
    // member var
    private CallbackManager fbCallbackManager;
    private GoogleSignInClient googleSignInClient;
    private String email, pwd;
    private boolean isPasswordShown = false;

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
        // findViews
        tilEmail = findViewById(R.id.til_email);
        etEmail = findViewById(R.id.et_email);
        tilPwd = findViewById(R.id.til_password);
        etPwd = findViewById(R.id.et_password);
        ivVisibility = findViewById(R.id.iv_visibility);
        tvForgotPwd = findViewById(R.id.tv_forgot_pwd);
        btnLogin = findViewById(R.id.btn_login);
        btnLoginByPhone = findViewById(R.id.btn_login_by_phone);
        btnLoginByFb = findViewById(R.id.btn_login_by_fb);
        lbFb = findViewById(R.id.lb_fb);
        btnLoginByGoogle = findViewById(R.id.btn_login_by_google);
        tvTnc = findViewById(R.id.tv_tnc);
        tvRegister = findViewById(R.id.tv_register);
        // setOnClickListener
        etEmail.addTextChangedListener(new InputTextWatcher(etEmail));
        etPwd.addTextChangedListener(new InputTextWatcher(etPwd));
        ivVisibility.setOnClickListener(this);
        tvForgotPwd.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
        btnLoginByPhone.setOnClickListener(this);
        btnLoginByFb.setOnClickListener(this);
        btnLoginByGoogle.setOnClickListener(this);
        tvTnc.setOnClickListener(this);
        tvRegister.setOnClickListener(this);
        // init facebook
        fbCallbackManager = CallbackManager.Factory.create();
        lbFb.registerCallback(fbCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                GraphRequest request = GraphRequest.newMeRequest(
                        AccessToken.getCurrentAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                if (object.has(S.REQ_EMAIL)) {
                                    try {
                                        email = object.getString(S.REQ_EMAIL);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    login(S.REQ_FB);
                                } else {
                                    showSnackBar(getString(R.string.error_general_content));
                                }
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,gender,first_name,last_name,picture");
                request.setParameters(parameters);
                request.executeAsync();
//                https://developers.facebook.com/docs/facebook-login/android/?sdk=maven
                /*AccessToken accessToken = AccessToken.getCurrentAccessToken();
                boolean isLoggedIn = accessToken != null && !accessToken.isExpired();
                if (isLoggedIn) {
                    LoginManager.getInstance().retrieveLoginStatus(LoginActivity.this,
                            new LoginStatusCallback() {
                                @Override
                                public void onCompleted(AccessToken accessToken) {
                                    // User was previously logged in, can log them in directly here.
                                    // If this callback is called, a popup notification appears that says
                                    // "Logged in as <User Name>"
                                }
                                @Override
                                public void onFailure() {
                                    // No access token could be retrieved for the user
                                }
                                @Override
                                public void onError(Exception exception) {
                                    // An error occurred
                                }
                            });
                } else {
                    LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this,
                            Collections.singletonList("public_profile,email"));
                }*/
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException e) {
                showSnackBar(e.getMessage());
            }
        });
        // initGoogle
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.server_client_id))
                .requestEmail()
                .build();
        // Build a GoogleSignInClient with the options specified by gso.
        googleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    @Override
    public void onClick(@NotNull View v) {
        switch (v.getId()) {
            case R.id.iv_visibility:
                if (!isPasswordShown) {
                    isPasswordShown = true;
                    ivVisibility.setImageResource(R.drawable.ic_visibility_off_black_24dp);
                    etPwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    isPasswordShown = false;
                    ivVisibility.setImageResource(R.drawable.ic_visibility_black_24dp);
                    etPwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
                break;
            case R.id.tv_forgot_pwd:
                showSnackBar(getString(R.string.coming_soon));
                break;
            case R.id.btn_login:
                if (isValidEmail() && isValidPassword()) {
                    login(S.REQ_EMAIL);
                }
                break;
            case R.id.btn_login_by_phone:
                showSnackBar(getString(R.string.coming_soon));
                break;
            case R.id.btn_login_by_fb:
                showSnackBar(getString(R.string.coming_soon));
//                lbFb.performClick();
                break;
            case R.id.btn_login_by_google:
                startActivityForResult(googleSignInClient.getSignInIntent(), SIGN_IN_GOOGLE);
                break;
            case R.id.tv_tnc:
                showSnackBar(getString(R.string.coming_soon));
                break;
            case R.id.tv_register:
                showSnackBar(getString(R.string.coming_soon));
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == SIGN_IN_GOOGLE) {
            // The Task returned from this call is always completed, no need to attach a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                // Signed in successfully, show authenticated UI.
                if (account != null) {
                    GoogleUserData userData = new GoogleUserData();
                    userData.setGoogleId(account.getId());
                    userData.setGoogleToken(account.getIdToken());
                    userData.setGoogleName(account.getDisplayName());
                    userData.setGivenName(account.getGivenName());
                    userData.setFamilyName(account.getFamilyName());
                    userData.setEmail(account.getEmail());
                    userData.setPhoto(account.getPhotoUrl() != null ? account.getPhotoUrl().toString() : "");
                    app.setAccountGoogle(userData);
//                    https://developers.google.com/identity/sign-in/android/disconnect
                    googleSignInClient.signOut()
                            .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NotNull Task<Void> task) {

                                }
                            });
                    email = account.getEmail();
                    login(S.REQ_GOOGLE);
                    /*GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
                            new NetHttpTransport(), new JacksonFactory())
                            // Specify the CLIENT_ID of the app that accesses the backend:
                            .setAudience(Collections.singletonList(getString(R.string.server_client_id)))
                            // Or, if multiple clients access the backend:
                            //.setAudience(Arrays.asList(CLIENT_ID_1, CLIENT_ID_2, CLIENT_ID_3))
                            .build();
                    if (account.getIdToken() != null) {
                        try {
                            // (Receive idTokenString by HTTPS POST)
                            GoogleIdToken idToken = verifier.verify(account.getIdToken());
                            if (idToken != null) {
                                GoogleIdToken.Payload payload = idToken.getPayload();

                                // Print user identifier
                                String userId = payload.getSubject();
                                System.out.println("User ID: " + userId);

                                // Get profile information from payload
//                              String hostedDomain = payload.getHostedDomain();
//                              boolean emailVerified = payload.getEmailVerified();
//                              String locale = (String) payload.get("locale");

                                GoogleUserData userData = new GoogleUserData();
                                userData.setGoogleId(userId);
                                userData.setGoogleToken(account.getIdToken());
                                userData.setName((String) payload.get("name"));
                                userData.setGivenName((String) payload.get("given_name"));
                                userData.setFamilyName((String) payload.get("family_name"));
                                userData.setEmail(payload.getEmail());
                                userData.setPhoto((String) payload.get("picture"));
                                app.setAccountGoogle(userData);
                                login();
                            } else {
                                System.out.println("Invalid ID token.");
                            }
                        } catch (GeneralSecurityException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (NetworkOnMainThreadException e) {
                            e.printStackTrace();
                        }
                    }*/
                }
            } catch (ApiException e) {
                // The ApiException status code indicates the detailed failure reason.
                // Please refer to the GoogleSignInStatusCodes class reference for more information.
                Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
                showSnackBar(e.getLocalizedMessage());
            }
        } else {
            fbCallbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    private boolean isValidEmail() {
        email = String.valueOf(etEmail.getText());
        if (CommonFunc.isEmptyString(email)) {
            tilEmail.setError(getString(R.string.email_mandatory));
            etEmail.requestFocus();
            return false;
        } else if (!CommonFunc.isValidEmail(email)) {
            tilEmail.setError(getString(R.string.email_invalid));
            etEmail.requestFocus();
            return false;
        } else {
            tilEmail.setErrorEnabled(false);
        }
        return true;
    }

    private boolean isValidPassword() {
        pwd = String.valueOf(etPwd.getText());
        if (CommonFunc.isEmptyString(pwd)) {
            tilPwd.setError(getString(R.string.password_mandatory));
            etPwd.requestFocus();
            return false;
        } else if (pwd.length() < 6) {
            tilPwd.setError(getString(R.string.password_invalid));
            etPwd.requestFocus();
            return false;
        } else {
            tilPwd.setErrorEnabled(false);
        }
        return true;
    }

    private void login(String loginWith) {
        KRUser user = new KRUser();
        user.setLoginWith(loginWith);
        user.setDeviceId(CommonFunc.generateUID());
        user.setEmail(email);
        if (loginWith.equals(S.REQ_EMAIL)) {
            user.setPassword(pwd);
        }
        showHideProgressBar();
        btnLogin.setEnabled(false);
        api.login(user).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NotNull Call<JsonObject> call, @NotNull Response<JsonObject> response) {
                JsonObject data = processResponse(response);
                if (data != null) {
                    if (data.has(S.RSPNS_USER)) {
                        app.setUser(gson.fromJson(data.getAsJsonObject(S.RSPNS_USER).toString(),
                                KRUser.class));
                    }
                    if (data.has(S.RSPNS_AUTH_TOKEN)) {
                        app.setAuthToken(data.get(S.RSPNS_AUTH_TOKEN).toString());
                    }
                    app.setLogin(true);
                    finish();
                    LeadListActivity.show(LoginActivity.this);
                } else {
                    showSnackBar("Email belum terdaftar, silakan hubungi admin");
                    btnLogin.setEnabled(true);
                }
                showHideProgressBar();
            }

            @Override
            public void onFailure(@NotNull Call<JsonObject> call, @NotNull Throwable t) {
                onRetrofitFailure(t.toString());
                btnLogin.setEnabled(true);
            }
        });
    }

    private class InputTextWatcher implements TextWatcher {
        private final View view;

        private InputTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.et_email:
                    tilEmail.setError("");
                    break;

                case R.id.et_password:
                    tilPwd.setError("");
                    break;
            }
        }
    }
}