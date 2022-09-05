package com.example.myproducts.ui.products

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.Observer
import com.example.myproducts.R
import androidx.recyclerview.widget.RecyclerView
import com.example.myproducts.BUNDLE_ID
import com.example.myproducts.ui.product_detail.ProductDetailFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProductsFragment : Fragment() {

    companion object {
        fun newInstance() = ProductsFragment()
    }

    private lateinit var viewModel: ProductsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_products, container, false)

        viewModel = ViewModelProvider(this)[ProductsViewModel::class.java]

        val recyclerView = view.findViewById<RecyclerView>(R.id.product_list)
        val messageView = view.findViewById<TextView>(R.id.responseMessage)

        val adapter = ProductsAdapter()
        adapter.setOnClickListener {
            val newFragment = ProductDetailFragment.newInstance()

            val args = Bundle()
            args.putInt(BUNDLE_ID, it.id!!)
            newFragment.arguments = args
            newFragment.show(childFragmentManager, "")
        }

        viewModel.products.observe(viewLifecycleOwner, Observer { newProducts ->
            newProducts.error_code?.let {
                messageView.text = "$it : ${newProducts.message}"
                messageView.visibility = View.VISIBLE
            } ?: run {
                adapter.products = newProducts.data!!
            }

        })

        recyclerView.adapter = adapter
        return view
    }
}