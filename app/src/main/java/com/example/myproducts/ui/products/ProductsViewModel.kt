package com.example.myproducts.ui.products

import android.app.Application
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
        _products.value = StateData(StateData.Status.LOADING, null, null, null)
        getProducts()
    }

    private fun getProducts() {
        viewModelScope.launch {
            _products.value = productsRepository.getProducts()
        }
    }

}