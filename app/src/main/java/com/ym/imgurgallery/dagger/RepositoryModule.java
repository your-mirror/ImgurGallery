package com.ym.imgurgallery.dagger;

import com.ym.imgurgallery.api.ImgurService;
import com.ym.imgurgallery.repository.ImgurRepository;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class RepositoryModule {

    @Provides
    @Singleton
    public ImgurRepository provideImgurRepository(ImgurService service) {
        return new ImgurRepository(service);
    }
}
