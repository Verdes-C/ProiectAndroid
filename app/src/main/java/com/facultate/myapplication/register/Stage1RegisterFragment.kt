package com.facultate.myapplication.register

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.facultate.myapplication.R
import com.facultate.myapplication.databinding.FragmentRegisterStage1Binding
import com.facultate.myapplication.hilt.UsersDB
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class Stage1RegisterFragment : Fragment(R.layout.fragment_register_stage_1) {

    private lateinit var binding: FragmentRegisterStage1Binding

    @Inject
    lateinit var auth: FirebaseAuth

    @Inject
    @UsersDB
     lateinit var usersDB: CollectionReference
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentRegisterStage1Binding.bind(view)
    }

    override fun onStart() {
        super.onStart()


        setClickListeners()
    }

    private fun setClickListeners() {
        val buttonToStage2 = binding.buttonGoToStage2

        buttonToStage2.setOnClickListener {
            val fullName = binding.textFieldName.editText?.text.toString()
            val email = binding.textFieldEmail.editText?.text.toString()
            val password = binding.textFieldPassword.editText?.text.toString()

            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(requireActivity()) { task ->
                    if (task.isSuccessful) {
                        saveUserData(fullName, email)
                    } else {
                        displayReasonToFail(task)
                    }
                }


        }
    }

    private fun displayReasonToFail(task: Task<AuthResult>) {
        binding.textViewSignUpError.text = task.exception.toString()
    }

    private fun saveUserData(fullName: String, email: String) {
        usersDB.whereEqualTo("userID", auth.currentUser!!.uid).get()
            .addOnSuccessListener { documents ->
                if (documents.isEmpty) {
                    val userData = hashMapOf(
                        "userID" to auth.currentUser!!.uid,
                        "name" to fullName,
                        "email" to email,
                    )

                    usersDB.document(auth.currentUser!!.uid)
                        .set(userData)
                        .addOnSuccessListener { documentReference ->
                            goToStage2()
                        }.addOnFailureListener { exception ->
                            binding.textViewSignUpError.text = exception.message
                        }
                } else {
//                   TODO Returning users?
                    usersDB.whereEqualTo("userID", auth.currentUser!!.uid).get()
                        .addOnSuccessListener { documents ->
                            for (document in documents) {
                                val documentReference = document.reference
                                documentReference.update("email", email)
                                documentReference.update("name", fullName)
                            }
                        }
                }
            }
    }

    private fun goToStage2() {
        val goToStage2 = Stage1RegisterFragmentDirections.actionSignUpStage1FragmentToSignUpStage2Fragment()
        findNavController().navigate(goToStage2)
    }


}