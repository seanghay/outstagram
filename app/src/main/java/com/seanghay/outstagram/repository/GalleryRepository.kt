package com.seanghay.outstagram.repository

import android.annotation.SuppressLint
import android.provider.MediaStore
import com.seanghay.outstagram.OutstagramLoader
import com.seanghay.outstagram.model.ChooseImageItem
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

object GalleryRepository {

    private val contentResolver by lazy {
        OutstagramLoader.appContext.contentResolver
    }

    @SuppressLint("Recycle")
    suspend fun getPhotos(): List<ChooseImageItem> = coroutineScope {
        val uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val projection =
            arrayOf(
                MediaStore.MediaColumns._ID,
                MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.MediaColumns.DATA,
                MediaStore.MediaColumns.DATE_ADDED
            )

        val selection: String? = null     //Selection criteria
        val selectionArgs = arrayOf<String>()  //Selection criteria
        val sortOrder: String = MediaStore.Files.FileColumns.DATE_ADDED + " DESC"

        val images = mutableListOf<ChooseImageItem>()

        val cursor = contentResolver.query(uri, projection, selection, selectionArgs, sortOrder) ?: return@coroutineScope images
        cursor.moveToFirst()
        val idColumn = cursor.getColumnIndex(MediaStore.MediaColumns._ID)
        val displayNameColumn = cursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME)
        val data = cursor.getColumnIndex(MediaStore.MediaColumns.DATA)

        while (cursor.moveToNext()) {
            val image = ChooseImageItem(
                cursor.getLong(idColumn),
                cursor.getString(displayNameColumn),
                cursor.getString(data)
            )

            images.add(image)
        }

        cursor.close()
        images
    }

}