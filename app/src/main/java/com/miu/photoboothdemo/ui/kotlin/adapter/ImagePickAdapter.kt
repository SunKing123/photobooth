package com.miu.photoboothdemo.ui.kotlin.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.miu.photoboothdemo.R
import com.miu.photoboothdemo.model.ImageBean
import com.miu.photoboothdemo.util.DateUtil

class ImagePickAdapter(private val context: Context, private val beanList: MutableList<ImageBean>) : BaseAdapter() {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var view = convertView
        val holder: ViewHolder
        if (convertView == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_view_photo, null)
            holder = ViewHolder()
            holder.mImageView = view.findViewById(R.id.item_image)
            holder.mNameTv = view.findViewById(R.id.item_name)
            holder.mCreateTime = view.findViewById(R.id.item_create_time)
            view.tag = holder
        } else {
            holder = (view?.tag) as ViewHolder
        }
        val myItem = beanList[position]
        var photoName = myItem.name;
        holder.mNameTv.setText(photoName.substring(0, photoName.indexOf(".")))
        Glide.with(context).load(myItem.getPath()).crossFade().into(holder.mImageView)
        holder.mCreateTime.setText(DateUtil.getDateByLongTime(myItem.getCreateTime()))
        return view!!
    }

    override fun getItem(position: Int): Any = beanList[position]
    override fun getItemId(position: Int): Long = position.toLong()
    override fun getCount(): Int = beanList.size

    inner class ViewHolder {
        lateinit var mNameTv: TextView
        lateinit var mCreateTime: TextView
        lateinit var mImageView: ImageView
    }
}