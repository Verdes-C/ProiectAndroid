package com.facultate.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.fragment.app.commit
import com.facultate.myapplication.cart.CartFragment
import com.facultate.myapplication.databinding.ActivityMainBinding
import com.facultate.myapplication.home.HomeFragment
import com.facultate.myapplication.profile.EditProfileFragment
import com.facultate.myapplication.profile.ProfileFragment
import com.facultate.myapplication.wishlist.WishlistFragment
import com.google.android.material.navigation.NavigationBarView

class MainActivity : AppCompatActivity(),
    NavigationBarView.OnItemSelectedListener,
    ProfileFragment.ProfileFragmentInterface,
    EditProfileFragment.ProfileEditFragmentInterface,
    CartFragment.CartFragmentInterface,
    WishlistFragment.WishlistFragmentInterface {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        goHome()
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

    override fun saveUserData() {
//        save user data
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

