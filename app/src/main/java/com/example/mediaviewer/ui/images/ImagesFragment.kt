package com.example.mediaviewer.ui.images

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.mediaviewer.databinding.FragmentImagesBinding
import com.example.mediaviewer.ui.images.viewmodel.ImagesViewModel
import com.example.mediaviewer.ui.images.viewmodel.ImagesViewModelFactory

class ImagesFragment : Fragment() {
    private val TAG = "ImagesFragment"
    private var _binding: FragmentImagesBinding? = null
    private lateinit var imagesViewModel: ImagesViewModel
    private lateinit var imagesViewModelFactory: ImagesViewModelFactory

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            Log.i(TAG, "requestPermissionLauncher: granted");
            imagesViewModel.getLocalImages()

        } else {
            Log.i(TAG, "requestPermissionLauncher: not granted");
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        requestPermissions()
        imagesViewModelFactory = ImagesViewModelFactory(requireActivity().applicationContext)

        imagesViewModel = ViewModelProvider(this,imagesViewModelFactory).get(ImagesViewModel::class.java)

        _binding = FragmentImagesBinding.inflate(inflater, container, false)
        _binding!!.imagesFragment = this

        return _binding!!.root
    }

    override fun onStart() {
        super.onStart()
        Log.i(TAG, "onStart: "  )
    }

    override fun onResume() {
        super.onResume()
        Log.i(TAG, "onResume: ")
    }
    fun onScreenTouched(view: View){
        //TODO("Disable clicking when photos is  displayed")
        Log.i(TAG, "onScreenTouched: clicked")
    }

    private fun requestPermissions() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
            when {
                ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.READ_MEDIA_IMAGES
                ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.READ_MEDIA_VIDEO
                ) == PackageManager.PERMISSION_GRANTED -> {
                    Log.i(TAG, "requestPermissions: modern granted")
                }

                else -> {
                    Log.i(TAG, "requestPermissions: modern request")
                    requestPermissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES)
                    requestPermissionLauncher.launch(Manifest.permission.READ_MEDIA_VIDEO)
                }
            }
        } else {
            when {
                ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED -> {
                    Log.i(TAG, "requestPermissions: old granted")
                }

                else -> {
                    Log.i(TAG, "requestPermissions: old request permissions")
                    requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                }
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}