package com.facultate.myapplication.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.facultate.myapplication.MainActivityViewModel
import com.facultate.myapplication.R
import com.facultate.myapplication.databinding.FragmentHomeBinding
import com.facultate.myapplication.model.domain.Category
import com.facultate.myapplication.model.domain.Product
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map


@AndroidEntryPoint
class HomeFragment : Fragment() {

    private val viewModel : MainActivityViewModel by viewModels()

    private lateinit var binding: FragmentHomeBinding

    private lateinit var recyclerViewRecommendations: RecyclerView
    private lateinit var recyclerViewDeals: RecyclerView
    private lateinit var recyclerViewCategories: RecyclerView


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        viewModel.refreshProducts()
        viewModel.refreshCategories()

        viewModel.store.stateFlow.map {
            it.products
        }.distinctUntilChanged().asLiveData().observe(viewLifecycleOwner){products ->
            if (products != emptyList<Product>()) setRecommendedProductsRecyclerView(view, products as ArrayList<Product>)
        }

        viewModel.store.stateFlow.map {
            it.productsDeals
        }.distinctUntilChanged().asLiveData().observe(viewLifecycleOwner){deals ->
            if (deals != emptyList<List<Product>>()) setDealsProductsRecyclerView(view,deals as ArrayList<Product>)
        }

        viewModel.store.stateFlow.map {
            it.categories
        }.distinctUntilChanged().asLiveData().observe(viewLifecycleOwner){categories->
            if (categories != emptyList<List<Category>>()) setCategoriesRecyclerView(view, categories as ArrayList<Category>)
        }
    }


    override fun onStart() {
        super.onStart()
    }

    private fun setRecommendedProductsRecyclerView(view: View, products:ArrayList<Product>) {
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        recyclerViewRecommendations = view.findViewById(R.id.recycler_view_recommendations)
        recyclerViewRecommendations.layoutManager = layoutManager
        val productAdapter = ProductCardAdapter(products)
        recyclerViewRecommendations.adapter = productAdapter
    }


    private fun setDealsProductsRecyclerView(view: View, deals : ArrayList<Product>) {
        val layoutManager = GridLayoutManager(context, 2, GridLayoutManager.HORIZONTAL, false)
        recyclerViewDeals = view.findViewById(R.id.recycler_view_deals)
        recyclerViewDeals.layoutManager = layoutManager
        val productAdapter = ProductCardAdapter(deals)
        recyclerViewDeals.adapter = productAdapter

    }

    private fun setCategoriesRecyclerView(view: View, categories: ArrayList<Category>) {
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        recyclerViewCategories = view.findViewById(R.id.recycler_view_categories)
        recyclerViewCategories.layoutManager = layoutManager
        val categoriesAdapter = CategoryCardAdapter(categories)
        recyclerViewCategories.adapter = categoriesAdapter
    }

    data class Products(
        var productImage: String,
        var productName: String,
        var productPrice: String,
        var productDescription: String,
        var productIsFavorite: Boolean
    )

}