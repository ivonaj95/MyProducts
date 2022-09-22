package com.example.myproducts

import android.widget.ImageView
import com.bumptech.glide.Glide

const val BUNDLE_ID = "product"
const val EMPTY_DATA = -1
const val CALL_NOT_EXECUTED = -2

const val DATABASE_NAME = "product_details"

const val REQUEST_LOADING_STATUS = "loadingResult"
const val KEY_STATUS = "status"
const val STATUS_LOADING = "loading"
const val STATUS_LOADED = "loaded"

fun addImageIntoView(imageView: ImageView, stringUrl: String) {
    Glide.with(imageView.context)
        .load(stringUrl)
        .into(imageView)
}