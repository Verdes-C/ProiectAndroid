package com.facultate.myapplication.hilt.service

import com.facultate.myapplication.model.network.NetworkProduct
import com.google.firebase.firestore.auth.User
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path


interface ProductsService {
    @GET("products")
    suspend fun getAllProducts(): Response<List<NetworkProduct>>

    @GET("products/{id}")
    suspend fun getProductById(@Path("id") id:Int): Response<List<NetworkProduct>>
}