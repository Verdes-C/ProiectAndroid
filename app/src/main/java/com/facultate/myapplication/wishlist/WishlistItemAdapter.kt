package com.facultate.myapplication.wishlist

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.facultate.myapplication.R
import com.facultate.myapplication.model.domain.UIProduct
import com.facultate.myapplication.redux.ApplicationState
import com.facultate.myapplication.redux.Store
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class WishlistItemAdapter(
    private val wishlistItemsList: ArrayList<UIProduct>,
    private val store: Store<ApplicationState>,
    private val navController: NavController,
) : RecyclerView.Adapter<WishlistItemAdapter.MyViewHolder>() {

    override fun getItemCount(): Int {
        return wishlistItemsList.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: WishlistItemAdapter.MyViewHolder, position: Int) {
        val currentItem = wishlistItemsList[position]

        bind(holder, currentItem, position)

    }

    private fun bind(
        holder: MyViewHolder,
        currentItem: UIProduct,
        position: Int
    ) {
        Glide.with(holder.itemView.context)
            .load(currentItem.product.image)
            .centerCrop()
            .placeholder(R.drawable.placeholder_image)
            .into(holder.wishlistItemImage);
        holder.wishlistItemName.text = currentItem.product.title
        holder.wishlistItemPrice.text = "$ ${currentItem.product.price}"
        holder.wishlistItemWishlitedIcon.setImageResource(R.drawable.wishlist_item)
        holder.wishlistItemWishlitedIcon.setColorFilter(Color.RED)
        holder.wishlistItemWishlitedIcon.setOnClickListener {
            animate(holder, currentItem, position)
        }
        holder.wishlistItemAddToCard.setOnClickListener {
            addToCart(currentItem)
        }
        holder.wishlistItemImage.setOnClickListener(sendToProductPage(currentItem))
        holder.wishlistItemName.setOnClickListener(sendToProductPage(currentItem))
    }

    private fun addToCart(currentItem: UIProduct) {
        CoroutineScope(Dispatchers.Main).launch {
            store.update { applicationState ->
                val userDataCopy = applicationState.userData
                if (userDataCopy.cartItems.any { it.key == currentItem.product.id.toString() }) {
                    userDataCopy.cartItems[currentItem.product.id.toString()] =
                        userDataCopy.cartItems[currentItem.product.id.toString()]!! + 1
                }else{
                    userDataCopy.cartItems[currentItem.product.id.toString()] = 1
                }
                FirebaseFirestore.getInstance().collection("Users")
                    .whereEqualTo("userID", FirebaseAuth.getInstance().currentUser!!.uid).get()
                    .addOnSuccessListener { results ->
                        results.documents[0].reference.update("cartItems", userDataCopy.cartItems)
                    }
                return@update applicationState.copy(
                    userData = userDataCopy
                )
            }
            coroutineContext.cancel()
        }
    }


    private fun removeItem(currentItem: UIProduct, position: Int) {
        CoroutineScope(Dispatchers.Main).launch {
            store.update { applicationState ->
                val userDataCopy = applicationState.userData
                userDataCopy.wishlistedProducts.remove(currentItem.product.id.toString())
                FirebaseFirestore.getInstance()
                    .collection("Users")
                    .whereEqualTo("userID", FirebaseAuth.getInstance().currentUser!!.uid)
                    .get()
                    .addOnSuccessListener { result ->
                        val wishlistItemResult = result.documents[0].reference
                        wishlistItemResult.update(
                            "wishlistedProducts",
                            userDataCopy.wishlistedProducts
                        )
                    }
                return@update applicationState.copy(
                    userData = userDataCopy
                )
            }
        }
        wishlistItemsList.remove(currentItem)
        notifyItemRangeChanged(position, wishlistItemsList.size - position + 1)
    }


    private fun animate(holder: MyViewHolder, currentItem: UIProduct, position: Int) {
        val currentPosition = holder.adapterPosition
        if (currentPosition == RecyclerView.NO_POSITION) {
            return
        }
        val animationDuration = 300.toLong()
        val shrinkAnimation = ObjectAnimator.ofPropertyValuesHolder(
            holder.wishlistItemWishlitedIcon,
            PropertyValuesHolder.ofFloat(View.SCALE_X, 1f, 0f),
            PropertyValuesHolder.ofFloat(View.SCALE_Y, 1f, 0f)
        ).apply {
            duration = animationDuration
            interpolator = AccelerateInterpolator()
            addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    removeItem(currentItem, position)
                }
            })
        }
        shrinkAnimation.start()
    }

    fun sendToProductPage(product:UIProduct):View.OnClickListener {
        val actionGoToProductPage = WishlistFragmentDirections.actionWishlistFragmentToProductFragment(product)
        return View.OnClickListener { view->
            navController.navigate(actionGoToProductPage)
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): WishlistItemAdapter.MyViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_wishlist_item, parent, false)
        return MyViewHolder(itemView)
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val wishlistItemImage =
            itemView.findViewById<ImageView>(R.id.image_view_wishlist_item_image)
        val wishlistItemName =
            itemView.findViewById<TextView>(R.id.text_view_wishlist_item_product_name)
        val wishlistItemPrice = itemView.findViewById<TextView>(R.id.image_view_wishlist_item_price)
        val wishlistItemWishlitedIcon =
            itemView.findViewById<ImageView>(R.id.image_view_wishlist_item_wishlited)
        val wishlistItemAddToCard = itemView.findViewById<Button>(R.id.buttonAddItemToCart)
    }
}