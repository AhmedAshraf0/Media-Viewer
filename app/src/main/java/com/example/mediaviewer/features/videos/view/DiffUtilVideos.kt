package com.example.mediaviewer.features.videos.view

import androidx.recyclerview.widget.DiffUtil
import com.example.mediaviewer.models.Video

class DiffUtilVideos : DiffUtil.ItemCallback<Video>() {
    override fun areItemsTheSame(oldItem: Video, newItem: Video): Boolean {
        return oldItem === newItem
    }

    override fun areContentsTheSame(oldItem: Video, newItem: Video): Boolean {
        return oldItem == newItem
    }

}