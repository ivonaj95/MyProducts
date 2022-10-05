package com.example.myproducts.ui.products

import android.util.Log
import androidx.lifecycle.*
import com.example.myproducts.repositories.ProductsRepository
import com.example.myproducts.domain.ProductDomain
import com.example.myproducts.entity.StateData
import com.example.myproducts.entity.StateInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductsViewModel @Inject constructor(
    private val productsRepository: ProductsRepository
) : ViewModel() {

    val products: Flow<List<ProductDomain>> = productsRepository.products

    private val _stateValue = MutableStateFlow(StateInfo(StateData.Status.LOADING, null, null))
    val stateValue: StateFlow<StateInfo>
        get() = _stateValue

    init {
        Log.d("IVONA", "LOADING PRODUCTS")
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