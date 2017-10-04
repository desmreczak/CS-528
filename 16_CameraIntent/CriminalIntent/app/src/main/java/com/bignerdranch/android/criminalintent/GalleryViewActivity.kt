package com.bignerdranch.android.criminalintent

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.media.Image
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.google.android.gms.vision.Frame
import com.google.android.gms.vision.face.Face
import com.google.android.gms.vision.face.FaceDetector
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.launch
import java.io.File
import java.util.*
import kotlin.collections.ArrayList
import com.google.android.gms.vision.face.FaceDetector.ALL_LANDMARKS



class GalleryViewActivity : AppCompatActivity() {

    companion object {
        private val FILES = "VIEW_FILES"
        private val IS_FD = "IS_FD"
        fun newIntent(fromActivity: Activity, files: List<File>, isFD: Boolean): Intent {
            val intent = Intent(fromActivity, GalleryViewActivity::class.java)
            return intent.putExtra(FILES, files.filter { it.exists() }
                    .map { it.path }.toTypedArray())
                    .putExtra(IS_FD, isFD)
        }

    }

    val bitmapArray = ArrayList<Bitmap>();
    var recyclerView: RecyclerView? = null
    var recyclerAdapter: GalleryAdapter? = null
    var layoutManager: RecyclerView.LayoutManager? = null
    var detector : FaceDetector? = null

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
        val shouldDetect = intent.getBooleanExtra(IS_FD, false)
        val detector = FaceDetector.Builder(this@GalleryViewActivity)
                .build()
        this@GalleryViewActivity.detector = detector

        launch (CommonPool) {
            files.forEach {
                val b = updatePhotoView(File(it))
                bitmapArray.add(b)
                val face = if (detector.isOperational && shouldDetect) detector.detect(Frame.Builder().setBitmap(b).build()) else SparseArray()
                launch(UI) {  recyclerAdapter?.loadPhoto(b, face) }
            }


        }

    }

    override fun onDestroy() {
        super.onDestroy()
        detector?.release()
    }

    private fun updatePhotoView(mPhotoFile: File) = PictureUtils.getScaledBitmap(mPhotoFile.getPath(), this)



    class GalleryAdapter (val files: MutableList<Bitmap?>): RecyclerView.Adapter<GalleryAdapter.ViewHolder>() {
        val faces = ArrayList<SparseArray<Face>>()
        override fun getItemCount(): Int = this.files.size

        fun loadPhoto(b: Bitmap, face: SparseArray<Face>) {
            files.add(b)
            faces.add(face)
            notifyDataSetChanged()
        }

        override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
            holder?.v?.setImageBitmap(files[position])
            holder?.v?.updateFaces(faces[position], (files[position]?.width ?: 1).toFloat(),(files[position]?.height ?: 1).toFloat() )
        }

        override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int) : ViewHolder{
            val imageView = LayoutInflater.from(parent?.context).inflate(R.layout.gallery_image, parent, false) as FaceDetectImageView
            return ViewHolder(imageView)
        }

        class ViewHolder(val v: FaceDetectImageView): RecyclerView.ViewHolder(v) {

        }


    }
}

