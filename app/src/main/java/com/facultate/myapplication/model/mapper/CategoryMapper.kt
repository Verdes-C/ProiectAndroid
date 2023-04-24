package com.facultate.myapplication.model.mapper

import com.facultate.myapplication.model.domain.Category
import java.math.RoundingMode
import javax.inject.Inject

class CategoryMapper @Inject constructor() {

    fun buildFrom(categoryName: String): Category {
        return Category(
            categoryName = categoryName
        )
    }

}