package com.bignerdranch.android.criminalintent

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.util.SparseArray
import com.google.android.gms.vision.face.Face


class FaceDetectImageView(context: Context?, attrs: AttributeSet?) : android.support.v7.widget.AppCompatImageView(context, attrs) {

    var faces: SparseArray<Face> = SparseArray()
    private val mBoxPaint: Paint
    private val BOX_STROKE_WIDTH = 30.0f
    private var widthR = 1.0F
    private var heighR = 1.0F
    private var dirty = false

    init {
        mBoxPaint = Paint()
        mBoxPaint.color = Color.RED
        mBoxPaint.style = Paint.Style.STROKE
        mBoxPaint.strokeWidth = BOX_STROKE_WIDTH
    }

    fun updateFaces(faces: SparseArray<Face>, widthR: Float, heighR: Float) {
        this.faces = faces.clone()
        this.widthR = width / widthR
        this.heighR = height / heighR
        dirty = true
        invalidate()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (!dirty) return
        canvas?.save()
        canvas?.scale(widthR, heighR)

        for (i in 0 until faces.size()) {
            Log.d("face", "${width}")
            val face = faces[i]
            if (face == null) continue
            Log.d("face", "${face.position}")
            // Draws a circle at the position of the detected face, with the face's track id below.
            val x = (face.position.x + face.width / 2)
            val y = (face.position.y + face.height / 2)


            // Draws a bounding box around the face.
            val xOffset = (face.width / 2.0f)
            val yOffset = (face.height / 2.0f)
            val left = x - xOffset
            val top = y - yOffset
            val right = x + xOffset
            val bottom = y + yOffset
            canvas?.drawRect(left, top, right, bottom, mBoxPaint)
        }
        canvas?.restore()
    }
}
