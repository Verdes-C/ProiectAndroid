package com.facultate.myapplication.register

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import androidx.activity.result.contract.ActivityResultContract
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.facultate.myapplication.databinding.ActivityRegisterStage2Binding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import java.io.ByteArrayOutputStream

class Stage2RegisterActivity : AppCompatActivity() {

//    for image capture
    private val requestCode = 200

    private lateinit var binding: ActivityRegisterStage2Binding

    private lateinit var auth:FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterStage2Binding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onStart() {
        super.onStart()

        auth = FirebaseAuth.getInstance()
        setClickListeners()

    }

    private fun setClickListeners() {
        val uploadImage = binding.buttonUploadImage

        uploadImage.setOnClickListener {
            requestCameraPermission()


        }
    }

    private fun requestCameraPermission(){
        if (ContextCompat.checkSelfPermission(this@Stage2RegisterActivity, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this@Stage2RegisterActivity, arrayOf(Manifest.permission.CAMERA), 1)
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
        val goToStage3 = Intent(this,Stage3RegisterActivity::class.java)
        startActivity(goToStage3)
    }


}