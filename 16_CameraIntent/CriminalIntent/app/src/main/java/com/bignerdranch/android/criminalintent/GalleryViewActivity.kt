package com.bignerdranch.android.criminalintent

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.media.Image
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.launch
import java.io.File
import java.util.*
import kotlin.collections.ArrayList

class GalleryViewActivity : AppCompatActivity() {

    companion object {
        private val FILES = "VIEW_FILES"
        fun newIntent(fromActivity: Activity, files: List<File>): Intent {
            val intent = Intent(fromActivity, GalleryViewActivity::class.java)
            return intent.putExtra(FILES, files.filter { it.exists() }
                    .map { it.path }.toTypedArray())
        }
    }
    var recyclerView: RecyclerView? = null
    var recyclerAdapter: GalleryAdapter? = null
    var layoutManager: RecyclerView.LayoutManager? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gallery_view)
        recyclerView = findViewById(R.id.activity_gallery_view_root) as? RecyclerView
        recyclerView?.setHasFixedSize(true)
        layoutManager = GridLayoutManager(this, 4)
        recyclerView?.layoutManager = layoutManager
        recyclerAdapter = GalleryAdapter(mutableListOf())
        recyclerView?.adapter = recyclerAdapter
        val files = intent.getStringArrayExtra(FILES)
        launch (CommonPool) {
            files.forEach {
                val b = updatePhotoView(File(it))
                launch(UI) {  recyclerAdapter?.loadPhoto(b) }
            }
        }

    }


    private fun updatePhotoView(mPhotoFile: File) = PictureUtils.getScaledBitmap(mPhotoFile.getPath(), this)



    class GalleryAdapter (val files: MutableList<Bitmap?>): RecyclerView.Adapter<GalleryAdapter.ViewHolder>() {
        override fun getItemCount(): Int = this.files.size

        fun loadPhoto(b: Bitmap) {
            files.add(b)
            notifyDataSetChanged()
        }

        override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
            holder?.v?.setImageBitmap(files[position])
        }

        override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int) : ViewHolder{
            val imageView = LayoutInflater.from(parent?.context).inflate(R.layout.gallery_image, parent, false) as ImageView
            return ViewHolder(imageView)
        }

        class ViewHolder(val v: ImageView): RecyclerView.ViewHolder(v) {

        }


    }
}

