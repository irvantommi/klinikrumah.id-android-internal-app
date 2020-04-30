package id.klinikrumah.internal.util.static_;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.text.Editable;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;
import java.util.regex.Pattern;

import id.klinikrumah.internal.constant.S;

public class CommonFunc {
    @NotNull
    public static String generateUID() {
        return UUID.randomUUID().toString().replace(S.DASH, "");
    }

    public static boolean isValidEmail(String email) {
        Pattern emailPattern = Pattern.compile("^(([^<>()\\[\\].,;:\\s@\"]+(\\.[^<>()\\[\\].,;:\\s@\"]+)*)|(\".+\"))@(([^<>()\\[\\].,;:\\s@\"]+\\.)+[^<>()\\[\\].,;:\\s@\"]{2,})$");
        return !TextUtils.isEmpty(email) && emailPattern.matcher(email).matches();
    }

    public static boolean isConnectToInternet(@NotNull Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Network[] networks = new Network[0];
            if (connectivityManager != null) {
                networks = connectivityManager.getAllNetworks();
            }
            NetworkInfo networkInfo;
            for (Network mNetwork : networks) {
                networkInfo = connectivityManager.getNetworkInfo(mNetwork);
                if (networkInfo != null && networkInfo.getState().equals(NetworkInfo.State.CONNECTED)) {
                    return true;
                }
            }
        } else if (connectivityManager != null) {
            //noinspection deprecation
            NetworkInfo[] info = connectivityManager.getAllNetworkInfo();
            for (NetworkInfo anInfo : info) {
                if (anInfo.getState() == NetworkInfo.State.CONNECTED) {
                    return true;
                }
            }
        }
        return false;
    }

    public static void callPhone(@NotNull Context context, String phoneNo) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + phoneNo));
        context.startActivity(intent);
    }

    public static void openUrl(@NotNull Context context, String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        context.startActivity(intent);
    }

    @NotNull
    public static Boolean isEmptyString(String input) {
        return input == null || input.trim().isEmpty();
    }

    public static String setDefaultIfEmpty(String input) {
        return TextUtils.isEmpty(input) ? S.DASH : input;
    }

    @NotNull
    public static String setStringFromEditable(Editable input) {
        return input != null ? input.toString().trim() : "";
    }

    @NotNull
    public static String setStringFromCharSequence(CharSequence input) {
        return input != null ? input.toString().trim() : "";
    }

    public static boolean isGranted(Context context, String permission) {
        return ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
    }

    public static void copyStream(@NonNull InputStream input, OutputStream output) throws IOException {
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = input.read(buffer)) != -1) {
            output.write(buffer, 0, bytesRead);
        }
    }

    public static void showDialog(Activity activity, String msg, String negativeBtnTxt,
                                  DialogInterface.OnClickListener onClick) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(activity);
        dialogBuilder.setMessage(msg)
                .setCancelable(false)
                .setNegativeButton(negativeBtnTxt, onClick);
        dialogBuilder.create().show();
    }
}