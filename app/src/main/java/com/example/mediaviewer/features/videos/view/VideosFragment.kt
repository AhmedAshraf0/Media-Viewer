package com.example.mediaviewer.features.videos.view

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
import com.example.mediaviewer.databinding.FragmentVideosBinding
import com.example.mediaviewer.features.images.viewmodel.ImagesViewModel
import com.example.mediaviewer.features.images.viewmodel.ImagesViewModelFactory
import com.example.mediaviewer.features.videos.viewmodel.VideosViewModel
import com.example.mediaviewer.features.videos.viewmodel.VideosViewModelFactory

class VideosFragment : Fragment() {
    private val TAG = "VideosFragment"
    private var _binding: FragmentVideosBinding? = null
    private lateinit var videosViewModel: VideosViewModel
    private lateinit var videosViewModelFactory: VideosViewModelFactory
    private lateinit var videosAdapter: VideosAdapter

    private var isCalledOnce = false

    //query requirements
    private val collection =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            MediaStore.Video.Media.getContentUri(
                MediaStore.VOLUME_EXTERNAL
            )
        } else {
            MediaStore.Video.Media.EXTERNAL_CONTENT_URI
        }
    private val projection = arrayOf(
        MediaStore.Video.Media._ID,
        MediaStore.Video.Media.DISPLAY_NAME,
        MediaStore.Video.Media.TITLE,
        MediaStore.Video.Media.DATE_TAKEN,
        MediaStore.Video.Media.SIZE
    )
    private val selection = "${MediaStore.Video.Media.SIZE} <= ?"
    private val selectionArgs = arrayOf("10000000") //bytes. 10 MBs
    private val sortOrder = "${MediaStore.Video.Media.DATE_TAKEN} DESC"


    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            Log.i(TAG, "requestPermissionLauncher: granted");
            updateUI(true)
            videosAdapter.updateVideos(videosViewModel.getLocalVideos(
                collection,
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
        videosViewModelFactory = VideosViewModelFactory(requireActivity().applicationContext)

        videosViewModel = ViewModelProvider(this,videosViewModelFactory).get(VideosViewModel::class.java)

        _binding = FragmentVideosBinding.inflate(inflater, container, false)
        _binding!!.videosFragment = this

        _binding?.videosRecyclerView?.layoutManager = GridLayoutManager(requireContext(),2)
        videosAdapter = VideosAdapter()
        _binding?.videosRecyclerView?.adapter = videosAdapter

        return _binding!!.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requestPermissions(isCalledOnce, false)
    }
    fun onScreenTouched(view: View){
        requestPermissions(isCalledOnce, true)
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
                        updateUI(true)
                        videosAdapter.updateVideos(videosViewModel.getLocalVideos(
                            collection,
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
                        updateUI(true)
                        videosAdapter.updateVideos(videosViewModel.getLocalVideos(
                            collection,
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
            _binding?.videosRecyclerView?.visibility = View.VISIBLE
        }else{
            Log.i(TAG, "updateUI: false")
            if(_binding?.imagesIcon?.visibility == View.GONE){
                _binding?.videosRecyclerView?.visibility = View.GONE
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