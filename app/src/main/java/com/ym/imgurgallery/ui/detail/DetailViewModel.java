package com.ym.imgurgallery.ui.detail;


import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.ym.imgurgallery.App;
import com.ym.imgurgallery.api.ImgurResponse;
import com.ym.imgurgallery.entity.ImgurComment;
import com.ym.imgurgallery.repository.ImgurRepository;

import javax.inject.Inject;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class DetailViewModel extends AndroidViewModel {
    @Inject
    ImgurRepository repository;

    MutableLiveData<ImgurResponse<ImgurComment>> commentsResponse = new MutableLiveData<>();

    public DetailViewModel(@NonNull Application application) {
        super(application);
        App.getComponent().inject(this);
    }

    public void requestComments(String id) {
        repository.getImagesBestComments(id).subscribe(new Observer<ImgurResponse<ImgurComment>>() {
            @Override
            public void onSubscribe(Disposable d) {}

            @Override
            public void onNext(ImgurResponse<ImgurComment> response) {
                commentsResponse.setValue(response);
            }

            @Override
            public void onError(Throwable e) {
                commentsResponse.setValue(new ImgurResponse<>(e));
            }

            @Override
            public void onComplete() {}
        });
    }
}
