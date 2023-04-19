package com.facultate.myapplication.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.facultate.myapplication.databinding.ActivityRegisterStage4Binding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class Stage4RegisterActivity : AppCompatActivity() {

    private var interests = arrayListOf<String>()

    private lateinit var binding: ActivityRegisterStage4Binding

    private lateinit var auth: FirebaseAuth
    private lateinit var db:FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterStage4Binding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onStart() {
        super.onStart()

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
        setClickListeners()
    }

    private fun setClickListeners() {
        val goToFinal = binding.buttonGoToStepFinal
        val optionGaming = binding.buttonAddOptionGaming
        val optionReading = binding.buttonAddOptionReading
        val optionFashion = binding.buttonAddOptionFashion
        val optionMusic = binding.buttonAddOptionMusic

        optionGaming.setOnClickListener {
            optionGaming.text = "√"
            if (!interests.contains("gaming")){
                interests.add("gaming")
            }
        }

        optionReading.setOnClickListener {
            optionReading.text = "√"
            if (!interests.contains("reading")){
                interests.add("reading")
            }
        }

        optionFashion.setOnClickListener {
            optionFashion.text = "√"
            if (!interests.contains("fashion")){
                interests.add("fashion")
            }
        }

        optionMusic.setOnClickListener {
            optionMusic.text = "√"
            if (!interests.contains("music")){
                interests.add("music")
            }
        }

        goToFinal.setOnClickListener {
//
            db.collection("Users")
                .whereEqualTo("userID",auth.currentUser!!.uid)
                .get()
                .addOnSuccessListener { documents->
                    for (document in documents){
                        val docRef = document.reference
                        docRef.update("interests",interests)
                    }
                }
            goToFinal()
        }
    }

    private fun goToFinal(){
        val goToFinal = Intent(this,FinalRegisterActivity::class.java)
        startActivity(goToFinal)
    }


}