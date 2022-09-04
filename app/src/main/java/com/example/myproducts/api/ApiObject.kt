package com.example.myproducts.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val BASE_URL = "https://dummyjson.com/"

private val retrofit = Retrofit.Builder()
    .baseUrl(BASE_URL)
    .addConverterFactory(GsonConverterFactory.create())
    .build()


object ApiObject {
    val retrofitService : ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }
}