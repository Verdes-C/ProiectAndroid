package com.facultate.myapplication.profile

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewModelScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.facultate.myapplication.MainActivityViewModel
import com.facultate.myapplication.NavGraphMasterDirections
import com.facultate.myapplication.R
import com.facultate.myapplication.databinding.FragmentProfileBinding
import com.facultate.myapplication.hilt.FirebaseModule
import com.facultate.myapplication.hilt.UsersDB
import com.facultate.myapplication.login.PromptLoginFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.storage.StorageReference
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private val viewModel: MainActivityViewModel by viewModels()
    private lateinit var binding: FragmentProfileBinding

    @Inject
    lateinit var auth: FirebaseAuth

    @Inject
    @UsersDB
    lateinit var usersDB: CollectionReference

    @Inject
    lateinit var imageStorage: StorageReference


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

        setClickListeners(view)
        updateUiWithUserData(view)
    }

    override fun onStart() {
        super.onStart()
    }

    private fun updateUiWithUserData(view: View) {
        val userImage = binding.imageViewUserImage
        val userName = binding.textViewUserName

        imageStorage
            .child("userProfileImages/${FirebaseModule.providesAuth().currentUser!!.uid}.jpg")
            .getBytes(1024 * 1024)
            .addOnSuccessListener { bytes ->
                val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                userImage.setImageBitmap(bitmap)
            }
            .addOnFailureListener { exception ->
                if (exception.message == "Object does not exist at location.") {
                    usersDB.whereEqualTo("userID", FirebaseModule.providesAuth().currentUser!!.uid)
                        .get()
                        .addOnSuccessListener { results ->
                            val documentRef = results.documents[0]
                            val profileImageURL = documentRef.data?.get("profileImageURL")
                            Glide.with(view.context)
                                .load(profileImageURL)
                                .centerCrop()
                                .placeholder(R.drawable.placeholder_image)
                                .into(userImage)
                        }
                        .addOnFailureListener { exception ->
//                            TODO ??? Maybe some error handling?
                        }
                } else {
//                    TODO ???
                    Log.e("ProfileImageDownload", "Error downloading image", exception)
                }
            }
        viewModel.viewModelScope.launch {
            viewModel.store.read { applicationState ->
                userName.text = applicationState.userData.name
            }
        }
    }

    private fun setClickListeners(view: View) {

        binding.imageViewUserEdit.setOnClickListener {
            val actionToEditProfile =
                ProfileFragmentDirections.actionProfileFragmentToEditProfileFragment()
            view.findNavController().navigate(actionToEditProfile)
        }

        binding.buttonUserLogout.setOnClickListener {
            auth.signOut()
//            TODO MODIFY TO BE A FRAGMENT
            val actionToLoginPrompt = NavGraphMasterDirections.actionGlobalToNavMaster()
            findNavController().navigate(actionToLoginPrompt)

        }
    }
}