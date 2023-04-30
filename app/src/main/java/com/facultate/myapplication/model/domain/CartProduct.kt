package com.facultate.myapplication.model.domain

 data class CartProduct(
    override var product: Product,
    override var isFavorite: Boolean,
    override var isDeal: Boolean,
    var quantity: Int = 1,
    var isRemoving: Boolean = false
    ) : UIProduct(product, isFavorite, isDeal)

