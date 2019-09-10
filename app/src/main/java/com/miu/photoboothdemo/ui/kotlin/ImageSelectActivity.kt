package com.miu.photoboothdemo.ui.kotlin

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.GridView
import android.widget.ProgressBar
import com.miu.photoboothdemo.model.ImageBean
import com.miu.photoboothdemo.presenter.ViewPhotosPresenter
import com.miu.photoboothdemo.ui.kotlin.adapter.ImagePickAdapter
import com.miu.photoboothdemo.view.PickPhotosView
import org.jetbrains.anko.*


class ImageSelectActivity : ParentActivity(), PickPhotosView {

    var gridView: GridView? = null
    var progressBar: ProgressBar? = null
    var mAdapter: ImagePickAdapter? = null
    var presenter: ViewPhotosPresenter = ViewPhotosPresenter(this, this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        presenter = ViewPhotosPresenter(this, this)

        relativeLayout {
            progressBar = progressBar {
                isIndeterminate = false
                visibility = View.GONE
                max = 100
                progress = 20
                secondaryProgress = 40
                incrementProgressBy(10)
                incrementSecondaryProgressBy(20)
            }.lparams(dip(60), dip(60)) {
                centerInParent()
            }

            gridView = gridView {
                columnWidth = 20
                numColumns = 3
                stretchMode = columnWidth
                verticalSpacing = dip(5)
                horizontalSpacing = dip(10)
                setDrawSelectorOnTop(false)

                setOnItemClickListener { adapterView, view, i, l ->
                    run {
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

            }.lparams() {
                gravity = Gravity.CENTER
            }
        }

        progressBar!!.visibility = View.VISIBLE
        presenter.getImages()
    }


    override fun showPhotoViews(imageBeanList: MutableList<ImageBean>?) {
        progressBar!!.visibility = View.GONE
        if (imageBeanList != null) {
            mAdapter = ImagePickAdapter(this, imageBeanList)
            gridView!!.adapter = mAdapter
        }
    }

    override fun showMessage(msg: String?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
