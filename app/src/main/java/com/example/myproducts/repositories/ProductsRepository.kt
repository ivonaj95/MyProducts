package com.example.myproducts.repositories

import com.example.myproducts.api.ApiService

class ProductsRepository(private val apiService: ApiService) {
    suspend fun getProducts() = apiService.getProducts()
}
