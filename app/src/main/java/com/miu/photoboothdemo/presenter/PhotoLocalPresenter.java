package com.miu.photoboothdemo.presenter;

import android.content.Context;
import android.graphics.Bitmap;
import com.miu.photoboothdemo.util.FileUtils;

import java.io.IOException;

import rx.Observable;
import timber.log.Timber;


public class PhotoLocalPresenter extends Presenter {

    Context context;

    public PhotoLocalPresenter(Context context) {
        super(context);
        this.context = context;
    }

    public void localizeBitmap(Bitmap bm, String fileName) {
        Observable<Void> task =
                Observable.create(
                        subscriber -> {

                            try {
                                FileUtils.deleteTempFile();

                                FileUtils.saveImageToLocal(bm, fileName);

                                scanFile(context);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            subscriber.onNext(null);
                        });

        task.subscribeOn(provider.io())
                .observeOn(provider.ui())
                .subscribe(obj -> Timber.i(">>> task completed"), Throwable::printStackTrace);
    }

}
