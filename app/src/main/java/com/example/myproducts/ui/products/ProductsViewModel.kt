package com.example.myproducts.ui.products

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.example.myproducts.repositories.ProductsRepository
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

    init {
        Log.d("IVONA", "LOADING PRODUCTS")
        _products.value = StateData(StateData.Status.LOADING, null, null, null)
        getProducts()
    }

    private fun getProducts() {
        viewModelScope.launch {
            val cachedProducts = productsRepository.getCachedProducts()
            cachedProducts?.let {
                if (it.isNotEmpty()) {
                    Log.d("IVONA", "CACHED PRODUCTS")
                    _products.value = StateData(StateData.Status.SUCCESS, it, null, null)
                }
            }
            val fetchedProducts = productsRepository.fetchAndCacheAllProducts()
            if ((fetchedProducts.status == StateData.Status.SUCCESS) || (_products.value!!.status == StateData.Status.LOADING)) {
                Log.d("IVONA", "FETCHED PRODUCTS")
                _products.value = fetchedProducts
            }
        }
    }

}