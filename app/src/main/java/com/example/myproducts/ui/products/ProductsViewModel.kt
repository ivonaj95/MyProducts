package com.example.myproducts.ui.products

import android.app.Application
import androidx.lifecycle.*
import com.example.myproducts.repositories.ProductsRepository
import com.example.myproducts.database.ProductDatabase
import com.example.myproducts.database.ProductDatabaseDao
import com.example.myproducts.entity.Product
import com.example.myproducts.entity.StateData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductsViewModel @Inject constructor(
    application: Application,
    private val productsRepository: ProductsRepository
) : AndroidViewModel(application) {

    private val _products = MutableLiveData<StateData<List<Product>>>()
    val products: LiveData<StateData<List<Product>>>
        get() = _products

    private var databaseDao: ProductDatabaseDao

    init {
        databaseDao = ProductDatabase.getInstance(application).productDatabaseDao
        getProducts()
    }

    private fun getProducts() {
        viewModelScope.launch {
            _products.value = productsRepository.getProducts(databaseDao)
        }
    }

}