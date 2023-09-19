package com.example.mediaviewer.ui.images.viewmodel

import android.content.ContentUris
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.mediaviewer.models.Image

class ImagesViewModel(private val applicationContext: Context) : ViewModel() {
    private final val TAG = "ImagesViewModel"
    val imagesList = mutableListOf<Image>()

    fun getLocalImages(){
        //logic
        var ctr = 0

        val projection = arrayOf(
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.DISPLAY_NAME,
            MediaStore.Images.Media.TITLE,
            MediaStore.Images.Media.DATE_TAKEN,
            MediaStore.Images.Media.SIZE
        )
        val selection = "${MediaStore.Images.Media.SIZE} >= ?"
        val selectionArgs = arrayOf("1")
        val sortOrder = "${MediaStore.Images.Media.DATE_TAKEN} DESC"


        applicationContext.contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            projection,
            selection,
            selectionArgs,
            sortOrder
        )?.use {
            //cache column indices
            val idColumn = it.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
            val nameColumn = it.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)
            val titleColumn = it.getColumnIndexOrThrow(MediaStore.Images.Media.TITLE)
            val dateTakenColumn = it.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_TAKEN)
            val sizeColumn = it.getColumnIndexOrThrow(MediaStore.Images.Media.SIZE)


            while (it.moveToNext()) {
                ctr++
                // Get values of columns for a given image.
                val id = it.getLong(idColumn)
                val name = it.getString(nameColumn)
                val title = it.getString(titleColumn)
                val date = it.getString(dateTakenColumn)
                val size = it.getInt(sizeColumn)

                val contentUri: Uri =
                    ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)

                imagesList += Image(contentUri,name,title,date,size)

                Log.i(TAG, "From MediaStore")
            }
            Log.i(TAG, "After MediaStore While loop: ctr = $ctr")


            //return true
        }
    }
}