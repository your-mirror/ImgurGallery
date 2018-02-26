package com.ym.imgurgallery.ui.list;


import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.ym.imgurgallery.App;
import com.ym.imgurgallery.api.ImgurResponse;
import com.ym.imgurgallery.entity.ImgurContent;
import com.ym.imgurgallery.repository.ImgurRepository;

import javax.inject.Inject;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class ListViewModel extends AndroidViewModel {

    @Inject
    ImgurRepository repository;

    public MutableLiveData<ImgurResponse<ImgurContent>> contentsResponse = new MutableLiveData<>();

    public ListViewModel(@NonNull Application application) {
        super(application);
        App.getComponent().inject(this);
    }

    public void requestGallery(final int page) {
        repository.getGallery(page).subscribe(new Observer<ImgurResponse<ImgurContent>>() {
            @Override
            public void onSubscribe(Disposable d) {}

            @Override
            public void onNext(ImgurResponse<ImgurContent> response) {
                contentsResponse.setValue(response);
            }

            @Override
            public void onError(Throwable e) {
                contentsResponse.setValue(new ImgurResponse<>(e));
            }

            @Override
            public void onComplete() {}
        });
    }
}
