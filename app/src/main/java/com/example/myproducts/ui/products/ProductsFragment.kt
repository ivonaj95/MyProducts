package com.example.myproducts.ui.products

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.example.myproducts.*
import com.example.myproducts.entity.StateData
import com.example.myproducts.ui.MyProductBaseFragment
import com.example.myproducts.ui.product_detail.ProductDetailFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class ProductsFragment : MyProductBaseFragment() {

    companion object {
        fun newInstance() = ProductsFragment()
    }

    private lateinit var viewModel: ProductsViewModel
    private lateinit var recyclerView: RecyclerView
    private var adapter = ProductsAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_products, container, false)

        viewModel = ViewModelProvider(this)[ProductsViewModel::class.java]

        recyclerView = view.findViewById(R.id.product_list)
        val messageView = view.findViewById<TextView>(R.id.responseMessage)

        adapter.setOnClickListener {
            val newFragment = ProductDetailFragment.newInstance()

            val args = Bundle()
            args.putInt(BUNDLE_ID, it.id!!)
            newFragment.arguments = args

            parentFragmentManager.beginTransaction()
                .hide(this)
                .add(R.id.container, newFragment)
                .addToBackStack(null)
                .commit()
        }

        lifecycleScope.launchWhenStarted {
            viewModel.products.collectLatest { newProducts ->
                if (newProducts.isNotEmpty()) {

                    // hide loading...
                    val bundle = Bundle()
                    bundle.putString(KEY_STATUS, STATUS_LOADED)
                    parentFragmentManager.setFragmentResult(REQUEST_LOADING_STATUS, bundle)

                    // refresh items
                    if (adapter.products.isNotEmpty()) {
                        // just for testing ....
                        Log.d("IVONA", "refresh ... ")
                        adapter.products = newProducts.sortedByDescending { product -> product.id }
                    } else {
                        adapter.products = newProducts
                    }
                }
            }

            viewModel.stateValue.collectLatest { newState ->
                val bundle = Bundle()

                when (newState.status) {
                    StateData.Status.LOADING -> {
                        bundle.putString(KEY_STATUS, STATUS_LOADING)
                        parentFragmentManager.setFragmentResult(REQUEST_LOADING_STATUS, bundle)
                    }
                    StateData.Status.SUCCESS -> {
                        bundle.putString(KEY_STATUS, STATUS_LOADED)
                        parentFragmentManager.setFragmentResult(REQUEST_LOADING_STATUS, bundle)
                    }
                    StateData.Status.ERROR -> {
                        bundle.putString(KEY_STATUS, STATUS_LOADED)
                        parentFragmentManager.setFragmentResult(REQUEST_LOADING_STATUS, bundle)

                        // do not show error if we already have some items loaded
                        if (adapter.products.isEmpty()) {
                            messageView.visibility = View.VISIBLE
                            messageView.text = "${newState.error_code} : ${newState.message}"
                        }
                    }
                }
            }
        }

        recyclerView.adapter = adapter
        return view
    }
}