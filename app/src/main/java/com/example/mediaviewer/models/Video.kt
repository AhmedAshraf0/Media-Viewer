package com.example.mediaviewer.models

import android.graphics.Bitmap
import android.net.Uri

//thumbnail? because some times it can't generate uri for a video
data class Video(val uri: Uri, val thumbnail: Bitmap?, val name: String, val title: String, val date: String, val size: Int)
