package com.example.myproducts.ui.product_detail

import android.util.Log
import androidx.lifecycle.*
import com.example.myproducts.domain.ProductDomain
import com.example.myproducts.entity.StateData
import com.example.myproducts.repositories.ProductsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductDetailViewModel @Inject constructor(
    private val productsRepository: ProductsRepository
) : ViewModel() {

    private val _product = MutableStateFlow<StateData<ProductDomain>>(
        StateData(
            StateData.Status.LOADING,
            null,
            null,
            null
        )
    )
    val product: StateFlow<StateData<ProductDomain>>
        get() = _product

    fun getProduct(id: Int) {
        viewModelScope.launch {
            val cachedData = productsRepository.getCachedProduct(id)
            cachedData?.let {
                Log.d("IVONA", "CACHED PRODUCT")
                _product.value = StateData(StateData.Status.SUCCESS, it, null, null)
            }

            val fetchedProduct = productsRepository.fetchProduct(id)
            if ((fetchedProduct.status == StateData.Status.SUCCESS) || (_product.value.status == StateData.Status.LOADING)) {
                Log.d("IVONA", "FETCHED PRODUCT")
                _product.value = productsRepository.fetchProduct(id)
            }
        }
    }
}