package com.miu.photoboothdemo.view;

import com.miu.photoboothdemo.model.ImageBean;

import java.util.List;

public interface PickPhotosView extends View {


     void showPhotoViews(List<ImageBean> imageBeanList);
}
