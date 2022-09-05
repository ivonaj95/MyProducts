package com.example.myproducts.api

import com.example.myproducts.entity.Product
import com.example.myproducts.entity.Products
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("products")
    suspend fun getProducts() : Response<Products>
    @GET("products/{id}")
    suspend fun getProduct(@Path("id") id: Int?) : Response<Product>
}