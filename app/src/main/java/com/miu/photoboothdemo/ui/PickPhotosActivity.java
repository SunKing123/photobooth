package com.miu.photoboothdemo.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.miu.photoboothdemo.R;
import com.miu.photoboothdemo.model.ImageBean;
import com.miu.photoboothdemo.presenter.ViewPhotosPresenter;
import com.miu.photoboothdemo.ui.adapter.PickPhotosAdapter;
import com.miu.photoboothdemo.util.DividerGridItemDecoration;
import com.miu.photoboothdemo.view.PickPhotosView;

import java.util.List;

import butterknife.BindView;

public class PickPhotosActivity extends BaseActivity implements PickPhotosView, PickPhotosAdapter.OnItemClickListener {

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    @BindView(R.id.loading)
    ProgressBar loading;

    private PickPhotosAdapter mAdapter;

    ViewPhotosPresenter presenter;

    @Override
    protected int bindLayout() {
        return R.layout.view_photos;
    }

    @Override
    protected void initData() {
        super.initData();
        presenter = new ViewPhotosPresenter(this, this);

        loading.setVisibility(View.VISIBLE);
        presenter.getImages();
    }


    @Override
    public void showPhotoViews(List<ImageBean> imageBeanList) {
        loading.setVisibility(View.GONE);
        if (imageBeanList != null) {
            mAdapter = new PickPhotosAdapter(this, imageBeanList);
            mRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
            mRecyclerView.addItemDecoration(new DividerGridItemDecoration(this));
            mRecyclerView.setAdapter(mAdapter);
            mAdapter.setOnItemClickListener(this);
        }
    }

    @Override
    public void OnItemClick(View v, ImageBean image, int position) {

        Intent intent = new Intent();
        if (image != null) {
            intent.putExtra("image_name", image.getName());
            intent.putExtra("image_path", image.getPath());
            intent.putExtra("image_create_time", image.getCreateTime());

            setResult(RESULT_OK, intent);
            finish();
        }

    }
}
