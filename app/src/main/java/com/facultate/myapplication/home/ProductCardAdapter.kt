package com.facultate.myapplication.home

import android.animation.*
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.view.animation.OvershootInterpolator
import android.widget.ImageView
import android.widget.TextView
import androidx.core.animation.addListener
import androidx.core.animation.doOnCancel
import androidx.core.animation.doOnEnd
import androidx.recyclerview.widget.RecyclerView
import com.facultate.myapplication.R

class ProductCardAdapter(private val productsList:ArrayList<HomeFragment.Products>):RecyclerView.Adapter<ProductCardAdapter.MyViewHolder>() {

    override fun getItemCount(): Int {
        return productsList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = productsList[position]

        holder.productImage.setImageResource(R.drawable.placeholder_image)
        holder.productName.text = currentItem.productName
        holder.productPrice.text = currentItem.productPrice
        holder.productDescription.text = currentItem.productDescription

        holder.productWishlistImage.setOnClickListener {
            animateHeartOnClick(holder, currentItem)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_products,parent,false)
        return MyViewHolder(itemView)
    }

    private fun animateHeartOnClick(
        holder: MyViewHolder,
        currentItem: HomeFragment.Products
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
                    if (currentItem.productIsFavorite) {
                        holder.productWishlistImage.setImageResource(R.drawable.wishlist)
                        holder.productWishlistImage.clearColorFilter()
                    } else {
                        holder.productWishlistImage.setImageResource(R.drawable.wishlist_item)
                        holder.productWishlistImage.setColorFilter(Color.RED)
                    }
                    holder.productWishlistImage.animate().apply {
                        scaleX(1f)
                        scaleY(1f)
                        duration = animationDuration
                        interpolator = AccelerateInterpolator()
                        start()
                    }

                    currentItem.productIsFavorite = !currentItem.productIsFavorite
                }
            })

        }
        growAnimation.start()
    }

    class MyViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){

        val productImage = itemView.findViewById<ImageView>(R.id.card_view_product_image)
        val productName = itemView.findViewById<TextView>(R.id.text_view_product_name)
        val productPrice = itemView.findViewById<TextView>(R.id.text_view_product_price)
        val productDescription = itemView.findViewById<TextView>(R.id.text_view_product_description)
        val productWishlistImage = itemView.findViewById<ImageView>(R.id.image_view_wishlist)

    }
}