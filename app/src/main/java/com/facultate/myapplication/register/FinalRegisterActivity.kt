package com.facultate.myapplication.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.facultate.myapplication.MainActivity
import com.facultate.myapplication.databinding.ActivityRegisterFinalBinding

class FinalRegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterFinalBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterFinalBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onStart() {
        super.onStart()

        setClickListeners()
    }

    private fun setClickListeners() {
        val finishRegistering = binding.buttonRegisterEnd

        finishRegistering.setOnClickListener {
//
            val goHome = Intent(this, MainActivity::class.java)
            startActivity(goHome)
        }
    }


}