package com.facultate.myapplication.redux

import com.facultate.myapplication.model.domain.Category
import com.facultate.myapplication.model.domain.Product
import com.facultate.myapplication.model.domain.UIProduct
import com.facultate.myapplication.model.domain.UserData
import javax.inject.Inject

data class ApplicationState(
    val products: ArrayList<Product> = arrayListOf(),
    val productsDeals: ArrayList<String> = arrayListOf(),
    val categories: ArrayList<Category> = arrayListOf(),
    var userData:UserData = UserData("","","","","", hashMapOf(), arrayListOf(), arrayListOf(),"")
)