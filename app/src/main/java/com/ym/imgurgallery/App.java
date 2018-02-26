package com.ym.imgurgallery;

import android.app.Application;

import com.ym.imgurgallery.dagger.AppComponent;
import com.ym.imgurgallery.dagger.AppModule;
import com.ym.imgurgallery.dagger.DaggerAppComponent;

public class App extends Application {

    private static AppComponent component;

    @Override
    public void onCreate() {
        super.onCreate();

        component = buildComponent();
    }

    protected AppComponent buildComponent() {
        return DaggerAppComponent.builder()
            .appModule(new AppModule(this))
            .build();
    }

    public static AppComponent getComponent() {
        return component;
    }
}
