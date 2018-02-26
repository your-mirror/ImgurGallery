package com.ym.imgurgallery.dagger;


import android.content.Context;

import com.ym.imgurgallery.R;
import com.ym.imgurgallery.api.ImgurApi;
import com.ym.imgurgallery.api.ImgurService;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class ServiceModule {

    @Provides
    @Singleton
    public ImgurService provideImgurService(Context context) {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.interceptors().add(chain -> {
            Request request = chain.request().newBuilder()
                .addHeader(
                    "Authorization",
                    "Client-ID " + context.getString(R.string.imgur_client_id))
                .build();
            return chain.proceed(request);
        });

        return new Retrofit.Builder()
            .baseUrl(ImgurApi.API)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(httpClient.build())
            .build().create(ImgurService.class);
    }
}
