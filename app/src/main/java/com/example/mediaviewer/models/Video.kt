package com.example.mediaviewer.models

import android.graphics.Bitmap
import android.net.Uri

data class Video(val uri: Uri, val thumbnail: Bitmap?, val name: String, val title: String, val date: String, val size: Int)
