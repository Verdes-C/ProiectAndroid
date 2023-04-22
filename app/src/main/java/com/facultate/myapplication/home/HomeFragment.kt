package com.facultate.myapplication.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.facultate.myapplication.R
import com.facultate.myapplication.databinding.FragmentHomeBinding
import com.facultate.myapplication.hilt.service.ProductsService
import com.facultate.myapplication.model.ProductMapper
import com.facultate.myapplication.model.domain.Product
import com.facultate.myapplication.model.network.NetworkProduct
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject


@AndroidEntryPoint
class HomeFragment : Fragment() {

    @Inject
    lateinit var productsService: ProductsService
    @Inject
    lateinit var productMapper: ProductMapper


    private lateinit var binding: FragmentHomeBinding
    private lateinit var rootView: View


    private lateinit var recyclerViewRecommendations: RecyclerView
    private lateinit var recyclerViewDeals: RecyclerView
    private lateinit var recyclerViewCategories: RecyclerView

    private lateinit var recommendedProductsArrayList: ArrayList<Product>
    private lateinit var dealsProductsArrayList: ArrayList<Products>
    private lateinit var categoriesProductsArrayList: ArrayList<Category>

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
        rootView = view


        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                val response: Response<List<NetworkProduct>> = productsService.getAllProducts()
                recommendedProductsArrayList = (response.body()?.map{ product ->
                    productMapper.buildFrom(product)
                } ?: emptyList()) as ArrayList<Product>
                setRecommendedProductsRecyclerView(view)

            }
        }
    }


    override fun onStart() {
        super.onStart()




//        recommendedProductsArrayList = arrayListOf(
//            Products(
//                "",
//                "Product for testing",
//                "9.99",
//                "This is a test product. It will be changed shortly",
//                false
//            ),
//            Products(
//                "",
//                "Product for testing",
//                "9.99",
//                "This is a test product. It will be changed shortly",
//                false
//            ),
//            Products(
//                "",
//                "Product for testing",
//                "9.99",
//                "This is a test product. It will be changed shortly",
//                false
//            ),
//            Products(
//                "",
//                "Product for testing",
//                "9.99",
//                "This is a test product. It will be changed shortly",
//                false
//            ),
//            Products(
//                "",
//                "Product for testing",
//                "9.99",
//                "This is a test product. It will be changed shortly",
//                false
//            ),
//            Products(
//                "",
//                "Product for testing",
//                "9.99",
//                "This is a test product. It will be changed shortly",
//                false
//            ),
//            Products(
//                "",
//                "Product for testing",
//                "9.99",
//                "This is a test product. It will be changed shortly",
//                false
//            ),
//            Products(
//                "",
//                "Product for testing",
//                "9.99",
//                "This is a test product. It will be changed shortly",
//                false
//            ),
//            Products(
//                "",
//                "Product for testing",
//                "9.99",
//                "This is a test product. It will be changed shortly",
//                false
//            ),
//            Products(
//                "",
//                "Product for testing",
//                "9.99",
//                "This is a test product. It will be changed shortly",
//                false
//            ),
//            Products(
//                "",
//                "Product for testing",
//                "9.99",
//                "This is a test product. It will be changed shortly",
//                false
//            ),
//            Products(
//                "",
//                "Product for testing",
//                "9.99",
//                "This is a test product. It will be changed shortly",
//                false
//            ),
//            Products(
//                "",
//                "Product for testing",
//                "9.99",
//                "This is a test product. It will be changed shortly",
//                false
//            ),
//            Products(
//                "",
//                "Product for testing",
//                "9.99",
//                "This is a test product. It will be changed shortly",
//                false
//            )
//        )


        dealsProductsArrayList = arrayListOf(
            Products(
                "",
                "Product for testing234",
                "95.99",
                "Thi3434s is a test product. It will be changed shortly",
                false
            ),
            Products(
                "",
                "Product for testing234",
                "95.99",
                "Thi3434s is a test product. It will be changed shortly",
                false
            ),
            Products(
                "",
                "Product for testing234",
                "95.99",
                "Thi3434s is a test product. It will be changed shortly",
                false
            ),
            Products(
                "",
                "Product for testing234",
                "95.99",
                "Thi3434s is a test product. It will be changed shortly",
                false
            ),
            Products(
                "",
                "Product for testing234",
                "95.99",
                "Thi3434s is a test product. It will be changed shortly",
                false
            ),
            Products(
                "",
                "Product for testing234",
                "95.99",
                "Thi3434s is a test product. It will be changed shortly",
                false
            ),
            Products(
                "",
                "Product for testing234",
                "95.99",
                "Thi3434s is a test product. It will be changed shortly",
                false
            ),
            Products(
                "",
                "Product for testing234",
                "95.99",
                "Thi3434s is a test product. It will be changed shortly",
                false
            ),
            Products(
                "",
                "Product for testing234",
                "95.99",
                "Thi3434s is a test product. It will be changed shortly",
                false
            ),
            Products(
                "",
                "Product for testing234",
                "95.99",
                "Thi3434s is a test product. It will be changed shortly",
                false
            ),
            Products(
                "",
                "Product for testing234",
                "95.99",
                "Thi3434s is a test product. It will be changed shortly",
                false
            ),
            Products(
                "",
                "Product for testing234",
                "95.99",
                "Thi3434s is a test product. It will be changed shortly",
                false
            ),
            Products(
                "",
                "Product for testing234",
                "95.99",
                "Thi3434s is a test product. It will be changed shortly",
                false
            ),
            Products(
                "",
                "Product for testing234",
                "95.99",
                "Thi3434s is a test product. It will be changed shortly",
                false
            ),
            Products(
                "",
                "Product for testing234",
                "95.99",
                "Thi3434s is a test product. It will be changed shortly",
                false
            )
        )

        categoriesProductsArrayList = arrayListOf(
            Category("Tech"),
            Category("Fashion"),
            Category("Gaming"),
            Category("Gaming"),
            Category("Gaming"),
            Category("Gaming"),
            Category("Gaming"),
        )

//        setDealsProductsRecyclerView(rootView)
//        setCategoriesRecyclerView(rootView)
    }

    private fun setRecommendedProductsRecyclerView(view: View) {
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        recyclerViewRecommendations = view.findViewById(R.id.recycler_view_recommendations)
        recyclerViewRecommendations.layoutManager = layoutManager
        val productAdapter = ProductCardAdapter(recommendedProductsArrayList)
        recyclerViewRecommendations.adapter = productAdapter
    }


//    private fun setDealsProductsRecyclerView(view: View) {
//        val layoutManager = GridLayoutManager(context, 2, GridLayoutManager.HORIZONTAL, false)
//        recyclerViewDeals = view.findViewById(R.id.recycler_view_deals)
//        recyclerViewDeals.layoutManager = layoutManager
//        val productAdapter = ProductCardAdapter(dealsProductsArrayList)
//        recyclerViewDeals.adapter = productAdapter
//
//    }

//    private fun setCategoriesRecyclerView(view: View) {
//        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
//        recyclerViewCategories = view.findViewById(R.id.recycler_view_categories)
//        recyclerViewCategories.layoutManager = layoutManager
//        val categoriesAdapter = CategoryCardAdapter(categoriesProductsArrayList)
//        recyclerViewCategories.adapter = categoriesAdapter
//    }

    data class Products(
        var productImage: String,
        var productName: String,
        var productPrice: String,
        var productDescription: String,
        var productIsFavorite: Boolean
    )

    data class Category(
        var categoryName: String
    )

}