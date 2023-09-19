package com.example.mediaviewer.features.images.view

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.mediaviewer.databinding.FragmentImagesBinding
import com.example.mediaviewer.features.images.viewmodel.ImagesViewModel
import com.example.mediaviewer.features.images.viewmodel.ImagesViewModelFactory

class ImagesFragment : Fragment() {
    private val TAG = "ImagesFragment"
    private var _binding: FragmentImagesBinding? = null
    private lateinit var imagesViewModel: ImagesViewModel
    private lateinit var imagesViewModelFactory: ImagesViewModelFactory
    private lateinit var imagesAdapter: ImagesAdapter

    private var isCalledOnce = false

    //query requirements
    private val projection = arrayOf(
        MediaStore.Images.Media._ID,
        MediaStore.Images.Media.DISPLAY_NAME,
        MediaStore.Images.Media.TITLE,
        MediaStore.Images.Media.DATE_TAKEN,
        MediaStore.Images.Media.SIZE
    )
    private val selection = "${MediaStore.Images.Media.SIZE} >= ?"
    private val selectionArgs = arrayOf("1")
    private val sortOrder = "${MediaStore.Images.Media.DATE_TAKEN} DESC"


    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            Log.i(TAG, "requestPermissionLauncher: granted");
            updateUI(true)
            imagesAdapter.updateImages(imagesViewModel.getLocalImages(
                projection,
                selection,
                selectionArgs,
                sortOrder
            ))
//            imagesAdapter.images = imagesViewModel.getLocalImages()
        } else {
            Log.i(TAG, "requestPermissionLauncher: not granted");
            updateUI(false)
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        imagesViewModelFactory = ImagesViewModelFactory(requireActivity().applicationContext)

        imagesViewModel = ViewModelProvider(this,imagesViewModelFactory).get(ImagesViewModel::class.java)

        _binding = FragmentImagesBinding.inflate(inflater, container, false)
        _binding!!.imagesFragment = this

        _binding?.imagesRecyclerView?.layoutManager = GridLayoutManager(requireContext(),2)
        imagesAdapter = ImagesAdapter()
        _binding?.imagesRecyclerView?.adapter = imagesAdapter

        return _binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requestPermissions(isCalledOnce, false)
    }
    fun onScreenTouched(view: View){
        requestPermissions(isCalledOnce, true)
        //TODO("When it will require the permissions , clicking should be disabled at until it responds and to not click more than once at the same time")
        Log.i(TAG, "onScreenTouched: clicked")
    }

    private fun requestPermissions(isCalledOnce: Boolean, isUserTouch: Boolean) {
        if(!isCalledOnce || isUserTouch){
            this.isCalledOnce = true
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
//                    imagesAdapter.images = imagesViewModel.getLocalImages()
                        updateUI(true)
                        imagesAdapter.updateImages(imagesViewModel.getLocalImages(
                            projection,
                            selection,
                            selectionArgs,
                            sortOrder
                        ))
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
//                    imagesAdapter.images = imagesViewModel.getLocalImages()
                        updateUI(true)
                        imagesAdapter.updateImages(imagesViewModel.getLocalImages(
                            projection,
                            selection,
                            selectionArgs,
                            sortOrder
                        ))
                    }

                    else -> {
                        Log.i(TAG, "requestPermissions: old request permissions")
                        requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                    }
                }
            }
        }


    }
    private fun updateUI(isGranted: Boolean){
        if(isGranted){
            Log.i(TAG, "updateUI: true")
            _binding?.imagesIcon?.visibility = View.GONE
            _binding?.userMessage?.visibility = View.GONE
            _binding?.imagesRecyclerView?.visibility = View.VISIBLE
        }else{
            Log.i(TAG, "updateUI: false")
            if(_binding?.imagesIcon?.visibility == View.GONE){
                _binding?.imagesRecyclerView?.visibility = View.GONE
                _binding?.imagesIcon?.visibility = View.VISIBLE
                _binding?.userMessage?.visibility = View.VISIBLE
            }
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}