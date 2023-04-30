package com.facultate.myapplication.register

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.facultate.myapplication.MainActivityViewModel
import com.facultate.myapplication.R
import com.facultate.myapplication.databinding.FragmentRegisterStage2Binding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.AndroidEntryPoint
import java.io.ByteArrayOutputStream
import javax.inject.Inject

@AndroidEntryPoint
class Stage2RegisterFragment : Fragment(R.layout.fragment_register_stage_2) {

//    for image capture
    private val requestCode = 200
    private val viewModel: MainActivityViewModel by viewModels()
    private lateinit var binding: FragmentRegisterStage2Binding
    @Inject
     lateinit var auth:FirebaseAuth

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentRegisterStage2Binding.bind(view)
    }

    override fun onStart() {
        super.onStart()
        setClickListeners()
    }

    private fun setClickListeners() {
        val uploadImage = binding.buttonUploadImage

        uploadImage.setOnClickListener {
            requestCameraPermission()
        }
        binding.textViewStage2Skip.setOnClickListener {
            goToStage3()
        }
    }

    private fun requestCameraPermission(){
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.CAMERA), 1)
        }else{
            launchCamera()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            launchCamera()
        }
    }

    private fun launchCamera() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(cameraIntent, requestCode)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && requestCode == requestCode && data != null) {
            val bitmap = data.extras?.get("data") as Bitmap
            attachImage(bitmap)
        }
        else{
            super.onActivityResult(requestCode, resultCode, data)

        }
    }

    private fun attachImage(bitmap: Bitmap) {
        binding.imageViewUserUploadedImage.setImageBitmap(bitmap)
        binding.textViewStage2Skip.visibility = View.INVISIBLE
        binding.buttonUploadImage.text = "Continue"
        binding.buttonUploadImage.setOnClickListener(null)
        binding.buttonUploadImage.setOnClickListener{

            uploadImageToFirebase(bitmap)

        }
    }

    fun uploadImageToFirebase(bitmap: Bitmap) {
        val storageRef = FirebaseStorage.getInstance().reference
        val imagesRef = storageRef.child("userProfileImages/${auth.currentUser!!.uid}.jpg")

        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()

        val uploadTask = imagesRef.putBytes(data)
        uploadTask.addOnSuccessListener {
            // Handle successful upload
            goToStage3()
        }.addOnFailureListener {
            // Handle unsuccessful upload
            goToStage3()
        }
    }

    private fun goToStage3() {
        val goToStage3 = Stage2RegisterFragmentDirections.actionSignUpStage2FragmentToSignUpStage3Fragment()
        findNavController().navigate(goToStage3)
    }


}