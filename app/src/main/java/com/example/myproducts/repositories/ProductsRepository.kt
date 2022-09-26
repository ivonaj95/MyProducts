package com.example.myproducts.repositories

import com.example.myproducts.CALL_NOT_EXECUTED
import com.example.myproducts.MyProductApplication
import com.example.myproducts.api.ApiService
import com.example.myproducts.database.ProductDatabase
import com.example.myproducts.domain.ProductDomain
import com.example.myproducts.domain.ProductMapper
import com.example.myproducts.entity.Product
import com.example.myproducts.entity.Products
import com.example.myproducts.entity.StateData
import com.example.myproducts.entity.StateInfo
import retrofit2.Response

class ProductsRepository(
    private val apiService: ApiService
) {
    private val productMapper = ProductMapper()

    private val productDatabaseDao =
        ProductDatabase.getInstance(MyProductApplication.instance).productDatabaseDao

    val products = productDatabaseDao.getAll()

    private suspend fun getProductsApi() = apiService.getProducts()
    private suspend fun getProductApi(id: Int) = apiService.getProduct(id)

    suspend fun fetchAndCacheAllProducts(): StateInfo {
        val productsResponse: Response<Products>?
        try {
            productsResponse = getProductsApi()
        } catch (e: Exception) {
            // Call is not executed....
            return StateInfo(StateData.Status.ERROR, CALL_NOT_EXECUTED, e.message)
        }

        if (productsResponse.isSuccessful) {
            productsResponse.body()?.let { copyProducts ->

                // Cache products...
                copyProducts.products?.let {
                    if (it.isNotEmpty()) {
                        productDatabaseDao.insertAll(
                            productMapper.fromEntityList(it),
                        )
                    }
                }

                return StateInfo(
                    StateData.Status.SUCCESS,
                    productsResponse.code(),
                    productsResponse.message()
                )
            }
        }

        return StateInfo(
            StateData.Status.ERROR,
            productsResponse.code(),
            productsResponse.message()
        )
    }

    suspend fun fetchProduct(id: Int): StateData<ProductDomain> {

        val productResponse: Response<Product>?
        try {
            productResponse = getProductApi(id)
        } catch (e: Exception) {
            // Call is not executed....
            return StateData(StateData.Status.ERROR, null, CALL_NOT_EXECUTED, e.message)
        }

        if (productResponse.isSuccessful) {

            productResponse.body()?.let { product ->
                return StateData(
                    StateData.Status.SUCCESS,
                    productMapper.mapFromEntity(product),
                    productResponse.code(),
                    productResponse.message()
                )

            }
        }
        return StateData(
            StateData.Status.ERROR,
            null,
            productResponse.code(),
            productResponse.message()
        )
    }

    suspend fun getCachedProduct(id: Int): ProductDomain? {
        return productDatabaseDao.get(id)
    }
}
