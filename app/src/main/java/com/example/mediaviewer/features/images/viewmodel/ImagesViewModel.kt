package com.example.mediaviewer.features.images.viewmodel

import android.content.ContentUris
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mediaviewer.models.Image
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ImagesViewModel(private val applicationContext: Context) : ViewModel() {
    private val TAG = "ImagesViewModel"
    private var _imagesList = MutableLiveData<List<Image>>()
    var imagesList : LiveData<List<Image>> = _imagesList

    fun getLocalImages(
        projection: Array<String>,
        selection: String,
        selectionArgs: Array<String>,
        sortOrder: String
    ){
        Log.i(TAG, "getLocalImages: Starting...")
        val images = mutableListOf<Image>()

        viewModelScope.launch(Dispatchers.IO){
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
                    // Get values of columns for a given image.
                    val id = it.getLong(idColumn)
                    val name = it.getString(nameColumn)
                    val title = it.getString(titleColumn)
                    val date = it.getString(dateTakenColumn)
                    val size = it.getInt(sizeColumn)

                    val contentUri: Uri =
                        ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)

                    images += Image(contentUri, name, title, date, size)
                }
                Log.i(TAG, "getLocalImages: Finished")
            }

            withContext(Dispatchers.Main){
                _imagesList.value = images
            }
        }
    }
}