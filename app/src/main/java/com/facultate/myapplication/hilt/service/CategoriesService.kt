package com.facultate.myapplication.hilt.service

import retrofit2.Response
import retrofit2.http.GET

interface CategoriesService {
    @GET("products/categories")
    suspend fun getAllCategories(): Response<List<String>>
}