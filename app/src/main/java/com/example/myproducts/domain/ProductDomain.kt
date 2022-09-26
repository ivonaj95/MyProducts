package com.example.myproducts.domain

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.myproducts.DATABASE_NAME
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = DATABASE_NAME)
data class ProductDomain(
    @PrimaryKey
    @SerializedName("id") var id: Int? = null,

    @ColumnInfo(name = "title")
    @SerializedName("title") var title: String? = null,

    @ColumnInfo(name = "description")
    @SerializedName("description") var description: String? = null,

    @ColumnInfo(name = "price")
    @SerializedName("price") var price: Int? = null,

    @ColumnInfo(name = "discount_percentage")
    @SerializedName("discount_percentage") var discountPercentage: Float? = null,

    @ColumnInfo(name = "rating")
    @SerializedName("rating") var rating: Float? = null,

    @ColumnInfo(name = "stock")
    @SerializedName("stock") var stock: Int? = null,

    @ColumnInfo(name = "brand")
    @SerializedName("brand") var brand: String? = null,

    @ColumnInfo(name = "category")
    @SerializedName("category") var category: String? = null,

    @ColumnInfo(name = "thumbnail")
    @SerializedName("thumbnail") var thumbnail: String? = null,

    @ColumnInfo(name = "images")
    @SerializedName("images") var images: List<String>? = null
) : Parcelable
