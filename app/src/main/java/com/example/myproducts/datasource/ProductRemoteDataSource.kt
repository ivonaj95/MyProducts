package com.example.myproducts.datasource

import com.example.myproducts.CALL_NOT_EXECUTED
import com.example.myproducts.api.ApiService
import com.example.myproducts.entity.Product
import com.example.myproducts.entity.Products
import com.example.myproducts.repositories.RemoteDataSource
import com.example.myproducts.repositories.CacheResponse
import retrofit2.Response

class ProductRemoteDataSource(
    private val apiService: ApiService
) : RemoteDataSource<Product> {

    private suspend fun getProductsApi() = apiService.getProducts()
    private suspend fun getProductApi(id: Int) = apiService.getProduct(id)

    override suspend fun fetchAll(): CacheResponse<List<Product>> {
        val productsResponse: Response<Products>?
        try {
            productsResponse = getProductsApi()
        } catch (e: Exception) {
            // Call is not executed....
            return CacheResponse(false, null, CALL_NOT_EXECUTED, e.message)
        }

        if (productsResponse.isSuccessful) {
            productsResponse.body()?.let { copyProducts ->

                return CacheResponse(
                    true,
                    copyProducts.products,
                    productsResponse.code(),
                    productsResponse.message()
                )

            }
        }

        return CacheResponse(
            false,
            null,
            productsResponse.code(),
            productsResponse.message()
        )
    }

    override suspend fun fetch(id: Int): CacheResponse<Product> {

        val productResponse: Response<Product>?
        try {
            productResponse = getProductApi(id)
        } catch (e: Exception) {
            // Call is not executed....
            return CacheResponse(false, null, CALL_NOT_EXECUTED, e.message)
        }

        return CacheResponse(
            productResponse.isSuccessful,
            productResponse.body(),
            productResponse.code(),
            productResponse.message()
        )
    }
}
