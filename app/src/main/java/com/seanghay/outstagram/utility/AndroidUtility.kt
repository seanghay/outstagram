package com.seanghay.outstagram.utility

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import androidx.exifinterface.media.ExifInterface


object AndroidUtility {
    const val RC_READ_EXTERNAL_STORAGE = 1
    const val RC_WRITE_EXTERNAL_STORAGE = 2
    const val RC_READ_WRITE_EXTERNAL_STORAGE = 3
    const val RC_RATIONALE = 3


    fun loadBitmap(filePath: String): Bitmap {
        return loadSync(filePath)
    }

    private fun loadSync(filePath: String): Bitmap {
        val b = BitmapFactory.decodeFile(filePath)
        val exif = ExifInterface(filePath)
        val orientation = exif.getAttributeInt(
            ExifInterface.TAG_ORIENTATION,
            ExifInterface.ORIENTATION_NORMAL
        )
        val rotationInDegrees = exifToDegrees(orientation)
        val matrix = Matrix()
        if (rotationInDegrees != 0) matrix.preRotate(rotationInDegrees.toFloat())
        return Bitmap.createBitmap(b, 0, 0, b.width, b.height, matrix, true)
    }


    private fun exifToDegrees(exifOrientation: Int): Int = when (exifOrientation) {
        ExifInterface.ORIENTATION_ROTATE_90 -> 90
        ExifInterface.ORIENTATION_ROTATE_180 -> 180
        ExifInterface.ORIENTATION_ROTATE_270 -> 270
        else -> 0
    }

}