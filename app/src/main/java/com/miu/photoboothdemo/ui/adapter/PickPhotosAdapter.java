package com.miu.photoboothdemo.ui.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.miu.photoboothdemo.R;
import com.miu.photoboothdemo.model.ImageBean;
import com.miu.photoboothdemo.util.DateUtil;

import java.util.List;

public class PickPhotosAdapter extends RecyclerView.Adapter {


    private Context mContext;
    private List<ImageBean> imageBeanList;
    private OnItemClickListener mItemClickListener;

    public PickPhotosAdapter(Context context, List<ImageBean> imageBeanList) {
        this.mContext = context;
        this.imageBeanList = imageBeanList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_view_photo, parent, false);
        return new DemoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ImageBean entity = imageBeanList.get(position);
        if (entity != null) {
            String photoName = entity.getName();
            ((DemoViewHolder) holder).mText.setText(photoName.substring(0, photoName.indexOf(".")));
            Bitmap bm = BitmapFactory.decodeFile(entity.getPath());
            ((DemoViewHolder) holder).mImageView.setImageBitmap(bm);
            ((DemoViewHolder) holder).mCreateTime.setText(DateUtil.getDateByLongTime(entity.getCreateTime()));

            ((DemoViewHolder) holder).mImageView.setOnClickListener(view -> {
                if (mItemClickListener != null) {
                    mItemClickListener.OnItemClick(view, entity, position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return imageBeanList.size();
    }

    private class DemoViewHolder extends RecyclerView.ViewHolder {

        private TextView mText;
        private TextView mCreateTime;
        private ImageView mImageView;

        public DemoViewHolder(View itemView) {
            super(itemView);
            mText = itemView.findViewById(R.id.item_name);
            mImageView = itemView.findViewById(R.id.item_image);
            mCreateTime = itemView.findViewById(R.id.item_create_time);
        }
    }


    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mItemClickListener = listener;
    }

    public interface OnItemClickListener {
        void OnItemClick(View v, ImageBean image, int position);
    }
}
