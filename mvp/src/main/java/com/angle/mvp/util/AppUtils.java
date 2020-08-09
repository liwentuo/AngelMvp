package com.angle.mvp.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;

public class AppUtils {


    public static boolean isValidUserCount(String count) {
        if (!TextUtils.isEmpty(count)) {
            if (count.length() == 11) {
                return true;
            }
        }
        return false;
    }

    public static boolean isValidUserPasssword(String password) {
        if (!TextUtils.isEmpty(password)) {
            if (password.length() >= 6) {
                return true;
            }
        }
        return false;
    }


    public static boolean isOnInternet(Context context) {
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivityManager != null) {
                NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
                if (networkInfo != null && networkInfo.isConnected()) {
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
