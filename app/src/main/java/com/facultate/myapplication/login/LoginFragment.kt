package com.facultate.myapplication.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.facultate.myapplication.MainActivity
import com.facultate.myapplication.MainActivityViewModel
import com.facultate.myapplication.NavGraphMasterDirections
import com.facultate.myapplication.R
import com.facultate.myapplication.databinding.FragmentLoginBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LoginFragment : Fragment(R.layout.fragment_login) {

    private val viewModel: MainActivityViewModel by viewModels()
    private lateinit var binding: FragmentLoginBinding

    @Inject
    lateinit var auth: FirebaseAuth
    private lateinit var client: GoogleSignInClient

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentLoginBinding.bind(view)
    }

    override fun onStart() {
        super.onStart()
        setClickListeners()
    }

    private fun setClickListeners() {
        val login = binding.buttonLogin
        val loginUsingGoogle = binding.buttonLoginGoogle
        val loginFailed = binding.textViewLoginFailed

        login.setOnClickListener {
            val email: String = binding.textFieldEmail.editText?.text.toString()
            val password = binding.textFieldPassword.editText?.text.toString()

            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(requireActivity()) { task ->
                    if (task.isSuccessful) {
                        goToMainActivity()
                    } else {
                        loginFailed.text = task.exception?.message
                    }
                }
        }

        loginUsingGoogle.setOnClickListener {
            val options = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()
            client = GoogleSignIn.getClient(requireActivity(), options)
            val intent = client.signInIntent
            startActivityForResult(intent, 10001)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 10001) {
            completeGoogleSignIn(data)
        }
    }

    private fun completeGoogleSignIn(data: Intent?) {
        val task = GoogleSignIn.getSignedInAccountFromIntent(data)
        val account = task.getResult(ApiException::class.java)
        val name = account.displayName
        val email = account.email
        val profileImageURL = account.photoUrl
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener {
                if (task.isSuccessful) {
                    val userData = hashMapOf(
                        "userID" to auth.currentUser!!.uid,
                        "name" to name,
                        "email" to email,
                        "profileImageURL" to profileImageURL
                    )
                    FirebaseFirestore.getInstance()
                        .collection("Users")
                        .document(auth.currentUser!!.uid)
                        .set(userData)
                        .addOnSuccessListener {
                            goToMainActivity()
                        }
                } else {
                    Toast.makeText(requireActivity(), task.exception?.message, Toast.LENGTH_SHORT)
                        .show()
                }
            }
    }

    fun goToMainActivity(){
        val actionToHome = NavGraphMasterDirections.actionGlobalToNavGraph()
        findNavController().navigate(actionToHome)
    }
}