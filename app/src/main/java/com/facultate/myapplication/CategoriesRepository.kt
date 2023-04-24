package com.facultate.myapplication

import com.facultate.myapplication.hilt.service.CategoriesService
import com.facultate.myapplication.model.domain.Category
import com.facultate.myapplication.model.mapper.CategoryMapper
import javax.inject.Inject

class CategoriesRepository @Inject constructor(
    private val categoriesService: CategoriesService,
    private val categoryMapper: CategoryMapper

) {

    suspend fun fetchAllCategories(): List<Category> {
        return categoriesService.getAllCategories().body()?.let { networkCategories ->
            networkCategories.map { categoryMapper.buildFrom(it) }
        } ?: emptyList()
    }

}