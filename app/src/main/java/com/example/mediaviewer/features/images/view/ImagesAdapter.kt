package com.example.mediaviewer.features.images.view

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.mediaviewer.R
import com.example.mediaviewer.databinding.CardImageBinding
import com.example.mediaviewer.models.Image

class ImagesAdapter : ListAdapter<Image,ImagesAdapter.ViewHolder>(DiffUtilImages()) {
    private val TAG = "ImagesAdapter"

    inner class ViewHolder(val cardImageBinding: CardImageBinding) :
        RecyclerView.ViewHolder(cardImageBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = DataBindingUtil.inflate<CardImageBinding>(LayoutInflater.from(parent.context),
            R.layout.card_image,parent,false)

        return ViewHolder(binding)
    }
    
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Log.i(TAG, "onBindViewHolder: ")
        holder.cardImageBinding.imageView.setImageURI(getItem(position).uri)
    }
}