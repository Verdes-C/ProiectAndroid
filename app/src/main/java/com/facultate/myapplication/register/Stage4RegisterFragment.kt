package com.facultate.myapplication.register

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.facultate.myapplication.MainActivityViewModel
import com.facultate.myapplication.R
import com.facultate.myapplication.databinding.FragmentRegisterStage4Binding
import com.facultate.myapplication.hilt.UsersDB
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class Stage4RegisterFragment : Fragment(R.layout.fragment_register_stage_4) {

    private var interests = arrayListOf<String>()
    private val viewModel: MainActivityViewModel by viewModels()
    private lateinit var binding: FragmentRegisterStage4Binding

    @Inject
     lateinit var auth: FirebaseAuth
    @Inject @UsersDB
     lateinit var usersDB:CollectionReference

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentRegisterStage4Binding.bind(view)
    }

    override fun onStart() {
        super.onStart()
        setClickListeners()
    }

    private fun setClickListeners() {
        val goToFinal = binding.buttonGoToStepFinal
        val optionGaming = binding.buttonAddOptionGaming
        val optionReading = binding.buttonAddOptionReading
        val optionFashion = binding.buttonAddOptionFashion
        val optionMusic = binding.buttonAddOptionMusic

        optionGaming.setOnClickListener {
            optionGaming.text = "√"
            if (!interests.contains("gaming")){
                interests.add("gaming")
            }
        }

        optionReading.setOnClickListener {
            optionReading.text = "√"
            if (!interests.contains("reading")){
                interests.add("reading")
            }
        }

        optionFashion.setOnClickListener {
            optionFashion.text = "√"
            if (!interests.contains("fashion")){
                interests.add("fashion")
            }
        }

        optionMusic.setOnClickListener {
            optionMusic.text = "√"
            if (!interests.contains("music")){
                interests.add("music")
            }
        }

        goToFinal.setOnClickListener {

            usersDB
                .whereEqualTo("userID",auth.currentUser!!.uid)
                .get()
                .addOnSuccessListener { documents->
                    for (document in documents){
                        val docRef = document.reference
                        docRef.update("interests",interests)
                    }
                }
            goToFinal()
        }
    }

    private fun goToFinal(){
        val goToFinal = Stage4RegisterFragmentDirections.actionSignUpStage4FragmentToSignUpStageFinal()
        findNavController().navigate(goToFinal)
    }


}