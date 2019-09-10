package com.miu.photoboothdemo.ui.kotlin

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.GridView
import android.widget.ProgressBar
import com.miu.photoboothdemo.R
import com.miu.photoboothdemo.model.ImageBean
import com.miu.photoboothdemo.presenter.ViewPhotosPresenter
import com.miu.photoboothdemo.ui.kotlin.adapter.ImagePickAdapter
import com.miu.photoboothdemo.view.PickPhotosView


class ImageSelectActivity : ParentActivity(), PickPhotosView, AdapterView.OnItemClickListener {

    var gridView: GridView? = null
    var loading: ProgressBar? = null
    var mAdapter: ImagePickAdapter? = null
    var presenter: ViewPhotosPresenter = ViewPhotosPresenter(this, this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.view_select)
        gridView = findViewById(R.id.grid_view)
        loading = findViewById(R.id.loading)

        presenter = ViewPhotosPresenter(this, this)
        loading!!.visibility = View.VISIBLE

        presenter.getImages()

    }


    override fun showPhotoViews(imageBeanList: MutableList<ImageBean>?) {
        loading!!.visibility = View.GONE
        if (imageBeanList != null) {
            mAdapter = ImagePickAdapter(this, imageBeanList)

            gridView!!.adapter = mAdapter
        }
    }

    override fun showMessage(msg: String?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onItemClick(adapterView: AdapterView<*>, view: View, i: Int, l: Long) {
        val intent = Intent()
        val image = mAdapter!!.getItem(i) as ImageBean
        if (image != null) {
            intent.putExtra("image_name", image.name)
            intent.putExtra("image_path", image.path)
            intent.putExtra("image_create_time", image.createTime)

            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }
}