package com.facultate.myapplication

import com.facultate.myapplication.hilt.service.ProductsService
import com.facultate.myapplication.model.ProductMapper
import com.facultate.myapplication.model.domain.Product
import javax.inject.Inject

class ProductsRepository @Inject constructor(
    private val productsService: ProductsService,
    private val productMapper: ProductMapper
) {

    suspend fun fetchAllProducts(): List<Product> {
        return productsService.getAllProducts().body()?.let { networkProducts ->
            networkProducts.map { productMapper.buildFrom(it) }
        } ?: arrayListOf<Product>()
    }
}