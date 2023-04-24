package com.facultate.myapplication

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.facultate.myapplication.model.domain.Category
import com.facultate.myapplication.model.domain.Product
import com.facultate.myapplication.redux.ApplicationState
import com.facultate.myapplication.redux.Store
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val productsRepository: ProductsRepository,
    private val categoriesRepository: CategoriesRepository,
    val store: Store<ApplicationState>
) : ViewModel() {

    private val _productsLiveData = MutableLiveData<List<Product>>()
    val productsLiveData: LiveData<List<Product>> = _productsLiveData
    private val _categoriesLiveData = MutableLiveData<List<Category>>()
    val categoriesLiveData: LiveData<List<Category>> = _categoriesLiveData

    fun refreshProducts() = viewModelScope.launch {
        val products: List<Product> = productsRepository.fetchAllProducts()
        store.update { applicationState ->
            return@update applicationState.copy(
                products = products
            )
        }
    }

    fun refreshDeals() = viewModelScope.launch {
//        TODO change to reflect puropse
        val deals: List<Product> = productsRepository.fetchAllProducts()
        store.update { applicationState ->
            return@update applicationState.copy(
                productsDeals = deals
            )
        }
    }

    fun refreshCategories() = viewModelScope.launch {
        val categories = categoriesRepository.fetchAllCategories()
        store.update { applicationState ->
            return@update applicationState.copy(
                categories = categories
            )
        }
    }

}