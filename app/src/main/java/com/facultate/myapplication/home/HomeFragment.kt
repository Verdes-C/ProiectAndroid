package com.facultate.myapplication.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.facultate.myapplication.MainActivityViewModel
import com.facultate.myapplication.R
import com.facultate.myapplication.databinding.FragmentHomeBinding
import com.facultate.myapplication.model.domain.Category
import com.facultate.myapplication.model.domain.UIProduct
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch


@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {

    private val viewModel: MainActivityViewModel by viewModels()

    private lateinit var binding: FragmentHomeBinding

    private lateinit var recyclerViewRecommendations: RecyclerView
    private lateinit var recyclerViewDeals: RecyclerView
    private lateinit var recyclerViewCategories: RecyclerView


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentHomeBinding.bind(view)

        lifecycleScope.launch {
            val waitForFunctions = listOf(
                async {viewModel.getUserData() },
                async {viewModel.refreshDeals() },
                async {viewModel.refreshProducts() },
                async {viewModel.refreshCategories() },
            )
            waitForFunctions.awaitAll()
        }

//        Flow for Product Recycler View
        combine(
            viewModel.store.stateFlow.map { it.products },
            viewModel.store.stateFlow.map { it.productsDeals },
            viewModel.store.stateFlow.map { it.userData.wishlistedProducts }
        ){products,deals,wishlistedIds ->
            if(!products.isEmpty()){
                products.mapNotNull { product ->
                    if(product.id.toString() !in deals){
                        return@mapNotNull UIProduct(product,product.id.toString() in wishlistedIds, product.id.toString() in deals)
                    }else null
                }
            }else arrayListOf<UIProduct>()
        }.distinctUntilChanged().asLiveData().observe(viewLifecycleOwner){products->
            if (!products.isNullOrEmpty()) setRecommendedProductsRecyclerView(view, products = products as ArrayList<UIProduct>)
        }

//        Flow for Data Recycler View
        combine(
            viewModel.store.stateFlow.map { it.products },
            viewModel.store.stateFlow.map { it.productsDeals },
            viewModel.store.stateFlow.map { it.userData.wishlistedProducts }
        ){products,deals,wishlistedIds ->
            if(!deals.isNullOrEmpty()){
                products.mapNotNull { product ->
                    if(product.id.toString() in deals) {
                       return@mapNotNull UIProduct(product,product.id.toString() in wishlistedIds, product.id.toString() in deals)
                    }
                    else null
                }
            }else arrayListOf<UIProduct>()
        }.distinctUntilChanged().asLiveData().observe(viewLifecycleOwner){deals->
            if (!deals.isNullOrEmpty()) setDealsProductsRecyclerView(view, deals = deals as ArrayList<UIProduct>)
        }

//      Flow for Categories Recycler View
//      TODO actual UX in real app
        viewModel.store.stateFlow.map {
            it.categories
        }.distinctUntilChanged().asLiveData().observe(viewLifecycleOwner)
        { categories ->
            if (categories != emptyList<List<Category>>()) setCategoriesRecyclerView(
                view,
                categories as ArrayList<Category>
            )
        }
    }


    override fun onStart() {
        super.onStart()
        lifecycleScope.launch {
            viewModel.store.stateFlow.map { it.userData }.distinctUntilChanged().asLiveData().observe(viewLifecycleOwner){userData->
                val greeting = "Hello, ${userData.name}"
                binding.textViewHomeGreeting.text = greeting
            }
        }
    }

//    Functions to set the recycler views accordingly
    private fun setRecommendedProductsRecyclerView(view: View, products: ArrayList<UIProduct>) {
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        recyclerViewRecommendations = view.findViewById(R.id.recycler_view_recommendations)
        recyclerViewRecommendations.layoutManager = layoutManager
        val productAdapter = ProductCardAdapter(products, viewModel.store,findNavController())
        recyclerViewRecommendations.adapter = productAdapter
    }


    private fun setDealsProductsRecyclerView(view: View, deals: ArrayList<UIProduct>) {
        val layoutManager = GridLayoutManager(context, 2, GridLayoutManager.HORIZONTAL, false)
        recyclerViewDeals = view.findViewById(R.id.recycler_view_deals)
        recyclerViewDeals.layoutManager = layoutManager
        val productAdapter = ProductCardAdapter(deals, viewModel.store, findNavController())
        recyclerViewDeals.adapter = productAdapter

    }

    private fun setCategoriesRecyclerView(view: View, categories: ArrayList<Category>) {
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        recyclerViewCategories = view.findViewById(R.id.recycler_view_categories)
        recyclerViewCategories.layoutManager = layoutManager
        val categoriesAdapter = CategoryCardAdapter(categories)
        recyclerViewCategories.adapter = categoriesAdapter
    }
}