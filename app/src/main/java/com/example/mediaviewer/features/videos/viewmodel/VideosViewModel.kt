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
import androidx.lifecycle.viewModelScope
import com.example.mediaviewer.models.Image
import com.example.mediaviewer.models.Video
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class VideosViewModel(private val applicationContext: Context) : ViewModel() {
    private val TAG = "VideosViewModel"
    private var _videosList = MutableLiveData<List<Video>>()
    var videosList : LiveData<List<Video>> = _videosList

    fun getLocalVideos(
        collection: Uri,
        projection: Array<String>,
        selection: String,
        selectionArgs: Array<String>,
        sortOrder: String
    ){
        Log.i(TAG, "getLocalVideos: Starting...")
        val videos = mutableListOf<Video>()

        viewModelScope.launch(Dispatchers.IO) {
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
                    // Get values of columns for a given image.
                    val id = it.getLong(idColumn)
                    val name = it.getString(nameColumn)
                    val title = it.getString(titleColumn)
                    val date = it.getString(dateTakenColumn)
                    val size = it.getInt(sizeColumn)

                    val contentUri: Uri =
                        ContentUris.withAppendedId(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, id)

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        videos += Video(
                            contentUri, applicationContext.contentResolver.loadThumbnail(
                                contentUri, Size(640, 480), null
                            ), name, title, date, size
                        )
                    } else {
                        videos += Video(
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
                }
                Log.i(TAG, "getLocalVideos: Finished")
            }

            withContext(Dispatchers.Main){
                _videosList.value = videos
            }
        }
    }
}