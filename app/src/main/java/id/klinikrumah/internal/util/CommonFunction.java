package id.klinikrumah.internal.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Build;
import android.text.TextUtils;

import java.util.regex.Pattern;

public class CommonFunction {
    public static boolean isValidEmail(String email) {
        Pattern emailPattern = Pattern.compile("^(([^<>()\\[\\].,;:\\s@\"]+(\\.[^<>()\\[\\].,;:\\s@\"]+)*)|(\".+\"))@(([^<>()\\[\\].,;:\\s@\"]+\\.)+[^<>()\\[\\].,;:\\s@\"]{2,})$");
        return !TextUtils.isEmpty(email) && emailPattern.matcher(email).matches();
    }

    public static boolean isConnectToInternet(Context context) {
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
}