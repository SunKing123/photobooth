package com.miu.photoboothdemo.model;


import com.miu.photoboothdemo.ui.HomeActivity;
import com.miu.photoboothdemo.ui.PickPhotosActivity;

import javax.inject.Singleton;

import dagger.Component;
@Singleton
@Component(modules = MainModule.class)
public interface MainComponent {
    void inject(HomeActivity activity);
    void inject(PickPhotosActivity activity);
}
