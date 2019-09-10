package com.miu.photoboothdemo;

import android.app.Application;
import android.content.Context;

import androidx.multidex.MultiDex;

public class PhotoBoothApplication extends Application {

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(base);
    }
}
