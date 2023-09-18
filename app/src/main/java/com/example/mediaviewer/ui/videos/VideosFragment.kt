package com.example.mediaviewer.ui.videos

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.mediaviewer.databinding.FragmentVideosBinding

class VideosFragment : Fragment() {
    private final val TAG = "VideosFragment"
    private var _binding: FragmentVideosBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val videosViewModel =
            ViewModelProvider(this).get(VideosViewModel::class.java)

        _binding = FragmentVideosBinding.inflate(inflater, container, false)
        _binding!!.videosFragment = this

        return _binding!!.root
    }

    fun onScreenTouched(view: View){
        Log.i(TAG, "onScreenTouched: clicked")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}