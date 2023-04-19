package com.facultate.myapplication.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.facultate.myapplication.databinding.ActivityRegisterStage3Binding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class Stage3RegisterActivity : AppCompatActivity() {

    private var joiningReason = "undecided"

    private lateinit var binding: ActivityRegisterStage3Binding

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterStage3Binding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onStart() {
        super.onStart()
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        setClickListeners()

    }

    private fun setClickListeners() {
        val buttonGoStage4 = binding.buttonGoToStep4
        val buttonDiscover = binding.buttonChoiceDiscover
        val buttonMonthly = binding.buttonChoiceEasierMonthly

        buttonDiscover.setOnClickListener {
            joiningReason = "discover"
        }
        buttonMonthly.setOnClickListener {
            joiningReason = "monthly"
        }

        buttonGoStage4.setOnClickListener {
//            update info
            db.collection("Users")
                .whereEqualTo("userID", auth.currentUser!!.uid)
                .get()
                .addOnSuccessListener { documents ->
                    lateinit var userData: HashMap<String, Any>
                    for (document in documents) {
                        val documentReference = document.reference
                        documentReference.update("joiningReason", joiningReason)
                    }
                }
            goToStage4()
        }
    }

    private fun goToStage4() {
        val goToStage4 = Intent(this, Stage4RegisterActivity::class.java)
        startActivity(goToStage4)
    }

}