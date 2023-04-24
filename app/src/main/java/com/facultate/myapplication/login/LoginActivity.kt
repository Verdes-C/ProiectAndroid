package com.facultate.myapplication.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.facultate.myapplication.MainActivity
import com.facultate.myapplication.R
import com.facultate.myapplication.databinding.ActivityLoginBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var client: GoogleSignInClient
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onStart() {
        super.onStart()

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
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
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        val goToHome = Intent(this, MainActivity::class.java)
                        startActivity(goToHome)
                    } else {
                        loginFailed.text = email
                    }
                }
        }

        loginUsingGoogle.setOnClickListener {
            val options = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()
            client = GoogleSignIn.getClient(this@LoginActivity,options)
            val intent = client.signInIntent
            startActivityForResult(intent, 10001)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==10001){
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            val account = task.getResult(ApiException::class.java)
            val name = account.displayName
            val email = account.email
            val credential = GoogleAuthProvider.getCredential(account.idToken,null)
            FirebaseAuth.getInstance().signInWithCredential(credential)
                .addOnCompleteListener{task->
                    if(task.isSuccessful){
                        var userData = hashMapOf(
                            "userID" to auth.currentUser!!.uid,
                            "name" to name,
                            "email" to email,
                        )
                        db.collection("Users")
                            .add(userData)
                            .addOnSuccessListener {
                                val i  = Intent(this,MainActivity::class.java)
                                startActivity(i)
                            }
                    }else{
                        Toast.makeText(this,task.exception?.message,Toast.LENGTH_SHORT).show()
                    }

                }
        }
    }


}