package com.example.myproducts.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myproducts.entity.Product

// ------------------not used now
class SharedViewModel : ViewModel() {

    val product = MutableLiveData<Product>()
        get() = field

    fun setActiveProduct(activeProduct : Product) {
        product.value = activeProduct
    }
}
