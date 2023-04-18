package com.facultate.myapplication.profile

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.facultate.myapplication.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private lateinit var listener: ProfileFragmentInterface

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is ProfileFragmentInterface) {
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
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setClickListeners()
    }

    private fun setClickListeners() {

        binding.imageViewUserEdit.setOnClickListener {
            listener.goToEditProfileFragment()
        }
    }

    fun handleBackPressed() {
        listener.goHome()
    }

    interface ProfileFragmentInterface{
        fun goToEditProfileFragment()
        fun goHome()
    }
}