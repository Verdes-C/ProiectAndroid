package com.facultate.myapplication.product

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.facultate.myapplication.MainActivityViewModel
import com.facultate.myapplication.R
import com.facultate.myapplication.databinding.FragmentProductBinding
import com.facultate.myapplication.hilt.UsersDB
import com.facultate.myapplication.model.domain.UIProduct
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ProductFragment : Fragment(R.layout.fragment_product) {

    @Inject
    lateinit var auth: FirebaseAuth

    @Inject
    @UsersDB
    lateinit var usersDB: CollectionReference
    private val viewModel: MainActivityViewModel by viewModels()
    private lateinit var binding: FragmentProductBinding
    private lateinit var reviewsRecyclerView: RecyclerView
    private lateinit var productData: UIProduct

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentProductBinding.bind(view)
        reviewsRecyclerView = binding.recyclerViewReviews
        productData = arguments?.getParcelable<UIProduct>("product_data")!!
        updateUiData(productData,view)

    }

    override fun onStart() {
        super.onStart()
        setClickListeners()
    }

    private fun updateUiData(productData: UIProduct?, view: View) {
        Glide.with(requireView())
            .load(productData?.product?.image)
            .centerCrop()
            .into(binding.imageViewProductImage)
        binding.textViewProductDescription.text = productData?.product?.description
        binding.textViewProductName.text = productData?.product?.title
        binding.textViewProductPrice.text =   String.format("%.2f",productData?.product?.price)
        setWishlistIconColor()
        setRecyclersView(view)
    }

    private fun setWishlistIconColor() {
        if (productData.isFavorite == true) {
            binding.imageViewAddToWishlist.apply {
                setImageResource(R.drawable.wishlist_item)
                setColorFilter(Color.RED)
            }
        } else {
            binding.imageViewAddToWishlist.apply {
                setImageResource(R.drawable.wishlist)
                setColorFilter(Color.BLACK)
            }
        }
    }

    private fun setClickListeners() {
        binding.imageViewAddToWishlist.setOnClickListener {
            toggleWishList()
        }
        binding.buttonAddToCart.setOnClickListener{
            CoroutineScope(Dispatchers.Main).launch {
                viewModel.store.update { applicationState ->
                    val userDataCopy = applicationState.userData
                    if(productData.product.id.toString() in userDataCopy.cartItems){
                        userDataCopy.cartItems[productData.product.id.toString()] = userDataCopy.cartItems[productData.product.id.toString()]!!.toInt() + 1
                    }else{
                        userDataCopy.cartItems[productData.product.id.toString()] = 1
                    }
                    usersDB.whereEqualTo("userID",auth.currentUser!!.uid)
                        .get()
                        .addOnSuccessListener { resuls->
                            resuls.documents[0].reference.update("cartItems",userDataCopy.cartItems)
                        }
                    return@update applicationState.copy(
                        userData = userDataCopy
                    )
                }
            }
        }

        binding.imageViewShare.setOnClickListener {
            val text = """
                ${productData.product.title}
                ${productData.product.description} for just ${productData.product.price}
                Grab it while it lasts. Offer is valid only within supply limits. Hurry up!
            """.trimIndent()
            val sendIntent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, text)
                type = "text/plain"
            }

            val shareIntent = Intent.createChooser(sendIntent, null)
            startActivity(shareIntent)
        }
    }

    private fun toggleWishList() {
        CoroutineScope(Dispatchers.Main).launch {
            viewModel.store.update { applicationState ->
                val userDataCopy = applicationState.userData
                if (productData.product.id.toString() in userDataCopy.wishlistedProducts) {
                    userDataCopy.wishlistedProducts.remove(productData.product.id.toString())
                } else {
                    userDataCopy.wishlistedProducts.add(productData.product.id.toString())
                }
                productData.isFavorite = !productData.isFavorite

                usersDB.whereEqualTo("userID", auth.currentUser!!.uid)
                    .get()
                    .addOnSuccessListener { result ->
                        result.documents[0].reference.update(
                            "wishlistedProducts",
                            userDataCopy.wishlistedProducts
                        )
                    }
                return@update applicationState.copy(
                    userData = userDataCopy
                )
            }
            setWishlistIconColor()
            coroutineContext.cancel()
        }
    }

    private fun setRecyclersView(view: View) {
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        reviewsRecyclerView = view.findViewById(R.id.recycler_view_reviews)
        reviewsRecyclerView.layoutManager = layoutManager
        val reviewsAdapter = ReviewsAdapter()
        reviewsRecyclerView.adapter = reviewsAdapter
    }
}