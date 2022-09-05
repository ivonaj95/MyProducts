package com.example.myproducts.ui.product_detail

import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.myproducts.BUNDLE_ID
import com.example.myproducts.entity.Product
import com.example.myproducts.R
import com.example.myproducts.addImageIntoView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*

@AndroidEntryPoint
class ProductDetailFragment : DialogFragment() {

    companion object {
        fun newInstance() = ProductDetailFragment()
    }

    private lateinit var imageView: ImageView
    private lateinit var nameView: TextView
    private lateinit var descriptionView: TextView
    private lateinit var priceView: TextView

    private lateinit var viewModel: ProductDetailViewModel

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val dialog = Dialog(requireContext(), R.style.Theme_MyProducts)
        val rootView = dialog.layoutInflater.inflate(R.layout.fragment_product_detail, container)
        dialog.setContentView(rootView)

        imageView = rootView.findViewById(R.id.main_product_image)
        nameView = rootView.findViewById(R.id.product_name)
        descriptionView = rootView.findViewById(R.id.product_description)
        priceView = rootView.findViewById(R.id.product_price)

        viewModel = ViewModelProvider(this)[ProductDetailViewModel::class.java]
        viewModel.getProduct(requireArguments().getInt(BUNDLE_ID))

        // if we use viewLifecycleOwner instead of this, app will crash
        viewModel.product.observe(this, Observer { newProduct ->
            refreshUI(newProduct, rootView)
        })

        return dialog
    }

    private fun refreshUI(product: Product, view: View) {
        product.thumbnail?.let { imageUrl ->
            addImageIntoView(
                view.findViewById(R.id.main_product_image),
                imageUrl
            )
        }
        product.thumbnail?.let { addImageIntoView(imageView, it) }
        nameView.text = product.title
        descriptionView.text = product.description
        priceView.text = "${product.price}$"
    }
}