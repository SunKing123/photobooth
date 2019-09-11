package com.miu.photoboothdemo.model;

import android.content.Context;

import com.miu.photoboothdemo.presenter.PhotoLocalPresenter;
import com.miu.photoboothdemo.presenter.ViewPhotosPresenter;
import com.miu.photoboothdemo.view.PickPhotosView;


import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class MainModule {

    private Context context;
    private PickPhotosView view;

    public MainModule(Context context) {
        this.context = context;
    }

    public MainModule(Context context, PickPhotosView view){
        this.context = context;
        this.view = view;
    }

    @Provides
    public Context providesContext() {
        return context;
    }

    @Provides
    public PickPhotosView providersView(){
        return view;
    }

    @Singleton
    @Provides
    public PhotoLocalPresenter providerPhotoLocalPresenter(Context context) {
        return new PhotoLocalPresenter(context);
    }

    @Singleton
    @Provides
    public ViewPhotosPresenter providerViewsPhotoPresenter(Context context, PickPhotosView view) {
        return new ViewPhotosPresenter(context, view);
    }

}
