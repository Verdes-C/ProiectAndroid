package com.facultate.myapplication.home

import android.animation.*
import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.view.animation.OvershootInterpolator
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.facultate.myapplication.R
import com.facultate.myapplication.model.domain.UIProduct
import com.facultate.myapplication.model.domain.UserData
import com.facultate.myapplication.redux.ApplicationState
import com.facultate.myapplication.redux.Store
import com.facultate.myapplication.wishlist.WishlistFragmentDirections
import com.google.android.material.card.MaterialCardView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.*

class ProductCardAdapter(
    private val productsList: ArrayList<UIProduct>,
    private val store: Store<ApplicationState>,
    private val navController: NavController
) : RecyclerView.Adapter<ProductCardAdapter.MyViewHolder>() {

    override fun getItemCount(): Int {
        return productsList.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = productsList[position]

        bind(holder, currentItem, position)
    }

    @SuppressLint("SetTextI18n")
    private fun bind(
        holder: MyViewHolder,
        currentItem: UIProduct,
        position: Int
    ) {
        Glide.with(holder.itemView.context)
            .load(currentItem.product.image)
            .centerCrop()
            .placeholder(R.drawable.placeholder_image)
            .into(holder.productImage);
        holder.productName.text = currentItem.product.title
        holder.productPrice.text = "$ ${currentItem.product.price}"
        holder.productDescription.text = currentItem.product.description

        if(currentItem.isFavorite){
            holder.productWishlistImage.setImageResource(R.drawable.wishlist_item)
            holder.productWishlistImage.setColorFilter(Color.RED)
        }
        holder.productWishlistImage.setOnClickListener {
            animateHeartOnClick(holder, currentItem)
            toggleFavorite(currentItem, position)
        }
        holder.cardProduct.setOnClickListener(sendToProductPage(currentItem))
    }

    private fun animateHeartOnClick(
        holder: MyViewHolder,
        currentItem: UIProduct
    ) {
        val animationDuration = 300.toLong()
        val growAnimation = ObjectAnimator.ofPropertyValuesHolder(
            holder.productWishlistImage,
            PropertyValuesHolder.ofFloat(View.SCALE_X, 1f, 1.5f),
            PropertyValuesHolder.ofFloat(View.SCALE_Y, 1f, 1.5f)
        ).apply {
            duration = animationDuration
            interpolator = OvershootInterpolator()
            addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    if (!currentItem.isFavorite) {
                        holder.productWishlistImage.setImageResource(R.drawable.wishlist)
                        holder.productWishlistImage.clearColorFilter()
                    } else {
                        holder.productWishlistImage.setImageResource(R.drawable.wishlist_item)
                        holder.productWishlistImage.setColorFilter(Color.RED)
                    }
//                    Go back to the original state
                    holder.productWishlistImage.animate().apply {
                        scaleX(1f)
                        scaleY(1f)
                        duration = animationDuration
                        interpolator = AccelerateInterpolator()
                        start()
                    }
                }
            })

        }
        growAnimation.start()
    }

    private fun toggleFavorite(currentItem: UIProduct, position: Int) {
        FirebaseFirestore.getInstance()
            .collection("Users")
            .whereEqualTo("userID", FirebaseAuth.getInstance().currentUser!!.uid)
            .get()
            .addOnSuccessListener { results ->
                val wishList = results.documents[0].reference
                var userDataCopy: UserData
                CoroutineScope(Dispatchers.Main).launch {
                    store.update { applicationState ->
                        userDataCopy = applicationState.userData
                        if (currentItem.product.id.toString() in userDataCopy.wishlistedProducts) {
                            userDataCopy.wishlistedProducts.remove(currentItem.product.id.toString())
                            currentItem.isFavorite = false
                        } else {
                            userDataCopy.wishlistedProducts.add(currentItem.product.id.toString())
                            currentItem.isFavorite = true
                        }
                        wishList.update("wishlistedProducts",userDataCopy.wishlistedProducts)
                        return@update applicationState.copy(
                            userData = userDataCopy
                        )
                    }
                    coroutineContext.cancel()
                }

            }
    }

    fun sendToProductPage(product:UIProduct):View.OnClickListener {
        val actionGoToProductPage = HomeFragmentDirections.actionHomeFragmentToProductFragment(product)
        return View.OnClickListener { view->
            navController.navigate(actionGoToProductPage)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_products, parent, false)
        return MyViewHolder(itemView)
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val productImage = itemView.findViewById<ImageView>(R.id.card_view_product_image)
        val productName = itemView.findViewById<TextView>(R.id.text_view_product_name)
        val productPrice = itemView.findViewById<TextView>(R.id.text_view_product_price)
        val productDescription = itemView.findViewById<TextView>(R.id.text_view_product_description)
        val productWishlistImage = itemView.findViewById<ImageView>(R.id.image_view_wishlist)
        val cardProduct = itemView.findViewById<MaterialCardView>(R.id.cardView_product)
    }
}