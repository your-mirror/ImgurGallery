package com.ym.imgurgallery.dagger;


import com.ym.imgurgallery.ui.detail.DetailViewModel;
import com.ym.imgurgallery.ui.list.ListFragment;
import com.ym.imgurgallery.ui.list.ListViewModel;
import com.ym.imgurgallery.ui.main.MainActivity;

import javax.inject.Singleton;

import dagger.Component;

@Component(modules = {AppModule.class, UtilsModule.class, ServiceModule.class, RepositoryModule.class})
@Singleton
public interface AppComponent {

    void inject(MainActivity activity);
    void inject(ListFragment fragment);
    void inject(ListViewModel listViewModel);
    void inject(DetailViewModel listViewModel);
}
