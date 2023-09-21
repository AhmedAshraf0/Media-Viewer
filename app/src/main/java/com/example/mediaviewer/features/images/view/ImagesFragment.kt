package com.example.mediaviewer.features.images.view

import android.Manifest
import android.content.pm.PackageManager
import android.content.res.Configuration
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
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.mediaviewer.databinding.FragmentImagesBinding
import com.example.mediaviewer.features.images.viewmodel.ImagesViewModel
import com.example.mediaviewer.features.images.viewmodel.ImagesViewModelFactory
import com.example.mediaviewer.utils.SharedPreferencesManager
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ImagesFragment : Fragment() {
    private val TAG = "ImagesFragment"

    //to not request permission each time user opens  the app
    private var sharedPreferences: SharedPreferencesManager? = null

    private var _binding: FragmentImagesBinding? = null

    private lateinit var imagesViewModel: ImagesViewModel
    private lateinit var imagesViewModelFactory: ImagesViewModelFactory

    private lateinit var imagesAdapter: ImagesAdapter

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
            imagesViewModel.getLocalImages(
                projection,
                selection,
                selectionArgs,
                sortOrder
            )
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
        sharedPreferences = SharedPreferencesManager(requireContext())

        imagesViewModelFactory = ImagesViewModelFactory(requireActivity().applicationContext)

        imagesViewModel =
            ViewModelProvider(this, imagesViewModelFactory).get(ImagesViewModel::class.java)

        _binding = FragmentImagesBinding.inflate(inflater, container, false)
        _binding!!.imagesFragment = this

        if(resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT)
            _binding?.imagesRecyclerView?.layoutManager = GridLayoutManager(requireContext(), 2)
        else
            _binding?.imagesRecyclerView?.layoutManager = GridLayoutManager(requireContext(), 4)

        imagesAdapter = ImagesAdapter()
        _binding?.imagesRecyclerView?.adapter = imagesAdapter


        imagesViewModel.imagesList.observe(viewLifecycleOwner){
            Log.i(TAG, "received from livedata: ${it.size}")
            if(imagesAdapter.itemCount == 0){
                imagesAdapter.submitList(it)
            }
            _binding?.progressBar?.visibility = View.GONE
        }

        return _binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if(imagesViewModel.imagesList.value.isNullOrEmpty()){
            if(sharedPreferences!!.getBoolean(("isFirstTime"))){
                sharedPreferences!!.saveBoolean("isFirstTime",false)
                requestPermissions(true, false)
            }
            requestPermissions(sharedPreferences!!.getBoolean("isFirstTime"), false)
        }else{
            updateUI(true)
            imagesAdapter.submitList(imagesViewModel.imagesList.value!!)
        }
    }

    fun onScreenTouched(view: View) {
        if(_binding!!.userMessage.visibility == View.VISIBLE)
            requestPermissions(sharedPreferences!!.getBoolean("isFirstTime"), true)
        Log.i(TAG, "onScreenTouched: clicked")
    }

    private fun requestPermissions(isCalledOnce: Boolean, isUserTouch: Boolean) {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
            if(ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.READ_MEDIA_IMAGES
                ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.READ_MEDIA_VIDEO
                ) == PackageManager.PERMISSION_GRANTED){
                if(isUserTouch || imagesAdapter.itemCount == 0){
                    updateUI(true)
                    imagesViewModel.getLocalImages(
                        projection,
                        selection,
                        selectionArgs,
                        sortOrder
                    )
                }
            }else if(isCalledOnce || isUserTouch){
                Log.i(TAG, "requestPermissions: modern request")
                requestPermissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES)
                requestPermissionLauncher.launch(Manifest.permission.READ_MEDIA_VIDEO)
            }
        }else{
            if(ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED){
                if(isUserTouch || imagesAdapter.itemCount == 0){
                    updateUI(true)
                    imagesViewModel.getLocalImages(
                        projection,
                        selection,
                        selectionArgs,
                        sortOrder
                    )
                }
            }else if(isCalledOnce || isUserTouch){
                Log.i(TAG, "requestPermissions: old request permissions")
                requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        }

        /*if (isCalledOnce || isUserTouch) {
            sharedPreferences!!.saveBoolean("isFirstTime",false)
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
                        if(imagesViewModel.imagesList.value.isNullOrEmpty())
                            imagesViewModel.getLocalImages(
                                projection,
                                selection,
                                selectionArgs,
                                sortOrder
                            )
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
                        if(imagesViewModel.imagesList.value.isNullOrEmpty())
                            imagesViewModel.getLocalImages(
                                projection,
                                selection,
                                selectionArgs,
                                sortOrder
                            )
                    }

                    else -> {
                        Log.i(TAG, "requestPermissions: old request permissions")
                        requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                    }
                }
            }
        }*/


    }

    private fun updateUI(hideUI: Boolean) {
        if (hideUI) {
            Log.i(TAG, "updateUI: true")
            _binding?.imagesIcon?.visibility = View.GONE
            _binding?.userMessage?.visibility = View.GONE
            _binding?.progressBar?.visibility = View.VISIBLE
            _binding?.imagesRecyclerView?.visibility = View.VISIBLE
        } else {
            Log.i(TAG, "updateUI: false")
            if (_binding?.imagesIcon?.visibility == View.GONE) {
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