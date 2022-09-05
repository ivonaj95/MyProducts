package com.example.myproducts.ui.products

import android.app.Application
import androidx.lifecycle.*
import com.example.myproducts.CALL_NOT_EXECUTED
import com.example.myproducts.EMPTY_DATA
import com.example.myproducts.repositories.ProductsRepository
import com.example.myproducts.database.ProductDatabase
import com.example.myproducts.database.ProductDatabaseDao
import com.example.myproducts.entity.Product
import com.example.myproducts.entity.Products
import com.example.myproducts.entity.StateData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
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
            val productsResponse: Response<Products>

            try {
                productsResponse = productsRepository.getProducts()

                if (productsResponse.isSuccessful) {
                    productsResponse.body()?.let { copyProducts ->

                        _products.value =
                            StateData(
                                productsResponse.body()!!.products,
                                null,
                                productsResponse.message()
                            )

                        databaseDao.clear()
                        databaseDao.insertAll(copyProducts.products!!)

                    } ?: run {
                        // Empty data --- error
                        _products.value = StateData(null, EMPTY_DATA, productsResponse.message())
                    }
                } else {
                    productsResponse.message()
                    _products.value =
                        StateData(null, productsResponse.code(), productsResponse.message())
                }
            } catch (e: Exception) {
                _products.value = StateData(null, CALL_NOT_EXECUTED, e.message)
            }
        }
    }

}