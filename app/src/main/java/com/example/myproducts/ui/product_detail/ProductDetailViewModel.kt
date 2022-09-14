package com.example.myproducts.ui.product_detail

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.myproducts.database.ProductDatabase
import com.example.myproducts.database.ProductDatabaseDao
import com.example.myproducts.entity.Product
import com.example.myproducts.entity.StateData
import com.example.myproducts.repositories.ProductsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductDetailViewModel @Inject constructor(
    application: Application,
    private val productsRepository: ProductsRepository
) : AndroidViewModel(application) {

    private val _product = MutableLiveData<StateData<Product>>()
    val product: LiveData<StateData<Product>>
        get() = _product

    private var databaseDao: ProductDatabaseDao

    init {
        _product.value = StateData(StateData.Status.LOADING, null, null, null)
        databaseDao = ProductDatabase.getInstance(application).productDatabaseDao
    }

    fun getProduct(id: Int) {
        viewModelScope.launch {
            _product.value = productsRepository.getProduct(databaseDao, id)
        }
    }
}