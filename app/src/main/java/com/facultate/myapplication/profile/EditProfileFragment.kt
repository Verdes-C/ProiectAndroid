package com.facultate.myapplication.profile

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.facultate.myapplication.databinding.FragmentProfileEditBinding
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import java.io.ByteArrayOutputStream

class EditProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileEditBinding
    private lateinit var listener: ProfileEditFragmentInterface

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    private lateinit var userData: Map<String, Any>

    private lateinit var userImage: ImageView
    private lateinit var userName: TextInputEditText
    private lateinit var userEmail: TextInputEditText
    private lateinit var userPhone: TextInputEditText
    private lateinit var userAdress: TextInputEditText

    private val requestCode = 2
    private lateinit var newImage: Bitmap
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is ProfileEditFragmentInterface) {
            listener = context
        } else {
            throw RuntimeException("$context must implement MyFragmentListener")
        }
    }

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


    }

    override fun onStart() {
        super.onStart()

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        userImage = binding.imageViewUserImage
        userName = binding.editTextUserEditName
        userEmail = binding.editTextUserEditEmail
        userPhone = binding.editTextUserEditPhone
        userAdress = binding.editTextUserEditAdress

        setClickListeners()
        readUserData()
    }

    private fun updateUiWithUserData() {

        val storageRef =
            Firebase.storage.reference.child("userProfileImages/${auth.currentUser!!.uid}.jpg")
        storageRef.getBytes(1024 * 1024)
            .addOnSuccessListener { bytes ->
                val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                userImage.setImageBitmap(bitmap)
            }
            .addOnFailureListener { exception ->
                Log.e("TAG", "Error downloading image", exception)
            }

        userName.setText(userData["name"].toString())
        userEmail.setText(userData["email"].toString())
        userPhone.setText(userData["phone"]?.toString() ?: "...")
        userAdress.setText(userData["address"]?.toString() ?: "...")

    }

    private fun readUserData() {
        db.collection("Users")
            .whereEqualTo("userID", auth.currentUser!!.uid)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    userData = document.data
                }
                updateUiWithUserData()
            }
    }


    private fun setClickListeners() {
        binding.buttonUserEditUpdate.setOnClickListener {
//            code to be put here
            listener.saveUserData(userData as HashMap<String, Any>)
        }

        binding.cardViewUserImage.setOnClickListener {
            launchCamera()
        }
    }

    fun handleBackPressed() {
        editUserData()
        saveImage()
        listener.saveUserData(userData as HashMap<String, Any>)
    }


    private fun editUserData() {
        userData = hashMapOf(
            "name" to userName.text.toString(),
            "email" to userEmail.text.toString(),
            "phone" to userPhone.text.toString(),
            "adress" to userAdress.text.toString()
        )
    }

    private fun saveImage() {
        val storageRef = FirebaseStorage.getInstance().reference
        val imagesRef = storageRef.child("userProfileImages/${auth.currentUser!!.uid}.jpg")

        val baos = ByteArrayOutputStream()
        newImage.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()

        val uploadTask = imagesRef.putBytes(data)
        uploadTask.addOnSuccessListener {
            // Handle successful upload
        }.addOnFailureListener {
            // Handle unsuccessful upload
        }
    }


    interface ProfileEditFragmentInterface {
        fun saveUserData(userData: HashMap<String, Any>)
    }

    private fun launchCamera() {
//      TODO FIX THIS SHIT
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(cameraIntent, requestCode)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && requestCode == requestCode && data != null) {
            newImage = data.extras?.get("data") as Bitmap
            binding.imageViewUserImage.setImageBitmap(newImage)
        } else {
            super.onActivityResult(requestCode, resultCode, data)

        }
    }

}