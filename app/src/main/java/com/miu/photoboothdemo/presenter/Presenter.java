package com.miu.photoboothdemo.presenter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.webkit.MimeTypeMap;

import com.miu.photoboothdemo.util.MediaScanner;
import com.miu.photoboothdemo.util.schedulers.RxSchedulerProvider;
import com.miu.photoboothdemo.view.View;

import java.io.File;


public abstract class Presenter<V extends View> {

    protected V view;
    protected Context context;
    public RxSchedulerProvider provider;


    public Presenter(Context context) {
        this.context = context;
        provider = RxSchedulerProvider.getInstance();
    }

    public Presenter(Context context, V view) {
        this.view = view;
        this.context = context;
        provider = RxSchedulerProvider.getInstance();
    }

    public V getView() {
        return view;
    }

    public void scanFile(Context context) {
        String path = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES).getAbsolutePath();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            File[] filePath = new File(path).listFiles();
            MediaScanner mediaScanner = new MediaScanner(context);
            for (File f : filePath) {
                String[] filePaths = new String[]{f.getAbsolutePath()};
                String[] mimeTypes = new String[]{MimeTypeMap.getSingleton().getMimeTypeFromExtension("jpg")};
                mediaScanner.scanFiles(filePaths, mimeTypes);
            }

        } else {
            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse(path)));
        }
    }
}
