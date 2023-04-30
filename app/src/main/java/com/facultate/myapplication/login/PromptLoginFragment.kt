package com.facultate.myapplication.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.facultate.myapplication.NavGraphDirections
import com.facultate.myapplication.NavGraphMasterDirections
import com.facultate.myapplication.R
import com.facultate.myapplication.databinding.FragmentLoginPromptBinding
import com.facultate.myapplication.register.Stage1RegisterFragment
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class PromptLoginFragment : Fragment(R.layout.fragment_login_prompt) {

    @Inject lateinit var auth:FirebaseAuth
    private lateinit var binding: FragmentLoginPromptBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentLoginPromptBinding.bind(view)
    }

    override fun onStart() {
        super.onStart()
        setClickListeners()
    }

    private fun setClickListeners() {
        val registerButton =  binding.buttonRegisterStart
        val loginButton = binding.buttonLoginStart

        registerButton.setOnClickListener {
            val goToStage1 = PromptLoginFragmentDirections.actionPromptLoginToStage1()
            findNavController().navigate(goToStage1)
        }

        loginButton.setOnClickListener {
            val goToLoginPage = PromptLoginFragmentDirections.actionMasterStartToLoginFragment()
            findNavController().navigate(goToLoginPage)
        }
    }
}