package com.miu.photoboothdemo.ui;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.FileProvider;
import androidx.core.os.EnvironmentCompat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;

import android.os.Environment;
import android.provider.MediaStore;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.miu.photoboothdemo.Constant;
import com.miu.photoboothdemo.R;
import com.miu.photoboothdemo.presenter.PhotoLocalPresenter;
import com.miu.photoboothdemo.util.BitmapProcessor;
import com.miu.photoboothdemo.util.DateUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * create by Ma
 * Home Activity
 * Take a photo & show view photos
 */
public class HomeActivity extends BaseActivity {


    @BindView(R.id.take_photo_btn)
    Button mButtonTakePhoto;

    @BindView(R.id.view_photos_btn)
    Button mButtonViewPhotos;

    @BindView(R.id.my_image)
    ImageView mImageView;

    @BindView(R.id.my_name)
    TextView mName;

    @BindView(R.id.my_name_tv)
    TextView mNameTv;

    @BindView(R.id.my_create_time_tv)
    TextView mCreateTimeTv;

    @BindView(R.id.my_create_time)
    TextView mCreateTime;

    Uri mUri;

    private BitmapProcessor bitmapProcessor;
    private String inputName = null;
    PhotoLocalPresenter presenter;
    private String mPhotoPath;

    @Override
    protected int bindLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void initData() {
        super.initData();

        bitmapProcessor = BitmapProcessor.getInstance();
        presenter = new PhotoLocalPresenter(this);
    }

    @OnClick(R.id.take_photo_btn)
    public void takePhotoClick(View v) {
        //Get camera permission
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!checkPermission()) {
                requestPermissions();
            } else {
                takePhoto();
            }
        } else {
            takePhoto();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case Constant.REQUEST_PERMISSION_CODE:
                boolean allowAllPermission = isAllowAllPermission(grantResults);

                if (allowAllPermission) {
                    takePhoto();
                } else {
                    showMessage("it needs camera&write file permission granted!");
                }

                break;
            case Constant.REQUEST_READ_PERMISSION_CODE:
                boolean allowReadPermission = isAllowAllPermission(grantResults);

                if (allowReadPermission) {
                    photoSelect();
                } else {
                    showMessage("it needs read file permission granted!");
                }
                break;
        }
    }

    private boolean isAllowAllPermission(@NonNull int[] grantResults) {
        boolean allowAllPermission = false;
        for (int i = 0; i < grantResults.length; i++) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                allowAllPermission = false;
                break;
            }
            allowAllPermission = true;
        }
        return allowAllPermission;
    }

    /**
     * Take a photo
     */
    private void takePhoto() {
        Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (captureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (photoFile != null) {
                mPhotoPath = photoFile.getAbsolutePath();
                mUri = FileProvider.getUriForFile(this, getPackageName() + ".fileprovider", photoFile);
                captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mUri);
                captureIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                startActivityForResult(captureIntent, Constant.REQUEST_TAKE_PHOTO_CODE);
            }
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
        String imageFileName = String.format(Constant.TEMP_NAME, timeStamp);
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        if (!storageDir.exists()) {
            storageDir.mkdir();
        }
        File tempFile = new File(storageDir, imageFileName);
        if (!Environment.MEDIA_MOUNTED.equals(EnvironmentCompat.getStorageState(tempFile))) {
            return null;
        }
        return tempFile;
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @OnClick(R.id.view_photos_btn)
    public void viewPhotosClick(View v) {
        if (!checkReadPermission()) {
            requestReadPermission();
        } else {
            photoSelect();
        }
    }

    private void photoSelect() {
        Intent intent = new Intent(this, PickPhotosActivity.class);
        startActivityForResult(intent, Constant.RESULT_VIEW_PHOTOS_CODE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case Constant.REQUEST_TAKE_PHOTO_CODE:
                    //show dialog allow user to change the name
                    showNameDefineDialog(mUri);
                    break;
                case Constant.RESULT_VIEW_PHOTOS_CODE:
                    if (data != null) {
                        String imageName = data.getStringExtra("image_name");
                        String imagePath = data.getStringExtra("image_path");
                        long imageCreateTime = data.getLongExtra("image_create_time", 0);

                        mNameTv.setVisibility(TextUtils.isEmpty(imageName) ? View.GONE : View.VISIBLE);
                        mName.setText(imageName);

                        mCreateTimeTv.setVisibility(imageCreateTime == 0 ? View.GONE : View.VISIBLE);
                        mCreateTime.setText(DateUtil.getDateByLongTime(imageCreateTime));

                        Glide.with(this).load(imagePath).crossFade().into(mImageView);

                    }
                    break;
            }
        }
    }

    /**
     * replace the name
     * input from user
     *
     * @param uri
     */
    private void showNameDefineDialog(Uri uri) {
        new MaterialDialog.Builder(this)
                .title("Photo Setting")
                .inputRangeRes(2, 20, R.color.colorPrimary)
                .inputType(InputType.TYPE_CLASS_TEXT)
                .input("Input Name", null, (dialog, input) -> {
                    inputName = dialog.getInputEditText().toString();

                })
                .positiveText("Sure")
                .onPositive((dialog, which) -> {
                    try {
                        inputName = dialog.getInputEditText().getText().toString();

                        if (TextUtils.isEmpty(inputName)) {
                            showMessage("Input Name can not be empty! ");
                        } else {

                            String photoName = inputName + ".jpg";
                            mName.setText(photoName);
                            mNameTv.setVisibility(TextUtils.isEmpty(inputName) ? View.GONE : View.VISIBLE);

                            String createTime = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss")
                                    .format(new Date());
                            mCreateTime.setText(createTime);
                            mCreateTimeTv.setVisibility(TextUtils.isEmpty(createTime) ? View.GONE : View.VISIBLE);

                            Bitmap bm = bitmapProcessor.getBitmapFormUri(this, uri);
                            mImageView.setImageBitmap(bm);


                            presenter.localizeBitmap(bm, photoName);
                        }


                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                })
                .show();
    }
}
