package com.example.myproducts.ui.products

import android.app.Application
import androidx.lifecycle.*
import com.example.myproducts.*
import com.example.myproducts.api.ApiObject
import com.example.myproducts.database.ProductDatabase
import com.example.myproducts.database.ProductDatabaseDao
import com.example.myproducts.entity.Product
import com.example.myproducts.entity.StateData
import kotlinx.coroutines.launch


class ProductsViewModel(application: Application) : AndroidViewModel(application) {

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
            try {
                val response = ApiObject.retrofitService.getProducts()
                if (response.isSuccessful) {
                    response.body()?.let { copyProducts ->

                        _products.value =
                            StateData(response.body()!!.products, null, response.message())

                        databaseDao.clear()
                        databaseDao.insertAll(copyProducts.products!!)

                    } ?: run {
                        // Empty data --- error
                        _products.value = StateData(null, EMPTY_DATA, response.message())
                    }
                } else {
                    response.message()
                    _products.value = StateData(null, response.code(), response.message())
                }
            } catch (e: Exception) {
                _products.value = StateData(null, CALL_NOT_EXECUTED, e.message)
            }

        }
    }

}