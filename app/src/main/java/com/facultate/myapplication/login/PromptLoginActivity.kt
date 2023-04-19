package com.facultate.myapplication.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.facultate.myapplication.databinding.ActivityLoginPromptBinding
import com.facultate.myapplication.register.Stage1RegisterActivity

class PromptLoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginPromptBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginPromptBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onStart() {
        super.onStart()

        setClickListeners()

    }

    private fun setClickListeners() {
        val registerButton =  binding.buttonRegisterStart
        val loginButton = binding.buttonLoginStart

        registerButton.setOnClickListener {
            val goToStage1 = Intent(this,Stage1RegisterActivity::class.java)
            startActivity(goToStage1)
        }

        loginButton.setOnClickListener {
            val goToLoginPage = Intent(this,LoginActivity::class.java)
            startActivity(goToLoginPage)
        }
    }
}