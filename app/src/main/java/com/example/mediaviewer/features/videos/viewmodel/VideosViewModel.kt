package com.example.mediaviewer.features.videos.viewmodel

import android.content.ContentUris
import android.content.Context
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import android.util.Size
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mediaviewer.models.Image
import com.example.mediaviewer.models.Video

class VideosViewModel(private val applicationContext: Context) : ViewModel() {
    private val TAG = "VideosViewModel"
    private val videosList = mutableListOf<Video>()

    //TODO("ADD COROUTINES")
    fun getLocalVideos(
        collection: Uri,
        projection: Array<String>,
        selection: String,
        selectionArgs: Array<String>,
        sortOrder: String
    ): MutableList<Video> {
        var ctr = 0

        applicationContext.contentResolver.query(
            collection,
            projection,
            selection,
            selectionArgs,
            sortOrder
        )?.use {
            //cache column indices
            val idColumn = it.getColumnIndexOrThrow(MediaStore.Video.Media._ID)
            val nameColumn = it.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME)
            val titleColumn = it.getColumnIndexOrThrow(MediaStore.Video.Media.TITLE)
            val dateTakenColumn = it.getColumnIndexOrThrow(MediaStore.Video.Media.DATE_TAKEN)
            val sizeColumn = it.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE)


            while (it.moveToNext()) {
                ctr++
                // Get values of columns for a given image.
                val id = it.getLong(idColumn)
                val name = it.getString(nameColumn)
                val title = it.getString(titleColumn)
                val date = it.getString(dateTakenColumn)
                val size = it.getInt(sizeColumn)

                val contentUri: Uri =
                    ContentUris.withAppendedId(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, id)

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    videosList += Video(
                        contentUri, applicationContext.contentResolver.loadThumbnail(
                            contentUri, Size(640, 480), null
                        ), name, title, date, size
                    )
                } else {
                    videosList += Video(
                        contentUri,
                        MediaStore.Video.Thumbnails.getThumbnail(
                            applicationContext.contentResolver,
                            id,
                            MediaStore.Images.Thumbnails.MINI_KIND,
                            null
                        ),
                        name,
                        title,
                        date,
                        size
                    )
                }

                Log.i(TAG, "From MediaStore")
            }
            Log.i(TAG, "After MediaStore While loop: ctr = $ctr")
            //call another function to display photos in rv
        }
        return videosList
    }
}