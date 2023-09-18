package com.example.mediaviewer

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.mediaviewer.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private final val TAG = "MainActivity"
    private lateinit var binding: ActivityMainBinding
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            Log.i(TAG, "requestPermissionLauncher: granted");
            //logic
        } else {
            Log.i(TAG, "requestPermissionLauncher: not granted");
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        requestPermissions()

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)

        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_images, R.id.navigation_videos
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    private fun requestPermissions() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
            when {
                ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.READ_MEDIA_IMAGES
                ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                    this,
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
                    this,
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
}