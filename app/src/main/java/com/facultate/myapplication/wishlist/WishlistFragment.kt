package com.facultate.myapplication.wishlist

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.facultate.myapplication.R
import com.facultate.myapplication.databinding.FragmentWishlistBinding

class WishlistFragment: Fragment() {

    private lateinit var binding: FragmentWishlistBinding
    private lateinit var recyclerViewWishlistItems:RecyclerView
    private lateinit var wishlistItems:ArrayList<WishListItem>

    private lateinit var listener:WishlistFragmentInterface

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if(context is WishlistFragmentInterface){
            listener = context
        }else{
            throw RuntimeException("$context must implement MyFragmentListener")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWishlistBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        wishlistItems = arrayListOf(
            WishListItem("","item1","22.45"),
            WishListItem("","item1","22.45"),
            WishListItem("","item1","22.45"),
            WishListItem("","item1","22.45"),
            WishListItem("","item1","22.45"),
            WishListItem("","item1","22.45"),
            WishListItem("","item1","22.45"),
            WishListItem("","item1","22.45")
        )

        setWishlistItemsRecyclerView(view)

    }


    private fun setWishlistItemsRecyclerView(view: View) {
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recyclerViewWishlistItems = view.findViewById(R.id.recycler_view_wishlist_items)
        recyclerViewWishlistItems.layoutManager = layoutManager
        val wishlistItemsAdapter = WishlistItemAdapter(wishlistItems)
        recyclerViewWishlistItems.adapter = wishlistItemsAdapter
    }

    fun handleBackPressed() {
        listener.goHome()
    }

    interface WishlistFragmentInterface{
        fun goHome()
    }

    data class WishListItem(
        val wishlistItemImage:String,
        val wishlistItemName:String,
        val wishlistItemPrice:String
    )

}