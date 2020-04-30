package id.klinikrumah.internal.view.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;

import org.jetbrains.annotations.NotNull;

import id.klinikrumah.internal.R;
import id.klinikrumah.internal.base.BaseActivity;
import id.klinikrumah.internal.model.GoogleUserData;

public class LoginActivity extends BaseActivity implements View.OnClickListener {
    private static final int SIGN_IN_GOOGLE = 69;
    private final String TAG = this.getClass().getSimpleName();
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
    private GoogleSignInClient googleSignInClient;

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
        etEmail = findViewById(R.id.et_email);
        etPwd = findViewById(R.id.et_password);
        tvForgotPwd = findViewById(R.id.tv_forgot_pwd);
        btnLogin = findViewById(R.id.btn_login);
        btnLoginByPhone = findViewById(R.id.btn_login_by_phone);
        btnLoginByFb = findViewById(R.id.btn_login_by_fb);
        btnLoginByGoogle = findViewById(R.id.btn_login_by_google);
        tvTnc = findViewById(R.id.tv_tnc);
        tvRegister = findViewById(R.id.tv_register);
        // setOnClickListener
        tvForgotPwd.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
        btnLoginByPhone.setOnClickListener(this);
        btnLoginByFb.setOnClickListener(this);
        btnLoginByGoogle.setOnClickListener(this);
        tvTnc.setOnClickListener(this);
        tvRegister.setOnClickListener(this);
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
            case R.id.tv_forgot_pwd:

                break;
            case R.id.btn_login:
                login();
                break;
            case R.id.btn_login_by_phone:

                break;
            case R.id.btn_login_by_fb:

                break;
            case R.id.btn_login_by_google:
                startActivityForResult(googleSignInClient.getSignInIntent(), SIGN_IN_GOOGLE);
                break;
            case R.id.tv_tnc:

                break;
            case R.id.tv_register:

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
//                    googleSignInClient.signOut()
//                            .addOnCompleteListener(this, new OnCompleteListener<Void>() {
//                                @Override
//                                public void onComplete(@NotNull Task<Void> task) {
//
//                                }
//                            });
                    login();
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
        }
    }

    private void login() {
        LeadListActivity.show(this);
    }
}