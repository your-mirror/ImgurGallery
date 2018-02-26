package com.ym.imgurgallery.repository;


import com.ym.imgurgallery.api.ImgurResponse;
import com.ym.imgurgallery.api.ImgurService;
import com.ym.imgurgallery.entity.ImgurComment;
import com.ym.imgurgallery.entity.ImgurContent;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ImgurRepository {

    private ImgurService service;

    public ImgurRepository(ImgurService service) {
        this.service = service;
    }

    public Observable<ImgurResponse<ImgurContent>> getGallery(int page) {
        return service.getGallery(page)
            .timeout(5, TimeUnit.SECONDS)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<ImgurResponse<ImgurComment>> getImagesBestComments(String id) {
        return service.getImagesBestComments(id)
            .timeout(5, TimeUnit.SECONDS)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread());
    }

}
