package com.example.myproducts.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.example.myproducts.DATABASE_NAME
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Product(
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