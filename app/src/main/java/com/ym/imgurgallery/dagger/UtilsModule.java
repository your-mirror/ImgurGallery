package com.ym.imgurgallery.dagger;

import android.content.Context;

import com.ym.imgurgallery.utils.java.LayoutUtil;
import com.ym.imgurgallery.utils.java.NetworkUtil;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class UtilsModule {

    @Provides
    @Singleton
    public LayoutUtil provideLayoutUtil(Context context) {
        return new LayoutUtil(context);
    }

    @Provides
    @Singleton
    public NetworkUtil provideNetworkUtil(Context context) {
        return new NetworkUtil(context);
    }
}
