package com.facultate.myapplication.profile

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.facultate.myapplication.MainActivity
import com.facultate.myapplication.databinding.FragmentProfileBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private lateinit var listener: ProfileFragmentInterface

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    private lateinit var userData:Map<String,Any>

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is ProfileFragmentInterface) {
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
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onStart() {
        super.onStart()

        auth = FirebaseAuth.getInstance()
        db = Firebase.firestore

        setClickListeners()
        readUserData()

    }

    private fun updateUiWithUserData() {
        val userImage = binding.imageViewUserImage
        val userName = binding.textViewUserName

        val storageRef = Firebase.storage.reference.child("userProfileImages/${auth.currentUser!!.uid}.jpg")
        storageRef.getBytes(1024 * 1024)
            .addOnSuccessListener { bytes ->
                val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                userImage.setImageBitmap(bitmap)
            }
            .addOnFailureListener { exception ->
                Log.e("TAG", "Error downloading image", exception)
            }

        userName.text = userData["name"].toString()
    }

    private fun readUserData() {
        db.collection("Users")
            .whereEqualTo("userID",auth.currentUser!!.uid)
            .get()
            .addOnSuccessListener { documents->
                for (document in documents){
                    userData = document.data
                }
                updateUiWithUserData()
            }
    }

    private fun setClickListeners() {

        binding.imageViewUserEdit.setOnClickListener {
            listener.goToEditProfileFragment()
        }

        binding.buttonUserLogout.setOnClickListener {
            auth.signOut()
            goToMainActivity()
        }
    }

    private fun goToMainActivity() {
        val goToMainActivity = Intent(context,MainActivity::class.java)
        startActivity(goToMainActivity)
    }

    fun handleBackPressed() {
        listener.goHome()
    }

    interface ProfileFragmentInterface{
        fun goToEditProfileFragment()
        fun goHome()
    }
}