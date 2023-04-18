package com.facultate.myapplication.cart

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.facultate.myapplication.R
import com.facultate.myapplication.databinding.FragmentCartBinding

class CartFragment : Fragment(),
    CartListItemsAdapter.CartFragmentListener
{

    private lateinit var binding: FragmentCartBinding
    private lateinit var recyclerViewCartListItems: RecyclerView
    private lateinit var cartListItems: ArrayList<CartListItem>
    private lateinit var totalPrice: String

    private lateinit var listenerCardFragment: CartFragmentInterface

    private lateinit var adapter: CartListItemsAdapter

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is CartFragmentInterface) {
            listenerCardFragment = context
        } else {
            throw RuntimeException("$context must implement MyFragmentListener")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        cartListItems = arrayListOf(
            CartListItem("", "item1", 25.55f, 2),
            CartListItem("", "item2", 25.55f, 2),
            CartListItem("", "item3", 25.55f, 2),
            CartListItem("", "item4", 25.55f, 2),
            CartListItem("", "item5", 25.55f, 2),
            CartListItem("", "item6", 25.55f, 2),
            CartListItem("", "item7", 25.55f, 2),
            CartListItem("", "item8", 25.55f, 2),
            CartListItem("", "item9", 25.55f, 2)
        )

        setClickListeners(view)
        setCartTotalPrice()
        setCartListItemsRecyclerView(view)



    }

    private fun setClickListeners(view: View) {
        binding.buttonCartClear.setOnClickListener {
            cartListItems = arrayListOf()
            setCartTotalPrice()
            setCartListItemsRecyclerView(view)
        }
    }

    private fun setCartTotalPrice() {
        if (cartListItems.size > 0) {
            totalPrice = calculateTotalPrice(cartListItems)
            binding.textViewCartTotal.text = "Total: $totalPrice€"
        } else {
            binding.textViewCartTotal.text = "Total: 0€"
        }
    }

    private fun calculateTotalPrice(products: ArrayList<CartFragment.CartListItem>): String {
        var totalPrice = 0.00
        for (product in products) {
            totalPrice += product.productPrice * product.productQuantity
        }
        return String.format("%.2f", totalPrice)
    }

    private fun setCartListItemsRecyclerView(view: View) {
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recyclerViewCartListItems = view.findViewById(R.id.recycler_view_cart_items)
        recyclerViewCartListItems.layoutManager = layoutManager
        adapter = CartListItemsAdapter(cartListItems, requireView(), this)
        recyclerViewCartListItems.adapter = adapter

    }

    override fun sendTrigger(trigger: Boolean) {
        binding.buttonCartBuyItems.isEnabled = trigger
        binding.buttonCartClear.isEnabled = trigger
    }

    override fun onItemRemoved(position: Int) {
        adapter.removeItem(position)
    }


    class CartListItem(
        var productImage: String,
        var productName: String,
        var productPrice: Float,
        var productQuantity: Int,
        var isRemoving:Boolean = false
    )

    fun handleBackPressed() {
        listenerCardFragment.goHome()
    }


    interface CartFragmentInterface {
        fun goHome()
    }

}