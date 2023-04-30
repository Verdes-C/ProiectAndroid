package com.facultate.myapplication.cart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.facultate.myapplication.MainActivityViewModel
import com.facultate.myapplication.R
import com.facultate.myapplication.databinding.FragmentCartBinding
import com.facultate.myapplication.hilt.UsersDB
import com.facultate.myapplication.model.domain.CartProduct
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class CartFragment : Fragment(), CartListItemsAdapter.CartRefresh
{

    @Inject @UsersDB lateinit var usersDB: CollectionReference
    @Inject lateinit var auth:FirebaseAuth

    private val viewModel : MainActivityViewModel by viewModels()
    private lateinit var binding: FragmentCartBinding

    private lateinit var recyclerViewCartListItems: RecyclerView




    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        combine(
            viewModel.store.stateFlow.map {
                it.products
            },
            viewModel.store.stateFlow.map {
                it.productsDeals
            },
            viewModel.store.stateFlow.map {
                it.userData
            }
        ){  products,deals,userData->
            products.mapNotNull { product ->
                if(product.id.toString() in userData.cartItems.keys){
                    return@mapNotNull CartProduct(product,product.id.toString() in userData.wishlistedProducts,product.id.toString() in deals,userData.cartItems[product.id.toString()].toString().toInt())
                }else{
                    return@mapNotNull null
                }
            }
        }.asLiveData().observe(viewLifecycleOwner){cartProducts->
            setCartListItemsRecyclerView(cartProducts as ArrayList<CartProduct>,view)
            refreshCartTotal(cartProducts)
        }

        setClickListeners()
    }

    override fun onStart() {
        super.onStart()
    }

    private fun setClickListeners() {
        binding.buttonCartClear.setOnClickListener {
            clearCart()
            setCartTotalPrice(arrayListOf())
        }
    }

    private fun setCartTotalPrice(cartProducts: ArrayList<CartProduct>) {
        if (cartProducts.size > 0) {
            val totalPrice = calculateTotalPrice(cartProducts)
            binding.textViewCartTotal.text = "Total: $ $totalPrice"
        } else {
            binding.textViewCartTotal.text = "Total: $ 0"
        }
    }

    private fun calculateTotalPrice(products: ArrayList<CartProduct>): String {
        var totalPrice = 0.00
        for (product in products) {
            totalPrice += product.product.price * product.quantity
        }
        return String.format("%.2f", totalPrice)
    }

    private fun setCartListItemsRecyclerView(cartProducts: ArrayList<CartProduct>, view: View) {
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recyclerViewCartListItems = view.findViewById(R.id.recycler_view_cart_items)
        recyclerViewCartListItems.layoutManager = layoutManager
        val adapter = CartListItemsAdapter(cartProducts,viewModel.store,this,findNavController())
        recyclerViewCartListItems.adapter = adapter

    }

    override fun refreshCartTotal(cartProducts: ArrayList<CartProduct>) {
        setCartTotalPrice(cartProducts)
    }

    override fun clearCart() {
        view?.let { setCartListItemsRecyclerView(arrayListOf(), it) }
        CoroutineScope(Dispatchers.Main).launch {
            viewModel.store.update { applicationState ->
                val userDataCopy = applicationState.userData
                userDataCopy.cartItems = hashMapOf()

                usersDB.whereEqualTo("userID",auth.currentUser!!.uid)
                    .get()
                    .addOnSuccessListener { results->
                        results.documents[0].reference.update("cartItems", hashMapOf<String,Int>())
                    }

                return@update applicationState.copy(
                    userData = userDataCopy
                )
            }
            coroutineContext.cancel()
        }
    }

    override fun buy() {
//        TODO IMPLEMENT NEW ACTIVITY
    }
}