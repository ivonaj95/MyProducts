package com.example.myproducts.ui.products

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myproducts.R
import com.example.myproducts.addImageIntoView
import com.example.myproducts.domain.ProductDomain
import javax.inject.Inject

class ProductsAdapter @Inject constructor() :
    RecyclerView.Adapter<ProductItemViewHolder>() {

    private lateinit var onClickListener: ProductItemClickListener

    var products = listOf<ProductDomain>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.product_view, parent, false)
        val viewHolder = ProductItemViewHolder(view)

        viewHolder.itemView.setOnClickListener {
            onClickListener.onClick(products[viewHolder.bindingAdapterPosition])
        }
        viewHolder.itemView.setOnFocusChangeListener { _, hasFocus ->
            viewHolder.productTitle.isSelected = hasFocus
        }

        return viewHolder
    }

    override fun onBindViewHolder(holder: ProductItemViewHolder, position: Int) {
        val item = products[position]
        holder.productTitle.text = item.title
        addImageIntoView(holder.productImage, item.thumbnail!!)
    }

    override fun getItemCount(): Int {
        return products.size
    }

    fun setOnClickListener(clickListener: (product: ProductDomain) -> Unit) {
        onClickListener = ProductItemClickListener(clickListener)
    }

}

class ProductItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val productImage: ImageView = itemView.findViewById(R.id.product_image)
    val productTitle: TextView = itemView.findViewById(R.id.product_title)
}

class ProductItemClickListener(val clickListener: (product: ProductDomain) -> Unit) {
    fun onClick(product: ProductDomain) = clickListener(product)
}
