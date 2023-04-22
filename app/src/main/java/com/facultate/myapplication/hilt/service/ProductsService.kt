package com.facultate.myapplication.hilt.service

import com.facultate.myapplication.model.network.NetworkProduct
import retrofit2.Response
import retrofit2.http.GET

interface ProductsService {
    @GET("products")
    suspend fun getAllProducts(): Response<List<NetworkProduct>>
}