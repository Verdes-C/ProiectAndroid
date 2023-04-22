package com.facultate.myapplication

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import com.facultate.myapplication.cart.CartFragment
import com.facultate.myapplication.databinding.ActivityMainBinding
import com.facultate.myapplication.home.HomeFragment
import com.facultate.myapplication.login.PromptLoginActivity
import com.facultate.myapplication.profile.EditProfileFragment
import com.facultate.myapplication.profile.ProfileFragment
import com.facultate.myapplication.wishlist.WishlistFragment
import com.google.android.material.navigation.NavigationBarView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity(),
    NavigationBarView.OnItemSelectedListener,
    ProfileFragment.ProfileFragmentInterface,
    EditProfileFragment.ProfileEditFragmentInterface,
    CartFragment.CartFragmentInterface,
    WishlistFragment.WishlistFragmentInterface {



    private lateinit var binding: ActivityMainBinding

    private lateinit var auth: FirebaseAuth
    private lateinit var db:FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }

    override fun onStart() {
        super.onStart()

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
        val currentUser = auth.currentUser
        if(currentUser != null){
            goHome()

        }else{
            val goToLoginPrompt = Intent(this,PromptLoginActivity::class.java)
            startActivity(goToLoginPrompt)
        }
        setClickListeners()
    }

    private fun setClickListeners() {
        binding.bottomNav.setOnItemSelectedListener(this)
    }

    override fun onNavigationItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.bottom_nav_button_home -> onHomeClicked()
        R.id.bottom_nav_button_wishlist -> onWishlistClicked()
        R.id.bottom_nav_button_profile -> onProfileClicked()
        R.id.bottom_nav_button_cart -> onCartClicked()
        else -> false
    }

    private fun onHomeClicked(): Boolean {
        goHome()
        return true
    }

    private fun onWishlistClicked(): Boolean {
        goToWishlist()
        return true
    }

    private fun onProfileClicked(): Boolean {
        goToProfile()
        return true
    }

    private fun onCartClicked(): Boolean {
        goToCart()
        return true
    }

    override fun goToEditProfileFragment() {
        val editProfileFragment = EditProfileFragment()
        supportFragmentManager.commit {
            setCustomAnimations(
                androidx.appcompat.R.anim.abc_fade_in,
                androidx.appcompat.R.anim.abc_fade_out
            )
            addToBackStack(null)
            setPrimaryNavigationFragment(editProfileFragment)
            replace(R.id.frame_content, editProfileFragment)
        }
    }

    override fun goHome() {
        val homeFragment = HomeFragment()
        supportFragmentManager.commit {
            setCustomAnimations(
                androidx.appcompat.R.anim.abc_fade_in,
                androidx.appcompat.R.anim.abc_fade_out
            )
            setPrimaryNavigationFragment(homeFragment)
            replace(R.id.frame_content, homeFragment)
        }
        binding.bottomNav.menu.findItem(R.id.bottom_nav_button_home)?.isChecked = true
    }

    private fun goToWishlist() {
        val wishList = WishlistFragment()
        supportFragmentManager.commit {
            setCustomAnimations(
                androidx.appcompat.R.anim.abc_fade_in,
                androidx.appcompat.R.anim.abc_fade_out
            )
            setPrimaryNavigationFragment(wishList)
            replace(R.id.frame_content, wishList)
        }
    }

    private fun goToProfile() {
        val profileFragment = ProfileFragment()
        supportFragmentManager.commit {
            setCustomAnimations(
                androidx.appcompat.R.anim.abc_fade_in,
                androidx.appcompat.R.anim.abc_fade_out
            )
            setPrimaryNavigationFragment(profileFragment)
            replace(R.id.frame_content, profileFragment)
        }
    }

    private fun goToCart() {
        val cartFragment = CartFragment()
        supportFragmentManager.commit {
            setCustomAnimations(
                androidx.appcompat.R.anim.abc_fade_in,
                androidx.appcompat.R.anim.abc_fade_out
            )
            setPrimaryNavigationFragment(cartFragment)
            replace(R.id.frame_content, cartFragment)
        }
    }

    override fun saveUserData(userData: HashMap<String, Any>) {
//        save user data
        db.collection("Users")
            .whereEqualTo("userID",auth.currentUser!!.uid)
            .get()
            .addOnSuccessListener { documents->
                for (document in documents){
                    val docRef = document.reference
                    docRef.update("name",userData["name"])
                    docRef.update("email",userData["email"])
                    docRef.update("phone",userData["phone"])
                    docRef.update("address",userData["address"])
                }
            }
        goToProfile()
    }

    override fun onBackPressed() {
        val fragment = supportFragmentManager.findFragmentById(R.id.frame_content)
        if (fragment is WishlistFragment) {
            fragment.handleBackPressed()
        } else if (fragment is ProfileFragment) {
            fragment.handleBackPressed()
        } else if (fragment is EditProfileFragment) {
            fragment.handleBackPressed()
        } else if (fragment is CartFragment) {
            fragment.handleBackPressed()
        } else {
            super.onBackPressed()
        }
    }


}

