/**********************************************************************
 * AUTHOR：YOLANDA
 * DATE：2015年4月8日上午9:03:54
 * Copyright © 56iq. All Rights Reserved
 *======================================================================
 * EDIT HISTORY
 *----------------------------------------------------------------------
 * |  DATE      | NAME       | REASON       | CHANGE REQ.
 *----------------------------------------------------------------------
 * | 2015年4月8日    | YOLANDA    | Created      |
 *
 * DESCRIPTION：create the File, and add the content.
 *
 ***********************************************************************/
package com.miu.photoboothdemo.util;

import android.content.Context;
import android.media.MediaScannerConnection;
import android.net.Uri;


public class MediaScanner implements MediaScannerConnection.MediaScannerConnectionClient {


    private MediaScannerConnection mediaScanConn = null;

    public MediaScanner(Context context) {
        mediaScanConn = new MediaScannerConnection(context, this);
    }

    private String[] filePaths;
    private String[] mimeTypes;


    public void scanFiles(String[] filePaths, String[] mimeTypes) {
        this.filePaths = filePaths;
        this.mimeTypes = mimeTypes;
        mediaScanConn.connect();
    }


    @Override
    public void onMediaScannerConnected() {
        for (int i = 0; i < filePaths.length; i++) {
            mediaScanConn.scanFile(filePaths[i], mimeTypes[i]);
        }
        filePaths = null;
        mimeTypes = null;
    }

    private int scanTimes = 0;

    @Override
    public void onScanCompleted(String path, Uri uri) {
        scanTimes++;
        if (scanTimes == filePaths.length) {
            mediaScanConn.disconnect();
            scanTimes = 0;
        }
    }
}
