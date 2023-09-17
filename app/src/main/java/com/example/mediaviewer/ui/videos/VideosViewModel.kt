package com.example.mediaviewer.ui.videos

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class VideosViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is Videos Fragment"
    }
    val text: LiveData<String> = _text
}