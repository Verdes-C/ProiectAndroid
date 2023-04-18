package com.facultate.myapplication.profile

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.facultate.myapplication.databinding.FragmentProfileEditBinding

class EditProfileFragment: Fragment() {

    private lateinit var binding: FragmentProfileEditBinding
    private lateinit var listener: ProfileEditFragmentInterface

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is ProfileEditFragmentInterface) {
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
        binding = FragmentProfileEditBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setClickListeners()

    }

    private fun setClickListeners() {
        binding.buttonUserEditUpdate.setOnClickListener {
//            code to be put here
            listener.saveUserData()
        }
    }

    fun handleBackPressed() {
        listener.saveUserData()
    }


    interface ProfileEditFragmentInterface{
        fun saveUserData()
    }
}