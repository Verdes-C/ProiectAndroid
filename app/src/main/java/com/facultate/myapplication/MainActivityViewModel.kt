package com.facultate.myapplication

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.facultate.myapplication.hilt.DealsDB
import com.facultate.myapplication.hilt.FirebaseModule
import com.facultate.myapplication.hilt.UsersDB
import com.facultate.myapplication.hilt.UsersDBWithQueryStringString
import com.facultate.myapplication.model.domain.Category
import com.facultate.myapplication.model.domain.Product
import com.facultate.myapplication.model.domain.UIProduct
import com.facultate.myapplication.model.domain.UserData
import com.facultate.myapplication.redux.ApplicationState
import com.facultate.myapplication.redux.Store
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Provider

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val productsRepository: ProductsRepository,
    private val categoriesRepository: CategoriesRepository,
    val store: Store<ApplicationState>,
    @UsersDB private val usersDB: CollectionReference,
    @DealsDB private val dealsDB: CollectionReference
) : ViewModel() {
    @Inject lateinit var auth: FirebaseAuth
    suspend fun refreshProducts() = viewModelScope.launch {
        val products: ArrayList<Product> =
            productsRepository.fetchAllProducts() as ArrayList<Product>
        store.update { applicationState ->
            return@update applicationState.copy(
                products = products
            )
        }
    }

    suspend fun refreshDeals() = viewModelScope.launch {
        dealsDB.document("regularDeals")
            .get()
            .addOnSuccessListener { result ->
                val data = result.data
                val deals: ArrayList<String> = data?.get("id") as ArrayList<String>
                CoroutineScope(Dispatchers.Main).launch {
                    store.update { applicationState ->
                        return@update applicationState.copy(
                            productsDeals = deals
                        )
                    }
                }
            }
            .addOnFailureListener {
            }
    }

    suspend fun refreshCategories() = viewModelScope.launch {
        val categories: ArrayList<Category> =
            categoriesRepository.fetchAllCategories() as ArrayList<Category>
        store.update { applicationState ->
            return@update applicationState.copy(
                categories = categories
            )
        }
    }

    suspend fun getUserData() = viewModelScope.launch {
        usersDB.whereEqualTo("userID",auth.currentUser?.uid)
            .get()
            .addOnSuccessListener { results ->
                val data = results.documents[0].data
                val name = data?.get("name") as? String ?: ""
                val email = data?.get("email") as? String ?: ""
                val userId = data?.get("userID") as? String ?: ""
                val phone = data?.get("phone") as? String ?: ""
                val adress = data?.get("adress") as? String ?: ""
                val cartItems = data?.get("cartItems") as? HashMap<String, Int> ?: hashMapOf()
                val interests = data?.get("interests") ?: arrayListOf<String>()
                val joiningReason = data?.get("joiningReason") as? String ?: "undecided"
                val wishlistedProducts = data?.get("wishlistedProducts") ?: arrayListOf<String>()
                viewModelScope.launch {
                    store.update { applicationState ->
                        return@update applicationState.copy(
                            userData = UserData(
                                name = name,
                                email = email,
                                userID = userId,
                                phone = phone,
                                adress = adress,
                                cartItems = cartItems,
                                wishlistedProducts = wishlistedProducts as ArrayList<String>,
                                interests = interests as ArrayList<String>,
                                joiningReason = joiningReason
                            )
                        )
                    }
                }
            }
            .addOnFailureListener {exception->
            Log.e("VIEWMODELERROR",exception.message,exception)
            }
    }

}