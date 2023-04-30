package com.facultate.myapplication.wishlist

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.facultate.myapplication.MainActivityViewModel
import com.facultate.myapplication.R
import com.facultate.myapplication.databinding.FragmentWishlistBinding
import com.facultate.myapplication.model.domain.UIProduct
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

@AndroidEntryPoint
class WishlistFragment : Fragment(R.layout.fragment_wishlist) {

    private lateinit var binding: FragmentWishlistBinding

    private val viewModel: MainActivityViewModel by viewModels()

    private lateinit var recyclerViewWishlistItems: RecyclerView


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentWishlistBinding.bind(view)

        combine(
            viewModel.store.stateFlow.map { it.products },
            viewModel.store.stateFlow.map { it.productsDeals },
            viewModel.store.stateFlow.map { it.userData }
        ) { products, deals, userData ->
            if (!products.isEmpty()) {
                products.mapNotNull { product ->
                    if (product.id.toString() in userData.wishlistedProducts) {
                        return@mapNotNull UIProduct(
                            product,
                            product.id.toString() in userData.wishlistedProducts,
                            product.id.toString() in deals
                        )
                    } else null
                }
            } else arrayListOf<UIProduct>()
        }.distinctUntilChanged().asLiveData().observe(viewLifecycleOwner) { products ->
            if (!products.isNullOrEmpty()) setWishlistItemsRecyclerView(
                view,
                wishlistItems = products as ArrayList<UIProduct>
            )
        }

    }

    override fun onStart() {
        super.onStart()
    }

    private fun setWishlistItemsRecyclerView(view: View, wishlistItems: ArrayList<UIProduct>) {
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recyclerViewWishlistItems = view.findViewById(R.id.recycler_view_wishlist_items)
        recyclerViewWishlistItems.layoutManager = layoutManager
        val wishlistItemsAdapter = WishlistItemAdapter(wishlistItems,viewModel.store,findNavController())
        recyclerViewWishlistItems.adapter = wishlistItemsAdapter
    }
}