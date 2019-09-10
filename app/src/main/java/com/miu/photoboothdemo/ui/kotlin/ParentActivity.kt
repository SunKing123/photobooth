package com.miu.photoboothdemo.ui.kotlin

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.miu.photoboothdemo.Constant

abstract class ParentActivity: AppCompatActivity(){

    protected fun checkPermission(): Boolean {
        val haveCameraPermission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED

        val haveWritePermission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED

        return haveCameraPermission && haveWritePermission
    }

    protected fun checkReadPermission(): Boolean {
        return ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    protected fun requestPermissions() {
        requestPermissions(arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE),
                Constant.REQUEST_PERMISSION_CODE)
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    protected fun requestReadPermission() {
        requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                Constant.REQUEST_READ_PERMISSION_CODE)
    }

}