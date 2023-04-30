package com.facultate.myapplication.register

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.facultate.myapplication.MainActivity
import com.facultate.myapplication.MainActivityViewModel
import com.facultate.myapplication.NavGraphMasterDirections
import com.facultate.myapplication.R
import com.facultate.myapplication.databinding.FragmentRegisterFinalBinding
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FinalRegisterFragment : Fragment(R.layout.fragment_register_final) {

    private val viewModel:MainActivityViewModel by viewModels()
    private lateinit var binding: FragmentRegisterFinalBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentRegisterFinalBinding.bind(view)
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
            val actionToHome = NavGraphMasterDirections.actionGlobalToNavGraph()
            findNavController().navigate(actionToHome)
        }
    }


}