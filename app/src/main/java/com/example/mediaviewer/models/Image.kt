package com.example.mediaviewer.models

import android.net.Uri

data class Image(val uri: Uri, val name: String, val title: String, val date: String, val size: Int)
