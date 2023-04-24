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
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.facultate.myapplication.R
import com.facultate.myapplication.model.domain.Product

class ProductCardAdapter(
    private val productsList: ArrayList<Product>,
    ):RecyclerView.Adapter<ProductCardAdapter.MyViewHolder>() {

    override fun getItemCount(): Int {
        return productsList.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = productsList[position]

        Glide.with(holder.itemView.context)
            .load(currentItem.image)
            .centerCrop()
            .placeholder(R.drawable.placeholder_image)
            .into(holder.productImage);
        holder.productName.text = currentItem.title
        holder.productPrice.text = currentItem.price.toString()
        holder.productDescription.text = currentItem.description

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
        currentItem: Product
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
                    if (currentItem.isFavorite) {
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

                    currentItem.isFavorite = !currentItem.isFavorite
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