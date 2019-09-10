package com.miu.photoboothdemo.ui.kotlin

import android.annotation.TargetApi
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.InputType
import android.text.TextUtils
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.content.FileProvider
import androidx.core.os.EnvironmentCompat
import com.afollestad.materialdialogs.MaterialDialog
import com.bumptech.glide.Glide
import com.miu.photoboothdemo.Constant
import com.miu.photoboothdemo.R
import com.miu.photoboothdemo.presenter.PhotoLocalPresenter
import com.miu.photoboothdemo.ui.PickPhotosActivity
import com.miu.photoboothdemo.util.BitmapProcessor
import com.miu.photoboothdemo.util.DateUtil
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk25.coroutines.onClick
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : ParentActivity() {

    var mPhotoPath: String? = null
    lateinit var mUri: Uri
    var inputName: String? = null

    var mNameTv: TextView? = null
    var mCreateTimeTv: TextView? = null

    var mName: TextView? = null
    var mCreateTime: TextView? = null

    var mImageView: ImageView? = null

    var presenter: PhotoLocalPresenter = PhotoLocalPresenter(this)

    @TargetApi(Build.VERSION_CODES.M)
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        verticalLayout {
            button("Take a photo") {
                gravity = Gravity.CENTER
                onClick {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (!checkPermission()) {
                            requestPermissions()
                        } else {
                            takePhoto()
                        }
                    } else {
                        takePhoto()
                    }
                }
            }.lparams(wrapContent, wrapContent) {
                gravity = Gravity.CENTER
                topMargin = dip(40)
                padding = dip(8)
            }

            button("View Photos") {
                gravity = Gravity.CENTER
                onClick {
                    if (!checkReadPermission()) {
                        requestReadPermission()
                    } else {
                        photoSelect()
                    }
                }
            }.lparams(wrapContent, wrapContent) {
                gravity = Gravity.CENTER
                topMargin = dip(10)
                padding = dip(8)
                bottomMargin = dip(10)
            }

            verticalLayout {
                linearLayout {
                    mNameTv = textView("Photo Name: ") {
                        textSize = 14F
                        visibility = View.INVISIBLE

                    }.lparams(wrapContent, wrapContent) {
                        leftMargin = dip(20)
                    }

                    mName = textView {
                        textSize = 14F

                    }.lparams(wrapContent, wrapContent) {
                        leftMargin = dip(20)
                    }
                }

                linearLayout {
                    mCreateTimeTv = textView("Create Time: ") {
                        textSize = 14F
                        visibility = View.INVISIBLE
                    }.lparams(wrapContent, wrapContent) {
                        leftMargin = dip(20)
                    }

                    mCreateTime = textView {
                        textSize = 14F

                    }.lparams(wrapContent, wrapContent) {
                        leftMargin = dip(20)
                    }
                }

                mImageView = imageView {

                }.lparams(wrapContent, wrapContent) {
                    gravity = Gravity.CENTER
                    topMargin = dip(10)
                }
            }

        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            Constant.REQUEST_PERMISSION_CODE -> {
                val allowAllPermission = isAllowAllPermission(grantResults)

                if (allowAllPermission) {
                    takePhoto()
                } else {
                    toast("it needs camera&write file permission granted!")
                }
            }
            Constant.REQUEST_READ_PERMISSION_CODE -> {
                val allowReadPermission = isAllowAllPermission(grantResults)

                if (allowReadPermission) {
                    photoSelect()
                } else {
                    toast("it needs read file permission granted!")
                }
            }
        }
    }


    private fun isAllowAllPermission(grantResults: IntArray): Boolean {
        var allowAllPermission = false
        for (i in grantResults.indices) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                allowAllPermission = false
                break
            }
            allowAllPermission = true
        }
        return allowAllPermission
    }

    fun takePhoto() {
        val captureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (captureIntent.resolveActivity(packageManager) != null) {
            var photoFile: File? = null
            try {
                photoFile = createImageFile()
            } catch (e: IOException) {
                e.printStackTrace()
            }

            if (photoFile != null) {
                mPhotoPath = photoFile.absolutePath
                mUri = FileProvider.getUriForFile(this, "$packageName.fileprovider", photoFile)
                captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mUri)
                captureIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
                startActivityForResult(captureIntent, Constant.REQUEST_TAKE_PHOTO_CODE)
            }
        }
    }

    @Throws(IOException::class)
    private fun createImageFile(): File? {
        val timeStamp = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())
        val imageFileName = String.format(Constant.TEMP_NAME, timeStamp)
        val storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES)
        if (!storageDir.exists()) {
            storageDir.mkdir()
        }
        val tempFile = File(storageDir, imageFileName)
        return if (Environment.MEDIA_MOUNTED != EnvironmentCompat.getStorageState(tempFile)) {
            null
        } else tempFile
    }

    fun photoSelect() {
        val intent = Intent(this, PickPhotosActivity::class.java)
        startActivityForResult(intent, Constant.RESULT_VIEW_PHOTOS_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                Constant.REQUEST_TAKE_PHOTO_CODE ->
                    //show dialog allow user to change the name
                    showNameDefineDialog(mUri)
                Constant.RESULT_VIEW_PHOTOS_CODE -> if (data != null) {
                    val imageName = data.getStringExtra("image_name")
                    val imagePath = data.getStringExtra("image_path")
                    val imageCreateTime = data.getLongExtra("image_create_time", 0)

                    mNameTv!!.visibility = (if (TextUtils.isEmpty(imageName)) View.GONE else View.VISIBLE)
                    mName!!.text = imageName

                    mCreateTimeTv!!.visibility = (if (imageCreateTime == 0L) View.GONE else View.VISIBLE)
                    mCreateTime!!.setText(DateUtil.getDateByLongTime(imageCreateTime))

                    Glide.with(this).load(imagePath).crossFade().into(mImageView)

                }
            }
        }
    }


    fun showNameDefineDialog(uri: Uri) {
        MaterialDialog.Builder(this)
                .title("Photo Setting")
                .inputRangeRes(2, 20, R.color.colorPrimary)
                .inputType(InputType.TYPE_CLASS_TEXT)
                .input("Input Name", null) { dialog, input ->
                    inputName = dialog.inputEditText!!.toString()

                }
                .positiveText("Sure")
                .onPositive { dialog, which ->
                    try {
                        inputName = dialog.inputEditText!!.text.toString()

                        if (TextUtils.isEmpty(inputName)) {
                            toast("Input Name can not be empty! ")
                        } else {

                            val photoName = "$inputName.jpg"
                            mName!!.text = photoName;
                            mNameTv!!.visibility = (if (TextUtils.isEmpty(inputName)) View.GONE else View.VISIBLE)

                            val createTime = SimpleDateFormat("yyyy-MM-dd hh:mm:ss")
                                    .format(Date())
                            mCreateTime!!.text = createTime
                            mCreateTimeTv!!.visibility = (if (TextUtils.isEmpty(createTime)) View.GONE else View.VISIBLE)

                            val bm = BitmapProcessor.getInstance().getBitmapFormUri(this, mUri)
                            mImageView!!.imageBitmap = bm;

                            presenter.localizeBitmap(bm, photoName)
                        }

                    } catch (e: FileNotFoundException) {
                        e.printStackTrace()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
                .show()
    }

}