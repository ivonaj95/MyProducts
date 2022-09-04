package com.example.myproducts.api

import com.example.myproducts.entity.Products
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {
    @GET("products")
    suspend fun getProducts() : Response<Products>
}