package com.facultate.myapplication.register

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.facultate.myapplication.MainActivityViewModel
import com.facultate.myapplication.R
import com.facultate.myapplication.databinding.FragmentRegisterStage3Binding
import com.facultate.myapplication.hilt.UsersDB
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class Stage3RegisterFragment : Fragment(R.layout.fragment_register_stage_3) {

    private var joiningReason = "undecided"
    private val viewModel: MainActivityViewModel by viewModels()
    private lateinit var binding: FragmentRegisterStage3Binding

    @Inject
     lateinit var auth: FirebaseAuth

    @Inject
    @UsersDB
     lateinit var db: CollectionReference

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentRegisterStage3Binding.bind(view)
    }

    override fun onStart() {
        super.onStart()
        setClickListeners()
    }

    private fun setClickListeners() {
        val buttonGoStage4 = binding.buttonGoToStep4
        val buttonDiscover = binding.buttonChoiceDiscover
        val buttonMonthly = binding.buttonChoiceEasierMonthly

        buttonDiscover.setOnClickListener {
            joiningReason = "discover"
        }
        buttonMonthly.setOnClickListener {
            joiningReason = "monthly"
        }

        buttonGoStage4.setOnClickListener {
//            update info
            db.whereEqualTo("userID", auth.currentUser!!.uid).get()
                .addOnSuccessListener { documents ->
                    lateinit var userData: HashMap<String, Any>
                    for (document in documents) {
                        val documentReference = document.reference
                        documentReference.update("joiningReason", joiningReason)
                    }
                }
            goToStage4()
        }
    }

    private fun goToStage4() {
        val goToStage4 = Stage3RegisterFragmentDirections.actionSignUpStage3FragmentToSignUpStage4Fragment()
        findNavController().navigate(goToStage4)
    }

}