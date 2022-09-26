package com.example.myproducts.ui.products

import android.util.Log
import androidx.lifecycle.*
import com.example.myproducts.repositories.ProductsRepository
import com.example.myproducts.domain.ProductDomain
import com.example.myproducts.entity.StateData
import com.example.myproducts.entity.StateInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductsViewModel @Inject constructor(
    private val productsRepository: ProductsRepository
) : ViewModel() {

    val products: LiveData<List<ProductDomain>> = productsRepository.products

    private val _stateValue = MutableLiveData<StateInfo>()
    val stateValue: LiveData<StateInfo>
        get() = _stateValue

    init {
        Log.d("IVONA", "LOADING PRODUCTS")
        _stateValue.value = StateInfo(StateData.Status.LOADING, null, null)
        getProducts()
    }

    private fun getProducts() {
        viewModelScope.launch {

            // for testing
            delay(200)

            _stateValue.value = productsRepository.fetchAndCacheAllProducts()
            Log.d("IVONA", "FETCHED PRODUCTS")
        }
    }

}