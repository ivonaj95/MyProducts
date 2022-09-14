package com.example.myproducts.domain

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ProductDomain(
    var id: Int? = null,
    var title: String? = null,
    var description: String? = null,
    var price: Int? = null,
    var discountPercentage: Float? = null,
    var rating: Float? = null,
    var stock: Int? = null,
    var brand: String? = null,
    var category: String? = null,
    var thumbnail: String? = null,
    var images: List<String>? = null
) : Parcelable
