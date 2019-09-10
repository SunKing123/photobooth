package com.miu.photoboothdemo.util.schedulers;

import androidx.annotation.NonNull;

import rx.Scheduler;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/** Created by seanzhou on 2/4/17. */
public final class RxSchedulerProvider implements SchedulerProvider {
    private static RxSchedulerProvider provider;

    private RxSchedulerProvider() {}

    public static synchronized RxSchedulerProvider getInstance() {
        if (provider == null) {
            provider = new RxSchedulerProvider();
        }
        return provider;
    }

    @NonNull
    @Override
    public Scheduler computation() {
        return Schedulers.computation();
    }

    @NonNull
    @Override
    public Scheduler io() {
        return Schedulers.io();
    }

    @NonNull
    @Override
    public Scheduler ui() {
        return AndroidSchedulers.mainThread();
    }
}
