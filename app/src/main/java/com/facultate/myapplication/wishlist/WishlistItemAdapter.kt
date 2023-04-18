package com.facultate.myapplication.wishlist

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.facultate.myapplication.R

class WishlistItemAdapter(private val wishlistItemsList:ArrayList<WishlistFragment.WishListItem>):RecyclerView.Adapter<WishlistItemAdapter.MyViewHolder>() {

    override fun getItemCount(): Int {
        return wishlistItemsList.size
    }

    override fun onBindViewHolder(holder: WishlistItemAdapter.MyViewHolder, position: Int) {
        val currentItem = wishlistItemsList[position]

        holder.wishlistItemImage.setImageResource(R.drawable.placeholder_image)
        holder.wishlistItemName.text = currentItem.wishlistItemName
        holder.wishlistItemPrice.text = currentItem.wishlistItemPrice

       holder.wishlistItemWishlitedIcon.setOnClickListener {
           animate(holder)
       }

    }

    private fun animate(holder: MyViewHolder) {
        val currentPosition = holder.adapterPosition
        if(currentPosition == RecyclerView.NO_POSITION){
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
                    removeItem(currentPosition)
                }
            })
        }

        shrinkAnimation.start()
    }

    private fun removeItem(currentPosition: Int) {
        wishlistItemsList.removeAt(currentPosition)
        notifyItemRemoved(currentPosition)
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): WishlistItemAdapter.MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_wishlist_item,parent,false)
        return MyViewHolder(itemView)
    }

    class MyViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val wishlistItemImage = itemView.findViewById<ImageView>(R.id.image_view_wishlist_item_image)
        val wishlistItemName = itemView.findViewById<TextView>(R.id.text_view_wishlist_item_product_name)
        val wishlistItemPrice = itemView.findViewById<TextView>(R.id.image_view_wishlist_item_price)
        val wishlistItemWishlitedIcon = itemView.findViewById<ImageView>(R.id.image_view_wishlist_item_wishlited)
    }
}