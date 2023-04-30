package com.facultate.myapplication.model

import com.facultate.myapplication.model.domain.Product
import com.facultate.myapplication.model.network.NetworkProduct
import java.math.BigDecimal
import java.math.RoundingMode
import javax.inject.Inject

class ProductMapper @Inject constructor() {

    fun buildFrom(networkProduct: NetworkProduct): Product {
        return Product(
            category = networkProduct.category,
            description = networkProduct.description,
            id = networkProduct.id,
            image = networkProduct.image,
            price = String.format("%.2f",networkProduct.price).toDouble(),
            title = networkProduct.title
        )
    }
}