package com.example.myproducts.domain

import com.example.myproducts.entity.Product

class ProductMapper : EntityMapper<Product, ProductDomain> {

    override fun mapFromEntity(entity: Product): ProductDomain {
        return ProductDomain(
            id = entity.id,
            title = entity.title,
            description = entity.description,
            price = entity.price,
            discountPercentage = entity.discountPercentage,
            rating = entity.rating,
            stock = entity.stock,
            brand = entity.brand,
            category = entity.category,
            thumbnail = entity.thumbnail,
            images = entity.images
        )
    }

    override fun mapToEntity(domainModel: ProductDomain): Product {
        return Product(
            id = domainModel.id,
            title = domainModel.title,
            description = domainModel.description,
            price = domainModel.price,
            discountPercentage = domainModel.discountPercentage,
            rating = domainModel.rating,
            stock = domainModel.stock,
            brand = domainModel.brand,
            category = domainModel.category,
            thumbnail = domainModel.thumbnail,
            images = domainModel.images
        )
    }

    fun fromEntityList(initial: List<Product>?): List<ProductDomain>? {
        initial?.let {
            return initial.map { mapFromEntity(it) }
        }
        return null
    }

}