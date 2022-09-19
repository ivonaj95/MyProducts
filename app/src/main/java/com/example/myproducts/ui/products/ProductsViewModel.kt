package com.example.myproducts.ui.products

import android.app.Application
import androidx.lifecycle.*
import com.example.myproducts.repositories.ProductsRepository
import com.example.myproducts.database.ProductDatabase
import com.example.myproducts.database.ProductDatabaseDao
import com.example.myproducts.domain.ProductDomain
import com.example.myproducts.entity.StateData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductsViewModel @Inject constructor(
    application: Application,
    private val productsRepository: ProductsRepository
) : AndroidViewModel(application) {

    private val _products = MutableLiveData<StateData<List<ProductDomain>>>()
    val products: LiveData<StateData<List<ProductDomain>>>
        get() = _products

    private var databaseDao: ProductDatabaseDao

    init {
        _products.value = StateData(StateData.Status.LOADING, null, null, null)
        databaseDao = ProductDatabase.getInstance(application).productDatabaseDao
        getProducts()
    }

    private fun getProducts() {
        viewModelScope.launch {
            _products.value = productsRepository.getProducts(databaseDao)
        }
    }

}