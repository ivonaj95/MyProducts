package com.example.myproducts.ui

import androidx.fragment.app.Fragment

open class MyProductBaseFragment : Fragment() {

    override fun onResume() {
        super.onResume()
        refreshFocus()
    }

    open fun refreshFocus() {
        view?.requestFocus()
    }

}