package com.miu.photoboothdemo.presenter;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;

import com.miu.photoboothdemo.model.ImageBean;
import com.miu.photoboothdemo.view.PickPhotosView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import timber.log.Timber;

public class ViewPhotosPresenter extends Presenter<PickPhotosView> {

    Context context;
    List<ImageBean> imageBeanList = new ArrayList<>();


    public ViewPhotosPresenter(Context context, PickPhotosView view) {
        super(context, view);
        this.context = context;
    }

    public void getImages() {
        imageBeanList.clear();
        Observable<Void> task =
                Observable.create(
                        subscriber -> {
                            Uri mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                            ContentResolver mContentResolver = context.getContentResolver();

                            Cursor mCursor = mContentResolver.query(mImageUri, new String[]{
                                            MediaStore.Images.Media.DATA,
                                            MediaStore.Images.Media.DISPLAY_NAME,
                                            MediaStore.Images.Media.DATE_ADDED,
                                            MediaStore.Images.Media._ID,
                                            MediaStore.Images.Media.MIME_TYPE},
                                    null,
                                    null,
                                    MediaStore.Images.Media.DATE_ADDED);

                            if (mCursor != null) {

                                if (mCursor.getCount() > 0) {
                                    mCursor.moveToFirst();
                                    do {
                                        String path = mCursor.getString(
                                                mCursor.getColumnIndex(MediaStore.Images.Media.DATA));
                                        String name = mCursor.getString(
                                                mCursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME));
                                        long time = mCursor.getLong(
                                                mCursor.getColumnIndex(MediaStore.Images.Media.DATE_ADDED));
                                        if (!"downloading".equals(getExtensionName(path)) && checkImgExists(path)) {
                                            imageBeanList.add(new ImageBean(name, path, time));
                                        }
                                    } while (mCursor.moveToNext());
                                }
                                mCursor.close();

                            }
                            subscriber.onNext(null);
                        });

        task.subscribeOn(provider.io())
                .observeOn(provider.ui())
                .subscribe(obj -> {
                    handler.sendEmptyMessage(0);
                }, Throwable::printStackTrace);
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
                if (getView() != null && imageBeanList.size() > 0) {
                    getView().showPhotoViews(imageBeanList);
                } else {
                    getView().showPhotoViews(null);
                }
            }
        }
    };

    private static boolean checkImgExists(String filePath) {
        return new File(filePath).exists();
    }

    public static String getExtensionName(String filename) {
        if (filename != null && filename.length() > 0) {
            int dot = filename.lastIndexOf('.');
            if (dot > -1 && dot < filename.length() - 1) {
                return filename.substring(dot + 1);
            }
        }
        return "";
    }
}
