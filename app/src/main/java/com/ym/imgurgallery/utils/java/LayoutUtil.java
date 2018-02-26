package com.ym.imgurgallery.utils.java;

import android.content.Context;
import android.util.DisplayMetrics;


public class LayoutUtil {

    private Context context;

    public LayoutUtil(Context context) {
        this.context = context;
    }

    public int calculateNoOfColumns() {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        int noOfColumns = (int) (dpWidth / 180);
        return noOfColumns;
    }
}