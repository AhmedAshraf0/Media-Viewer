package com.example.mediaviewer.features.videos.view

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.mediaviewer.R
import com.example.mediaviewer.databinding.CardVideoBinding
import com.example.mediaviewer.models.Video

class VideosAdapter : RecyclerView.Adapter<VideosAdapter.ViewHolder>() {
    private val TAG = "VideosAdapter"
    var videos = mutableListOf<Video>()

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
        holder.cardVideoBinding.imageView.setImageBitmap(videos[position].thumbnail)
    }
    override fun getItemCount(): Int {
        return videos.size
    }

    fun updateVideos(videos: MutableList<Video>){
        Log.i(TAG, "updateVideos: just received videos ${videos.size}")
        this.videos = videos
        notifyDataSetChanged()
    }

}