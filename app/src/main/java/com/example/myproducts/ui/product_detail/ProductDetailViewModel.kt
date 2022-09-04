package com.example.myproducts.ui.product_detail

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.myproducts.database.ProductDatabase
import com.example.myproducts.database.ProductDatabaseDao
import com.example.myproducts.entity.Product
import kotlinx.coroutines.launch

class ProductDetailViewModel(application: Application) : AndroidViewModel(application) {

    private val _product = MutableLiveData<Product>()
    val product: LiveData<Product>
        get() = _product

    private var databaseDao: ProductDatabaseDao

    init {
        databaseDao = ProductDatabase.getInstance(application).productDatabaseDao
    }

    fun getProduct(id: Int) {
        viewModelScope.launch {
            _product.value = databaseDao.get(id)
        }
    }
}