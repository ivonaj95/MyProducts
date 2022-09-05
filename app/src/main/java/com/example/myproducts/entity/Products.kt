package com.example.myproducts.entity

import com.google.gson.annotations.SerializedName

data class Products (
    @SerializedName("products") var products : List<Product>? = null,
    @SerializedName("total") var total : Int? = null,
    @SerializedName("skip") var skip : Int? = null,
    @SerializedName("limit") var limit : Int? = null
    )