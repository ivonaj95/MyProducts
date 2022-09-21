package com.example.myproducts.repositories

import com.example.myproducts.datasource.ProductLocalDataSource
import com.example.myproducts.datasource.ProductRemoteDataSource
import com.example.myproducts.domain.ProductDomain
import com.example.myproducts.domain.ProductMapper
import com.example.myproducts.entity.Product
import com.example.myproducts.entity.StateData

class ProductsRepository(
    productRemoteDataSource: ProductRemoteDataSource,
    productLocalDataSource: ProductLocalDataSource
) : CacheRepository<Product>(
    remoteDataSource = productRemoteDataSource,
    localDataSource = productLocalDataSource
) {
    private val productMapper = ProductMapper()

    suspend fun getProducts(): StateData<List<ProductDomain>> {
        val products = fetchAllAndCache()
        if (products.isSuccessful) {
            return StateData(
                StateData.Status.SUCCESS,
                productMapper.fromEntityList(products.body),
                products.code,
                products.message
            )
        }
        return StateData(StateData.Status.ERROR, null, products.code, products.message)
    }

    suspend fun getProduct(id: Int): StateData<ProductDomain> {
        val product = fetchItemAndCache(id)
        if (product.isSuccessful) {
            product.body?.let {
                return StateData(
                    StateData.Status.SUCCESS,
                    productMapper.mapFromEntity(it),
                    product.code,
                    product.message
                )
            }
        }
        return StateData(StateData.Status.ERROR, null, product.code, product.message)

    }
}
