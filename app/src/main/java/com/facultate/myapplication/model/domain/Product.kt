package com.facultate.myapplication.model.domain

import java.math.BigDecimal
import javax.inject.Inject

class Product(

    val category: String,
    val description: String,
    val id: Int,
    val image: String,
    val price: BigDecimal,
    val title: String

)