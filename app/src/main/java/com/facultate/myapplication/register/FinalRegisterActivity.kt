package com.facultate.myapplication.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.facultate.myapplication.MainActivity
import com.facultate.myapplication.databinding.ActivityRegisterFinalBinding
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.delay

class FinalRegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterFinalBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterFinalBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onStart() {
        super.onStart()

        setClickListeners()
        loadPromotionalVideo()
    }

    private fun loadPromotionalVideo() {
        val promoVideo = binding.videoViewPromo
        val storageRef = Firebase.storage.reference.child("videos/promo.mp4")

        storageRef.downloadUrl.addOnSuccessListener { uri ->
            promoVideo.setVideoURI(uri)
            promoVideo.start()
        }.addOnFailureListener { exception ->
            // Handle any errors
            exception.message?.let { Log.d("VIDEO", it) }
        }

    }

    private fun setClickListeners() {
        val finishRegistering = binding.buttonRegisterEnd

        finishRegistering.setOnClickListener {
//
            val goHome = Intent(this, MainActivity::class.java)
            startActivity(goHome)
        }
    }


}