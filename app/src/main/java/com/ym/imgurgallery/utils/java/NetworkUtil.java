package com.ym.imgurgallery.utils.java;

import android.content.Context;
import android.net.ConnectivityManager;

public class NetworkUtil {

    private Context context;

    public NetworkUtil(Context context) {
        this.context = context;
    }

    public boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }
}
