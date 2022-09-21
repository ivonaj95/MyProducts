package com.example.myproducts.ui

import android.view.View
import androidx.fragment.app.Fragment

open class MyProductBaseFragment : Fragment() {

    var activeFocus: View? = null

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (hidden) {
            activeFocus = requireActivity().currentFocus
        } else {
            activeFocus?.requestFocus()
        }

    }
}