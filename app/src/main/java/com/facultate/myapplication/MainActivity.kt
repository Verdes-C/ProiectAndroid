package com.facultate.myapplication

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.facultate.myapplication.databinding.ActivityMainBinding
import com.facultate.myapplication.hilt.UsersDB
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {


    private lateinit var binding: ActivityMainBinding

    @Inject
    lateinit var auth: FirebaseAuth

    @Inject
    @UsersDB
    lateinit var usersDB: CollectionReference
    private lateinit var navController: NavController

    private val permissionRequestCode = 1002


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }

    override fun onStart() {
        super.onStart()

        askForPermissions()

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.frame_content) as NavHostFragment
        navController = Navigation.findNavController(this, R.id.frame_content)
        navHostFragment.navController.graph = navController.graph
        NavigationUI.setupWithNavController(binding.bottomNav, navController)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            val destinationIdToHide = arrayListOf<Int>(
                R.id.master_start,
                R.id.sign_up_stage1_fragment,
                R.id.sign_up_stage2_fragment,
                R.id.sign_up_stage3_fragment,
                R.id.sign_up_stage4_fragment,
                R.id.sign_up_stage_final,
                R.id.login_fragment
            )
            if (destination.id in destinationIdToHide) {
                binding.bottomNav.visibility = View.GONE
            } else {
                binding.bottomNav.visibility = View.VISIBLE
            }
        }
        if (auth.currentUser != null) {
            val destination = intent.extras?.get("destination")
            if(destination == "cart"){
                val goToNavGraph = NavGraphMasterDirections.actionGlobalToNavGraph()
                navController.navigate(goToNavGraph)
                val goToCart = NavGraphDirections.actionNavGraphToCart()
                navController.navigate(goToCart)
            }else{
                val goToHome = NavGraphMasterDirections.actionGlobalToCart()
                navController.navigate(goToHome)
            }
        } else {
            val goToLoginPrompt = NavGraphMasterDirections.actionGlobalToNavMaster()
            navController.navigate(goToLoginPrompt)
        }


    }

    private fun askForPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED
            || ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.CAMERA,
                    Manifest.permission.POST_NOTIFICATIONS
                ),
                permissionRequestCode
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == permissionRequestCode) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                TODO
            } else {
//                TODO
            }
        }
    }

}


