package com.facultate.myapplication.cart

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.facultate.myapplication.R

class CartListItemsAdapter(
    private var cartListItemsList: ArrayList<CartFragment.CartListItem>,
    private val rootView: View,
    private var listenerCartFragment: CartFragmentListener,
    private var isRemoving:Boolean = false
) : RecyclerView.Adapter<CartListItemsAdapter.MyViewHolder>() {


    override fun getItemCount(): Int {
        if (cartListItemsList.size > 0) {
            listenerCartFragment.sendTrigger(true)
        } else {
            listenerCartFragment.sendTrigger(false)
        }
        return cartListItemsList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = cartListItemsList[position]

        holder.cartListItemImage.setImageResource(R.drawable.placeholder_image)
        holder.cartListItemName.text = currentItem.productName
        holder.cartListItemPrice.text = String.format("%.2f",(currentItem.productQuantity * currentItem.productPrice))
        holder.cartListItemQuantity.text = currentItem.productQuantity.toString()

        holder.cartListItemButtonDecrease.isEnabled = !currentItem.isRemoving
        holder.cartListItemButtonIncrease.isEnabled = !currentItem.isRemoving

        holder.cartListItemButtonDecrease.setOnClickListener {
            productQuantityDecrease(currentItem, holder.adapterPosition, holder)
        }
        holder.cartListItemButtonIncrease.setOnClickListener {
            productQuantityIncrease(currentItem, holder.adapterPosition)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_cart_item, parent, false)
        return MyViewHolder(itemView)
    }

    private fun productQuantityIncrease(
        currentItem: CartFragment.CartListItem,
        currentPosition: Int
    ) {
        currentItem.productQuantity++
        notifyItemChanged(currentPosition)
        displayTotalPrice()
    }

    private fun productQuantityDecrease(
        currentItem: CartFragment.CartListItem,
        currentPosition: Int,
        holder: MyViewHolder
    ) {
        if (currentPosition == RecyclerView.NO_POSITION){
            return
        }
        if (currentItem.productQuantity > 1) {
            currentItem.productQuantity--
            holder.cartListItemPrice.text =
                (currentItem.productQuantity * currentItem.productPrice).toString()
            notifyItemChanged(currentPosition)
            displayTotalPrice()
        } else {
            currentItem.isRemoving = true
            cartListItemsList.removeAt(currentPosition)
            notifyItemRemoved(currentPosition)
            displayTotalPrice()
        }
    }

    fun displayTotalPrice() {
        var totalPrice = 0.00
        for (product in cartListItemsList) {
            totalPrice += product.productQuantity * product.productPrice
        }
        rootView.findViewById<TextView>(R.id.text_view_cart_total).text =
            "Total: ${String.format("%.2f", totalPrice)}€"
    }

    interface CartFragmentListener {
        fun sendTrigger(trigger: Boolean)
        fun onItemRemoved(position: Int)
    }

    fun removeItem(position: Int) {
        cartListItemsList.removeAt(position)
        notifyItemRemoved(position)
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cartListItemImage = itemView.findViewById<ImageView>(R.id.image_view_cart_item_image)
        val cartListItemName =
            itemView.findViewById<TextView>(R.id.text_view_cart_item_product_name)
        val cartListItemPrice =
            itemView.findViewById<TextView>(R.id.text_view_cart_item_product_price)

        val cartListItemQuantity =
            itemView.findViewById<TextView>(R.id.text_view_cart_item_quantity)
        val cartListItemButtonDecrease =
            itemView.findViewById<Button>(R.id.button_cart_item_decrement_quantity)
        val cartListItemButtonIncrease =
            itemView.findViewById<Button>(R.id.button_cart_item_increment_quantity)
    }
}