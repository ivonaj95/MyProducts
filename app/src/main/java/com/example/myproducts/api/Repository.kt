package com.example.myproducts.api

class Repository(private val apiService: ApiService) {
    suspend fun getCountries() = apiService.getProducts()
}
