package com.example.myproducts.ui.product_detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.myproducts.*
import com.example.myproducts.domain.ProductDomain
import com.example.myproducts.entity.StateData
import com.example.myproducts.ui.MyProductBaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class ProductDetailFragment : MyProductBaseFragment() {

    companion object {
        fun newInstance() = ProductDetailFragment()
    }

    private lateinit var imageView: ImageView
    private lateinit var nameView: TextView
    private lateinit var descriptionView: TextView
    private lateinit var priceView: TextView

    private lateinit var viewModel: ProductDetailViewModel
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_product_detail, container, false)
        imageView = view.findViewById(R.id.main_product_image)
        nameView = view.findViewById(R.id.product_name)
        descriptionView = view.findViewById(R.id.product_description)
        priceView = view.findViewById(R.id.product_price)

        viewModel = ViewModelProvider(this)[ProductDetailViewModel::class.java]
        viewModel.getProduct(requireArguments().getInt(BUNDLE_ID))

        lifecycleScope.launchWhenStarted {

            viewModel.product.collectLatest { newProduct ->
                val bundle = Bundle()
                when (newProduct.status) {
                    StateData.Status.LOADING -> {
                        bundle.putString(KEY_STATUS, STATUS_LOADING)
                        parentFragmentManager.setFragmentResult(REQUEST_LOADING_STATUS, bundle)
                    }
                    StateData.Status.SUCCESS -> {
                        bundle.putString(KEY_STATUS, STATUS_LOADED)
                        parentFragmentManager.setFragmentResult(REQUEST_LOADING_STATUS, bundle)
                        newProduct.data?.let { refreshUI(it) }
                    }
                    StateData.Status.ERROR -> {
                        bundle.putString(KEY_STATUS, STATUS_LOADED)
                        parentFragmentManager.setFragmentResult(REQUEST_LOADING_STATUS, bundle)
                        nameView.text = newProduct.error_code.toString()
                        descriptionView.text = newProduct.message
                    }
                }
            }
        }

        return view
    }

    private fun refreshUI(product: ProductDomain) {
        product.thumbnail?.let {
            addImageIntoView(imageView, it)
            imageView.visibility = View.VISIBLE
        }
        nameView.text = product.title
        descriptionView.text = product.description
        priceView.text = "${product.price}$"
    }
}