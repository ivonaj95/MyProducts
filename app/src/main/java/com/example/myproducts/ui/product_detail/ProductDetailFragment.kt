package com.example.myproducts.ui.product_detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.myproducts.BUNDLE_ID
import com.example.myproducts.MainActivity
import com.example.myproducts.R
import com.example.myproducts.addImageIntoView
import com.example.myproducts.domain.ProductDomain
import com.example.myproducts.entity.StateData
import com.example.myproducts.ui.MyProductBaseFragment
import dagger.hilt.android.AndroidEntryPoint

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

        viewModel.product.observe(viewLifecycleOwner, Observer { newProduct ->
            when (newProduct.status) {
                StateData.Status.LOADING -> {
                    (activity as MainActivity).showLoading()
                }
                StateData.Status.SUCCESS -> {
                    (activity as MainActivity).hideLoading()
                    newProduct.data?.let { refreshUI(it) }
                }
                StateData.Status.ERROR -> {
                    (activity as MainActivity).hideLoading()
                    nameView.text = newProduct.error_code.toString()
                    descriptionView.text = newProduct.message
                }
            }
        })

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