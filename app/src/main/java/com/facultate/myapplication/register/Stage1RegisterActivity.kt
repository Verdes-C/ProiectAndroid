package com.facultate.myapplication.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.facultate.myapplication.databinding.ActivityRegisterStage1Binding
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class Stage1RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterStage1Binding
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterStage1Binding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onStart() {
        super.onStart()
        auth = FirebaseAuth.getInstance()
        db = Firebase.firestore

        setClickListeners()
    }

    private fun setClickListeners() {
        val buttonToStage2 = binding.buttonGoToStage2

        buttonToStage2.setOnClickListener {
            val fullName = binding.textFieldName.editText?.text.toString()
            val email = binding.textFieldEmail.editText?.text.toString()
            val password = binding.textFieldPassword.editText?.text.toString()

            auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this) { task->
                if (task.isSuccessful){
                    saveUserData(fullName, email)
                }
                else{
                    displayReasonToFail(task)
                }
            }


        }
    }

    private fun displayReasonToFail(task: Task<AuthResult>) {
        binding.textViewSignUpError.text = task.exception.toString()
    }

    private fun saveUserData(fullName: String, email: String) {
        db.collection("Users")
            .whereEqualTo("userID",auth.currentUser!!.uid)
            .get()
            .addOnSuccessListener { documents ->
                if (documents.isEmpty){
                    val userData = hashMapOf(
                        "userID" to auth.currentUser!!.uid,
                        "name" to fullName,
                        "email" to email,
                    )

                    db.collection("Users")
                        .add(userData)
                        .addOnSuccessListener { documentReference ->
                            goToStage2()
                        }
                        .addOnFailureListener {exception ->
                            binding.textViewSignUpError.text = exception.message
                        }
                }else{
//                   TODO Returning users?
                    db.collection("Users")
                        .whereEqualTo("userID",auth.currentUser!!.uid)
                        .get()
                        .addOnSuccessListener { documents ->
                            var userData = hashMapOf<String,Any>()
                            for (document in documents){
                                val documentReference = document.reference
                                documentReference.update("email",email)
                                documentReference.update("name",fullName)
                            }
                        }
                }
            }
    }

    private fun goToStage2(){
        val goToStage2 = Intent(this,Stage2RegisterActivity::class.java)
        startActivity(goToStage2)
    }


}