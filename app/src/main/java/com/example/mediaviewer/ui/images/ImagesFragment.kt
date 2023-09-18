package com.example.mediaviewer.ui.images

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.mediaviewer.databinding.FragmentImagesBinding

class ImagesFragment : Fragment() {
    private final val TAG = "ImagesFragment"
    private var _binding: FragmentImagesBinding? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val imagesViewModel =
            ViewModelProvider(this).get(ImagesViewModel::class.java)

        _binding = FragmentImagesBinding.inflate(inflater, container, false)
        _binding!!.imagesFragment = this

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