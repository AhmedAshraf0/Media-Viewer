package com.example.mediaviewer.features.videos.view

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.mediaviewer.R
import com.example.mediaviewer.databinding.CardVideoBinding
import com.example.mediaviewer.features.images.view.DiffUtilImages
import com.example.mediaviewer.features.images.view.ImagesAdapter
import com.example.mediaviewer.models.Image
import com.example.mediaviewer.models.Video

class VideosAdapter : ListAdapter<Video, VideosAdapter.ViewHolder>(DiffUtilVideos()) {
    private val TAG = "VideosAdapter"
//    var videos = listOf<Video>()

    inner class ViewHolder(val cardVideoBinding: CardVideoBinding) :
        RecyclerView.ViewHolder(cardVideoBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = DataBindingUtil.inflate<CardVideoBinding>(
            LayoutInflater.from(parent.context),
            R.layout.card_video,parent,false)

        return ViewHolder(binding)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Log.i(TAG, "onBindViewHolder: ")
        holder.cardVideoBinding.imageView.setImageBitmap(getItem(position).thumbnail)
    }

    /*fun updateVideos(videos: List<Video>){
        Log.i(TAG, "updateVideos: just received videos ${videos.size}")
        this.videos = videos
        notifyDataSetChanged()
    }*/

}