package com.ym.imgurgallery.api;

import com.ym.imgurgallery.entity.ImgurComment;
import com.ym.imgurgallery.entity.ImgurContent;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ImgurService {

    @GET("gallery/hot/viral/{page}")
    Observable<ImgurResponse<ImgurContent>> getGallery(@Path("page") int page);

    @GET("gallery/{id}/comments/best")
    Observable<ImgurResponse<ImgurComment>> getImagesBestComments(@Path("id") String id);
}
