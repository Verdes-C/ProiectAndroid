package com.facultate.myapplication.cart

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.facultate.myapplication.R
import com.facultate.myapplication.home.HomeFragmentDirections
import com.facultate.myapplication.model.domain.CartProduct
import com.facultate.myapplication.model.domain.UIProduct
import com.facultate.myapplication.redux.ApplicationState
import com.facultate.myapplication.redux.Store
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.*

class CartListItemsAdapter(
    private var cartListItemsList: ArrayList<CartProduct>,
    val store: Store<ApplicationState>,
    private val cartListener: CartRefresh? = null,
    private val navController: NavController
) : RecyclerView.Adapter<CartListItemsAdapter.MyViewHolder>() {

    private val usersDB: Query = FirebaseFirestore.getInstance().collection("Users")
        .whereEqualTo("userID", FirebaseAuth.getInstance().currentUser!!.uid)

    override fun getItemCount(): Int {
        return cartListItemsList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = cartListItemsList[position]

        bind(holder, currentItem)
    }

    private fun bind(
        holder: MyViewHolder,
        currentItem: CartProduct
    ) {
        Glide.with(holder.itemView.context)
            .load(currentItem.product.image)
            .centerCrop()
            .placeholder(R.drawable.placeholder_image)
            .into(holder.cartListItemImage)
        holder.cartListItemName.text = currentItem.product.title
        holder.cartListItemPrice.text =
            String.format("%.2f", (currentItem.product.price * currentItem.quantity))
        holder.cartListItemQuantity.text = currentItem.quantity.toString()
        holder.cartListItemButtonDecrease.isEnabled = !currentItem.isRemoving
        holder.cartListItemButtonIncrease.isEnabled = !currentItem.isRemoving
        holder.cartListItemButtonDecrease.setOnClickListener {
            productQuantityDecrease(currentItem, holder.adapterPosition, store, holder)
        }
        holder.cartListItemButtonIncrease.setOnClickListener {
            productQuantityIncrease(currentItem, holder.adapterPosition, store)
        }
        holder.cartItem.setOnClickListener(sendToProductPage(currentItem as UIProduct))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_cart_item, parent, false)
        return MyViewHolder(itemView)
    }

    private fun productQuantityIncrease(
        currentItem: CartProduct,
        currentPosition: Int,
        store: Store<ApplicationState>,
    ) {
        currentItem.quantity++
        notifyItemChanged(currentPosition)
        CoroutineScope(Dispatchers.Main).launch {
            store.update { applicationState ->
                val userDataCopy = applicationState.userData
                userDataCopy.cartItems[currentItem.product.id.toString()] = currentItem.quantity
                usersDB.get().addOnSuccessListener { results->
                    results.documents[0].reference.update("cartItems",userDataCopy.toHashMap().get("cartItems"))
                }
                return@update applicationState.copy(
                    userData = userDataCopy
                )
            }
            cartListener?.refreshCartTotal(cartListItemsList)
            coroutineContext.cancel()
        }
    }

    private fun productQuantityDecrease(
        currentItem: CartProduct,
        currentPosition: Int,
        store: Store<ApplicationState>,
        holder: MyViewHolder
    ) {
        if (currentPosition == RecyclerView.NO_POSITION) {
            return
        }
        CoroutineScope(Dispatchers.Main).launch {
            store.update { applicationState ->
                val userDataCopy = applicationState.userData
                if (currentItem.quantity > 1) {
                    currentItem.quantity--
                    holder.cartListItemPrice.text =
                        (currentItem.product.price * currentItem.quantity).toString()
                    notifyItemChanged(currentPosition)
                    userDataCopy.cartItems[currentItem.product.id.toString()] = currentItem.quantity
                } else {
                    currentItem.isRemoving = true
                    cartListItemsList.removeAt(currentPosition)
                    notifyItemRemoved(currentPosition)
                    userDataCopy.cartItems.remove(currentItem.product.id.toString())
                }

                usersDB.get().addOnSuccessListener { results->
                    results.documents[0].reference.update("cartItems",userDataCopy.toHashMap().get("cartItems"))
                }

                return@update applicationState.copy(
                    userData = userDataCopy
                )
            }
        }
        cartListener?.refreshCartTotal(cartListItemsList)
    }

    fun sendToProductPage(product: UIProduct):View.OnClickListener {
        val actionGoToProductPage = CartFragmentDirections.actionCartFragmentToProductFragment(product)
        return View.OnClickListener { view->
            navController.navigate(actionGoToProductPage)
        }
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
        val cartItem = itemView.findViewById<ConstraintLayout>(R.id.constraint_cart_item)
    }

    interface CartRefresh {
        fun refreshCartTotal(cartProduct: ArrayList<CartProduct>)
        fun clearCart()
        fun buy()
    }
}