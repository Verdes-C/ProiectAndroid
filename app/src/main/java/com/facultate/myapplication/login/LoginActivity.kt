package com.facultate.myapplication.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.facultate.myapplication.MainActivity
import com.facultate.myapplication.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onStart() {
        super.onStart()

        auth = FirebaseAuth.getInstance()
        setClickListeners()

    }

    private fun setClickListeners() {
        val login = binding.buttonLogin
        val loginFailed = binding.textViewLoginFailed

        login.setOnClickListener {
            val email: String = binding.textFieldEmail.editText?.text.toString()
            val password = binding.textFieldPassword.editText?.text.toString()

            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        val goToHome = Intent(this,MainActivity::class.java)
                        startActivity(goToHome)
                    } else {
                        loginFailed.text = email
                    }
                }


        }
    }





}