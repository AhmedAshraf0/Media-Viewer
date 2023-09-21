package com.example.mediaviewer.features.images.view

import androidx.recyclerview.widget.DiffUtil
import com.example.mediaviewer.models.Image

class DiffUtilImages : DiffUtil.ItemCallback<Image>() {
    override fun areItemsTheSame(oldItem: Image, newItem: Image): Boolean {
        return oldItem === newItem
    }

    override fun areContentsTheSame(oldItem: Image, newItem: Image): Boolean {
        return oldItem == newItem
    }
}