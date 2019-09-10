package com.miu.photoboothdemo.ui;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.miu.photoboothdemo.Constant;

import butterknife.ButterKnife;

public abstract class BaseActivity extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(bindLayout());
        ButterKnife.bind(this);

        initData();
        initView(savedInstanceState);
    }

    protected void initData() {
    }

    public void initView(Bundle savedInstanceState) {
    }

    protected abstract int bindLayout();

    public void showMessage(String msg) {
        if (!TextUtils.isEmpty(msg)) {
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        }
    }

    protected boolean checkPermission() {
        boolean haveCameraPermission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;

        boolean haveWritePermission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;

        return haveCameraPermission && haveWritePermission;
    }

    protected boolean checkReadPermission() {
        boolean haveReadPermission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        return haveReadPermission;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    protected void requestPermissions() {
        requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                Constant.REQUEST_PERMISSION_CODE);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    protected void requestReadPermission(){
        requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                Constant.REQUEST_READ_PERMISSION_CODE);
    }
}
