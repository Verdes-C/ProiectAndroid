package com.facultate.myapplication.redux

import com.facultate.myapplication.model.domain.Category
import com.facultate.myapplication.model.domain.Product

data class ApplicationState(
    val products: List<Product> = emptyList(),
    val productsDeals: List<Product> = emptyList(),
    val categories: List<Category> = emptyList(),
    var favoriteProducts: List<Product> = emptyList()
)