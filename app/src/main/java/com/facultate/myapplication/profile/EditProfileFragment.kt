package com.facultate.myapplication.profile

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.VectorDrawable
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.facultate.myapplication.MainActivityViewModel
import com.facultate.myapplication.R
import com.facultate.myapplication.databinding.FragmentProfileEditBinding
import com.facultate.myapplication.hilt.FirebaseModule
import com.facultate.myapplication.hilt.UsersDB
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import javax.inject.Inject

@AndroidEntryPoint
class EditProfileFragment : Fragment() {

    private val viewModel: MainActivityViewModel by viewModels()
    private lateinit var binding: FragmentProfileEditBinding

    @Inject
    lateinit var auth: FirebaseAuth

    @Inject
    @UsersDB
    lateinit var usersDB: CollectionReference

    @Inject
    lateinit var imageStorage: StorageReference

    private val requestCodeScreen = 2

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileEditBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setClickListeners()
        updateUiWithUserData(view)
    }

    override fun onStart() {
        super.onStart()
    }

    private fun updateUiWithUserData(view: View) {

        imageStorage.child("userProfileImages/${auth.currentUser!!.uid}.jpg")
            .getBytes(1024 * 1024)
            .addOnSuccessListener { bytes ->
                val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                binding.imageViewUserImage.setImageBitmap(bitmap)
            }
            .addOnFailureListener { exception ->
                if (exception.message == "Object does not exist at location.") {
                    usersDB.whereEqualTo("userID", auth.currentUser!!.uid)
                        .get()
                        .addOnSuccessListener { results ->
                            val documentRef = results.documents[0]
                            Glide.with(view.context)
                                .load(documentRef.data?.get("profileImageURL"))
                                .centerCrop()
                                .placeholder(R.drawable.placeholder_image)
                                .into(binding.imageViewUserImage)
                            binding.editTextUserEditName.setText(documentRef.get("name")?.toString())
                            binding.editTextUserEditEmail.setText(documentRef.get("email")?.toString())
                            binding.editTextUserEditPhone.setText(documentRef.get("phone")?.toString() ?: "")
                            binding.editTextUserEditAdress.setText(documentRef.get("adress")?.toString()?: "")
                        }
                }
            }

        viewModel.viewModelScope.launch {
            viewModel.store.read { applicationState ->
                val userData = applicationState.userData
                binding.editTextUserEditName.setText(userData.name)
                binding.editTextUserEditEmail.setText(userData.email)
                binding.editTextUserEditPhone.setText(userData.phone)
                binding.editTextUserEditAdress.setText(userData.adress)
            }
        }
    }

    private fun setClickListeners() {
        binding.buttonUserEditUpdate.setOnClickListener {
//            code to be put here
            saveUserData()
        }

        binding.cardViewUserImage.setOnClickListener {
            requestCameraPermission()
        }
    }

    private fun saveUserData() {
        viewModel.viewModelScope.launch {
            viewModel.store.update { applicationState ->
                val userDataCopy = applicationState.userData
                userDataCopy.name = binding.editTextUserEditName.text.toString()
                userDataCopy.email = binding.editTextUserEditEmail.text.toString()
                userDataCopy.phone = binding.editTextUserEditPhone.text.toString()
                userDataCopy.adress = binding.editTextUserEditAdress.text.toString()

                usersDB.whereEqualTo("userID", FirebaseModule.providesAuth().currentUser!!.uid)
                    .get()
                    .addOnSuccessListener { results ->
                        results.documents[0].reference.update(userDataCopy.toHashMap())
                    }

                return@update applicationState.copy(
                    userData = userDataCopy
                )
            }
        }
//        Save userImage
        val image = binding.imageViewUserImage.drawable
        val imageBitmap:Bitmap
        val baos = ByteArrayOutputStream()
        if (image is BitmapDrawable) {
            imageBitmap = (image as BitmapDrawable).bitmap
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        } else if (image is VectorDrawable) {
            // Convert the VectorDrawable to a BitmapDrawable
            val bitmap = Bitmap.createBitmap(
                image.intrinsicWidth,
                image.intrinsicHeight,
                Bitmap.Config.ARGB_8888
            )
            val canvas = Canvas(bitmap)
            image.setBounds(0, 0, canvas.width, canvas.height)
            image.draw(canvas)
            imageBitmap = BitmapDrawable(resources, bitmap).bitmap
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        }
        val data = baos.toByteArray()
        val storageRef = FirebaseStorage.getInstance().reference
        val imagesRef = storageRef.child("userProfileImages/${auth.currentUser!!.uid}.jpg")
        val uploadTask = imagesRef.putBytes(data)
        uploadTask.addOnSuccessListener {
            // Handle successful upload
        }.addOnFailureListener {
            // Handle unsuccessful upload
        }

        val goToProfile = EditProfileFragmentDirections.actionEditProfileFragmentToProfileFragment()
        findNavController().navigate(goToProfile)
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
//      TODO FIX THIS SHIT
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(cameraIntent, requestCodeScreen)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && requestCode == requestCodeScreen && data != null) {
            val newImage = data.extras?.get("data") as Bitmap
            binding.imageViewUserImage.setImageBitmap(newImage)
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }
}