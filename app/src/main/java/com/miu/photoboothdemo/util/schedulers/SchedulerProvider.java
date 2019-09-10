package com.miu.photoboothdemo.util.schedulers;

import androidx.annotation.NonNull;

import rx.Scheduler;

/** Allow providing different types of {@link Scheduler}s. */
public interface SchedulerProvider {
    @NonNull
    Scheduler computation();

    @NonNull
    Scheduler io();

    @NonNull
    Scheduler ui();
}
